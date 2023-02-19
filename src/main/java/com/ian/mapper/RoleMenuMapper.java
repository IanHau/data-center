package com.ian.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ian.entity.RoleMenu;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 角色菜单关系表 Mapper 接口
 * </p>
 *
 * @author ianhau
 * @since 2023-02-19
 */
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {

    /**
     * 根据id删除角色
     *
     * @param roleId 角色id
     * @return 1
     */
    @Delete("delete from sys_role_menu where role_id = #{roleId}")
    int deleteByRoleId(@Param("roleId") Integer roleId);

    /**
     * 根据角色id查询菜单id列表
     *
     * @param roleId 角色id
     * @return 菜单id列表
     */
    @Select("select menu_id from sys_role_menu where role_id = #{roleId}")
    List<Integer> selectByRoleId(@Param("roleId") Integer roleId);
}
