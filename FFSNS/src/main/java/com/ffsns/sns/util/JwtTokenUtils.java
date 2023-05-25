package com.ffsns.sns.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

public class JwtTokenUtils {

    public static boolean isExpired(String token, String key){
    // token 의 유효기간이 존재하는지
        Date expiredDate = extractClaims(token , key).getExpiration();
        return expiredDate.before(new Date());
    }
    public static String getUserName(String token , String key){
        return extractClaims(token, key).get("userName", String.class);
    }
    public static Boolean validate(String token, String userName, String key) {
        String usernameByToken = getUserName(token, key);
        return usernameByToken.equals(userName) && !isExpired(token, key);
    }




    //token 에서 claims 를 가지고 와야 한다.
    private static Claims extractClaims(String token, String key){
        // key 값과 토큰을 받아와서 리턴해주는 것이다.
        return Jwts.parserBuilder().setSigningKey(getKey(key)).build().parseClaimsJws(token).getBody();
    }


   public static String generateToken(String userName, String key, long expiredTimeMs ){
       Claims claims = Jwts.claims();
       claims.put("userName",userName);

       return Jwts.builder()
               .setClaims(claims)
               .setIssuedAt(new Date(System.currentTimeMillis()))
               .setExpiration(new Date(System.currentTimeMillis()+ expiredTimeMs))
               .signWith(getKey(key), SignatureAlgorithm.HS256)
               .compact();

   }

   private static Key getKey(String key){
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
   }


}
