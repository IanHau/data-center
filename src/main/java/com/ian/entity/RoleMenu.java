package com.ian.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 角色菜单关系表
 * </p>
 *
 * @author ianhau
 * @since 2023-02-19
 */
@Getter
@Setter
@TableName("sys_role_menu")
@ApiModel(value = "RoleMenu对象", description = "角色菜单关系表")
public class RoleMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("角色id")
    private Integer roleId;

    @ApiModelProperty("菜单id")
    private Integer menuId;


}
