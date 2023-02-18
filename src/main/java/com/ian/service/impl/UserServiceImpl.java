package com.ian.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ian.entity.User;
import com.ian.mapper.UserMapper;
import com.ian.service.IUserService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户信息 服务实现类
 * </p>
 *
 * @author ianhau
 * @since 2023-02-18
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
