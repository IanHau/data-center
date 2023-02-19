package com.ian.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户信息
 * </p>
 *
 * @author ianhau
 * @since 2023-02-19
 */
@Getter
@Setter
  @TableName("sys_user")
@ApiModel(value = "User对象", description = "用户信息")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

      @ApiModelProperty("用户id")
        @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      @ApiModelProperty("用户名")
      private String username;

      @ApiModelProperty("密码")
      private String password;

      @ApiModelProperty("用户昵称")
      private String nickname;

      @ApiModelProperty("角色")
      private String role;

      @ApiModelProperty("头像")
      private String avatarUrl;

      @ApiModelProperty("邮箱")
      private String email;

      @ApiModelProperty("电话号码")
      private String phone;

      @ApiModelProperty("地址")
      private String address;

      @ApiModelProperty("创建时间")
      private LocalDateTime createTime;


}
