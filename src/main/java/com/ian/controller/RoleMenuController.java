package com.ian.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ian.common.Result;

import com.ian.service.IRoleMenuService;
import com.ian.entity.RoleMenu;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 角色菜单关系表 前端控制器
 * </p>
 *
 * @author ianhau
 * @since 2023-02-19
 */
@RestController
@RequestMapping("/role-menu")
public class RoleMenuController {

    @Resource
    private IRoleMenuService roleMenuService;

    @PostMapping
    public Result save(@RequestBody RoleMenu roleMenu) {
        roleMenuService.saveOrUpdate(roleMenu);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result deleteOne(@PathVariable Integer id) {
        roleMenuService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        roleMenuService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(roleMenuService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(roleMenuService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<RoleMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        return Result.success(roleMenuService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

}

