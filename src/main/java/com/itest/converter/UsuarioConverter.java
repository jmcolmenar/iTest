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
import com.itest.model.UserInfoModel;
import org.springframework.stereotype.Component;

@Component("usuarioConverter")
public class UsuarioConverter {

    public UserInfoModel convertUsuarioToUserInfoModel(Usuario usuario){
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
