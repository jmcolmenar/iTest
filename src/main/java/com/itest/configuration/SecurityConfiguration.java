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
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {


    @Autowired
    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
        // TODO: Delete this testing user. Will be added from database
        auth.inMemoryAuthentication().withUser("learner").password("password").roles("LEARNER");
        auth.inMemoryAuthentication().withUser("tutor").password("password").roles("TUTOR");
        auth.inMemoryAuthentication().withUser("admin").password("password").roles("ADMIN");
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/angularjs/**", "/css/**", "/img/**", "/fonts/**", "/index.html");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/learner_index.html", "/api/learner/**", "/learner/**").access("hasRole('ROLE_LEARNER')");
        http.authorizeRequests().antMatchers("/tutor_index.html", "/api/tutor/**", "/tutor/**").access("hasRole('ROLE_TUTOR')");
        http.authorizeRequests().antMatchers("/admin_index.html", "/api/admin/**", "/admin/**").access("hasRole('ROLE_ADMIN')");
        http.formLogin().successHandler(new CustomAuthenticationSuccessHandler()).failureUrl("/index.html");
        http.logout().logoutSuccessUrl("/");

        // TODO: Implement and use the remaining "handler" classes. For example (When it accesses to denied content)

        // Disable crsf token (Temporally)
        http.csrf().disable();
    }
}