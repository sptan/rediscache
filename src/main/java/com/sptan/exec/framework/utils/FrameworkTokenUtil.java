package com.sptan.exec.framework.utils;


import com.alibaba.fastjson.JSONObject;
import com.sptan.exec.framework.user.MemberProfile;
import com.sptan.exec.framework.user.UserProfile;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClock;
import org.apache.commons.lang3.StringUtils;
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
public class FrameworkTokenUtil implements Serializable {

    private static final long serialVersionUID = -3301605591108950415L;
    private static Clock clock = DefaultClock.INSTANCE;

    private static String secret;

    private static Long expiration;

    private static String tokenHeader;


    @Value("${jwt.secret}")
    public void setSecret(String secret) {
        FrameworkTokenUtil.secret = secret;
    }

    @Value("${jwt.expiration}")
    public void setExpiration(Long expiration) {
        FrameworkTokenUtil.expiration = expiration;
    }

    @Value("${jwt.header}")
    public void setTokenHeader(String tokenHeader) {
        FrameworkTokenUtil.tokenHeader = tokenHeader;
    }

    public static String getUsernameFromToken(String token) {
        if (token.indexOf("Bearer") == 0) {
            token = token.substring(7);
        }
        if (org.springframework.util.StringUtils.isEmpty(token)) {
            return null;
        }
        return getClaimFromToken(token, Claims::getSubject);
    }


    public static UserProfile getUserProfile(String token) {
        if (org.springframework.util.StringUtils.isEmpty(token)) {
            return null;
        }
        if (token.indexOf("Bearer") == 0) {
            token = token.substring(6).trim();
        }
        if (org.springframework.util.StringUtils.isEmpty(token)) {
            return null;
        }
        String sub = getClaimFromToken(token, Claims::getSubject);
        UserProfile userProfile = JSONObject.parseObject(sub, UserProfile.class);
        if (StringUtils.contains(sub, "APP")) {
            userProfile = JSONObject.parseObject(sub, MemberProfile.class);
        }
        return userProfile;
    }

    private static Date getIssuedAtDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getIssuedAt);
    }

    private static Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    private static <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private static Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    private static Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(clock.now());
    }

    private static Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
        return (lastPasswordReset != null && created.before(lastPasswordReset));
    }

    private static Boolean ignoreTokenExpiration(String token) {
        // here you specify tokens, for that the expiration is ignored
        return false;
    }

    public static String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }

    private static String doGenerateToken(Map<String, Object> claims, String subject) {
        final Date createdDate = clock.now();
        final Date expirationDate = calculateExpirationDate(createdDate);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public static Boolean canTokenBeRefreshed(String token, Date lastPasswordReset) {
        final Date created = getIssuedAtDateFromToken(token);
        return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset)
                && (!isTokenExpired(token) || ignoreTokenExpiration(token));
    }

    public static String refreshToken(String token) {
        final Date createdDate = clock.now();
        final Date expirationDate = calculateExpirationDate(createdDate);

        final Claims claims = getAllClaimsFromToken(token);
        claims.setIssuedAt(createdDate);
        claims.setExpiration(expirationDate);

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public static String getToken(HttpServletRequest request) {
        final String requestHeader = request.getHeader(tokenHeader);
        if (requestHeader != null && requestHeader.startsWith("Bearer")) {
            return requestHeader.substring(6).trim();
        }
        return null;
    }

    private static Date calculateExpirationDate(Date createdDate) {
        return new Date(createdDate.getTime() + expiration);
    }
}

