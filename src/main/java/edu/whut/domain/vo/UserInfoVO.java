package edu.whut.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class UserInfoVO implements Serializable {
    private Integer uid;
    private String userNickname;
    private String loginIp;
    private LocalDateTime loginTime;
}
