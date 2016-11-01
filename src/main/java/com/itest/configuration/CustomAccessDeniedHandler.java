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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Returns 401 error (Unauthorized) and an error message in JSON format. For default Spring-Security redirect to login page
 */
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        // Return 401 error code (Unauthorized) with exception message
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
