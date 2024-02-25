package edu.whut.utils.security;

import cn.hutool.core.util.ObjectUtil;
import edu.whut.constants.HttpStatus;
import edu.whut.domain.vo.LoginUserVO;
import edu.whut.exception.ServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 获取登陆的用户信息
 */
public class SecurityUtil {

    /**
     * 获取  Authentication
     * @return
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 获取用户
     */
    public static LoginUserVO getLoginUser() {
        return (LoginUserVO) getAuthentication().getPrincipal();
    }

    /**
     * 获取用户id
     * @return
     */
    public static Integer getUserId() {
        Integer userId = getLoginUser().getId();
        if(ObjectUtil.isNull(userId)) {
            throw new ServiceException(HttpStatus.FORBIDDEN,"禁止操作");
        }
        return userId;
    }

    /**
     * 获取用户名
     * @return
     */
    public static String getUsername() {
        String username = getLoginUser().getUsername();
        if(ObjectUtil.isNull(username)) {
            throw new ServiceException(HttpStatus.FORBIDDEN,"禁止操作");
        }
        return username;
    }

}
