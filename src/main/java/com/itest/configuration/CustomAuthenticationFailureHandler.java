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
import com.itest.jsonModel.ErrorMessage;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Return 401 error code when login is not successful and an error message in JSON format
 */
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        // Return 401 error code (Unauthorized)
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // Set content type of response to JSON
        httpServletResponse.setContentType("application/json");

        // Return the error message in JSON
        ObjectMapper mapper = new ObjectMapper();
        ErrorMessage errorMessage = new ErrorMessage(e.getMessage());
        String errorMessageJson = mapper.writeValueAsString(errorMessage);
        httpServletResponse.getWriter().write(errorMessageJson);
    }
}