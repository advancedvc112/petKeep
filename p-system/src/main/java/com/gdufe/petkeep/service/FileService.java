package com.gdufe.petkeep.service;

import com.gdufe.petkeep.vo.FileUploadVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传业务接口
 */
public interface FileService {

    /**
     * 上传文件到 MinIO，记录到 tb_file，返回路径信息
     *
     * @param file   上传的文件
     * @param subDir 子目录（animal / checkin）
     * @return 文件保存信息
     */
    FileUploadVO upload(MultipartFile file, String subDir);
}
