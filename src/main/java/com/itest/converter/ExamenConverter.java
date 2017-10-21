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

import com.itest.component.FormatterComponent;
import com.itest.entity.Calificacion;
import com.itest.entity.Examen;
import com.itest.model.DoneExamModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("examenConverter")
public class ExamenConverter {

    @Autowired
    @Qualifier("formatterComponent")
    private FormatterComponent formatterComponent;

    public List<DoneExamModel> convertExamenListToDoneExamHeaderList(List<Examen> examenList, int userId){
        // Initialize the Donde Exam Header list
        List<DoneExamModel> doneExamModelList = new ArrayList<>();

        // Check the Examen list to convert is not empty
        if(examenList != null && !examenList.isEmpty()){

            for(Examen exam : examenList){
                // Initialize DoneExamModel object
                DoneExamModel doneExam = new DoneExamModel();
                doneExam.setExamId(exam.getIdexam());
                doneExam.setExamName(exam.getTitulo());
                doneExam.setMaximumScore(this.formatterComponent.formatNumberWithTwoDecimals(exam.getNotaMax()));

                // Get the "Calificacion" object corresponding to the user
                Calificacion calificacion = null;
                List<Calificacion> calificacionList = exam.getCalifs();
                calificacionList.sort((o1, o2) -> o2.getFechaFin().compareTo(o1.getFechaFin()));
                for(Calificacion calificacionAux : exam.getCalifs()){
                    if(calificacionAux.getUsuarios().getIdusu() == userId){
                        calificacion = calificacionAux;
                        break;
                    }
                }

                doneExam.setScore(this.formatterComponent.formatNumberWithTwoDecimals(calificacion.getNota()));
                doneExam.setStartDate(this.formatterComponent.formatDateToString(calificacion.getFechaIni()));
                doneExam.setEndDate(this.formatterComponent.formatDateToString(calificacion.getFechaFin()));
                doneExam.setTime(this.formatterComponent.formatMillisecondsToHoursMinutesAndSeconds(calificacion.getFechaFin().getTime() - calificacion.getFechaIni().getTime()));

                // Add DoneExamModel object to the list
                doneExamModelList.add(doneExam);
            }
        }

        // Return the Donde Exam Header list
        return doneExamModelList;
    }

}
