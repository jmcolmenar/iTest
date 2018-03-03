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
package com.itest.constant;

import java.util.Locale;

public class LanguageConstant {

    // The identifiers of cultures
    public static final int SPANISH_ID = 0;
    public static final int ENGLISH_ID = 1;

    // The strings with the languages of locales
    public static final String SPANISH_LANGUAGE = "es";
    public static final String ENGLISH_LANGUAGE = "en";


    // The strings with the locales
    public static Locale SPANSIH_LOCALE = new Locale(SPANISH_LANGUAGE, "");
    public static Locale ENGLISH_LOCALE = new Locale(ENGLISH_LANGUAGE, "");
}
