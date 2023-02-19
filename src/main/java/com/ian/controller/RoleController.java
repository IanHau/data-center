package com.ian.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ian.common.Result;
import com.ian.entity.Role;
import com.ian.service.IRoleService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author ianhau
 * @since 2023-02-19
 */
@RestController
@RequestMapping("/role")
public class RoleController {

    @Resource
    private IRoleService roleService;

    @PostMapping
    public Result save(@RequestBody Role role) {
        roleService.saveOrUpdate(role);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result deleteOne(@PathVariable Integer id) {
        roleService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        roleService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(roleService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(roleService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        return Result.success(roleService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

}

