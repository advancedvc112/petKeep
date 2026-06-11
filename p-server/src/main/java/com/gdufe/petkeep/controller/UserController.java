package com.gdufe.petkeep.controller;

import com.gdufe.petkeep.common.R;
import com.gdufe.petkeep.dto.LoginDTO;
import com.gdufe.petkeep.dto.RegisterDTO;
import com.gdufe.petkeep.service.UserService;
import com.gdufe.petkeep.vo.LoginVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 用户相关接口
 *
 * 登录/注册无需 Token，其余接口均需携带 Token
 */
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * POST /api/user/login
     * <p>
     * 请求体：{ "username": "admin", "password": "admin123" }
     * 返回：{ code:200, data:{ token, userId, username, nickname, role, avatar } }
     */
    @PostMapping("/login")
    public R<LoginVO> login(@Valid @RequestBody LoginDTO dto) {
        return R.ok(userService.login(dto));
    }

    /**
     * POST /api/user/register
     * <p>
     * 请求体：{ "username":"test", "password":"123456", "nickname":"测试用户" }
     */
    @PostMapping("/register")
    public R<Void> register(@Valid @RequestBody RegisterDTO dto) {
        userService.register(dto);
        return R.ok();
    }
}
