package com.xu.blog.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.xu.blog.enums.token.TokenEnum;
import com.xu.blog.param.UserToken;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;

@Component
public class JWTUtil {

    private static long timeOut = 1200000000; //20分钟

    //解析Token，同时也能验证Token，当验证失败返回null
    public static Map<String, Claim> analysisToken(String token) {
        JWTVerifier verifier = JWT.require(getAlgorithm()).build();
        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaims();
    }

    //根据密钥获取签名
    public static Algorithm getAlgorithm() {
        try {
            return Algorithm.HMAC512(TokenEnum.KEY.getCode());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(TokenEnum.ERROR_TOKEN_KEY.getMessage());
        }
    }


    /**
     * 获取token
     *
     * @return
     */
    public static String createToken(UserToken userToken) {
        String token = JWT.create()
                .withClaim("user", JSON.toJSONString(userToken))
                .withClaim("userName", userToken.getUserName())
                .withExpiresAt(new Date(System.currentTimeMillis() + timeOut))
                .sign(JWTUtil.getAlgorithm());
        return token;
    }

    public static void main(String[] args) {
        UserToken userToken=new UserToken();

        userToken.setUserName("张三丰");
        String token = createToken(userToken);
        System.out.println(token);
//        String token = JWT.create()
//                .withClaim("user", JSONObject.toJSONString(userToken))
//                .withClaim("userName", JSONObject.toJSONString(userToken))
//                .withExpiresAt(new Date(System.currentTimeMillis() + 1200000000000l))
//                .sign(JWTUtil.getAlgorithm());
//        System.out.println(token);
        Long time = System.currentTimeMillis();
        int time5int = (int) ((time - (time % (1000 * 60 * 1))) / 1000);
        Long time5 = time5int * 1000L  ;
        Long time10 = time5 - 1000 * 60 * 1;

        String md51 = DigestUtils.md5DigestAsHex((TokenEnum.KEY.getCode() + time5).getBytes()).toUpperCase();
        System.out.println(md51);
        Map<String, Claim> stringClaimMap = analysisToken(token);
        for (String s : stringClaimMap.keySet()) {
            System.out.println(s);
            System.out.println((stringClaimMap.get(s).asString()));
        }
        if (stringClaimMap.containsKey("user")) {
            String tokenStr = stringClaimMap.get("user").asString();
            UserToken userToken2 = JSONObject.parseObject(tokenStr, UserToken.class);
            //System.out.println(userToken2);
        }
    }
}
