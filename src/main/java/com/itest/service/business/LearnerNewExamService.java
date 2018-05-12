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
package com.itest.service.business;

import com.itest.model.NewExamModel;
import com.itest.model.NewExamQuestionModel;

import java.util.Date;
import java.util.List;

public interface LearnerNewExamService {

    /**
     * Check if the exam is already done by the learner
     * @param learnerId The learner identifier
     * @param examId The exam identifier
     * @return Whether the exam is already done or not
     */
    boolean isExamAlreadyDonde(int learnerId, int examId);

    /**
     * Check if the exam must be finised due to the exam end date is before than now
     * @param examId The exam identifier
     * @return Whether the exam must be finished or not
     */
    boolean examMustBeFinished(int examId);

    /**
     * Check if the exam is ended out of date
     * @param examId The exam identifier
     * @param learnerId The learner identifier
     * @param examEndDate The exam end date
     * @return Whether the exam is ended out of date or not
     */
    boolean isExamEndedOutOfDate(int examId, int learnerId, Date examEndDate);

    /**
     * Generate a new exam to perform by the learner
     * @param learnerId The learner identifier
     * @param examId The exam identifier
     * @param ip The ip of the learner
     * @return The generated exam for learner
     */
    NewExamModel generateNewExamForLearner(int learnerId, int examId, String ip);

    /**
     * Update the answered question of the exam in database
     * @param examId The exam identifier
     * @param learnerId The learner identifier
     * @param questionList The answered questions of the exam
     */
    void updateAnsweredQuestionsInDatabase(int examId, int learnerId, List<NewExamQuestionModel> questionList);

    /**
     * Calculates the exam score and update it in database
     * @param examId The exam identifer
     * @param learnerId The learner identifier
     * @param questionList The question list of the exam
     * @param examEndDate The exam end date
     */
    void calculateExamScore(int examId, int learnerId, List<NewExamQuestionModel> questionList, Date examEndDate);
}
