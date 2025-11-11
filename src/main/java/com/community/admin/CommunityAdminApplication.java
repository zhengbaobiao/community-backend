package com.community.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 社区服务后台管理系统启动类
 *
 * @author Community Team
 * @since 1.0.0
 */
@SpringBootApplication
@MapperScan("com.community.admin.mapper")
public class CommunityAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommunityAdminApplication.class, args);
        System.out.println("\n========================================");
        System.out.println("  社区服务后台管理系统启动成功！");
        System.out.println("========================================\n");
    }
}
