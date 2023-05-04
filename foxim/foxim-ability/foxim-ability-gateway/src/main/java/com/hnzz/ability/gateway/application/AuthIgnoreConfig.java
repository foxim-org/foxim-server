package com.hnzz.ability.gateway.application;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 无需认证访问资源的列表配置类
 * @author xdf
 * @version 1.0
 */
@Data
@ConfigurationProperties(prefix = "auth.ignore")
@Component
public class AuthIgnoreConfig {
    /**
     * 配置文件中无需认证的数组
     */
    private List<String> paths;
    /**
     * 管理员登录
     */
    private List<String> admins;
}
