package com.gdufe.petkeep.controller;

import com.gdufe.petkeep.common.R;
import com.gdufe.petkeep.service.FileService;
import com.gdufe.petkeep.vo.FileUploadVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传接口
 *
 * 所有接口均需登录（Token）
 * 上传后返回 savePath，前端将其回填到表单的 coverImg / imgUrl 字段
 */
@RestController
@RequestMapping("/api/file")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    /**
     * POST /api/file/upload?subDir=animal
     * <p>
     * 上传文件到 MinIO，同时写入 tb_file 记录
     * 返回：{ fileId, savePath, accessUrl }
     *
     * subDir 取值：animal（动物封面）/ checkin（打卡照片）
     */
    @PostMapping("/upload")
    public R<FileUploadVO> upload(@RequestParam("file") MultipartFile file,
                                   @RequestParam(value = "subDir", defaultValue = "animal") String subDir) {
        return R.ok(fileService.upload(file, subDir));
    }
}
