package com.ian.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ian.entity.Role;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author ianhau
 * @since 2023-02-19
 */
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 根据唯一标识查询角色id
     *
     * @param flag 唯一标识
     * @return 角色id
     */
    @Select("select id from sys_role where flag = #{flag}")
    Integer selectByFlag(@Param("flag") String flag);

}
