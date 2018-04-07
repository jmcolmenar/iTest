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

import com.itest.constant.UserRoleConstant;
import com.itest.entity.Usuario;
import com.itest.model.UserInfoModel;
import com.itest.repository.UsuarioRepository;
import com.itest.service.TranslationService;
import com.itest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

@Service("userServiceImpl")
public class UserServiceImpl implements UserService {

    @Autowired
    @Qualifier("usuarioRepository")
    private UsuarioRepository usuarioRepository;

    @Autowired
    @Qualifier("translationServiceImpl")
    TranslationService translationService;

    /**
     * Check if the current user is authorized in the application
     * @param auth The current authentication context
     * @return If the user is authorized
     */
    public boolean isAuthorizedUser(Authentication auth){
        boolean isAuthorized = false;

        // Check the Authentication object
        if(auth == null){
            return false;
        }

        // Get the roles of authenticated user
        Set<String> roles = AuthorityUtils.authorityListToSet(auth.getAuthorities());

        // Check if the authenticated user has the Admin, Tutor ,Learner or Kid role
        if(roles != null && !roles.isEmpty()){
            isAuthorized = roles.contains(UserRoleConstant.ROLE_LEARNER)
                    || roles.contains(UserRoleConstant.ROLE_KID)
                    || roles.contains(UserRoleConstant.ROLE_TUTOR)
                    || roles.contains(UserRoleConstant.ROLE_ADMIN);
        }

        // Return if the current user is authorized
        return isAuthorized;
    }

    /**
     * Get the identifier of the current user
     * @return The user identifier
     */
    public int getUserIdOfCurrentUser(){
        // Get the current user from SecurityContextHolder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Get the username
        String username = authentication.getName();

        // Return the user identifier from username
        return this.usuarioRepository.getUserIdByUsername(username);
    }

    /**
     * Set the locale of application from the user language
     * @param httpServletRequest The http request object
     * @param httpServletResponse The http response object
     */
    public void setLocaleByCurrentUser(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        // Get the current user from database
        int userId = this.getUserIdOfCurrentUser();
        Usuario user = this.usuarioRepository.findByIdusu(userId);

        // Get the language of current user
        int languageId = user.getIdioma();

        // Set the locale by the language of user
        this.translationService.setLocale(languageId, httpServletRequest, httpServletResponse);
    }

    /**
     * Change the user password
     * @param oldPass The old password
     * @param newPass The new password
     * @param repeatPass The repeated new password
     * @return An error message when the passwor can not be changed. Null if the password is changed successfully
     */
    public String changeUserPassword(String oldPass, String newPass, String repeatPass){

        // The error message
        String errorMessage = null;

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
                errorMessage = this.translationService.getMessage("changePassword.errorIncorrectPassword");
            }
        }else{

            // Set the error message when the new and repeated password is not the same
            errorMessage = this.translationService.getMessage("changePassword.errorNewAndRepeatPassword");
        }

        // Return the error message
        return errorMessage;
    }

    /**
     * Get the user information
     * @return The objet model with the user information data
     */
    public UserInfoModel getUserInfo(){

        // Get the user Id of current user
        int userId = this.getUserIdOfCurrentUser();

        // Get the user from database by user id
        Usuario usuario = this.usuarioRepository.findByIdusu(userId);

        // Convert the user database object to user model object
        UserInfoModel userInfoModel = this.convertUsuarioToUserInfoModel(usuario);

        // Return the user info model
        return userInfoModel;
    }

    /**
     * Update the user data in database
     * @param userInfoModel The user information model object
     */
    public void updateUserData(UserInfoModel userInfoModel){

        // Get the user id of current user
        int userId = this.getUserIdOfCurrentUser();

        // Get the user entity from database
        Usuario usuario = this.usuarioRepository.findByIdusu(userId);

        // Update the user in database
        usuario.setNombre(userInfoModel.getName());
        usuario.setApes(userInfoModel.getLastName());
        usuario.setDni(userInfoModel.getDni());
        usuario.setEmail(userInfoModel.getEmail());
        usuario.setIdioma(userInfoModel.getLanguageId());
        this.usuarioRepository.save(usuario);
    }

    private UserInfoModel convertUsuarioToUserInfoModel(Usuario usuario){
        // Initialize the user info model
        UserInfoModel userInfoModel = new UserInfoModel();

        // Fill the user model from user database object
        userInfoModel.setUsername(usuario.getUsuario());
        userInfoModel.setName(usuario.getNombre());
        userInfoModel.setLastName(usuario.getApes());
        userInfoModel.setEmail(usuario.getEmail() != null ? usuario.getEmail() : "");
        userInfoModel.setDni(usuario.getDni());
        userInfoModel.setLanguageId(usuario.getIdioma());

        // Return the user model
        return userInfoModel;
    }
}
