package com.sptan.exec.framework.utils;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sptan.exec.framework.security.JwtUser;
import com.sptan.exec.framework.user.MemberProfile;
import com.sptan.exec.framework.user.UserProfile;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author liupeng
 */
@Component
public class JwtTokenUtil implements Serializable {

    private static final long serialVersionUID = -3301605591108950415L;
    private Clock clock = DefaultClock.INSTANCE;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    @Value("${jwt.appExpiration}")
    private Long appExpiration;

    @Value("${jwt.header}")
    private String tokenHeader;

    public UserProfile getUserProfile(String token) {
        if (token.indexOf("Bearer") == 0) {
            token = token.substring(6).trim();
        }
        if (org.springframework.util.StringUtils.isEmpty(token)) {
            return null;
        }
        String str = getClaimFromToken(token, Claims::getSubject);
        UserProfile userProfile = JSONObject.parseObject(str, UserProfile.class);
        return userProfile;
    }


    private Date getIssuedAtDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getIssuedAt);
    }

    private Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(clock.now());
    }

    private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
        return (lastPasswordReset != null && created.before(lastPasswordReset));
    }

    private Boolean ignoreTokenExpiration(String token) {
        // here you specify tokens, for that the expiration is ignored
        return false;
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        JwtUser jwtUser = (JwtUser) userDetails;
        UserProfile userProfile = buildUserProfile(jwtUser);
        return doGenerateToken(claims, JSON.toJSONString(userProfile), appExpiration);
    }

    public String generateAppToken(MemberProfile memberProfile) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, JSON.toJSONString(memberProfile), appExpiration);
    }


    private UserProfile buildUserProfile(JwtUser jwtUser) {
        UserProfile result = new UserProfile(jwtUser.getId(), jwtUser.getUsername());
        return result;
    }

    private String doGenerateToken(Map<String, Object> claims, String subject, Long appExpiration) {
        final Date createdDate = clock.now();
        final Date expirationDate = calculateExpirationDate(createdDate, appExpiration);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public Boolean canTokenBeRefreshed(String token, Date lastPasswordReset) {
        final Date created = getIssuedAtDateFromToken(token);
        return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset)
                && (!isTokenExpired(token) || ignoreTokenExpiration(token));
    }

    public String refreshToken(String token) {
        final Date createdDate = clock.now();
        final Date expirationDate = calculateExpirationDate(createdDate, appExpiration);

        final Claims claims = getAllClaimsFromToken(token);
        claims.setIssuedAt(createdDate);
        claims.setExpiration(expirationDate);

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public String getToken(HttpServletRequest request) {
        final String requestHeader = request.getHeader(tokenHeader);
        if (requestHeader != null && requestHeader.startsWith("Bearer")) {
            return requestHeader.substring(6).trim();
        }
        return null;
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        JwtUser user = (JwtUser) userDetails;
        final Date created = getIssuedAtDateFromToken(token);
//        final Date expiration = getExpirationDateFromToken(token);
//        如果token存在，且token创建日期 > 最后修改密码的日期 则代表token有效
        return (!isTokenExpired(token)
                && !isCreatedBeforeLastPasswordReset(created, user.getLastPasswordResetDate())
        );
    }

    private Date calculateExpirationDate(Date createdDate, Long appExpiration) {
        if (appExpiration == null || 0L == appExpiration) {
            return new Date(createdDate.getTime() + expiration);
        } else {
            return new Date(createdDate.getTime() + appExpiration);
        }
    }
}

