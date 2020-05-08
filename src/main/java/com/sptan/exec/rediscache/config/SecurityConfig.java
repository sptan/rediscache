package com.sptan.exec.rediscache.config;

import com.sptan.exec.rediscache.annotation.AnonymousAccess;
import com.sptan.exec.rediscache.security.JwtAuthenticationEntryPoint;
import com.sptan.exec.rediscache.security.JwtAuthorizationTokenFilter;
import com.sptan.exec.rediscache.security.PrivilegeFilterInvocationSecurityMetadataSource;
import com.sptan.exec.rediscache.service.JwtUserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * The type Security config.
 *
 * @author liupeng
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtAuthenticationEntryPoint unauthorizedHandler;

    private final JwtUserDetailsServiceImpl jwtUserDetailsService;

    private final ApplicationContext applicationContext;

    /**
     * 自定义基于JWT的安全过滤器
     */
    private final JwtAuthorizationTokenFilter authenticationTokenFilter;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private PrivilegeFilterInvocationSecurityMetadataSource privilegeFilterInvocationSecurityMetadataSource;

    /**
     * Instantiates a new Security config.
     *
     * @param unauthorizedHandler       the unauthorized handler
     * @param jwtUserDetailsService     the jwt user details service
     * @param authenticationTokenFilter the authentication token filter
     * @param applicationContext        the application context
     */
    public SecurityConfig(JwtAuthenticationEntryPoint unauthorizedHandler,
                          JwtUserDetailsServiceImpl jwtUserDetailsService,
                          JwtAuthorizationTokenFilter authenticationTokenFilter,
                          ApplicationContext applicationContext) {
        this.unauthorizedHandler = unauthorizedHandler;
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.authenticationTokenFilter = authenticationTokenFilter;
        this.applicationContext = applicationContext;
    }

    /**
     * Configure global.
     *
     * @param auth the auth
     * @throws Exception the exception
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .userDetailsService(jwtUserDetailsService)
            .passwordEncoder(passwordEncoderBean());
    }

    /**
     * Granted authority defaults granted authority defaults.
     *
     * @return the granted authority defaults
     */
    @Bean
    GrantedAuthorityDefaults grantedAuthorityDefaults() {
        // Remove the ROLE_ prefix
        return new GrantedAuthorityDefaults("");
    }

    /**
     * Password encoder bean password encoder.
     *
     * @return the password encoder
     */
    @Bean
    public PasswordEncoder passwordEncoderBean() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        Map<RequestMappingInfo, HandlerMethod> handlerMethodMap =
            applicationContext.getBean(RequestMappingHandlerMapping.class).getHandlerMethods();
        Set<String> anonymousUrls = new HashSet<>();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> infoEntry : handlerMethodMap.entrySet()) {
            HandlerMethod handlerMethod = infoEntry.getValue();
            AnonymousAccess anonymousAccess = handlerMethod.getMethodAnnotation(AnonymousAccess.class);
            PreAuthorize preAuthorize = handlerMethod.getMethodAnnotation(PreAuthorize.class);
            anonymousUrls.addAll(infoEntry.getKey().getPatternsCondition().getPatterns());
        }
        httpSecurity
            // 禁用 CSRF
            .csrf().disable()
            // 授权异常
            .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
            // 不创建会话
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            // 过滤请求
            .authorizeRequests()
            .antMatchers(
                HttpMethod.GET,
                "/*.html",
                "/**/*.html",
                "/**/*.css",
                "/**/*.js"
            ).permitAll()
            // swagger start
            .antMatchers("/swagger-ui.html").permitAll()
            .antMatchers("/configuration/**").permitAll()
            .antMatchers("/swagger-resources/**").permitAll()
            .antMatchers("/webjars/**").permitAll()
            .antMatchers("/*/api-docs").permitAll()
            // swagger end
            // 文件
            .antMatchers("/avatar/**").permitAll()
            .antMatchers("/file/**").permitAll()
            // 放行OPTIONS请求
            .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            .antMatchers("/druid/**").permitAll()
            // 自定义匿名访问所有url放行 ： 允许 匿名和带权限以及登录用户访问
            .antMatchers(anonymousUrls.toArray(new String[0])).permitAll()
            // 所有请求都需要认证
            .anyRequest().authenticated()
            // 防止iframe 造成跨域
            .and().headers().frameOptions().disable();
        httpSecurity
            .addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        //定义过滤的url
        String[] permits = new String[] {
            "/h5/**",
            "/oauth/**",
            "/basic/oauth/**",
            "/index.html",
            "/app/**",
            "/extcore/**",
            "/login",
            "/"
        };
    }
}
