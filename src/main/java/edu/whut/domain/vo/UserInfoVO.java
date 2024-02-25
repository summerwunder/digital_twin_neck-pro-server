package edu.whut.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserInfoVO {
    private Integer uid;
    private String userNickname;
    private String loginIp;
    private LocalDateTime loginTime;
}
