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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    @Qualifier("customAuthenticationSuccessHandler")
    private CustomAuthenticationSuccessHandler authenticationSuccessHandler;

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
        http.authorizeRequests().antMatchers("/api/user/**", "/user/**").access("hasAnyAuthority('"+ CustomAuthenticationSuccessHandler.ROLE_LEARNER +"','"+ CustomAuthenticationSuccessHandler.ROLE_KID +"','"+ CustomAuthenticationSuccessHandler.ROLE_TUTOR +"','"+ CustomAuthenticationSuccessHandler.ROLE_ADMIN +"')");
        http.authorizeRequests().antMatchers("/api/learner/**", "/learner/**").access("hasAnyAuthority('"+ CustomAuthenticationSuccessHandler.ROLE_LEARNER +"','"+ CustomAuthenticationSuccessHandler.ROLE_KID +"')");
        http.authorizeRequests().antMatchers("/api/tutor/**", "/tutor/**").access("hasAuthority('"+ CustomAuthenticationSuccessHandler.ROLE_TUTOR +"')");
        http.authorizeRequests().antMatchers("/api/admin/**", "/admin/**").access("hasAuthority('"+ CustomAuthenticationSuccessHandler.ROLE_ADMIN +"')");

        // Specifies the AuthenticationEntryPoint and AccessDeniedHandler to be used
        http.exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint());
        http.exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler());

        // Specifies the AuthenticationSuccessHandler and AuthenticationFailureHandler to be used
        http.formLogin().successHandler(this.authenticationSuccessHandler);
        http.formLogin().failureHandler(new CustomAuthenticationFailureHandler());

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