package edu.whut.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 登陆用户信息
 */
@Data
public class LoginUserDTO{
    private String userName;
    private String userPwd;
}
