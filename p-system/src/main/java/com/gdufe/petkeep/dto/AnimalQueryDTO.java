package com.gdufe.petkeep.dto;

import lombok.Data;

/**
 * 动物档案分页查询条件
 */
@Data
public class AnimalQueryDTO {

    /** 当前页（默认第 1 页） */
    private Integer pageNum = 1;

    /** 每页条数（默认 10 条） */
    private Integer pageSize = 10;

    /** 按名字模糊搜索 */
    private String name;

    /** 按类型筛选：0=猫  1=狗，null=全部 */
    private Integer type;
}
