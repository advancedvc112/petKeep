package com.gdufe.petkeep.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 文件上传记录表 — tb_file
 */
@Data
@TableName("tb_file")
public class FileRecord {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 原始文件名 */
    private String originalName;

    /** 服务端保存文件名（UUID 重命名） */
    private String saveName;

    /** 服务端相对存储路径 */
    private String savePath;

    /** 完整可访问 URL（部署后填写） */
    private String accessUrl;

    /** 文件大小（字节） */
    private Long fileSize;

    /** MIME 类型 */
    private String fileType;

    /** 上传者用户 ID */
    private Long uploaderId;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
