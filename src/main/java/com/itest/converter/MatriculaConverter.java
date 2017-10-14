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

import com.itest.entity.Asignatura;
import com.itest.entity.Grupo;
import com.itest.entity.Matricula;
import com.itest.model.CourseModel;
import com.itest.model.SubjectModel;
import com.itest.service.TranslationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.*;

@Component("matriculaConverter")
public class MatriculaConverter {

    @Autowired
    @Qualifier("translationServiceImpl")
    TranslationService translationService;

    public List<CourseModel> convertMatriculaListToCourseModelList(List<Matricula> matriculaList){
        // Initialize the list with the Course model objects
        List<CourseModel> courseModelList = new ArrayList<>();

        // Map  of year with subject list
        Map<String, List<SubjectModel>> yearAndSubjectsMap = new HashMap<>();

        // Through all matricula list
        for (Matricula mat: matriculaList ) {
            // Get year of group
            String year = mat.getGrupos() != null ? mat.getGrupos().getAnio() : null;

            // Check if there is a group with a year
            if(year != null) {
                // Check if the map contains the year
                if(yearAndSubjectsMap.containsKey(year)){
                    // Get the subject list
                    List<SubjectModel> subjectList = yearAndSubjectsMap.get(year);

                    // Add subject model to list
                    SubjectModel subject = this.convertGrupoToSubjectModel(mat.getGrupos());
                    subjectList.add(subject);
                }else{
                    // Initialize the subject list to this year
                    List<SubjectModel> subjectList = new ArrayList<>();

                    // Add the subject model to the list
                    SubjectModel subject = this.convertGrupoToSubjectModel(mat.getGrupos());
                    subjectList.add(subject);

                    // Put the year and subject list in the map
                    yearAndSubjectsMap.put(year, subjectList);
                }
            }
        }

        // Through all map entries to fill the course model list
        for( Map.Entry<String, List<SubjectModel>> entry : yearAndSubjectsMap.entrySet()){
            // Initialize a new course model object
            CourseModel course = new CourseModel(entry.getKey(), entry.getValue());

            // Add the course model to list
            courseModelList.add(course);
        }

        return courseModelList;
    }

    private SubjectModel convertGrupoToSubjectModel(Grupo group){
        // Initialize subject model object
        SubjectModel subjectModel = new SubjectModel();

        // Fill the object with group id, subject id and subject name
        subjectModel.setGroupId(group.getIdgrupo());
        subjectModel.setSubjectId(group.getAsignaturas().getIdasig());
        subjectModel.setSubjectName(group.getAsignaturas().getNombre());

        // fill the subject description
        Asignatura asignatura = group.getAsignaturas();
        String subjectDescription = this.translationService.getMessage("coursesList.subjectGroup") + " " + group.getGrupo();
        subjectModel.setSubjectDescription(subjectDescription);

        // Return the subject model object
        return subjectModel;
    }

}
