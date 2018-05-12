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
package com.itest.service.controller;

import com.itest.model.request.ChangePasswordRequest;
import com.itest.model.request.UpdateUserProfileRequest;
import com.itest.model.response.*;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface UserManagementService {

    CheckSessionResponse checkSession();

    ChangePasswordResponse changePassword(ChangePasswordRequest request);

    GetFullNameResponse getFullName();

    GetUserProfileResponse getUserProfile();

    UpdateUserProfileResponse updateUserProfile(UpdateUserProfileRequest request, HttpServletRequest httpRequest, HttpServletResponse httpResponse);
}
