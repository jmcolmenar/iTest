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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class CustomErrorController implements ErrorController{

    // Error controller path and unauthorized error code
    private static final String  ERROR_PATH = "/error";
    public static final int UNAUTHORIZED_ERROR_CODE = 401;

    // Error attributes
    private final ErrorAttributes errorAttributes;

    @Autowired
    public CustomErrorController(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }

    @RequestMapping(value = ERROR_PATH)
    public String error(HttpServletRequest request){
        // Get the error code from the request
        int errorCode = this.getErrorCode(request);

        // Check the error code
        if(errorCode == UNAUTHORIZED_ERROR_CODE){
            // Return unautohrized error page
            return "unauthorized_error";
        }else{
            // Return generic error page
            return "generic_error";
        }
    }

    private int getErrorCode(HttpServletRequest aRequest) {
        // Get the errors from request
        RequestAttributes requestAttributes = new ServletRequestAttributes(aRequest);
        Map<String, Object> errors = this.errorAttributes.getErrorAttributes(requestAttributes, false);

        // Get the error code
        Object status = errors.get("status");
        int errorCode = status != null ? (int)status : 0;

        // Return the error code
        return errorCode;
    }

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }
}
