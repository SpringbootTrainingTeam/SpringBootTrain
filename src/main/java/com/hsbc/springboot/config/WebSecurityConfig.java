package com.hsbc.springboot.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    private final BootUserDetailsService bootUserDetailsService;

    @Autowired
    public WebSecurityConfig(BootUserDetailsService bootUserDetailsService) {
        this.bootUserDetailsService = bootUserDetailsService;
    }

    /**
     * authentication manager
     *
     * @param auth authentication manager builder
     * @throws Exception exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(bootUserDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/css/**", "/js/**", "/img/**", "/layui/**").permitAll()
                .anyRequest().authenticated()

                .and().formLogin().loginPage("/index")
                .loginProcessingUrl("/login").defaultSuccessUrl("/repository",true)
                .failureUrl("/login?error").permitAll()
                .and().sessionManagement().invalidSessionUrl("/sign_in")
                .and().rememberMe().tokenValiditySeconds(1209600)
                .and().logout().logoutSuccessUrl("/logout").permitAll()
                .and().csrf().disable();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encode = passwordEncoder.encode("123");
        log.info("123 encoded by BCryPasswordEncoder is : {}", encode);
        return new BCryptPasswordEncoder();
    }
}
