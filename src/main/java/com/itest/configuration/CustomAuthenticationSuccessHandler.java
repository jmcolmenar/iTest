/*

This file is part of iTest.

Copyright (C) 2016
   Marcos Martinez Cañete(mmartinezcan@alumnos.urjc.es)
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itest.jsonModel.CurrentUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;


/**
 * Return the current logged user
 */
@Component("customAuthenticationSuccessHandler")
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    // Roles of users
    public static final String ROLE_LEARNER = "LEARNER";
    public static final String ROLE_KID = "KID";
    public static final String ROLE_TUTOR = "TUTOR";
    public static final String ROLE_ADMIN = "ADMIN";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        // Return 200 code (Ok)
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);

        // Set content type of response to JSON
        httpServletResponse.setContentType("application/json");

        // Check if the user is a valid user
        boolean isValidUser = this.isAuthenticatedUser(authentication);

        // Return the current user in JSON format
        ObjectMapper mapper = new ObjectMapper();
        CurrentUser currentUser = new CurrentUser(isValidUser, authentication.getName());
        String loggedUserJson = mapper.writeValueAsString(currentUser);
        httpServletResponse.getWriter().print(loggedUserJson);
    }

     /**
     * Check if the current user in the Authentication context is valid
     * @param auth The current authentication
     * @return If the current user is a valid user
     */
    public boolean isAuthenticatedUser(Authentication auth) {
        boolean authenticated = false;

        // Get the roles of authenticated user
        Set<String> roles = AuthorityUtils.authorityListToSet(auth.getAuthorities());

        // Check if there is an authenticated user with the Admin, Tutor or Learner role
        if(roles != null && !roles.isEmpty()){
            authenticated = roles.contains(ROLE_LEARNER) || roles.contains(ROLE_KID) || roles.contains(ROLE_TUTOR) || roles.contains(ROLE_ADMIN);
        }

        return authenticated;
    }
}