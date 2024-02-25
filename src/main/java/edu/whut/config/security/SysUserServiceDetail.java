package edu.whut.config.security;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import edu.whut.domain.vo.LoginUserVO;
import edu.whut.mapper.UserMapper;
import edu.whut.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SysUserServiceDetail implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LoginUserVO loginUser=new LoginUserVO();
        // 根据账号查询用户
        LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName,username);
        User user = userMapper.selectOne(queryWrapper);
        log.info("sysUser=========>{}",user);
        if(ObjectUtil.isNotNull(user)){
            loginUser.setSysUser(user);
            loginUser.setId(user.getUid());
            loginUser.setToken(null);
            return loginUser;
        }
        return null;
    }
}
