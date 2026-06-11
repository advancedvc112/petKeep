package com.gdufe.petkeep.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 动物档案新增/更新请求体
 */
@Data
public class AnimalSaveDTO {

    /** 更新时传 id，新增时不传 */
    private Long id;

    @NotBlank(message = "动物名字不能为空")
    private String name;

    @NotNull(message = "动物类型不能为空")
    private Integer type;

    @NotBlank(message = "常驻区域不能为空")
    private String area;

    /** 封面照片路径（对应 tb_file.save_path） */
    private String coverImg;

    /** 备注描述 */
    private String description;
}
