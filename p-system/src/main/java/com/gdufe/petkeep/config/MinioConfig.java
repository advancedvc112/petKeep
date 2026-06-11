package com.gdufe.petkeep.config;

import io.minio.MinioClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MinIO 配置类
 * <p>
 * 从 application.yml 中 minio.* 读取配置，创建 MinioClient Bean。
 * 项目启动时自动检查并创建 bucket（如不存在）。
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "minio")
public class MinioConfig {

    /** API 端点，如 http://175.178.40.188:9000 */
    private String endpoint;

    /** 访问密钥 */
    private String accessKey;

    /** 私钥 */
    private String secretKey;

    /** 存储桶名称 */
    private String bucketName;

    /** 外网访问前缀（拼接后返回前端完整 URL） */
    private String externalUrl;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }
}
