package com.gdufe.petkeep.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 登录成功返回 VO
 */
@Data
@AllArgsConstructor
public class LoginVO {

    /** JWT Token */
    private String token;

    /** 用户 ID */
    private Long userId;

    /** 用户名 */
    private String username;

    /** 昵称 */
    private String nickname;

    /** 角色：0=普通用户  1=管理员 */
    private Integer role;

    /** 头像 URL */
    private String avatar;
}
