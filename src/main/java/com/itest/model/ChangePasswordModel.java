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
package com.itest.model;

public class ChangePasswordModel {

    // If the change password is changed successfully
    private boolean isChanged;

    // The error message when the password of user cannot be changed
    private String errorMessage;

    public ChangePasswordModel() {
    }

    public ChangePasswordModel(boolean isChanged, String errorMessage) {
        this.isChanged = isChanged;
        this.errorMessage = errorMessage;
    }

    public boolean getIsChanged() {
        return isChanged;
    }

    public void setIsChanged(boolean changed) {
        isChanged = changed;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
