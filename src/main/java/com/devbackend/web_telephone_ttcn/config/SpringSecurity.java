package com.devbackend.web_telephone_ttcn.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.Collection;

@Configuration
@EnableWebSecurity
public class SpringSecurity{
    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests((authtize) -> authtize
                        .requestMatchers("/*","/category/*", "/product**/*", "/register/**", "/cart/*").permitAll()
                        .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form.loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .successHandler(((request, response, authentication) -> {
                            //kiểm tra vai trò của người dùng
                            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
                            for (GrantedAuthority authority : authorities) {
                                if (authority.getAuthority().equals("ROLE_ADMIN")) {
                                    // Nếu là admin thì chuyển hướng đến /admin
                                    response.sendRedirect("/admin/home");
                                    return;
                                }
                            }
                            //nếu không phải admin chuyển đến trang user
                            response.sendRedirect("/");
                        }))
                        .permitAll())
                .logout(logout -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/")
                        .permitAll())
                .exceptionHandling(e->e
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            //khi bị từ chối truy cập
                            response.sendRedirect("/access-denied");
                        })
                        .authenticationEntryPoint((request, response, authException) -> {
                            if (authException instanceof DisabledException) {
                                // Xử lý khi tài khoản bị vô hiệu hóa
                                response.sendRedirect("/login?disabled");
                            } else {
                                // Xử lý các trường hợp khác
                                response.sendRedirect("/login?error");
                            }
                        })
                );
        return http.build();
    }

    //cho phép truy cập vào các file tĩnh (css, js, img) không cần xác thực
    @Bean
    WebSecurityCustomizer webSecurityCustomizer() {
        return (web -> web.ignoring().requestMatchers( "/css/**", "/img/**", "/js/**"));
    }
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }
}
