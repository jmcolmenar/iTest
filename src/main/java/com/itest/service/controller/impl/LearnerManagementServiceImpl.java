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
package com.itest.service.controller.impl;

import com.itest.model.*;
import com.itest.model.request.*;
import com.itest.model.response.*;
import com.itest.service.business.*;
import com.itest.service.controller.LearnerManagementService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Service("learnerManagementServiceImpl")
public class LearnerManagementServiceImpl implements LearnerManagementService {

    private final Log LOG = LogFactory.getLog(LearnerManagementServiceImpl.class);

    @Autowired
    @Qualifier("learnerGroupServiceImpl")
    private LearnerGroupService learnerGroupService;

    @Autowired
    @Qualifier("userServiceImpl")
    private UserService userService;

    @Autowired
    @Qualifier("learnerExamServiceImpl")
    private LearnerExamService learnerExamService;

    @Autowired
    @Qualifier("learnerReviewExamServiceImpl")
    private LearnerReviewExamService learnerReviewExamService;

    @Autowired
    @Qualifier("learnerNewExamServiceImpl")
    private LearnerNewExamService learnerNewExamService;

    public GetCoursesResponse getCourseList() {

        // Initialize the Get Courses Response
        GetCoursesResponse getCoursesResponse = new GetCoursesResponse();

        try{
            // Get the current user identifier
            int userId = this.userService.getUserIdOfCurrentUser();

            // Get the courses list of user
            List<CourseModel> courseModelList = this.learnerGroupService.getCourseList(userId);

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
            // Get the request variables and current user identifier
            int groupId = request.getGroupId();
            int userId = this.userService.getUserIdOfCurrentUser();

            // Get the subject from the group
            SubjectModel subjectModel = this.learnerGroupService.getSubjectFromGroup(groupId);

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
            LOG.debug("Error getting the information of user exams. Exception: " + exc.getMessage());

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
            // Get the request variables and current user identifier
            int examId = request.getExamId();
            int userId = this.userService.getUserIdOfCurrentUser();

            // Get the done exam by the learner
            DoneExamInfoModel doneExam = this.learnerExamService.getDoneExam(examId, userId);
            
            // Get the questions of the exam
            List<ReviewExamQuestionModel> questionList = this.learnerReviewExamService.getExamQuestionsToReviewList(examId, userId);

            // Get the subject name from the exam
            String subjectName = this.learnerExamService.getSubjectNameFromExam(examId);

            // Set the variable of the response object
            getExamToReviewResponse.setExamId(examId);
            getExamToReviewResponse.setSubjectName(subjectName);
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


    public GetTutorsToSendEmailResponse getTutorsToSendEmail(GetTutorsToSendEmailRequest request){

        // Initialize the response
        GetTutorsToSendEmailResponse getTutorsResponse = new GetTutorsToSendEmailResponse();

        try{
            // Get the request variables
            int groupId = request.getGroupId();

            // Get the list of tutors to send an email from belongin to a group
            List<TutorInfoToSendEmailModel> tutorList = this.learnerGroupService.getTutorsToSendEmailFromGroup(groupId);

            // Set the tutor list of response object
            getTutorsResponse.setTutorInfoList(tutorList);

        }catch(Exception exc){
            // Log the exception
            LOG.debug("Error getting the tutors to send an email. Exception: " + exc.getMessage());

            // Has an error retrieving the done exams by the user
            getTutorsResponse.setHasError(true);
        }

        return getTutorsResponse;
    }

    public GetNewExamResponse getNewExam(GetNewExamRequest request, HttpServletRequest httpRequest){

        // Initialize the response
        GetNewExamResponse getNewExamResponse = new GetNewExamResponse();

        // Initialize the private variables
        int learnerId = 0;
        int examId = 0;

        try{
            // Get the request variables
            examId = request.getExamId();
            String ip = httpRequest.getRemoteAddr();

            // Get the identifer of current learner
            learnerId = this.userService.getUserIdOfCurrentUser();

            // Check if the exam is already started
            if(this.learnerNewExamService.isExamAlreadyStarted(learnerId, examId)){

                // Set the error when the exam has already done by the user
                getNewExamResponse.setHasError(true);
                getNewExamResponse.setErrorMessage("The exam is already started"); // TODO: Translate the error
            }

            // Check if the exam is already done
            if(this.learnerNewExamService.isExamAlreadyDonde(learnerId, examId)){

                // Set the error when the exam has already done by the user
                getNewExamResponse.setHasError(true);
                getNewExamResponse.setErrorMessage("The exam is already done"); // TODO: Translate the error
            }

            // Check if the exam is out of time
            if(this.learnerNewExamService.isExamToStartOutOfTime(examId)){

                // Set the error because the exam must be finished
                getNewExamResponse.setHasError(true);
                getNewExamResponse.setErrorMessage("The exam is not available because it is out of time"); // TODO: Translate the error
            }

            // Get the exam to perform if there are no errors
            if(!getNewExamResponse.isHasError()){

                // Generate the new exam
                NewExamModel newExamModel = this.learnerNewExamService.generateNewExamForLearner(learnerId, examId, ip);

                // Set the generated exam to response
                getNewExamResponse.setNewExam(newExamModel);

                // Set the session interval period to avoid the session expires -> The maximum inactive interval will be the exam duration more an extra time
                httpRequest.getSession().setMaxInactiveInterval(newExamModel.getExamTime() * 60 + 15);
            }

        }catch (Exception exc){
            // Log the exception
            LOG.debug("Error getting the new exam to perform by learner. LearnerId:" + learnerId + " , ExamId:" + examId +" . Exception: " + exc.getMessage());

            // Has an error getting the new exam to perform by learner
            getNewExamResponse.setHasError(true);
        }

        // Return the response
        return getNewExamResponse;
    }

    public EndExamResponse endExam(EndExamRequest request){
        // Initialize the response
        EndExamResponse endExamResponse = new EndExamResponse();

        // Initialize the private variables
        int learnerId = 0;
        int examId = 0;

        try{
            // Get the request variables
            examId = request.getExamId();
            List<NewExamQuestionModel> questionList = request.getQuestionList();

            // Set the end date of the exam
            Date examEndDate = new Date(System.currentTimeMillis());

            // Get the the learner identifier
            learnerId = this.userService.getUserIdOfCurrentUser();

            // Check if the exam has ended out of time
            boolean isEndedOutOfTime = this.learnerNewExamService.isExamToEndOutOfTime(examId, learnerId, examEndDate);
            if(isEndedOutOfTime){

                // Set the error when the exam has ended out of time
                endExamResponse.setHasError(true);
                endExamResponse.setErrorMessage("The exam is ended out of time"); // TODO: Translate the error

            }else{

                // Update the answered questions in database
                this.learnerNewExamService.updateAnsweredQuestionsInDatabase(examId, learnerId, questionList);

                // Calculates the exam score
                this.learnerNewExamService.calculateExamScore(examId, learnerId, questionList, examEndDate);

                // Get the exam info and set to the response object
                ExamScoreInfoModel examScoreInfo = this.learnerNewExamService.getExamScoreInfo(examId, learnerId);
                endExamResponse.setExamScoreInfo(examScoreInfo);
            }
        }catch (Exception exc){
            // Log the exception
            LOG.debug("Error ending the exam. LearnerId:" + learnerId + " , ExamId:" + examId +" . Exception: " + exc.getMessage());

            // Has an error ending the exam
            endExamResponse.setHasError(true);
        }

        // Return the response
        return endExamResponse;
    }

}
