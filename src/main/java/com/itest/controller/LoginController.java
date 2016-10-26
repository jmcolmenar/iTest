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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Set;


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
    public String index(){
        // Return index page
        return "index";
    }

    /**
     * Return the index page as a view
     * @return The index page
     */
    @RequestMapping(value = "/redirect", method = RequestMethod.GET)
    public String redirectTo(){
        // Get current authentication
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // Return the page to redirect
        return "redirect:/" + authenticationSuccessHandler.getRouteToRedirect(auth);
    }

    /**
     * Return the view to invalid user
     * @return The page to invalid user
     */
    @RequestMapping(value = "/invalidUser", method = RequestMethod.GET)
    public String invalidUser(){
        // Return invalid user page
        return "invalid_user";
    }


    /**
     * Check the current authenticated user
     * @return If there is an authenticated and valid user
     */
    @RequestMapping(value = "/checkAuthentication", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public boolean checkAuthentication(){
        // Get current authentication
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // Checks if there is an authenticated user
        return this.isAuthenticatedUser(auth);
    }

    /**
     * Get the username of authenticated user
     * @return The username (null: If there is not an authenticated user)
     */
    @RequestMapping(value = "/getUsername", method = RequestMethod.GET, produces = "application/json")
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



    /**
     * Check if the current authenticated user is valid
     * @param auth The current authentication
     * @return If the current user is a valid user
     */
    private boolean isAuthenticatedUser(Authentication auth) {
        boolean authenticated = false;

        // Get the roles of authenticated user
        Set<String> roles = AuthorityUtils.authorityListToSet(auth.getAuthorities());

        // Check if there is an authenticated user with the Admin, Tutor or Learner role
        if(roles != null && !roles.isEmpty()){
            authenticated = roles.contains("ROLE_LEARNER") || roles.contains("ROLE_TUTOR") || roles.contains("ROLE_ADMIN");
        }

        return authenticated;
    }

}