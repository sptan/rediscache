package com.sptan.exec.rediscache.security;

import com.sptan.exec.framework.security.JwtUser;
import com.sptan.exec.framework.utils.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author liupeng
 */
@Slf4j
@Component
public class JwtAuthorizationTokenFilter extends OncePerRequestFilter {

    private static final String APP_API = "appApi";


    @Value("${jwt.online}")
    private String onlineKey;

    @Autowired
    @Qualifier("jwtUserDetailsServiceImpl")
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private RedisTemplate redisTemplate;

//    public JwtAuthorizationTokenFilter( UserDetailsService userDetailsService, JwtTokenUtil jwtTokenUtil,
//    RedisTemplate redisTemplate) {
//        this.userDetailsService = userDetailsService;
//        this.jwtTokenUtil = jwtTokenUtil;
//        this.redisTemplate = redisTemplate;
//    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String path = request.getRequestURI();
        if (!StringUtils.isEmpty(path) && path.indexOf(APP_API) >= 0) {
            chain.doFilter(request, response);
            return;
        }
        String authToken = jwtTokenUtil.getToken(request);
        OnlineUser onlineUser = null;
        try {
            onlineUser = (OnlineUser) redisTemplate.opsForValue().get(onlineKey + authToken);
        } catch (ExpiredJwtException e) {
            log.error(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        if (onlineUser != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // It is not compelling necessary to load the use details from the database. You could also store the
            // information
            // in the token and read it from it. It's up to you ;)
            JwtUser userDetails = (JwtUser) this.userDetailsService.loadUserByUsername(onlineUser.getUserName());
            // For simple validation it is completely sufficient to just check the token integrity. You don't have to
            // call
            // the database compellingly. Again it's up to you ;)
            if (jwtTokenUtil.validateToken(authToken, userDetails)) {
                UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        chain.doFilter(request, response);
    }
}
