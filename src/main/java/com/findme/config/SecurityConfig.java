package com.findme.config;


import com.findme.models.RoleStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private String query =
            "select u.email_address, r.role_status " +
            "from users u " +
            "join users_role ur on u.id = ur.user_id " +
            "join role r on ur.role_id = r.id where u.email_address = ?";



    private final DriverManagerDataSource dataSource;

    @Autowired
    public SecurityConfig(DriverManagerDataSource dataSource) {
        this.dataSource = dataSource;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .authoritiesByUsernameQuery(query)
                .dataSource(dataSource);
    }


    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeRequests()
                .antMatchers("/", "/registration", "/login").permitAll()
                .antMatchers("/user/*").hasAnyAuthority(RoleStatus.USER.toString())
                .antMatchers("/delete-post").hasAnyAuthority(RoleStatus.ADMIN.toString(), RoleStatus.SUPER_ADMIN.toString())
                .antMatchers("/update-user").hasAuthority(RoleStatus.SUPER_ADMIN.toString())
                .anyRequest().authenticated();
    }
}