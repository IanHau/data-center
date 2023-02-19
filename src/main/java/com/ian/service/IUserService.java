package com.ian.service;

import com.ian.controller.req.UserReq;
import com.ian.controller.resp.UserVO;
import com.ian.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户信息 服务类
 * </p>
 *
 * @author ianhau
 * @since 2023-02-19
 */
public interface IUserService extends IService<User> {

    UserVO register(UserReq userReq);

    UserVO login(UserReq input);
}
