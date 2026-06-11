package com.gdufe.petkeep.vo;

import lombok.Data;

import java.util.List;

/**
 * 动物分页结果 VO
 */
@Data
public class AnimalPageVO {

    /** 总记录数 */
    private Long total;

    /** 当前页码 */
    private Long pageNum;

    /** 每页条数 */
    private Long pageSize;

    /** 数据列表 */
    private List<AnimalVO> records;
}
