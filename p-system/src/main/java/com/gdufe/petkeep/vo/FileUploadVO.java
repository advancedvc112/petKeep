package com.gdufe.petkeep.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 文件上传返回 VO
 */
@Data
@AllArgsConstructor
public class FileUploadVO {

    /** 文件记录 ID（tb_file.id） */
    private Long fileId;

    /** 存储相对路径（save_path，存到业务表用此值） */
    private String savePath;

    /** 完整访问 URL（直接给前端显示） */
    private String accessUrl;
}
