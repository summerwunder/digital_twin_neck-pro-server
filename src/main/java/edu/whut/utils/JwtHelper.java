package edu.whut.utils;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.druid.util.StringUtils;
import edu.whut.constants.CacheConstants;
import edu.whut.constants.ConnectConstants;
import edu.whut.domain.vo.LoginUserVO;
import edu.whut.utils.redis.RedisCacheUtil;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Data
@Component
@ConfigurationProperties(prefix = "jwt.token")
@Slf4j
public class JwtHelper {

    private  long tokenExpiration; //有效时间,单位毫秒 1000毫秒 == 1秒
    private  String tokenSignKey;  //当前程序签名秘钥

    @Autowired
    private RedisCacheUtil redisCacheUtil;
    //生成token字符串
    public  String createToken(String uuid) {
        System.out.println("tokenExpiration = " + tokenExpiration);
        System.out.println("tokenSignKey = " + tokenSignKey);
        String token = Jwts.builder()
                .setSubject("YYGH-USER")
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration*1000*60)) //单位分钟
                .claim("uuid", uuid)
                .signWith(SignatureAlgorithm.HS512, tokenSignKey)
                .compressWith(CompressionCodecs.GZIP)
                .compact();
        return token;
    }

    //从token字符串获取userid
    public  String getUuid(String token) {
        if(StringUtils.isEmpty(token)) return null;
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        String uuId = (String)claims.get("uuid");
        return uuId;
    }



    //判断token是否有效
    public  boolean isExpiration(String token){
        try {
            boolean isExpire = Jwts.parser()
                    .setSigningKey(tokenSignKey)
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration().before(new Date());
            //没有过期，有效，返回false
            return isExpire;
        }catch(Exception e) {
            //过期出现异常，返回true
            return true;
        }
    }

    public void refreshRedisToken(LoginUserVO loginUserVO){
        redisCacheUtil.setCacheObject(CacheConstants.LOGIN_USER_KEY+
               loginUserVO.getToken(),loginUserVO,30, TimeUnit.MINUTES);
    }

    public LoginUserVO getLoginUser(HttpServletRequest request) {
        String token = request.getHeader(ConnectConstants.tokenHeader);
        if(ObjectUtil.isNotEmpty(token)){
            //LoginUserVO loginUserVO = redisCacheUtil.getCacheObject(token);
            if(!isExpiration(token)){
                // 解析token
                String uuid = getUuid(token);
                redisCacheUtil.getCacheObject(CacheConstants.LOGIN_USER_KEY+token);
            }
            // 从redis中获取数据
            LoginUserVO loginUserVO = redisCacheUtil.getCacheObject(CacheConstants.LOGIN_USER_KEY + token);
            log.info("[getLoginUser]loginUserVo---------->{}",loginUserVO);
            // 获取登录时间
            LocalDateTime loginTime = loginUserVO.getSysUser().getLoginTime();
            // 获取当前时间
            LocalDateTime currentTime = LocalDateTime.now();
            // 计算时间间隔（分钟）
            long minutesDiff = ChronoUnit.MINUTES.between(loginTime, currentTime);
            if (minutesDiff >= 20) {
                refreshRedisToken(loginUserVO);
            }
            return loginUserVO;
        }
        return null;
    }
}
