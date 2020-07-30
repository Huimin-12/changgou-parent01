package com.changgou.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    //获取cookie当中的jti数据值
    public String getJtiCookie(ServerHttpRequest request) {
        HttpCookie cookie = request.getCookies().getFirst("uid");
        if (cookie != null) {
            String jti = cookie.getValue();
            return jti;
        }
        return null;
    }
    //获取存储在redis当中的jwt令牌
    public String getJwtRides(String jti) {
        String jwt = stringRedisTemplate.boundValueOps(jti).get();
        return jwt;
    }
}
