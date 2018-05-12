/*

This file is part of iTest.

Copyright (C) 2016
   Marcos Martinez Cañete (mmartinezcan@alumnos.urjc.es)
   Jose Manuel Colmenar Verdugo (josemanuel.colmenar@urjc.es)

iTest is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

iTest is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.
You should have received a copy of the GNU General Public License
along with iTest.  If not, see <http://www.gnu.org/licenses/>.

*/
package com.itest.configuration;

import javax.sql.DataSource;

import com.itest.constant.UserRoleConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    @Qualifier("customAccessDeniedHandler")
    private AccessDeniedHandler accessDeniedHandler;

    @Autowired
    @Qualifier("customAuthenticationEntryPoint")
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    @Qualifier("customAuthenticationFailureHandler")
    private AuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    @Qualifier("customAuthenticationSuccessHandler")
    private AuthenticationSuccessHandler authenticationSuccessHandler;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private DataSource dataSource;

    //@Autowired
    //CustomCsrfTokenRepository tokenRepository;

    @Autowired
    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
        // Configure the authentication through database. Use MD5 algorithm to encode the password
        auth.jdbcAuthentication()
                .dataSource(this.dataSource)
                .passwordEncoder(new Md5PasswordEncoder())
                .usersByUsernameQuery("select usuario as username, passw as password, (1=1) as enabled from usuarios where usuario = ?")
                .authoritiesByUsernameQuery("select usuario as username, permiso as authority from permisos where usuario = ?");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Authorizes the requests to resources depending of user's roles
        http.authorizeRequests().mvcMatchers("/api/user/checkSession").permitAll();
        http.authorizeRequests().antMatchers("/api/user/**", "/user/**").access("hasAnyAuthority('"+ UserRoleConstant.ROLE_LEARNER +"','"+ UserRoleConstant.ROLE_KID +"','"+ UserRoleConstant.ROLE_TUTOR  +"','"+ UserRoleConstant.ROLE_ADVANCED_TUTOR +"','"+ UserRoleConstant.ROLE_ADMIN +"')");
        http.authorizeRequests().antMatchers("/api/learner/**", "/learner/**").access("hasAnyAuthority('"+ UserRoleConstant.ROLE_LEARNER +"','"+ UserRoleConstant.ROLE_KID +"')");
        http.authorizeRequests().antMatchers("/api/tutor/**", "/tutor/**").access("hasAnyAuthority('"+ UserRoleConstant.ROLE_TUTOR +"','"+ UserRoleConstant.ROLE_ADVANCED_TUTOR+"')");
        http.authorizeRequests().antMatchers("/api/admin/**", "/admin/**").access("hasAuthority('"+ UserRoleConstant.ROLE_ADMIN +"')");

        // Specifies the AuthenticationEntryPoint and AccessDeniedHandler to be used
        http.exceptionHandling().authenticationEntryPoint(this.authenticationEntryPoint);
        http.exceptionHandling().accessDeniedHandler(this.accessDeniedHandler);

        // Specifies the AuthenticationSuccessHandler and AuthenticationFailureHandler to be used
        http.formLogin().successHandler(this.authenticationSuccessHandler);
        http.formLogin().failureHandler(this.authenticationFailureHandler);

        // The URL to redirect to after logout has occurred. Redirects to index action of login controller and delete the session cookie
        http.logout().logoutSuccessUrl("/");

        // Disable CSRF Token (Temporally)
        http.csrf().disable();

        // TODO: Set the token repository by CookieCsrfTokenRepository setting the Cookie name
        //CookieCsrfTokenRepository csrfRepository = new CookieCsrfTokenRepository();
        //csrfRepository.setCookieName("XSRF-TOKEN");
        //csrfRepository.setCookieHttpOnly(false);
        //http.csrf().csrfTokenRepository(csrfRepository);

        // TODO: Or set the token repository by custom
        //http.csrf().csrfTokenRepository(tokenRepository);
    }
}