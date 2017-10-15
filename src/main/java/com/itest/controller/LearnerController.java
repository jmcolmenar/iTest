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
import com.itest.model.DoneExamHeader;
import com.itest.model.UserProfileModel;
import com.itest.service.CourseManagementService;
import com.itest.service.LearnerManagementService;
import com.itest.service.TranslationService;
import com.itest.service.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
@RequestMapping("/api/learner/")
public class LearnerController {

    @Autowired
    @Qualifier("courseManagementServiceImpl")
    private CourseManagementService courseManagementService;

    @Autowired
    @Qualifier("userManagementServiceImpl")
    private UserManagementService userManagementService;

    @Autowired
    @Qualifier("translationServiceImpl")
    TranslationService translationService;

    @Autowired
    @Qualifier("learnerManagementServiceImpl")
    private LearnerManagementService learnerManagementService;

    @GetMapping("/getFullName")
    public Map<String, String> getFullName(){

        // Get the full name of current user
        String fullname = this.userManagementService.getUserFullName();

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
        ChangePasswordModel changePasswordModel = this.userManagementService.changeUserPassword(oldPassword, newPassword, repeatPassword);

        // Return the model to change the password
        return changePasswordModel;
    }

    @GetMapping("/getUserProfile")
    public UserProfileModel getUserProfile(){
        // Call to the service to get the user profile model
        UserProfileModel userProfileModel = this.userManagementService.getUserProfile();

        // Set the language identifier of user profile model
        userProfileModel.setLanguageId(this.translationService.getCurrentLanguageId());

        // Return the user profile model
        return userProfileModel;
    }

    @PostMapping("/updateUserProfile")
    public ResponseEntity updateUerProfile(@RequestParam(value = "name", required = false) String name,
                                           @RequestParam(value = "lastName", required = false) String lastName,
                                           @RequestParam(value = "dni", required = false) String dni,
                                           @RequestParam(value = "email", required = false) String email,
                                           @RequestParam(value = "languageId", required = false) int languageId,
                                           HttpServletRequest request, HttpServletResponse response){
        // Update the user profile in database
        boolean isUpdated = this.userManagementService.updateUserProfile(name, lastName, dni, email);

        // Set the language by the language identifier
        this.translationService.setLocale(languageId, request, response);

        // Check if the user is updated to return an Ok or Bad response
        if(isUpdated){
            return new ResponseEntity(HttpStatus.OK);
        }else{
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/getDoneExams")
    public List<DoneExamHeader> getDoneExams(@RequestParam(value = "groupId") int groupId){
        // Get the Done examen Header by a group
        List<DoneExamHeader> doneExamsHeaderList = this.learnerManagementService.GetDoneExamsHeader(groupId);

        // Return the list of Done Exam Header
        return doneExamsHeaderList;
    }


}
