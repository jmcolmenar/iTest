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
package com.itest.service.controller.impl;

import com.itest.model.UserInfoModel;
import com.itest.model.request.ChangePasswordRequest;
import com.itest.model.request.UpdateUserProfileRequest;
import com.itest.model.response.*;
import com.itest.service.business.TranslationService;
import com.itest.service.controller.UserManagementService;
import com.itest.service.business.UserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service("userManagementServiceImpl")
public class UserManagementServiceImpl implements UserManagementService {

    private static final Log LOG = LogFactory.getLog(UserManagementServiceImpl.class);

    @Autowired
    @Qualifier("userServiceImpl")
    UserService userService;

    @Autowired
    @Qualifier("translationServiceImpl")
    TranslationService translationService;

    public CheckSessionResponse checkSession(){

        // Initialize the change password response
        CheckSessionResponse checkSessionResponse = new CheckSessionResponse();

        try{
            // Get current authentication
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            // Check if the user in the current session is authorized
            boolean isAuthorizedUser = this.userService.isAuthorizedUser(auth);

            // Fill the response object
            checkSessionResponse.setAuthorizedUser(isAuthorizedUser);

        }catch(Exception exc){
            // Log the exception
            LOG.error("Error checking the user session. Exception: " + exc.getMessage());

            // Set the error when an exception is thrown
            checkSessionResponse.setHasError(true);
        }

        // Return the response
        return checkSessionResponse;
    }

    public ChangePasswordResponse changePassword(ChangePasswordRequest request){

        // Initialize the change password response
        ChangePasswordResponse changePasswordResponse = new ChangePasswordResponse();

        try{
            // Get the Request variables
            String oldPass = request.getOldPassword();
            String newPass = request.getNewPassword();
            String repeatPass = request.getRepeatPassword();

            // Change the user password
            String errorMessage = this.userService.changeUserPassword(oldPass, newPass, repeatPass);

            // Check if has an error changing the user password
            if(errorMessage != null){

                // Set the error message and the error flag in the response object
                changePasswordResponse.setErrorMessage(errorMessage);
                changePasswordResponse.setHasError(true);
            }
        }catch(Exception exc){
            // Log the exception
            LOG.error("Error changing the user password. Exception: " + exc.getMessage());

            // Set the error message when an exception is thrown
            String errorMessage = this.translationService.getMessage("changePassword.errorChangingPassword");
            changePasswordResponse.setHasError(true);
            changePasswordResponse.setErrorMessage(errorMessage);
        }

        return changePasswordResponse;
    }

    public GetFullNameResponse getFullName() {
        // Initialize the response object
        GetFullNameResponse getFullNameResponse = new GetFullNameResponse();

        try{
            // Get the user info
            UserInfoModel userInfoModel = this.userService.getUserInfo();

            // Set the full name variable of response object
            getFullNameResponse.setFullName(userInfoModel.getName() + " " + userInfoModel.getLastName());

        }catch (Exception exc){
            // Log the exception
            LOG.error("Error getting the fullname of user. Exception: " + exc.getMessage());

            // Set the error getting the full name
            getFullNameResponse.setHasError(true);
        }

        // Return the response
        return getFullNameResponse;
    }

    public GetUserProfileResponse getUserProfile() {
        // Initialize the response
        GetUserProfileResponse getUserProfileResponse = new GetUserProfileResponse();

        try{

            // Get the user info
            UserInfoModel userInfoModel = this.userService.getUserInfo();

            // Set the variables of user profile response
            getUserProfileResponse.setUsername(userInfoModel.getUsername());
            getUserProfileResponse.setName(userInfoModel.getName());
            getUserProfileResponse.setLastName(userInfoModel.getLastName());
            getUserProfileResponse.setEmail(userInfoModel.getEmail());
            getUserProfileResponse.setDni(userInfoModel.getDni());
            getUserProfileResponse.setLanguageId(userInfoModel.getLanguageId());

        }catch(Exception exc){
            // Log the exception
            LOG.error("Error getting the user profile. Exception: " + exc.getMessage());

            // Has an error getting the user profile
            getUserProfileResponse.setHasError(true);
        }

        // Return user profile response
        return getUserProfileResponse;
    }

    public UpdateUserProfileResponse updateUserProfile(UpdateUserProfileRequest request, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        // Initialize the Response
        UpdateUserProfileResponse updateUserProfileResponse = new UpdateUserProfileResponse();

        try{
            // Get the Request variables
            String name = request.getName();
            String lastname = request.getLastName();
            String dni = request.getDni();
            String email = request.getEmail();
            int languageId = request.getLanguageId();

            // Create an User Info Model with the request data
            UserInfoModel userInfoModel = new UserInfoModel();
            userInfoModel.setName(name);
            userInfoModel.setLastName(lastname);
            userInfoModel.setDni(dni);
            userInfoModel.setEmail(email);
            userInfoModel.setLanguageId(languageId);

            // Update the user data in database
            this.userService.updateUserData(userInfoModel);

            // Set the application language from current user language
            this.userService.setLocaleByCurrentUser(httpRequest, httpResponse);

            // Set the success message updating the user profile
            updateUserProfileResponse.setSuccessMessage(this.translationService.getMessage("userProfile.updateProfileSuccessfully"));

        }catch(Exception exc){
            // Log the exception
            LOG.error("Error updating the user profile. Exception: " + exc.getMessage());

            // It has an error updating the user profile
            updateUserProfileResponse.setHasError(true);
        }

        // Return the user profile response
        return updateUserProfileResponse;
    }
}
