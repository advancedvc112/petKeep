package com.gdufe.petkeep.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户表 — tb_user
 */
@Data
@TableName("tb_user")
public class User {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 登录账号 */
    private String username;

    /** BCrypt 加密密码 */
    private String password;

    /** 显示昵称 */
    private String nickname;

    /** 角色：0=普通用户  1=管理员 */
    private Integer role;

    /** 头像 URL */
    private String avatar;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /** 逻辑删除：0=正常  1=已删除 */
    @TableLogic
    private Integer deleted;
}
