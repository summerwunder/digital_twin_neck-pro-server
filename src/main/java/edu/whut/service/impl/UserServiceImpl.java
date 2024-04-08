package edu.whut.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.whut.config.security.SysUserServiceDetail;
import edu.whut.constants.HttpStatus;
import edu.whut.domain.dto.LoginUserDTO;
import edu.whut.domain.dto.RegisterUserDTO;
import edu.whut.domain.vo.LoginUserVO;
import edu.whut.domain.vo.UserInfoVO;
import edu.whut.exception.ServiceException;
import edu.whut.mapper.DevicesMapper;
import edu.whut.mapper.UserMapDevicesMapper;
import edu.whut.pojo.Devices;
import edu.whut.pojo.User;
import edu.whut.pojo.UserMapDevices;
import edu.whut.service.UserService;
import edu.whut.mapper.UserMapper;
import edu.whut.utils.JwtHelper;
import edu.whut.utils.ip.IpUtil;
import edu.whut.utils.security.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
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
    @Autowired
    private DevicesMapper devicesMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserMapDevicesMapper userMapDevicesMapper;
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
        //更新ip和登陆时间
        User updateUser=new User();
        updateUser.setLoginIp(IpUtil.getIp(request));
        updateUser.setLoginTime(LocalDateTime.now());
        LambdaUpdateWrapper<User> wrapper=
                new LambdaUpdateWrapper<>();
        wrapper.eq(User::getUserName,
                principal.getSysUser().getUserName());
        userMapper.update(updateUser,wrapper);
        //此时需要将所有的所属的所有设备都设置为离线状态
        LambdaQueryWrapper<UserMapDevices> userMapDevicesLambdaQueryWrapper
                =new LambdaQueryWrapper<>();
        userMapDevicesLambdaQueryWrapper.
                eq(UserMapDevices::getUId,principal.getSysUser().getUid());
        //获取所有设备id
        List<Integer> ids=
                userMapDevicesMapper.selectList(userMapDevicesLambdaQueryWrapper).stream()
                        .map(item->item.getDId()).toList();
        Devices devices=new Devices();
        devices.setDStatus(0);
        LambdaQueryWrapper<Devices> devicesLambdaQueryWrapper
                =new LambdaQueryWrapper<>();
        devicesLambdaQueryWrapper.in(Devices::getDid,ids);
        //更新为不在线状态
        devicesMapper.update(devices,devicesLambdaQueryWrapper);
        return token;
    }


    /**
     * 获取用户的个人信息
     * @return
     */
    @Override
    public UserInfoVO getUserInfo() {
        Integer id= SecurityUtil.getUserId();
        User user = userMapper.selectById(id);
        if(ObjectUtil.isNull(user)){
            throw new ServiceException(HttpStatus.FORBIDDEN,"");
        }
        //将用户的部分数据传输到用户信息表中
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtil.copyProperties(user,userInfoVO);
        return userInfoVO;
    }

    @Override
    public boolean registerForUser(RegisterUserDTO registerUserDTO) {
        //首先检索是否存在相同用户名
        LambdaQueryWrapper<User> lambdaQueryWrapper=
                new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUserName,registerUserDTO.getUsername());
        if(ObjectUtil.isNotNull(userMapper.selectOne(lambdaQueryWrapper))){
            //说明存在相同用户名
            return false;
        }
        //设置秘文
        String userPassword = passwordEncoder.encode(registerUserDTO.getUserpwd());
        //创建新的用户
        User user=new User();
        user.setUserName(registerUserDTO.getUsername());
        user.setUserPwd(userPassword);
        user.setEmail(registerUserDTO.getUseremail());
        user.setUserNickname(registerUserDTO.getNickname());
        user.setLoginTime(null);
        user.setLoginIp(null);
        userMapper.insert(user);
        return true;
    }
}




