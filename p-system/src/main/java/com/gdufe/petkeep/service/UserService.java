package com.gdufe.petkeep.service;

import com.gdufe.petkeep.dto.LoginDTO;
import com.gdufe.petkeep.dto.RegisterDTO;
import com.gdufe.petkeep.vo.LoginVO;

/**
 * 用户业务接口
 */
public interface UserService {

    /**
     * 用户登录
     * @return 登录成功返回 token + 用户信息
     */
    LoginVO login(LoginDTO dto);

    /**
     * 用户注册（默认 role=0 普通用户）
     */
    void register(RegisterDTO dto);
}
