package edu.whut.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
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
import edu.whut.utils.ip.IpUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    @Autowired
    private UserMapper userMapper;
    @Override
    public String checkUserPwd(LoginUserDTO loginUser, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authentication
                =new UsernamePasswordAuthenticationToken
                   (loginUser.getUserName(),loginUser.getUserPwd());
        Authentication authenticate = authenticationManager.authenticate(authentication);
        LoginUserVO principal = (LoginUserVO) authenticate.getPrincipal();
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        log.info("principal------------>",principal);
        // 根据loginUser创建token
        if(ObjectUtil.isNull(principal)) {
            throw new ServiceException(HttpStatus.UNAUTHORIZED,"认证失败！");
        }
        // 创建token,此处的token时由UUID编码而成JWT字符串
        // 返回token
        String token =
                jwtHelper.createToken(UUID.randomUUID().toString().replace("-", ""));
        principal.setToken(token);
        jwtHelper.refreshRedisToken(principal);
        //String token = jwtUtils.createToken(loginUser);
        //log.info("token===》{}",token);
        //更新ip和登陆时间
        User updateUser=new User();
        updateUser.setLoginIp(IpUtil.getIp(request));
        updateUser.setLoginTime(LocalDateTime.now());
        LambdaUpdateWrapper<User> wrapper=
                new LambdaUpdateWrapper<>();
        wrapper.eq(User::getUserName,
                principal.getSysUser().getUserName());
        userMapper.update(updateUser,wrapper);
        return token;
    }
}




