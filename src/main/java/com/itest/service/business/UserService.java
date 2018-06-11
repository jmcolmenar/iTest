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
package com.itest.service.business;

import com.itest.entity.Usuario;
import com.itest.model.UserInfoModel;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface UserService {

    /**
     * Check if the current user is authorized in the application
     * @param auth The current authentication context
     * @return If the user is authorized
     */
    boolean isAuthorizedUser(Authentication auth);

    /**
     * Get the identifier of the current user
     * @return The user identifier
     */
    int getUserIdOfCurrentUser();

    /**
     * Set the locale of application from the user language
     * @param httpServletRequest The http request object
     * @param httpServletResponse The http response object
     */
    void setLocaleByCurrentUser(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse);

    /**
     * Change the user password
     * @param oldPass The old password
     * @param newPass The new password
     * @param repeatPass The repeated new password
     * @return An error message when the passwor can not be changed. Null if the password is changed successfully
     */
    String changeUserPassword(String oldPass, String newPass, String repeatPass);

    /**
     * Change the password of user to a new one
     * @param usuario The user database object
     * @param newPass The new password
     * @param repeatPass The repeated new password
     * @return An error message when the password can not be changed. Null if the password is changed successfully
     */
    String changeNewPassword(Usuario usuario, String newPass, String repeatPass);

    /**
     * Get the user information
     * @return The objet model with the user information data
     */
    UserInfoModel getUserInfo();

    /**
     * Update the user data in database
     * @param userInfoModel The user information model object
     */
    void updateUserData(UserInfoModel userInfoModel);

}
