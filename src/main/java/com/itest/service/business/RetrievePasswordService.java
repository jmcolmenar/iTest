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

public interface RetrievePasswordService {

    /**
     * Generate a new token for the user to retrieve the password
     * @param username The username
     * @return String with an error message if the token has not been generated. Otherwise, null
     */
    String generateTokenToRetrievePassword(String username);

    /**
     * Check if the token is valid to retrieve the user password
     * @param token The token to retrieve the user password
     * @return Error message because the token is invalid. Null if the token is valid
     */
    String isValidToken(String token);

    /**
     * Get the user database object from token to retrieve the password
     * @param token The token to retrieve the password
     * @return The user database object from token
     */
    Usuario getUserFromTokenToRetrievePassword(String token);

    /**
     * Update the token as used in database
     * @param token The used token
     */
    void UpdateUsedToken(String token);
}
