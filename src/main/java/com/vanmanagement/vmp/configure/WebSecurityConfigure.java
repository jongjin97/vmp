package com.vanmanagement.vmp.configure;

import com.vanmanagement.vmp.jwt.Jwt;
import com.vanmanagement.vmp.jwt.JwtAuthenticationProvider;
import com.vanmanagement.vmp.jwt.JwtAuthenticationTokenFilter;
import com.vanmanagement.vmp.jwt.TokenInterceptor;
import com.vanmanagement.vmp.users.Role;
import com.vanmanagement.vmp.users.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;


@Configuration
@EnableWebSecurity
public class WebSecurityConfigure implements WebMvcConfigurer {

    private final Jwt jwt;
    private final TokenInterceptor tokenInterceptor;
    private final JwtTokenConfigure jwtTokenConfigure;

    public WebSecurityConfigure(Jwt jwt, TokenInterceptor tokenInterceptor, JwtTokenConfigure jwtTokenConfigure) {
        this.jwt = jwt;
        this.tokenInterceptor = tokenInterceptor;
        this.jwtTokenConfigure = jwtTokenConfigure;
    }
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter() {
        return new JwtAuthenticationTokenFilter(jwtTokenConfigure.getHeader(), jwt);
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public JwtAuthenticationProvider jwtAuthenticationProvider(UserService userService) {
        return new JwtAuthenticationProvider(userService);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf()
                .disable()
                .headers()
                .disable()
                .exceptionHandling()
//                .accessDeniedHandler(accessDeniedHandler)
//                .authenticationEntryPoint(unauthorizedHandler)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
//                .antMatchers("/api/users/login").permitAll()
//                .antMatchers("/api/token/**").hasRole(Role.USER.name())
                .antMatchers("/api/payment/**").hasRole(Role.USER.name())
                .antMatchers("/api/users/point").hasRole(Role.USER.name())
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .disable();
        http
                .addFilterBefore(jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/WEB-INF/**", "/static/**", "/templates/**", "/h2/**");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/users/login", "/api/users/signup");
    }

    @Bean
    public CorsRegistry corsRegistry(){
        CorsRegistry registry = new CorsRegistry();
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")
                .allowedHeaders("Content-Type, Access-Control-Allow-Origin", "Access-Control-Allow-Headers",
                        "Authorization", "X-Requested-With", "requestId", "Correlation-Id")
                .allowedMethods("OPTIONS", "GET", "POST", "PUT", "DELETE")
                .maxAge(3600);
        return registry;
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        corsConfiguration.setAllowedHeaders(Arrays.asList("Origin","Access-Control-Allow-Origin",
                "Content-Type","Accept","Authorization","Origin,Accept","X-Requested-With",
                "Access-Control-Request-Method","Access-Control-Request-Headers"));
        corsConfiguration.setExposedHeaders(Arrays.asList("Origin","Content-Type","Accept","Authorization",
                "Access-Control-Allow-Origin","Access-Control-Allow-Origin","Access-Control-Allow-Credentials"));
        corsConfiguration.setAllowedMethods(Arrays.asList("GET","PUT","POST","DELETE","OPTIONS"));
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(urlBasedCorsConfigurationSource);

    }
}