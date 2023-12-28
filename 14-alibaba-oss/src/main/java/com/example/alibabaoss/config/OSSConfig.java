package com.example.alibabaoss.config;

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 获取配置文件中对应的信息
 *
 * @author booty
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "aliyun.oss")
public class OSSConfig implements InitializingBean {
    //AccessKey账号
    private String accessKeyId;
    //AccessKey密钥
    private String accessKeySecret;
    //oss访问域名（内网）
    private String endpoint;
    //oss的存储空间
    private String bucketName;


    @Override
    public void afterPropertiesSet() throws Exception {
        if (accessKeyId == null || accessKeySecret == null || endpoint == null || bucketName == null) {
            throw new RuntimeException("初始化阿里云配置失败");
        }
    }
}
