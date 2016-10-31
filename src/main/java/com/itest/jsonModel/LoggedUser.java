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

package com.itest.jsonModel;

/**
 * JSON model of logged user
 */
public class LoggedUser {

    private boolean validUser;

    private String username;

    /**
     * Create new JSON model to logged username
     * @param validUser If the logged user is a valid user (With the needed roles)
     * @param username The username of logged user
     */
    public LoggedUser(boolean validUser, String username) {
        this.validUser = validUser;
        this.username = username;
    }

    public boolean isValidUser() {
        return validUser;
    }

    public String getUsername() {
        return username;
    }
}
