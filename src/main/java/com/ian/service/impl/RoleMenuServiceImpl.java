package com.ian.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ian.entity.RoleMenu;
import com.ian.mapper.RoleMenuMapper;
import com.ian.service.IRoleMenuService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色菜单关系表 服务实现类
 * </p>
 *
 * @author ianhau
 * @since 2023-02-19
 */
@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements IRoleMenuService {

}
