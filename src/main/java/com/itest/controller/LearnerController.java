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

import com.itest.model.ChangePasswordModel;
import com.itest.model.CourseModel;
import com.itest.service.CourseManagementService;
import com.itest.service.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/learner/")
public class LearnerController {

    @Autowired
    @Qualifier("courseManagementServiceImpl")
    private CourseManagementService courseManagementService;

    @Autowired
    @Qualifier("userManagementServiceImpl")
    private UserManagementService userManagementService;

    @GetMapping("/getFullName")
    public Map<String, String> getFullName(){

        // Get the full name of current user
        String fullname = this.userManagementService.getFullNameOfUser();

        // Fill the map
        Map<String, String> map = new HashMap<>();
        map.put("fullName", fullname);

        // Return the full name
        return map;
    }

    @GetMapping("/getCourses")
    public List<CourseModel> getCourses(){

        // Get the course list of current user
        List<CourseModel> courseList = this.courseManagementService.getCourseOfUser();

        // Return the course list
        return courseList;

    }

    @PostMapping("/changePassword")
    public ChangePasswordModel changePassword(@RequestParam(value = "oldPassword", required = false)String oldPassword,
                               @RequestParam(value = "newPassword", required = false)String newPassword,
                               @RequestParam(value = "repeatPassword", required = false)String repeatPassword){

        // Call to the service to process the change password
        ChangePasswordModel changePasswordModel = this.userManagementService.changePasswordOfCurrentUser(oldPassword, newPassword, repeatPassword);

        // Return the model to change the password
        return changePasswordModel;
    }


}
