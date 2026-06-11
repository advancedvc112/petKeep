package com.gdufe.petkeep.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gdufe.petkeep.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户 Mapper
 * <p>
 * 继承 BaseMapper 即拥有所有基础 CRUD 方法。
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
