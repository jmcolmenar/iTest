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

import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {

        // Redirect depending of user roles
        httpServletResponse.sendRedirect(this.getRouteToRedirect(authentication));
    }

    /**
     *  Get the route to redirect depending of user's roles
     * @param authentication Authentication object
     * @return The route to redirect
     */
    public String getRouteToRedirect(Authentication authentication) {
        String route = null;

        // Get the roles of authenticated user
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

        // Redirect depending of user's roles
        if (roles.contains("ROLE_LEARNER")) {
            // Redirect to learner menu
            route = "learner_index.html";
        }else if(roles.contains("ROLE_TUTOR")){
            // Redirect to tutor menu
            route = "tutor_index.html";
        }else if(roles.contains("ROLE_ADMIN")){
            // Redirect to admin menu
            route = "admin_index.html";
        }else{
            // Redirect to index
            route = "index.html";
        }

        // Return the route to redirect
        return route;
    }
}