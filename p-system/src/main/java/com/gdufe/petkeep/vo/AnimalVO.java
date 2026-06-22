package com.gdufe.petkeep.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 动物档案视图 VO
 */
@Data
public class AnimalVO {

    private Long id;

    /** 动物名字 */
    private String name;

    /** 类型：0=猫  1=狗 */
    private Integer type;

    /** 类型中文名 */
    private String typeName;

    /** 常驻区域 */
    private String area;

    /** 封面照片完整访问 URL */
    private String coverImg;

    /** 备注描述 */
    private String description;

    /** 该动物的打卡总数 */
    private Long checkinCount;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
