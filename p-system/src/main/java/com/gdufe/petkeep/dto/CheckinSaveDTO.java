package com.gdufe.petkeep.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 打卡发布请求体
 */
@Data
public class CheckinSaveDTO {

    @NotNull(message = "动物 ID 不能为空")
    private Long animalId;

    @NotBlank(message = "打卡内容不能为空")
    private String content;

    /** 现场照片路径（可选） */
    private String imgUrl;
}
