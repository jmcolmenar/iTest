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
package com.itest.service;

import com.itest.model.ChangePasswordModel;
import com.itest.model.UserProfileModel;

public interface UserManagementService {

    public abstract String getUsernameOfCurrentUser();

    public abstract int getUserIdOfCurrentUser();

    public abstract ChangePasswordModel changePasswordOfCurrentUser(String oldPass, String newPass, String repeatPass);

    public abstract String getFullNameOfUser();

    public abstract UserProfileModel getUserProfile();

    public abstract boolean updateUserProfile(String name, String lastname, String dni, String email);
}
