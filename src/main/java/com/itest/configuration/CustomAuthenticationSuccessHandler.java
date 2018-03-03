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
import com.itest.model.CurrentUserModel;
import com.itest.service.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * Return the current logged user
 */
@Component("customAuthenticationSuccessHandler")
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    @Qualifier("userManagementServiceImpl")
    UserManagementService userManagementService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        // Return 200 code (Ok)
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);

        // Set content type of response to JSON
        httpServletResponse.setContentType("application/json");

        // Check if the user is a valid user
        boolean isValidUser = this.userManagementService.isAuthorizedUser(authentication);

        // Return the current user in JSON format
        ObjectMapper mapper = new ObjectMapper();
        CurrentUserModel currentUserModel = new CurrentUserModel(isValidUser, authentication.getName());
        String loggedUserJson = mapper.writeValueAsString(currentUserModel);
        httpServletResponse.getWriter().print(loggedUserJson);
    }
}