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

import com.itest.entity.Grupo;
import com.itest.model.SubjectModel;
import com.itest.service.TranslationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("grupoConverter")
public class GrupoConverter {

    @Autowired
    @Qualifier("translationServiceImpl")
    private TranslationService translationService;

    public SubjectModel convertGrupoToSubjectModel(Grupo grupo){
        // Initialize the subject model
        SubjectModel subjectModel = new SubjectModel();

        // Fill the subject model
        subjectModel.setGroupId(grupo.getIdgrupo());
        subjectModel.setSubjectId(grupo.getAsignaturas().getIdasig());
        subjectModel.setYear(grupo.getAnio());
        subjectModel.setSubjectName(grupo.getAsignaturas().getNombre());
        subjectModel.setSubjectDescription(grupo.getAsignaturas().getNombre());
        subjectModel.setSubjectDescription(this.translationService.getMessage("coursesList.subjectGroup") + " " + grupo.getGrupo());

        // Return the subject model
        return subjectModel;
    }

}
