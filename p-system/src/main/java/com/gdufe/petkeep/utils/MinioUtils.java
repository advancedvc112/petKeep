package com.gdufe.petkeep.utils;

import com.gdufe.petkeep.config.MinioConfig;
import io.minio.*;
import io.minio.http.Method;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * MinIO 文件操作工具
 * <p>
 * 封装上传、删除、拼接访问 URL 等操作。
 * 启动时自动检查 bucket 是否存在，不存在则创建。
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MinioUtils {

    private final MinioClient minioClient;
    private final MinioConfig minioConfig;

    @PostConstruct
    public void init() {
        try {
            boolean exists = minioClient.bucketExists(
                    BucketExistsArgs.builder().bucket(minioConfig.getBucketName()).build());
            if (!exists) {
                minioClient.makeBucket(
                        MakeBucketArgs.builder().bucket(minioConfig.getBucketName()).build());
                log.info("MinIO bucket [{}] 创建成功", minioConfig.getBucketName());
            } else {
                log.info("MinIO bucket [{}] 已存在", minioConfig.getBucketName());
            }
        } catch (Exception e) {
            log.error("MinIO 初始化失败", e);
        }
    }

    /**
     * 上传文件到 MinIO
     *
     * @param file    上传的文件
     * @param subDir  子目录（如 animal / checkin）
     * @return 保存的相对路径（subDir/uuid.ext）
     */
    public String upload(MultipartFile file, String subDir) {
        try {
            // 生成唯一文件名
            String originalName = file.getOriginalFilename();
            String suffix = "";
            if (originalName != null && originalName.contains(".")) {
                suffix = originalName.substring(originalName.lastIndexOf("."));
            }
            String saveName = UUID.randomUUID().toString().replace("-", "") + suffix;
            String objectName = subDir + "/" + saveName;

            // 上传
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(minioConfig.getBucketName())
                            .object(objectName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build());

            log.info("MinIO 上传成功 → objectName={}, size={}B", objectName, file.getSize());
            return objectName;
        } catch (Exception e) {
            log.error("MinIO 上传失败", e);
            throw new RuntimeException("文件上传失败：" + e.getMessage());
        }
    }

    /**
     * 获取带签名的访问 URL（私有桶）
     *
     * @param savePath 上传返回的相对路径
     * @return 预签名 URL（如 http://175.178.40.188:9000/pet-keep/animal/xxx.jpg?X-Amz-...）
     */
    public String getAccessUrl(String savePath) {
        if (savePath == null || savePath.isBlank()) {
            return null;
        }
        // 去掉可能已经包含的 bucket 前缀
        if (savePath.startsWith(minioConfig.getBucketName() + "/")) {
            savePath = savePath.substring(minioConfig.getBucketName().length() + 1);
        }
        // 去掉前导斜杠（防止路径格式不一致）
        if (savePath.startsWith("/")) {
            savePath = savePath.substring(1);
        }
        // 去掉尾部的双斜杠
        if (savePath.contains("//")) {
            savePath = savePath.replace("//", "/");
        }
        try {
            // 生成预签名 URL，有效期 7 天
            String presignedUrl = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(minioConfig.getBucketName())
                            .object(savePath)
                            .expiry(7, TimeUnit.DAYS)
                            .build()
            );
            log.debug("生成预签名 URL → objectName={}, 有效期=7天", savePath);
            return presignedUrl;
        } catch (Exception e) {
            log.error("生成预签名 URL 失败 → objectName={}", savePath, e);
            // 降级处理：返回基础 URL（图片可能无法访问）
            return minioConfig.getExternalUrl() + "/" + savePath;
        }
    }

    /**
     * 删除 MinIO 中的文件
     *
     * @param savePath 上传时返回的相对路径
     */
    public void delete(String savePath) {
        if (savePath == null || savePath.isBlank()) {
            return;
        }
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(minioConfig.getBucketName())
                            .object(savePath)
                            .build());
            log.info("MinIO 删除成功 → objectName={}", savePath);
        } catch (Exception e) {
            log.warn("MinIO 删除失败 → objectName={}, error={}", savePath, e.getMessage());
        }
    }
}
