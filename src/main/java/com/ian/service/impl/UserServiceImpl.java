package com.ian.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ian.common.Constants;
import com.ian.common.RoleEnum;
import com.ian.controller.req.UserReq;
import com.ian.controller.resp.UserVO;
import com.ian.entity.Menu;
import com.ian.entity.User;
import com.ian.exception.ServiceException;
import com.ian.mapper.RoleMapper;
import com.ian.mapper.RoleMenuMapper;
import com.ian.mapper.UserMapper;
import com.ian.service.IMenuService;
import com.ian.service.IUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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

    private static final Log LOGGER = Log.get();

    @Resource
    private UserMapper userMapper;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Resource
    private IMenuService menuService;

    @Override
    public UserVO login(UserReq userReq) {
        // 用户密码 md5加密
        userReq.setPassword(SecureUtil.md5(userReq.getPassword()));
        User one = getUserInfo(userReq);
        UserVO userVO = new UserVO();
        if (one == null) {
            throw new ServiceException(Constants.CODE_600, "用户名或密码错误");
        }
        BeanUtil.copyProperties(one, userVO, true);
        // 设置token
//            String token = TokenUtils.genToken(one.getId().toString(), one.getPassword());
//            userReq.setToken(token);
        List<Menu> roleMenus = getRoleMenus(one.getRole());
        userVO.setMenus(roleMenus);
        return userVO;
    }

    @Override
    public UserVO register(UserReq userReq) {
        // 用户密码 md5加密
        userReq.setPassword(SecureUtil.md5(userReq.getPassword()));
        // 检查注册用户名
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userReq.getUsername());
        User one = getOne(queryWrapper);
        if (one != null) {
            throw new ServiceException(Constants.CODE_600, "用户已存在");
        }

        User toCreate = new User();
        toCreate.setUsername(userReq.getUsername());
        toCreate.setPassword(userReq.getPassword());
        toCreate.setNickname(userReq.getNickname() == null ? userReq.getUsername() : userReq.getNickname());
        toCreate.setRole(RoleEnum.ROLE_STUDENT.toString());
        save(toCreate);

        UserVO userVO = new UserVO();
        BeanUtil.copyProperties(toCreate, userVO, true);

        return userVO;
    }


    private User getUserInfo(UserReq userReq) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userReq.getUsername());
        queryWrapper.eq("password", userReq.getPassword());
        User one;
        try {
            one = getOne(queryWrapper);
        } catch (Exception e) {
            LOGGER.error(e);
            throw new ServiceException(Constants.CODE_500, "系统错误");
        }
        return one;
    }

    /**
     * 获取当前角色的菜单列表
     *
     * @param roleFlag
     * @return
     */
    private List<com.ian.entity.Menu> getRoleMenus(String roleFlag) {
        Integer roleId = roleMapper.selectByFlag(roleFlag);
        // 当前角色的所有菜单id集合
        List<Integer> menuIds = roleMenuMapper.selectByRoleId(roleId);
        // 查出系统所有的菜单(树形)
        List<Menu> menus = menuService.findMenus("");
        // new一个最后筛选完成之后的list
        List<Menu> roleMenus = new ArrayList<>();
        // 筛选当前用户角色的菜单
        for (Menu menu : menus) {
            if (menuIds.contains(menu.getId())) {
                roleMenus.add(menu);
            }
            List<Menu> children = menu.getChildren();
            // removeIf()  移除 children 里面不在 menuIds集合中的元素
            children.removeIf(child -> !menuIds.contains(child.getId()));
        }
        return roleMenus;
    }
}
