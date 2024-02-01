package edu.whut.service;

import edu.whut.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.whut.utils.Result;

/**
* @author wunder
* @description 针对表【t_user】的数据库操作Service
* @createDate 2024-02-01 12:02:14
*/
public interface UserService extends IService<User> {

    Result getLogin(User user);
}
