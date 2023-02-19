package com.ian.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ian.entity.Menu;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author ianhau
 * @since 2023-02-19
 */
public interface IMenuService extends IService<Menu> {

    /**
     * 获取菜单信息
     *
     * @param menuName 菜单名
     * @return 菜单信息
     */
    List<Menu> findMenus(String menuName);
}
