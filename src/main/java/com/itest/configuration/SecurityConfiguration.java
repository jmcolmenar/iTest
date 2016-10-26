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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    CustomAccessDeniedHandler accessDeniedHandler;

    @Autowired
    CustomAuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    CustomAuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
        // TODO: Delete this testing user. Will be added from database
        auth.inMemoryAuthentication().withUser("learner").password("password").roles("LEARNER");
        auth.inMemoryAuthentication().withUser("tutor").password("password").roles("TUTOR");
        auth.inMemoryAuthentication().withUser("admin").password("password").roles("ADMIN");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Authorizes the requests to resources depending of user's roles
        http.authorizeRequests().antMatchers("/api/learner/**", "/learner/**").access("hasRole('ROLE_LEARNER')");
        http.authorizeRequests().antMatchers("/api/tutor/**", "/tutor/**").access("hasRole('ROLE_TUTOR')");
        http.authorizeRequests().antMatchers("/api/admin/**", "/admin/**").access("hasRole('ROLE_ADMIN')");

        // Specifies the AuthenticationEntryPoint and AccessDeniedHandler to be used
        http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);
        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler);

        // Specifies the AuthenticationSuccessHandler to be used when login is successfull
        http.formLogin().successHandler(authenticationSuccessHandler);

        // TODO: URL to send users if authentication fails
        http.formLogin().failureUrl("/login_error.html");

        // The URL to redirect to after logout has occurred. Redirects to index action of login controller and delete the session cookie
        http.logout().logoutSuccessUrl("/").permitAll().clearAuthentication(false).invalidateHttpSession(false).permitAll();

        // Disable CSRF Token (Temporally)
        http.csrf().disable();
    }
}