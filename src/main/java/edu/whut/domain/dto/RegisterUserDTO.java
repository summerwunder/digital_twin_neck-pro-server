package edu.whut.domain.dto;

import lombok.Data;

@Data
public class RegisterUserDTO {
    private String username;
    private String userpwd;
    private String useremail;
    private String nickname;
}
