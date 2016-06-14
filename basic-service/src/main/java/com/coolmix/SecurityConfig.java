package com.coolmix;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Autowired
	DataSource dataSource;

	@Autowired
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
  	  auth.jdbcAuthentication().dataSource(dataSource)
  		.usersByUsernameQuery(
  			"select username,password, enabled from users where username=?")
  		.authoritiesByUsernameQuery(
  			"select username, role from user_roles where username=?");
    }
    
	@Configuration
	@Order(1)                                                        
	public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.authorizeRequests()
				.antMatchers("/service/**")
				.access("hasRole('ROLE_ADMIN')")                    
			.and()
	        	.httpBasic()
			.and()
        		.csrf().disable();
		}
	}
	
	@Configuration                                                   
	public static class FormLoginWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.authorizeRequests()
				.antMatchers("/hello").access("hasRole('ROLE_ADMIN')")		
				.anyRequest().permitAll()
			.and()
				.formLogin().loginPage("/login")
				.permitAll()
				.usernameParameter("username").passwordParameter("password")
			.and()
				.logout().logoutSuccessUrl("/login?logout")	
			.and()
				.exceptionHandling().accessDeniedPage("/403")
			.and()
				.csrf();
		}
	}


}