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

import com.itest.component.FormatterComponent;
import com.itest.entity.Examen;
import com.itest.entity.LogExamen;
import com.itest.entity.Pregunta;
import com.itest.entity.Respuesta;
import com.itest.model.ReviewExamAnswerModel;
import com.itest.model.ReviewExamQuestionModel;
import com.itest.repository.ExamenRepository;
import com.itest.repository.LogExamenRepository;
import com.itest.service.business.LearnerExamService;
import com.itest.service.business.LearnerReviewExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("learnerReviewExamServiceImpl")
public class LearnerReviewExamServiceImpl implements LearnerReviewExamService {

    @Autowired
    @Qualifier("examenRepository")
    private ExamenRepository examenRepository;

    @Autowired
    @Qualifier("logExamenRepository")
    private LogExamenRepository logExamenRepository;

    @Autowired
    @Qualifier("learnerExamServiceImpl")
    private LearnerExamService learnerExamService;

    @Autowired
    @Qualifier("formatterComponent")
    private FormatterComponent formatterComponent;

    /**
     * Get the questions of the exam to review by the learner
     * @param examId Exam identifier to review
     * @param learnerId User identifier of the learner
     * @return The list of exam questions to review
     */
    public List<ReviewExamQuestionModel> getExamQuestionsToReviewList(int examId, int learnerId){

        // Get the logs of the exam to review
        List<LogExamen> logExamList = this.logExamenRepository.findByExamIdAndUserIdOrderById(examId, learnerId);

        // Convert the database objects to model objects
        List<ReviewExamQuestionModel> questionModelList = this.convertLogExamListToReviewExamQuestionModelList(logExamList);

        // Get the current exam from database
        Examen exam = this.examenRepository.findOne(examId);

        // Get the score for each question
        for (ReviewExamQuestionModel question : questionModelList) {
            double score = this.learnerExamService.calculateQuestionScore(learnerId, examId, question.getQuestionId(), questionModelList.size());
            String scoreAsString = this.formatterComponent.formatNumberWithTwoDecimals(score);
            question.setScore(scoreAsString.equals("0.00") ? "0" : scoreAsString);
        }

        // Return the question model list
        return questionModelList;
    }

    /**
     * Convert the log of the exam to the question list of exam to review
     * @param logExamenList The log exam list from database
     * @return The model object list with the question of the exam to review
     */
    private List<ReviewExamQuestionModel> convertLogExamListToReviewExamQuestionModelList(List<LogExamen> logExamenList){

        // Initialize the Auxiliar Map of Exam Question
        Map<Integer, List<ReviewExamAnswerModel>> examQuestionMapAux = new HashMap<>();

        // Initialize the Exam Question model list
        List<ReviewExamQuestionModel> examQuestionModelList = new ArrayList<>();

        if(logExamenList != null && logExamenList.size() > 0){
            for (LogExamen logExamen : logExamenList) {

                // Get the question id
                int questionId = logExamen.getPreguntas().getIdpreg();

                // Get the answer database object and fill the asnwer model
                Respuesta respuesta = logExamen.getRespuestas();
                ReviewExamAnswerModel answerModel = new ReviewExamAnswerModel();
                answerModel.setAsnwerId(respuesta.getIdresp());
                answerModel.setText(respuesta.getTexto());
                answerModel.setRight(respuesta.getSolucion() == 1);
                answerModel.setChecked(logExamen.getMarcada() == 1);

                // Check if the question has been added to the auxiliar map
                if(examQuestionMapAux.containsKey(questionId)){

                    // Add the answer to the answer list
                    examQuestionMapAux.get(questionId).add(answerModel);

                }else{

                    // Get the question database object and fill the Exam Question model
                    Pregunta pregunta = logExamen.getPreguntas();
                    ReviewExamQuestionModel examQuestionModel = new ReviewExamQuestionModel();
                    examQuestionModel.setQuestionId(pregunta.getIdpreg());
                    examQuestionModel.setStatement(pregunta.getEnunciado());
                    examQuestionModel.setComment(pregunta.getComentario());
                    examQuestionModel.setNumberCorrectAnswers(pregunta.getNRespCorrectas());
                    examQuestionModel.setActiveConfidenceLevel(logExamen.getNivelConfianza() == 1);

                    // Add the question model to the list
                    examQuestionModelList.add(examQuestionModel);

                    // Initialize a list of answer model and add the answer
                    List<ReviewExamAnswerModel> answerModelList = new ArrayList<>();
                    answerModelList.add(answerModel);

                    // Put the answer list in the dictionary associated by the question
                    examQuestionMapAux.put(pregunta.getIdpreg(), answerModelList);
                }
            }
        }

        // Fill the question objects with the answer list from the map
        for (ReviewExamQuestionModel questionModel : examQuestionModelList) {
            questionModel.setAnswerList(examQuestionMapAux.get(questionModel.getQuestionId()));
        }

        // Return the list of Question Exam Model
        return examQuestionModelList;
    }

}
