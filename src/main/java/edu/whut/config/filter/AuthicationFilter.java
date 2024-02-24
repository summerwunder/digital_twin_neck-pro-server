package edu.whut.config.filter;

import cn.hutool.core.util.ObjectUtil;
import edu.whut.constants.ConnectConstants;
import edu.whut.domain.vo.LoginUserVO;
import edu.whut.utils.JwtHelper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class AuthicationFilter extends OncePerRequestFilter {
    @Autowired
    private  JwtHelper jwtHelper;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getRequestURI().equals("/sys/user/login")){
            filterChain.doFilter(request,response);
            return;
        }
        // 获取登录用户
        LoginUserVO loginUserVO = (LoginUserVO) jwtHelper.getLoginUser(request);
        if(ObjectUtil.isNotNull(loginUserVO)){
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginUserVO, null, loginUserVO.getAuthorities());
            // 将用户信息存储到SecurityContext中，SecurityContext存储到SecurityContextHolder中
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        filterChain.doFilter(request,response);
    }
}
