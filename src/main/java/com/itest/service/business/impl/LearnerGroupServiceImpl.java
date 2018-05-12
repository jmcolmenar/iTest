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
package com.itest.service.business.impl;

import com.itest.entity.Grupo;
import com.itest.entity.Matricula;
import com.itest.entity.Usuario;
import com.itest.model.CourseModel;
import com.itest.model.SubjectModel;
import com.itest.model.TutorInfoToSendEmailModel;
import com.itest.repository.GrupoRepository;
import com.itest.repository.MatriculaRepository;
import com.itest.repository.UsuarioRepository;
import com.itest.service.business.LearnerGroupService;
import com.itest.service.business.TranslationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("learnerGroupServiceImpl")
public class LearnerGroupServiceImpl implements LearnerGroupService{

    @Autowired
    @Qualifier("matriculaRepository")
    private MatriculaRepository matriculaRepository;

    @Autowired
    @Qualifier("grupoRepository")
    private GrupoRepository grupoRepository;

    @Autowired
    @Qualifier("translationServiceImpl")
    private TranslationService translationService;

    @Autowired
    @Qualifier("usuarioRepository")
    private UsuarioRepository usuarioRepository;

    /**
     * Get the courses list of the learner
     * @param learnerId The learner identifier
     * @return The course list
     */
    public List<CourseModel> getCourseList(int learnerId){
        // Get the Matricula table list of user
        List<Matricula> matriculaList = this.matriculaRepository.selectMatriculaListByUserId(learnerId);

        // Convert the Matricula objects list to Course model
        List<CourseModel> courseModelList = this.convertMatriculaListToCourseModelList(matriculaList);

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
        SubjectModel subjectModel = this.convertGrupoToSubjectModel(group);

        // Return the subject model
        return subjectModel;
    }

    /**
     * Get the tutors to send an email from a group
     * @param groupId The group identifier
     * @return The list of tutors
     */
    public List<TutorInfoToSendEmailModel> getTutorsToSendEmailFromGroup(int groupId){

        // Get the tutors of the group from database
        List<Usuario> tutorInfoListFromDatabase = this.usuarioRepository.findTutorsByGroupId(groupId);

        // Create the list of tutor info to send an email
        List<TutorInfoToSendEmailModel> tutorInfoModelList = new ArrayList<>();
        if(tutorInfoListFromDatabase != null && tutorInfoListFromDatabase.size() > 0){

            for (Usuario tutor : tutorInfoListFromDatabase) {

                // Check the email is not empty
                if(tutor.getEmail() != null && !tutor.getEmail().trim().isEmpty()){
                    TutorInfoToSendEmailModel tutorInfoToSendEmailModel = new TutorInfoToSendEmailModel();
                    tutorInfoToSendEmailModel.setFullName(tutor.getNombre() + " " + tutor.getApes());
                    tutorInfoToSendEmailModel.setEmail(tutor.getEmail());

                    // Add tutor info model to the list
                    tutorInfoModelList.add(tutorInfoToSendEmailModel);
                }
            }
        }

        // return the tutor info list
        return tutorInfoModelList;
    }


    private List<CourseModel> convertMatriculaListToCourseModelList(List<Matricula> matriculaList){
        // Initialize the list with the Course model objects
        List<CourseModel> courseModelList = new ArrayList<>();

        // Map  of year with subject list
        Map<String, List<SubjectModel>> yearAndSubjectsMap = new HashMap<>();

        // Through all matricula list
        for (Matricula mat: matriculaList ) {
            // Get year of group
            String year = mat.getGrupo() != null ? mat.getGrupo().getAnio() : null;

            // Check if there is a group with a year
            if(year != null) {
                // Check if the map contains the year
                if(yearAndSubjectsMap.containsKey(year)){
                    // Get the subject list
                    List<SubjectModel> subjectList = yearAndSubjectsMap.get(year);

                    // Add subject model to list
                    SubjectModel subject = this.convertGrupoToSubjectModel(mat.getGrupo());
                    subjectList.add(subject);
                }else{
                    // Initialize the subject list to this year
                    List<SubjectModel> subjectList = new ArrayList<>();

                    // Add the subject model to the list
                    SubjectModel subject = this.convertGrupoToSubjectModel(mat.getGrupo());
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

    private SubjectModel convertGrupoToSubjectModel(Grupo grupo){
        // Initialize the subject model
        SubjectModel subjectModel = new SubjectModel();

        // Fill the subject model
        subjectModel.setGroupId(grupo.getIdgrupo());
        subjectModel.setSubjectId(grupo.getAsignatura().getIdasig());
        subjectModel.setYear(grupo.getAnio());
        subjectModel.setSubjectName(grupo.getAsignatura().getNombre());
        subjectModel.setSubjectDescription(this.translationService.getMessage("coursesList.subjectGroup") + " " + grupo.getGrupo());

        // Return the subject model
        return subjectModel;
    }
}
