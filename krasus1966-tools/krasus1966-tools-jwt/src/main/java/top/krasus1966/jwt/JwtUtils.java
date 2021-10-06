package top.krasus1966.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;

import java.util.Date;
import java.util.Map;

/**
 * @author Krasus1966
 * @date 2021/5/18 11:58
 **/
public class JwtUtils {

    /**
     * 颁发jwt
     *
     * @param id         id
     * @param claims     需要保存的信息
     * @param time       超时时间
     * @param secretInfo 加密方式
     * @return
     */
    public static String createjwt(String id, Map<String, Object> claims, Long time, SecretInfo secretInfo) {
        Date now = new Date(System.currentTimeMillis());
        long nowMillis = System.currentTimeMillis();
        JwtBuilder builder = Jwts.builder()
                .setClaims(claims)
                .setId(id)
                .setIssuedAt(now)
                .signWith(secretInfo.getSignatureAlgorithm(), secretInfo.getKey());
        if (time >= 0) {
            long expMillis = nowMillis + time;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }
        return builder.compact();
    }


    /**
     * 验证并获得jwt信息
     *
     * @param token      jwt
     * @param secretInfo 解密方式
     * @return
     */
    public static Claims verifyJwt(String token, SecretInfo secretInfo) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secretInfo.getKey())
                    .parseClaimsJws(token).getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }
}
