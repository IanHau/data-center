package com.ian.controller.req;

import lombok.Data;

/**
 * @author ianhau
 */
@Data
public class UserReq {
    private String nickname;
    private String username;
    private String password;
}
