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

import com.itest.converter.ExamenConverter;
import com.itest.converter.MatriculaConverter;
import com.itest.entity.Examen;
import com.itest.entity.Matricula;
import com.itest.model.CourseModel;
import com.itest.model.DoneExamModel;
import com.itest.model.request.GetDoneExamsRequest;
import com.itest.model.response.GetCoursesResponse;
import com.itest.model.response.GetDoneExamsResponse;
import com.itest.repository.ExamenRepository;
import com.itest.repository.MatriculaRepository;
import com.itest.service.LearnerManagementService;
import com.itest.service.UserManagementService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service("learnerManagementServiceImpl")
public class LearnerManagementServiceImpl implements LearnerManagementService {

    private final Log LOG = LogFactory.getLog(LearnerManagementServiceImpl.class);

    @Autowired
    @Qualifier("matriculaRepository")
    private MatriculaRepository matriculaRepository;

    @Autowired
    @Qualifier("matriculaConverter")
    private MatriculaConverter matriculaConverter;

    @Autowired
    @Qualifier("examenRepository")
    private ExamenRepository examenRepository;

    @Autowired
    @Qualifier("userManagementServiceImpl")
    private UserManagementService userManagementService;

    @Autowired
    @Qualifier("examenConverter")
    private ExamenConverter examenConverter;

    public GetCoursesResponse getCourseList() {

        // Initialize the Get Courses Response
        GetCoursesResponse getCoursesResponse = new GetCoursesResponse();

        try{
            // Get the user identifier of current user
            int userId = this.userManagementService.getUserIdOfCurrentUser();

            // Get the Matricula table list of current user
            List<Matricula> matriculaList = this.matriculaRepository.selectMatriculaListByUsernameOfUsuario(userId);

            // Convert the Matricula objects list to Course model
            List<CourseModel> courseModelList = this.matriculaConverter.convertMatriculaListToCourseModelList(matriculaList);

            // Order the list by year (From highest to lowest)
            Collections.sort(courseModelList, (o1, o2) -> o2.getYear().compareTo(o1.getYear()));

            // Fill the response object with the course model list
            getCoursesResponse.setCoursesList(courseModelList);

        }catch(Exception exc){
            // Log the exception
            LOG.error("Error getting the courses. Exception: " + exc.getMessage());

            // Has an error getting the courses
            getCoursesResponse.setHasError(true);
        }

        // Return the response
        return getCoursesResponse;
    }

    public GetDoneExamsResponse getDoneExamsHeader(GetDoneExamsRequest request){

        // Initialize the response
        GetDoneExamsResponse getDoneExamsResponse = new GetDoneExamsResponse();

        try{
            // Get the request variables
            int groupId = request.getGroupId();

            // Get the done exams from database and user id
            List<Examen> examList = this.examenRepository.findDoneExams(userManagementService.getUserIdOfCurrentUser(), groupId);

            // Get the identifier of current user
            int userId = this.userManagementService.getUserIdOfCurrentUser();

            // Convert the entity object list to model object list
            List<DoneExamModel> doneExamsList = this.examenConverter.convertExamenListToDoneExamHeaderList(examList, userId);

            // Fill the response with the done exams list
            getDoneExamsResponse.setDoneExamsList(doneExamsList);

        }catch(Exception exc){
            // Log the exception
            LOG.debug("Error getting the done exams of user. Exception: " + exc.getMessage());

            // Has an error retrieving the done exams by the user
            getDoneExamsResponse.setHasError(true);
        }

        // Return the response
        return getDoneExamsResponse;
    }

}
