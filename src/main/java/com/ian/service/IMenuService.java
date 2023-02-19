package com.ian.service;

import com.ian.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ianhau
 * @since 2023-02-19
 */
public interface IMenuService extends IService<Menu> {

    List<Menu> findMenus(String s);
}
