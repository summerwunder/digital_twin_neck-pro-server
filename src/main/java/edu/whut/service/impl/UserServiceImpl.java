package edu.whut.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.whut.config.security.SysUserServiceDetail;
import edu.whut.constants.HttpStatus;
import edu.whut.domain.dto.LoginUserDTO;
import edu.whut.domain.vo.LoginUserVO;
import edu.whut.exception.ServiceException;
import edu.whut.pojo.User;
import edu.whut.service.UserService;
import edu.whut.mapper.UserMapper;
import edu.whut.utils.JwtHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
* @author wunder
* @description 针对表【t_user】的数据库操作Service实现
* @createDate 2024-02-24 19:22:05
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtHelper jwtHelper;
    @Override
    public String checkUserPwd(LoginUserDTO loginUser) {
        UsernamePasswordAuthenticationToken authentication
                =new UsernamePasswordAuthenticationToken
                   (loginUser.getUserName(),loginUser.getUserPwd());
        Authentication authenticate = authenticationManager.authenticate(authentication);
        LoginUserVO principal = (LoginUserVO) authenticate.getPrincipal();
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        log.info("principal------------>",principal);
        // 根据loginUser创建token
        if(ObjectUtil.isNull(loginUser)) {
            throw new ServiceException(HttpStatus.UNAUTHORIZED,"认证失败！");
        }
        // 创建token,此处的token时由UUID编码而成JWT字符串
        //TODO 返回token
        String token =
                jwtHelper.createToken(UUID.randomUUID().toString().replace("-", ""));
        principal.setToken(token);
        jwtHelper.refreshRedisToken(principal);
        //String token = jwtUtils.createToken(loginUser);
        //log.info("token===》{}",token);
        return token;
    }
}




