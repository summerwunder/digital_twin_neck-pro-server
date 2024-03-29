package edu.whut.controller;

import edu.whut.constants.HttpStatus;
import edu.whut.domain.dto.LoginUserDTO;
import edu.whut.domain.dto.RegisterUserDTO;
import edu.whut.domain.vo.LoginUserVO;
import edu.whut.domain.vo.UserInfoVO;
import edu.whut.response.Result;
import edu.whut.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("sys/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping("login")
    public Result getLoginInfo(@RequestBody LoginUserDTO loginUser, HttpServletRequest request){
        //log.info("login---------->{}",loginUser);
        String token= null;
        try {
            token = userService.checkUserPwd(loginUser,request);
        } catch (Exception e) {
            return Result.error(HttpStatus.ERROR,"账号或密码错误");
        }
        Map<String,Object> map=new HashMap();
        map.put("token",token);
        return Result.success("登录成功",map);
    }
    @PostMapping("register")
    public Result getRegister(@RequestBody RegisterUserDTO registerUserDTO){
        boolean isOk=userService.registerForUser(registerUserDTO);
        if(isOk){
            return Result.success("注册成功");
        }else{
            return Result.success("已存在相同的用户名，请修改");
        }

    }
    @GetMapping("info")
    public Result getUserInfo(){
        //获取用户信息
        UserInfoVO loginUser=userService.getUserInfo();
        return Result.success("成功获取用户信息",loginUser);
    }


}
