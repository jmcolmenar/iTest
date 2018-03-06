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
package com.itest.component;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component("formatterComponent")
public class FormatterComponent {


    public String formatDateToString(Date date){
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        return df.format(date);
    }

    public String formatNumberWithTwoDecimals(float number){
        return String.format("%.2f", number);
    }

    public String formatNumberWithTwoDecimals(BigDecimal number){
        return String.format("%.2f", number);
    }

    public String formatMillisecondsToHoursMinutesAndSeconds(long milliseconds){
        int seconds = (int) (milliseconds / 1000) % 60 ;
        int minutes = (int) ((milliseconds / (1000*60)) % 60);
        int hours = (int) ((milliseconds / (1000*60*60)) % 24);

        // Format => H:MM:SS
        return hours + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds);
    }

}
