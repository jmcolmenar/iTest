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
package com.itest.service;

import com.itest.model.DoneExamInfoModel;
import com.itest.model.ExamExtraInfoModel;
import com.itest.model.ExamQuestionModel;

import java.util.List;

public interface LearnerExamService {

    /**
     * Get the list of available exams of current subject for the learner
     * @param groupId Group identifier corresponding to the current subject
     * @param learnerId User identifier of the learner
     * @return The list of available exams
     */
    List<ExamExtraInfoModel> getAvailableExamsList(int groupId, int learnerId);

    /**
     * Get the list of next exams of current subject for the learner
     * @param groupId Group identifier corresponding to the current subject
     * @param learnerId User identifier of the learner
     * @return The list of next exams
     */
    List<ExamExtraInfoModel> getNextExamsList(int groupId, int learnerId);

    /**
     * Get the list of done exams of current subject for the learner
     * @param groupId Group identifier corresponding to the current subject
     * @param learnerId User identifier of the learner
     * @return The list of done exams
     */
    List<DoneExamInfoModel> getDoneExamsList(int groupId, int learnerId);

    /**
     * Get the done exam info corresponding to the user
     * @param examId The exam identifier
     * @param learnerId The user identifier
     * @return The done exam by the user
     */
    DoneExamInfoModel getDoneExam(int examId, int learnerId);

    /**
     * Get the subject name from an exam
     * @param examId The exam identifier
     * @return The subject name
     */
    String getSubjectNameFromExam(int examId);

    /**
     * Get the questions of the exam to review by the learner
     * @param examId Exam identifier to review
     * @param learnerId User identifier of the learner
     * @return The list of exam questions to review
     */
    List<ExamQuestionModel> getExamQuestionsToReviewList(int examId, int learnerId);
}
