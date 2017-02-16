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
package com.itest.service.impl;

import com.itest.converter.UsuarioConverter;
import com.itest.entity.Usuario;
import com.itest.model.ChangePasswordModel;
import com.itest.model.UserProfileModel;
import com.itest.repository.UsuarioRepository;
import com.itest.service.TranslationService;
import com.itest.service.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service("userManagementServiceImpl")
public class UserManagementServiceImpl implements UserManagementService {

    @Autowired
    @Qualifier("usuarioRepository")
    private UsuarioRepository usuarioRepository;

    @Autowired
    @Qualifier("usuarioConverter")
    private UsuarioConverter usuarioConverter;

    @Autowired
    @Qualifier("translationServiceImpl")
    TranslationService translationService;

    @Override
    public int getUserIdOfCurrentUser() {
        // Get the current user from SecurityContextHolder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Get the username
        String username = authentication.getName();

        // Return the user identifier from username
        return this.usuarioRepository.getUserIdByUsername(username);
    }

    @Override
    public ChangePasswordModel changeUserPassword(String oldPass, String newPass, String repeatPass){
        // The change password model to return
        ChangePasswordModel changePasswordModel = new ChangePasswordModel();

        // The error message to fill when has an error
        String errorMessage = null;

        // The variable to indicate that the password has been changed successfully (False for default)
        boolean isChangedSuccessfuly = false;

        try{
            // Check the new password and the repeated password is the same
            if(newPass.equals(repeatPass)){
                // Get the user Id of current user
                int userId = this.getUserIdOfCurrentUser();

                // Get the password of current user (In MD5 format)
                String currentpasswordMd5 = this.usuarioRepository.getPasswordByUserId(userId);

                // Initialize the encoder MD5 algorithmic
                Md5PasswordEncoder md5PasswordEncoder = new Md5PasswordEncoder();

                // Get the old password encoded in MD5 format
                String oldPassordMd5 = md5PasswordEncoder.encodePassword(oldPass, null);

                // Check the old password is the same that the current password of user
                if(currentpasswordMd5.equals(oldPassordMd5)){
                    // Update the password of user
                    String newPasswordMd5 = md5PasswordEncoder.encodePassword(newPass, null);
                    this.usuarioRepository.updatePasswordByUserId(userId, newPasswordMd5);

                    // The password has been changed successfully
                    isChangedSuccessfuly = true;
                }else{
                    // Set the error message when the password of user is not correct
                    errorMessage = this.translationService.getMessage("changePassword.errorIncorrectPassword");
                }
            }else{
                // Set the error message when the new and repeated password is not the same
                errorMessage = this.translationService.getMessage("changePassword.errorNewAndRepeatPassword");
            }
        }catch(Exception exc){
            // TODO: Log the exception

            // Set the error message when an exception is thrown
            errorMessage = this.translationService.getMessage("changePassword.errorChangingPassword");
        }

        // Return the Change Password model
        changePasswordModel.setIsChanged(isChangedSuccessfuly);
        changePasswordModel.setErrorMessage(errorMessage);
        return changePasswordModel;
    }

    @Override
    public String getUserFullName() {
        // The full name to return
        String fullName;

        try{
            // Get the user id of current user
            int userId = this.getUserIdOfCurrentUser();

            // Get the user from database by user id
            Usuario usuario = this.usuarioRepository.findByIdusu(userId);

            // Set the full name
            fullName =  usuario.getNombre() + " " + usuario.getApes();

        }catch (Exception exc){
            // TODO: Log the exception

            // Fill a generic full name when has an error
            fullName = "Full Name";
        }

        // Return the full name
        return fullName;
    }

    @Override
    public UserProfileModel getUserProfile() {
        // The model of user profile to return
        UserProfileModel userProfileModel;

        try{
            // Get the user id of current user
            int userId = this.getUserIdOfCurrentUser();

            // Get the user from database by user id
            Usuario usuario = this.usuarioRepository.findByIdusu(userId);

            // Convert the "Usuario" entity from database to user profile model
            userProfileModel = this.usuarioConverter.convertUsuarioToUserProfileModel(usuario);

        }catch(Exception exc){
            // TODO: Log the exception

            // Initialize an empty user when has an exception
            userProfileModel = new UserProfileModel();
        }

        // Return user profile model
        return userProfileModel;
    }

    @Override
    public boolean updateUserProfile(String name, String lastname, String dni, String email) {
        // The variable to known if the user profile is updated
        boolean isUpdated;

        try{
            // Get the user id of current user
            int userId = this.getUserIdOfCurrentUser();

            // Update user profile in database
            this.usuarioRepository.updateUserByUserId(userId, name, lastname, dni, email);

            // The user profile is updated successfully
            isUpdated = true;
        }catch(Exception exc){
            // TODO: Log the exception

            // The user is not updated
            isUpdated = false;
        }

        // Return if the user profile is updated
        return isUpdated;
    }
}
