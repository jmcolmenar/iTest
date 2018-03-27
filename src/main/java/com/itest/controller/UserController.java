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

import com.itest.model.request.ChangePasswordRequest;
import com.itest.model.request.UpdateUserProfileRequest;
import com.itest.model.response.ChangePasswordResponse;
import com.itest.model.response.GetFullNameResponse;
import com.itest.model.response.GetUserProfileResponse;
import com.itest.model.response.UpdateUserProfileResponse;
import com.itest.service.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/user/")
public class UserController {

    @Autowired
    @Qualifier("userManagementServiceImpl")
    private UserManagementService userManagementService;

    @GetMapping("/getFullName")
    public GetFullNameResponse getFullName(){

        // Call to the service to get the full name of current user
        GetFullNameResponse response = this.userManagementService.getFullName();

        // Return the response
        return response;
    }

    @PostMapping("/changePassword")
    public ChangePasswordResponse changePassword(@RequestBody ChangePasswordRequest request){

        // Call to the service to change the user password
        ChangePasswordResponse changePasswordModel = this.userManagementService.changePassword(request);

        // Return the response object
        return changePasswordModel;
    }

    @GetMapping("/getUserProfile")
    public GetUserProfileResponse getUserProfile(){

        // Call to the service to get the user profile
        GetUserProfileResponse response = this.userManagementService.getUserProfile();

        // Return the user profile response
        return response;
    }

    @PostMapping("/updateUserProfile")
    public UpdateUserProfileResponse updateUserProfile(@RequestBody UpdateUserProfileRequest request, HttpServletRequest httpRequest, HttpServletResponse httpResponse){

        // Call to the service to update the user profile
        UpdateUserProfileResponse response = this.userManagementService.updateUserProfile(request, httpRequest, httpResponse);

        // Return the user profile response
        return response;
    }
}
