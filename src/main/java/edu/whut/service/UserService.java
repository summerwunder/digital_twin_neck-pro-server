package edu.whut.service;

import edu.whut.domain.dto.LoginUserDTO;
import edu.whut.domain.vo.LoginUserVO;
import edu.whut.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.whut.response.Result;
import jakarta.servlet.http.HttpServletRequest;

/**
* @author wunder
* @description 针对表【t_user】的数据库操作Service
* @createDate 2024-02-24 19:22:05
*/
public interface UserService extends IService<User> {

    String checkUserPwd(LoginUserDTO loginUser, HttpServletRequest request);
}
