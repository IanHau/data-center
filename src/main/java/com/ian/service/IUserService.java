package com.ian.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ian.controller.req.UserReq;
import com.ian.controller.resp.UserVO;
import com.ian.entity.User;

/**
 * <p>
 * 用户信息 服务类
 * </p>
 *
 * @author ianhau
 * @since 2023-02-19
 */
public interface IUserService extends IService<User> {

    /**
     * 注册
     *
     * @param input 用户请求信息
     * @return 用户信息
     */
    UserVO register(UserReq input);

    /**
     * 登录
     *
     * @param input 用户请求信息
     * @return 用户信息
     */
    UserVO login(UserReq input);
}
