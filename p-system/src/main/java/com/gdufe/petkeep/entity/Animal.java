package com.gdufe.petkeep.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 动物档案表 — tb_animal
 */
@Data
@TableName("tb_animal")
public class Animal {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 暂定名字 */
    private String name;

    /** 类型：0=猫  1=狗 */
    private Integer type;

    /** 常驻区域 */
    private String area;

    /** 封面照片路径（tb_file.save_path） */
    private String coverImg;

    /** 备注描述 */
    private String description;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /** 逻辑删除 */
    @TableLogic
    private Integer deleted;
}
