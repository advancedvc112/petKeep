package com.gdufe.petkeep.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 打卡动态视图 VO
 */
@Data
public class CheckinVO {

    private Long id;

    /** 动物 ID */
    private Long animalId;

    /** 动物名字 */
    private String animalName;

    /** 打卡用户昵称 */
    private String userNickname;

    /** 打卡文字内容 */
    private String content;

    /** 现场照片完整访问 URL */
    private String imgUrl;

    /** 打卡时间 */
    private LocalDateTime createTime;
}
