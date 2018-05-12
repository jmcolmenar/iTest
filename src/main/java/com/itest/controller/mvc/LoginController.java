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
package com.itest.controller.mvc;

import com.itest.constant.UserRoleConstant;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Set;

/**
 * Controller for Login functionality
 */
@Controller
public class LoginController {

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
     * Redirect to corresponding index page of logged user
     * @return The index page of logged user
     */
    @RequestMapping(value = "/redirect", method = RequestMethod.GET)
    public String redirectTo(){
        // Get current authentication
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // Redirect to the index page of user
        return "redirect:" + this.getActionToRedirect(auth);
    }

    /**
     * Redirect to learner index page
     * @return The learner index page view
     */
    @RequestMapping(value = "/learner/", method = RequestMethod.GET)
    public String redirectToLearnerIndex(){
        return "learner/index";
    }

    /**
     * Redirect to tutor index page
     * @return The tutor index page view
     */
    @RequestMapping(value = "/tutor/", method = RequestMethod.GET)
    public String redirectToTutorIndex(){
        return "tutor/index";
    }

    /**
     * Redirect to admin index page
     * @return The admin index page view
     */
    @RequestMapping(value = "/admin/", method = RequestMethod.GET)
    public String redirectToAdminIndex(){
        return "admin/index";
    }

    /**
     *  Get the action of controller to redirect depending on user's roles
     * @param authentication Authentication object
     * @return The action of controller to redirect
     */
    private String getActionToRedirect(Authentication authentication) {
        String route = "/";

        // Get the roles of authenticated user
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

        // Redirects depending of user's roles
        if (roles.contains(UserRoleConstant.ROLE_LEARNER) || roles.contains(UserRoleConstant.ROLE_KID)) {
            // Redirects to learner index
            route = "/learner/";
        }else if(roles.contains(UserRoleConstant.ROLE_TUTOR) || roles.contains(UserRoleConstant.ROLE_ADVANCED_TUTOR)){
            // Redirects to tutor index
            route = "/tutor/";
        }else if(roles.contains(UserRoleConstant.ROLE_ADMIN)){
            // Redirects to admin index
            route = "/admin/";
        }

        // Return the route to redirect
        return route;
    }

}