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
package com.itest.controller;

import com.itest.configuration.CustomAuthenticationSuccessHandler;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;



/**
 * Controller for Login functionality
 */
@Controller("/")
public class LoginController {

    /**
     * Return the index page
     * @return Te view corresponding to index page
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(){
        // Get current authentication
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // Get the route to redirect depending of authentication
        CustomAuthenticationSuccessHandler customHandler = new CustomAuthenticationSuccessHandler();
        String redirectTo = customHandler.getRouteToRedirect(auth);

        return redirectTo;
    }

    /**
     * Get the username of authenticated user
     * @return The username (null: If there is not an authenticated user)
     */
    @RequestMapping(value = "/getusername", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String getUsername(){
        String name = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // Get the username if there is an authenticated user
        if(auth.isAuthenticated()){
            name = auth.getName();
        }

        return name;
    }
}