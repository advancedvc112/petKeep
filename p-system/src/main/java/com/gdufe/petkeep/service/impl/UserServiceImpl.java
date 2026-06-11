package com.gdufe.petkeep.service.impl;

import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gdufe.petkeep.common.BusinessException;
import com.gdufe.petkeep.dto.LoginDTO;
import com.gdufe.petkeep.dto.RegisterDTO;
import com.gdufe.petkeep.entity.User;
import com.gdufe.petkeep.mapper.UserMapper;
import com.gdufe.petkeep.service.UserService;
import com.gdufe.petkeep.utils.JwtUtils;
import com.gdufe.petkeep.vo.LoginVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 用户业务实现
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final JwtUtils jwtUtils;

    @Override
    public LoginVO login(LoginDTO dto) {
        // 1. 查用户
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>().eq(User::getUsername, dto.getUsername()));
        if (user == null) {
            throw new BusinessException("用户名或密码错误");
        }

        // 2. 验密码（BCrypt）
        if (!BCrypt.checkpw(dto.getPassword(), user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }

        // 3. 生成 Token
        String token = jwtUtils.generateToken(user.getId(), user.getRole());

        // 4. 组装返回
        return new LoginVO(
                token,
                user.getId(),
                user.getUsername(),
                user.getNickname(),
                user.getRole(),
                user.getAvatar()
        );
    }

    @Override
    public void register(RegisterDTO dto) {
        // 1. 校验用户名唯一
        Long count = userMapper.selectCount(
                new LambdaQueryWrapper<User>().eq(User::getUsername, dto.getUsername()));
        if (count > 0) {
            throw new BusinessException("用户名已存在");
        }

        // 2. 加密密码
        String hashedPwd = BCrypt.hashpw(dto.getPassword());

        // 3. 插入
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(hashedPwd);
        user.setNickname(dto.getNickname() != null ? dto.getNickname() : dto.getUsername());
        user.setRole(0);  // 默认普通用户
        userMapper.insert(user);
    }
}
