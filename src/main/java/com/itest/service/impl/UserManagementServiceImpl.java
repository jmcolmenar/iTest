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
import com.itest.model.request.ChangePasswordRequest;
import com.itest.model.request.UpdateUserProfileRequest;
import com.itest.model.response.ChangePasswordResponse;
import com.itest.model.response.GetFullNameResponse;
import com.itest.model.response.GetUserProfileResponse;
import com.itest.model.response.UpdateUserProfileResponse;
import com.itest.repository.UsuarioRepository;
import com.itest.service.TranslationService;
import com.itest.service.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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


    public int getUserIdOfCurrentUser() {
        // Get the current user from SecurityContextHolder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Get the username
        String username = authentication.getName();

        // Return the user identifier from username
        return this.usuarioRepository.getUserIdByUsername(username);
    }

    public ChangePasswordResponse changeUserPassword(ChangePasswordRequest request){

        // Initialize the change password response
        ChangePasswordResponse changePasswordResponse = new ChangePasswordResponse();

        try{
            // Get the Request variables
            String oldPass = request.getOldPassword();
            String newPass = request.getNewPassword();
            String repeatPass = request.getRepeatPassword();

            // Check the new password and the repeated password is the same
            if(newPass.equals(repeatPass)){

                // Get the user Id of current user
                int userId = this.getUserIdOfCurrentUser();

                // Get the password of current user (In MD5 format)
                Usuario usuario = this.usuarioRepository.findByIdusu(userId);
                String currentpasswordMd5 = usuario.getPassw();

                // Initialize the encoder MD5 algorithmic and encode the old password
                Md5PasswordEncoder md5PasswordEncoder = new Md5PasswordEncoder();
                String oldPassordMd5 = md5PasswordEncoder.encodePassword(oldPass, null);

                // Check the old password is the same that the current password of user
                if(currentpasswordMd5.equals(oldPassordMd5)){

                    // Update the password of user
                    String newPasswordMd5 = md5PasswordEncoder.encodePassword(newPass, null);
                    usuario.setPassw(newPasswordMd5);
                    this.usuarioRepository.save(usuario);
                }else{

                    // Set the error message when the password of user is not correct
                    String errorMessage = this.translationService.getMessage("changePassword.errorIncorrectPassword");
                    changePasswordResponse.setHasError(true);
                    changePasswordResponse.setErrorMessage(errorMessage);
                }
            }else{

                // Set the error message when the new and repeated password is not the same
                String errorMessage = this.translationService.getMessage("changePassword.errorNewAndRepeatPassword");
                changePasswordResponse.setHasError(true);
                changePasswordResponse.setErrorMessage(errorMessage);
            }
        }catch(Exception exc){
            // TODO: Log the exception

            // Set the error message when an exception is thrown
            String errorMessage = this.translationService.getMessage("changePassword.errorChangingPassword");
            changePasswordResponse.setHasError(true);
            changePasswordResponse.setErrorMessage(errorMessage);
        }

        return changePasswordResponse;
    }

    public GetFullNameResponse getUserFullName() {
        // Initialize the response object
        GetFullNameResponse getFullNameResponse = new GetFullNameResponse();

        try{
            // Get the user id of current user
            int userId = this.getUserIdOfCurrentUser();

            // Get the user from database by user id
            Usuario usuario = this.usuarioRepository.findByIdusu(userId);

            // Set the full name
            getFullNameResponse.setFullName(usuario.getNombre() + " " + usuario.getApes());

        }catch (Exception exc){
            // TODO: Log the exception

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

            // Get the user id of current user
            int userId = this.getUserIdOfCurrentUser();

            // Get the user from database by user id
            Usuario usuario = this.usuarioRepository.findByIdusu(userId);

            // Convert the "Usuario" entity from database to response object
            getUserProfileResponse = this.usuarioConverter.convertUsuarioToGetUserProfileResponse(usuario);

            // Set the language identifier of user profile response
            getUserProfileResponse.setLanguageId(this.translationService.getCurrentLanguageId());

        }catch(Exception exc){
            // TODO: Log the exception

            // Has an error getting the user profile
            getUserProfileResponse.setHasError(true);
        }

        // Return user profile response
        return getUserProfileResponse;
    }

    public UpdateUserProfileResponse updateUserProfile(UpdateUserProfileRequest request, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        // Initialize the Response
        UpdateUserProfileResponse userProfileResponse = new UpdateUserProfileResponse();

        try{
            // Get the Request variables
            String name = request.getName();
            String lastname = request.getLastName();
            String dni = request.getDni();
            String email = request.getEmail();
            int languageId = request.getLanguageId();

            // Get the user id of current user
            int userId = this.getUserIdOfCurrentUser();

            // Get the user entity from database
            Usuario usuario = this.usuarioRepository.findByIdusu(userId);

            // Update the user in database
            usuario.setNombre(name);
            usuario.setApes(lastname);
            usuario.setDni(dni);
            usuario.setEmail(email);
            this.usuarioRepository.save(usuario);

            // Set the language by the language identifier
            this.translationService.setLocale(languageId, httpRequest, httpResponse);

        }catch(Exception exc){
            // TODO: Log the exception

            // It has an error updating the user profile
            userProfileResponse.setHasError(true);
        }

        // Return the user profile response
        return userProfileResponse;
    }
}
