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

import com.itest.model.DoneExamInfoModel;
import com.itest.model.ExamExtraInfoModel;
import com.itest.model.MultimediaElementModel;

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
     * Calculates the score of a question of test type in an donde exam by a learner
     * @param learnerId The learner identifier
     * @param examId The exam identifier
     * @param questionId The question identifier
     * @param numberQuestionsOfCurrentExam The number of questions in the current exam
     * @param updateAnswerScoreInDatabase To check if the score for the correct and incorrect answers must be updated in database
     * @return The score of the question
     */
    double calculateTestQuestionScore(int learnerId, int examId, int questionId, int numberQuestionsOfCurrentExam, boolean updateAnswerScoreInDatabase);

    /**
     * Calculates the score of a question of short answer type in an donde exam by a learner
     * @param learnerId The learner identifier
     * @param examId The exam identifier
     * @param questionId The question identifier
     * @param numberQuestionsOfCurrentExam The number of questions in the current exam
     * @param updateAnswerScoreInDatabase To check if the score for the correct and incorrect answers must be updated in database
     * @return The score of the question
     */
    double calculateShortAnswerQuestionScore(int learnerId, int examId, int questionId, int numberQuestionsOfCurrentExam, boolean updateAnswerScoreInDatabase);
}
