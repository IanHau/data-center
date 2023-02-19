package com.ian.controller.resp;

import com.ian.entity.Menu;
import lombok.Data;

import java.util.List;

/**
 * @author ianhau
 */
@Data
public class UserVO {
    private Integer id;
    private String username;
    private String password;
    private String nickname;
    private String avatarUrl;
    private String token;
    private String role;
    private List<Menu> menus;
}
