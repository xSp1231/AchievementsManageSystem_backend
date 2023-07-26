package com.example.infomanagesystem.utils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtUtils {
    //static 关键字修饰的成员变量或方法，可以在没有创建对象的情况下直接调用，也可以直接通过类名来调用，
    // 而不需要通过对象来调用。这是因为 static 成员变量和方法是属于类的
    // 而不是属于某个对象的，所以可以通过类名来调用。
    //jwt由三部分构成
    private static final String SECRET_KEY = "xspxspxsp";
    private static final long EXPIRATION_TIME = 86400000/2; // 12 hours

    public static String generateToken(String username,String role) {  //生成jwt
        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRATION_TIME); //设置过期时间

        return Jwts.builder()
                .setSubject(username)
                .claim("role",role)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }
    public static String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public static String getRoleFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();

        return (String) claims.get("role");
    }


    public static String getSubjectFromToken(String token) { //getSubjectFromToken 方法用于从 JWT 中获取 subject，即用户名等信息
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public static boolean validateToken(String token) { //判断是否合法
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            System.out.println("异常"+e.getMessage());
            return false;
        }
    }
}
