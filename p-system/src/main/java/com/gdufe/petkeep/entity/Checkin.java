package com.gdufe.petkeep.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 打卡动态表 — tb_checkin
 */
@Data
@TableName("tb_checkin")
public class Checkin {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 关联动物 ID */
    private Long animalId;

    /** 打卡用户 ID */
    private Long userId;

    /** 文字描述 */
    private String content;

    /** 现场照片路径（可选） */
    private String imgUrl;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 逻辑删除 */
    @TableLogic
    private Integer deleted;
}
