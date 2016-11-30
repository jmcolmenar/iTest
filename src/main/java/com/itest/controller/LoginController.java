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
import com.itest.jsonModel.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;


/**
 * Controller for Login functionality
 */
@Controller
public class LoginController {

    @Autowired
    CustomAuthenticationSuccessHandler authenticationSuccessHandler;

    /**
     * Return the index page as a view
     * @return The index page
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(HttpServletResponse response){
        // Set cache to "no-store" in the response to avoid caching the index page
        response.setHeader("Cache-Control", "no-store");

        // Return index page
        return "index";
    }

    /**
     * Redirect to corresponding index page of logged user
     * @return The index page of logged user
     */
    @RequestMapping(value = "/redirect", method = RequestMethod.GET)
    public String redirectTo(){
        // Get current authentication
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // Return the page to redirect
        return "redirect:/" + authenticationSuccessHandler.getRouteToRedirect(auth);
    }

    /**
     * Check the current authenticated user
     * @return The current user
     */
    @RequestMapping(value = "/checkCurrentUser", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public CurrentUser checkAuthentication(){
        // Get current authentication
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // Check if the user is a valid user
        boolean isValidUser = authenticationSuccessHandler.isAuthenticatedUser(auth);

        // Return the logged user
        return new CurrentUser(isValidUser, auth.getName());
    }

}