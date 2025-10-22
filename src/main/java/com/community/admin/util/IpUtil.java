package com.community.admin.util;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

/**
 * IP工具类
 *
 * @author Community Team
 * @since 1.0.0
 */
@Slf4j
public class IpUtil {

    private static final String UNKNOWN = "unknown";
    private static final String LOCALHOST_IP = "127.0.0.1";
    private static final String LOCALHOST_IPV6 = "0:0:0:0:0:0:0:1";

    /**
     * 获取客户端IP地址
     *
     * @param request HttpServletRequest
     * @return IP地址
     */
    public static String getIpAddress(HttpServletRequest request) {
        if (request == null) {
            return UNKNOWN;
        }

        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        // 处理IPv6本地地址
        if (LOCALHOST_IPV6.equals(ip)) {
            ip = LOCALHOST_IP;
        }

        // 处理多个IP的情况(取第一个非unknown的IP)
        if (ip != null && ip.contains(",")) {
            String[] ips = ip.split(",");
            for (String strIp : ips) {
                if (!UNKNOWN.equalsIgnoreCase(strIp)) {
                    ip = strIp;
                    break;
                }
            }
        }

        return ip;
    }

    /**
     * 获取IP归属地
     * 
     * @param ip IP地址
     * @return IP归属地
     */
    public static String getIpLocation(String ip) {
        if (ip == null || ip.isEmpty() || LOCALHOST_IP.equals(ip)) {
            return "本地";
        }
        
        // 这里可以集成IP归属地查询服务,如:
        // 1. 高德地图IP定位API
        // 2. 百度地图IP定位API
        // 3. 淘宝IP地址库
        // 4. 本地IP数据库(如:ip2region)
        
        // 暂时返回未知
        return "未知";
    }
}
