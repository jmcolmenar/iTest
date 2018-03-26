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
import com.itest.model.*;
import com.itest.model.request.GetExamToReviewRequest;
import com.itest.model.request.GetExamsInfoRequest;
import com.itest.model.response.GetCoursesResponse;
import com.itest.model.response.GetExamToReviewResponse;
import com.itest.model.response.GetExamsInfoResponse;
import com.itest.repository.GrupoRepository;
import com.itest.repository.MatriculaRepository;
import com.itest.service.LearnerExamService;
import com.itest.service.LearnerManagementService;
import com.itest.service.UserManagementService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

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
    @Qualifier("grupoRepository")
    private GrupoRepository grupoRepository;

    @Autowired
    @Qualifier("grupoConverter")
    private GrupoConverter grupoConverter;

    @Autowired
    @Qualifier("userManagementServiceImpl")
    private UserManagementService userManagementService;

    @Autowired
    @Qualifier("learnerExamServiceImpl")
    private LearnerExamService learnerExamService;

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

    public GetExamsInfoResponse getExamsInfo(GetExamsInfoRequest request){

        // Initialize the response
        GetExamsInfoResponse getExamsInfoResponse = new GetExamsInfoResponse();

        try{
            // Get the request variables
            int groupId = request.getGroupId();

            // Get the group entity from database and convert to the subject model
            Grupo group = this.grupoRepository.findOne(groupId);
            SubjectModel subjectModel = this.grupoConverter.convertGrupoToSubjectModel(group);

            // Get the identifier of current user
            int userId = this.userManagementService.getUserIdOfCurrentUser();

            // Get the available, next and done exams from database
            List<ExamExtraInfoModel> availableExamsList = this.learnerExamService.getAvailableExamsList(groupId, userId);
            List<ExamExtraInfoModel> nextExamsList = this.learnerExamService.getNextExamsList(groupId, userId);
            List<DoneExamInfoModel> doneExamsList = this.learnerExamService.getDoneExamsList(groupId, userId);

            // Fill the variables of the response
            getExamsInfoResponse.setSubject(subjectModel);
            getExamsInfoResponse.setAvailableExamsList(availableExamsList);
            getExamsInfoResponse.setNextExamsList(nextExamsList);
            getExamsInfoResponse.setDoneExamsList(doneExamsList);

        }catch(Exception exc){
            // Log the exception
            LOG.debug("Error getting the done exams of user. Exception: " + exc.getMessage());

            // Has an error retrieving the done exams by the user
            getExamsInfoResponse.setHasError(true);
        }

        // Return the response
        return getExamsInfoResponse;
    }


    public GetExamToReviewResponse getExamToReview(GetExamToReviewRequest request) {

        // Initialize the response
        GetExamToReviewResponse getExamToReviewResponse = new GetExamToReviewResponse();

        try{
            // Get the request variables
            int examId = request.getExamId();

            // Get the identifier of current user
            int userId = this.userManagementService.getUserIdOfCurrentUser();

            // Get the done exam by the learner
            DoneExamInfoModel doneExam = this.learnerExamService.getDoneExam(examId, userId);
            
            // Get the questions of the exam
            List<ExamQuestionModel> questionList = this.learnerExamService.getExamQuestionsToReviewList(examId, userId);

            // Set the variable of the response object
            getExamToReviewResponse.setExamId(examId);
            getExamToReviewResponse.setSubjectName("Asignatura");
            getExamToReviewResponse.setExamTitle(doneExam.getExamName());
            getExamToReviewResponse.setScore(doneExam.getScore());
            getExamToReviewResponse.setMaxScore(doneExam.getMaxScore());
            getExamToReviewResponse.setQuestionList(questionList);

        }catch(Exception exc){
            // Log the exception
            LOG.error("Error getting the done exams of user. Exception: " + exc.getMessage());

            // Has an error retrieving the exam info to review
            getExamToReviewResponse.setHasError(true);
        }

        // Return the response
        return getExamToReviewResponse;
    }

}
