package edu.whut.config.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.whut.response.Result;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationEntryPointFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthEntryHandler implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json; charset=utf-8");
        String value = new ObjectMapper().writeValueAsString(Result.error("未登陆，无权限"));
        response.getWriter().write(value);
    }
}
