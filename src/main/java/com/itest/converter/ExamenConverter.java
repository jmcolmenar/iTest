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

import com.itest.Helper.FormatterHelper;
import com.itest.entity.Calificacion;
import com.itest.entity.Examen;
import com.itest.model.DoneExamHeader;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("examenConverter")
public class ExamenConverter {

    public List<DoneExamHeader> convertExamenListToDoneExamHeaderList(List<Examen> examenList){
        // Initialize the Donde Exam Header list
        List<DoneExamHeader> doneExamHeaderList = new ArrayList<>();

        // Check the Examen list to convert is not empty
        if(examenList != null && !examenList.isEmpty()){

            for(Examen exam : examenList){
                // Initialize DoneExamHeader object
                DoneExamHeader doneExam = new DoneExamHeader();
                doneExam.setExamId(exam.getIdexam());
                doneExam.setExamName(exam.getTitulo());
                doneExam.setMaximumScore(FormatterHelper.formatNumberWithTwoDecimals(exam.getNotaMax()));

                List<Calificacion> calificacionList = exam.getCalifs();
                calificacionList.sort((o1, o2) -> o2.getFechaFin().compareTo(o1.getFechaFin()));
                Calificacion calificacion = calificacionList.get(0);

                doneExam.setScore(FormatterHelper.formatNumberWithTwoDecimals(calificacion.getNota()));
                doneExam.setStartDate(FormatterHelper.formatDateToString(calificacion.getFechaIni()));
                doneExam.setEndDate(FormatterHelper.formatDateToString(calificacion.getFechaFin()));
                doneExam.setTime(FormatterHelper.formatMillisecondsToHoursMinutesAndSeconds(calificacion.getFechaFin().getTime() - calificacion.getFechaIni().getTime()));

                // Add DoneExamHeader object to the list
                doneExamHeaderList.add(doneExam);
            }
        }

        // Return the Donde Exam Header list
        return doneExamHeaderList;
    }

}
