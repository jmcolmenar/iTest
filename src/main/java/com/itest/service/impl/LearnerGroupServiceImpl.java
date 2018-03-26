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
package com.itest.service.impl;

import com.itest.converter.GrupoConverter;
import com.itest.converter.MatriculaConverter;
import com.itest.entity.Grupo;
import com.itest.entity.Matricula;
import com.itest.model.CourseModel;
import com.itest.model.SubjectModel;
import com.itest.repository.GrupoRepository;
import com.itest.repository.MatriculaRepository;
import com.itest.service.LearnerGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service("learnerGroupServiceImpl")
public class LearnerGroupServiceImpl implements LearnerGroupService{

    @Autowired
    @Qualifier("matriculaRepository")
    private MatriculaRepository matriculaRepository;

    @Autowired
    @Qualifier("matriculaConverter")
    private MatriculaConverter matriculaConverter;

    @Autowired
    @Qualifier("grupoRepository")
    private GrupoRepository grupoRepository;

    @Autowired
    @Qualifier("grupoConverter")
    private GrupoConverter grupoConverter;

    /**
     * Get the courses list of the learner
     * @param learnerId The learner identifier
     * @return The course list
     */
    public List<CourseModel> getCourseList(int learnerId){
        // Get the Matricula table list of user
        List<Matricula> matriculaList = this.matriculaRepository.selectMatriculaListByUserId(learnerId);

        // Convert the Matricula objects list to Course model
        List<CourseModel> courseModelList = this.matriculaConverter.convertMatriculaListToCourseModelList(matriculaList);

        // Order the list by year (From highest to lowest)
        Collections.sort(courseModelList, (o1, o2) -> o2.getYear().compareTo(o1.getYear()));

        // Return the course model list
        return courseModelList;
    }

    /**
     * Get the subject corresponding to a group
     * @param groupId The group identifier
     * @return The subject model
     */
    public SubjectModel getSubjectFromGroup(int groupId){
        // Get the group entity from database
        Grupo group = this.grupoRepository.findOne(groupId);

        // Convert to the subject model
        SubjectModel subjectModel = this.grupoConverter.convertGrupoToSubjectModel(group);

        // Return the subject model
        return subjectModel;
    }

}
