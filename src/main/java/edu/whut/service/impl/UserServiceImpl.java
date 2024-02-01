package edu.whut.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.whut.pojo.User;
import edu.whut.service.UserService;
import edu.whut.mapper.UserMapper;
import edu.whut.utils.JwtHelper;
import edu.whut.utils.Result;
import edu.whut.utils.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
* @author wunder
* @description 针对表【t_user】的数据库操作Service实现
* @createDate 2024-02-01 12:02:14
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtHelper jwtHelper;
    @Override
    public Result getLogin(User user) {
        LambdaQueryWrapper<User> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername,user.getUsername());
        User loginUser = userMapper.selectOne(wrapper);
        if (loginUser == null) {
            return Result.build(null, ResultCodeEnum.USERNAME_ERROR);
        }

        if(!(StringUtils.isEmpty(user.getUserPwd()))&&
                user.getUserPwd().equals(loginUser.getUserPwd())){
            System.out.println(loginUser.getUid().longValue());
            String token= jwtHelper.createToken(loginUser.getUid().longValue());
            Map map=new HashMap();
            map.put("token",token);
            return Result.ok(map);
        }else{
            return Result.build(null,ResultCodeEnum.PASSWORD_ERROR);
        }
    }
}




