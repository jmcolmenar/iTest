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
package com.itest.converter;

import com.itest.entity.Usuario;
import com.itest.model.response.GetUserProfileResponse;
import org.springframework.stereotype.Component;

@Component("usuarioConverter")
public class UsuarioConverter {

    public GetUserProfileResponse convertUsuarioToGetUserProfileResponse(Usuario usuario){
        // Initialize the user porfile response
        GetUserProfileResponse getUserProfileResponse = new GetUserProfileResponse();

        // Fill the user profile model with the "Usuario" entity from database
        getUserProfileResponse.setUsername(usuario.getUsuario());
        getUserProfileResponse.setName(usuario.getNombre());
        getUserProfileResponse.setLastName(usuario.getApes());
        getUserProfileResponse.setEmail(usuario.getEmail() != null ? usuario.getEmail() : "");
        getUserProfileResponse.setDni(usuario.getDni());

        // Return the user profile response
        return getUserProfileResponse;
    }

}
