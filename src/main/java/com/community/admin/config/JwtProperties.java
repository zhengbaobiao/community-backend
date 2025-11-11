package com.community.admin.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * JWT配置属性
 *
 * @author Community Team
 * @since 1.0.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    /**
     * 密钥
     */
    private String secret;

    /**
     * Token有效期(秒)
     */
    private Long expiration;

    /**
     * Token刷新时间(秒)
     */
    private Long refreshTime;

    /**
     * Token前缀
     */
    private String tokenPrefix;

    /**
     * Token请求头名称
     */
    private String header;
}
