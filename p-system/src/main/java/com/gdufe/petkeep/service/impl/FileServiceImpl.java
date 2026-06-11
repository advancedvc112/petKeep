package com.gdufe.petkeep.service.impl;

import com.gdufe.petkeep.common.BusinessException;
import com.gdufe.petkeep.common.UserContext;
import com.gdufe.petkeep.entity.FileRecord;
import com.gdufe.petkeep.mapper.FileRecordMapper;
import com.gdufe.petkeep.service.FileService;
import com.gdufe.petkeep.utils.MinioUtils;
import com.gdufe.petkeep.vo.FileUploadVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传业务实现
 */
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final MinioUtils minioUtils;
    private final FileRecordMapper fileRecordMapper;

    @Override
    public FileUploadVO upload(MultipartFile file, String subDir) {
        // 1. 基础校验
        if (file.isEmpty()) {
            throw new BusinessException("上传文件不能为空");
        }

        // 2. 上传到 MinIO，拿到 savePath
        String savePath = minioUtils.upload(file, subDir);

        // 3. 记录到 tb_file
        FileRecord record = new FileRecord();
        record.setOriginalName(file.getOriginalFilename());
        // saveName 从 savePath 中提取（subDir/uuid.ext）
        record.setSaveName(savePath.substring(savePath.lastIndexOf("/") + 1));
        record.setSavePath(savePath);
        record.setAccessUrl(minioUtils.getAccessUrl(savePath));
        record.setFileSize(file.getSize());
        record.setFileType(file.getContentType());
        record.setUploaderId(UserContext.getUserId());
        fileRecordMapper.insert(record);

        // 4. 返回
        return new FileUploadVO(record.getId(), savePath, minioUtils.getAccessUrl(savePath));
    }
}
