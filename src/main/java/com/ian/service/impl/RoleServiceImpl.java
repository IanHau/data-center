package com.ian.service.impl;

import com.ian.entity.Role;
import com.ian.mapper.RoleMapper;
import com.ian.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ianhau
 * @since 2023-02-19
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

}
