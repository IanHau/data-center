package com.ian.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ian.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户信息 Mapper 接口
 * </p>
 *
 * @author ianhau
 * @since 2023-02-18
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
