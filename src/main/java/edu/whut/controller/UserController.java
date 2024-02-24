package edu.whut.controller;

import edu.whut.constants.HttpStatus;
import edu.whut.domain.dto.LoginUserDTO;
import edu.whut.domain.vo.LoginUserVO;
import edu.whut.response.Result;
import edu.whut.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("sys/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping("login")
    public Result getLoginInfo(@RequestBody LoginUserDTO loginUser){
        log.info("login---------->{}",loginUser);
        String token= null;
        try {
            token = userService.checkUserPwd(loginUser);
        } catch (Exception e) {
            return Result.error(HttpStatus.ERROR,"账号或密码错误");
        }
        return Result.success("登录成功",token);
    }

    @PostMapping("test")
    public Result testSecurity(@RequestBody LoginUserDTO loginUser){
        //return userService.checkUserPwd(loginUser);
        return Result.success("fine");
    }
}
