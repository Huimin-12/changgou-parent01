package com.changgou.oauth;

import org.junit.Test;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;

public class ParseJwtTest {

    @Test
    public void parseJwt(){
        //基于公钥去解析jwt
        String jwt ="eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhZGRyZXNzIjoiYmVpamluZyIsImNvbXBhbnkiOiJIdWltaW4ifQ.KkJyhfPX4JpAtvYZq3cDDzrbeVGcMHnmVVTKwH9uqFhPktspPKb6REDEbXUTNb0KHzUE20WhLYTjAp0kGf24po_FCgi_OipeJLxlvszQkuAmSgZrzyXsAAaInepYTrXx9GWHuLJtATKVs_bs-CK8Kwq8oh5uUXV3LjRYtpAgnTz_Ua_cks-cV9X_wy_Ih9CD_tUJoZFWebPfe6dD1dEYmX1bQiG4GvVsZqPe9t9m9VZOKfyD0sfVWiYSTn2h3qqTFiDUwH3uthFRR6Y7SpzvyADo1FHbM0krTGKvxYoCzUgYulv0EX8Mb422EXP5hTPfYWZbGffnaQOvENhM4fChFQ";

        String publicKey ="-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvFsEiaLvij9C1Mz+oyAmt47whAaRkRu/8kePM+X8760UGU0RMwGti6Z9y3LQ0RvK6I0brXmbGB/RsN38PVnhcP8ZfxGUH26kX0RK+tlrxcrG+HkPYOH4XPAL8Q1lu1n9x3tLcIPxq8ZZtuIyKYEmoLKyMsvTviG5flTpDprT25unWgE4md1kthRWXOnfWHATVY7Y/r4obiOL1mS5bEa/iNKotQNnvIAKtjBM4RlIDWMa6dmz+lHtLtqDD2LF1qwoiSIHI75LQZ/CNYaHCfZSxtOydpNKq8eb1/PGiLNolD4La2zf0/1dlcr5mkesV570NxRmU1tFm8Zd3MZlZmyv9QIDAQAB-----END PUBLIC KEY-----";

        Jwt token = JwtHelper.decodeAndVerify(jwt, new RsaVerifier(publicKey));

        String claims = token.getClaims();
        System.out.println(claims);
    }
}
