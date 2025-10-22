package com.community.admin.aspect;

import com.alibaba.fastjson2.JSON;
import com.community.admin.annotation.OperationLog;
import com.community.admin.entity.AdminOperationLog;
import com.community.admin.mapper.AdminOperationLogMapper;
import com.community.admin.util.IpUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 操作日志切面
 *
 * @author Community Team
 * @since 1.0.0
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class OperationLogAspect {

    private final AdminOperationLogMapper operationLogMapper;

    /**
     * 定义切点:所有带有@OperationLog注解的方法
     */
    @Pointcut("@annotation(com.community.admin.annotation.OperationLog)")
    public void operationLogPointcut() {
    }

    /**
     * 环绕通知:记录操作日志
     */
    @Around("operationLogPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        
        // 获取注解信息
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        OperationLog annotation = signature.getMethod().getAnnotation(OperationLog.class);
        
        // 获取请求信息
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes != null ? attributes.getRequest() : null;
        
        // 构建日志对象
        AdminOperationLog operationLog = new AdminOperationLog();
        operationLog.setCreatedAt(LocalDateTime.now());
        
        // 设置操作类型
        operationLog.setOperation(annotation.value());
        
        // 设置请求方法
        if (request != null) {
            operationLog.setMethod(request.getMethod() + " " + request.getRequestURI());
            operationLog.setIp(IpUtil.getIpAddress(request));
            operationLog.setLocation(IpUtil.getIpLocation(operationLog.getIp()));
        }
        
        // 设置请求参数
        Object[] args = joinPoint.getArgs();
        if (args != null && args.length > 0) {
            try {
                operationLog.setParams(JSON.toJSONString(args));
            } catch (Exception e) {
                operationLog.setParams("参数序列化失败");
            }
        }
        
        // 获取当前登录用户
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getPrincipal() != null) {
                Object principal = authentication.getPrincipal();
                if (principal instanceof Long) {
                    operationLog.setUserId((Long) principal);
                }
            }
        } catch (Exception e) {
            log.warn("获取当前用户失败: {}", e.getMessage());
        }
        
        // 执行目标方法
        Object result = null;
        try {
            result = joinPoint.proceed();
            operationLog.setStatus(1); // 成功
            
            // 设置返回结果
            if (result != null) {
                try {
                    String resultStr = JSON.toJSONString(result);
                    // 限制结果长度
                    if (resultStr.length() > 2000) {
                        resultStr = resultStr.substring(0, 2000) + "...";
                    }
                    operationLog.setResult(resultStr);
                } catch (Exception e) {
                    operationLog.setResult("结果序列化失败");
                }
            }
        } catch (Throwable throwable) {
            operationLog.setStatus(0); // 失败
            operationLog.setErrorMsg(throwable.getMessage());
            throw throwable;
        } finally {
            // 计算执行时长
            long endTime = System.currentTimeMillis();
            operationLog.setExecuteTime((int) (endTime - startTime));
            
            // 异步保存日志
            saveLogAsync(operationLog);
        }
        
        return result;
    }

    /**
     * 异步保存日志
     */
    @Async
    public void saveLogAsync(AdminOperationLog operationLog) {
        try {
            operationLogMapper.insert(operationLog);
        } catch (Exception e) {
            log.error("保存操作日志失败: {}", e.getMessage(), e);
        }
    }
}
