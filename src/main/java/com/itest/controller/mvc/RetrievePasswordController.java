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

import com.itest.service.business.RetrievePasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/retrievePassword/")
public class RetrievePasswordController {

    @Autowired
    @Qualifier("retrievePasswordServiceImpl")
    RetrievePasswordService retrievePasswordService;

    @GetMapping("/")
    public String getRetrievePasswordView(){
        return "retrieve_password";
    }

    @GetMapping("/retrieve")
    public String retrieveUserPassword(Model model, @RequestParam("token") String token, HttpServletRequest request){

        // Check if the token is valid to retrieve the password
        String errorMessage = this.retrievePasswordService.isValidToken(token);
        if(errorMessage == null){

            // Add token to request attribute
            request.getSession().setAttribute("token", token);

            // Return the page to change the password
            return "change_password_from_email";

        }else{

            // Set the error message
             model.addAttribute("errorMessage", errorMessage);

            // Return the error page when the token is not valid
            return "invalid_token_error";
        }
    }

}
