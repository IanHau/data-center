package com.ian.controller;


import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ian.common.Constants;
import com.ian.common.Result;
import com.ian.controller.req.UserReq;
import com.ian.entity.User;
import com.ian.service.IUserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 用户信息 前端控制器
 * </p>
 *
 * @author ianhau
 * @since 2023-02-18
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private IUserService userService;

    @PostMapping("/login")
    public Result login(@RequestBody UserReq input) {
        String username = input.getUsername();
        String password = input.getPassword();
        if (CharSequenceUtil.isBlank(username) || CharSequenceUtil.isBlank(password)) {
            return Result.error(Constants.CODE_400, "参数错误");
        }
        return Result.success(userService.login(input));
    }

    @PostMapping("/register")
    public Result register(@RequestBody UserReq userReq) {
        String username = userReq.getUsername();
        String password = userReq.getPassword();
        if (CharSequenceUtil.isBlank(username) || CharSequenceUtil.isBlank(password)) {
            return Result.error(Constants.CODE_400, "参数错误");
        }
        return Result.success(userService.register(userReq));
    }

    @PostMapping
    public Result save(@RequestBody User user) {
        userService.saveOrUpdate(user);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result deleteOne(@PathVariable Integer id) {
        userService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        userService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(userService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(userService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String username,
                           @RequestParam(defaultValue = "") String email,
                           @RequestParam(defaultValue = "") String address) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (!"".equals(username)) {
            queryWrapper.like("username", username);
        }
        if (!"".equals(email)) {
            queryWrapper.like("email", email);
        }
        if (!"".equals(address)) {
            queryWrapper.like("address", address);
        }
        queryWrapper.orderByDesc("id");
        return Result.success(userService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

}

