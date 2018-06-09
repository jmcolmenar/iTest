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
import com.itest.entity.*;
import com.itest.model.ReviewExamAnswerModel;
import com.itest.model.ReviewExamQuestionModel;
import com.itest.repository.LogExamenFillRepository;
import com.itest.repository.LogExamenRepository;
import com.itest.service.business.LearnerExamService;
import com.itest.service.business.LearnerMultimediaService;
import com.itest.service.business.LearnerReviewExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.itest.constant.QuestionTypeConstant.SHORT_ANSWER;
import static com.itest.constant.QuestionTypeConstant.TEST;

@Service("learnerReviewExamServiceImpl")
public class LearnerReviewExamServiceImpl implements LearnerReviewExamService {

    @Autowired
    @Qualifier("logExamenRepository")
    private LogExamenRepository logExamenRepository;

    @Autowired
    @Qualifier("logExamenFillRepository")
    private LogExamenFillRepository logExamenFillRepository;

    @Autowired
    @Qualifier("learnerExamServiceImpl")
    private LearnerExamService learnerExamService;

    @Autowired
    @Qualifier("learnerMultimediaServiceImpl")
    private LearnerMultimediaService learnerMultimediaService;

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

        // Get the logs of questions of Short Answer type
        List<LogExamenFill> logExamFillList = this.logExamenFillRepository.findByExamIdAndUserIdOrderdById(examId, learnerId);

        // Convert the database objects to model objects and add them to the question model list
        questionModelList.addAll(this.convertLogExamFillListToReviewExamQuestionModelList(logExamFillList));

        // Get the score for each question
        for (ReviewExamQuestionModel question : questionModelList) {

            // Check the question type
            double score = 0.0;
            if(question.getType() == TEST){

                // Calculate the question score of test type
                score = this.learnerExamService.calculateTestQuestionScore(learnerId, examId, question.getQuestionId(), questionModelList.size(), false);

            }else if(question.getType() == SHORT_ANSWER){

                // Calculate the question score of Short Answer type
                score = this.learnerExamService.calculateShortAnswerQuestionScore(learnerId, examId, question.getQuestionId(), questionModelList.size(), false);
            }

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

        // Initialize the Exam Question model list
        List<ReviewExamQuestionModel> examQuestionModelList = new ArrayList<>();

        // Convert all log exams to question model
        if(logExamenList != null && logExamenList.size() > 0){
            for (LogExamen logExamen : logExamenList) {

                // Get the question id
                int questionId = logExamen.getPregunta().getIdpreg();

                // Get the answer database object and fill the asnwer model
                Respuesta respuesta = logExamen.getRespuesta();
                ReviewExamAnswerModel answerModel = new ReviewExamAnswerModel();
                answerModel.setAsnwerId(respuesta.getIdresp());
                answerModel.setText(respuesta.getTexto());
                answerModel.setRight(respuesta.getSolucion() == 1);
                answerModel.setChecked(logExamen.getMarcada() == 1);

                // Set the multimedia elements of answer
                answerModel.setMultimediaList(this.learnerMultimediaService.getMultimediaModelListFromDatabaseObjectList(respuesta.getExtraRespuestas()));

                // Check if the question has been added to the list
                if(examQuestionModelList.stream().anyMatch(x -> x.getQuestionId() == questionId)){

                    // Add ansert to the question in the list
                    ReviewExamQuestionModel question = examQuestionModelList.stream().filter(x -> x.getQuestionId() == questionId).findFirst().get();
                    question.getAnswerList().add(answerModel);

                }else{

                    // Get the question database object and convert it to Exam Question model
                    Pregunta pregunta = logExamen.getPregunta();
                    ReviewExamQuestionModel examQuestionModel = this.convertPreguntaToExamQuestionModel(pregunta, logExamen.getNivelConfianza() == 1);

                    // Initialize a list of answer model and add the answer
                    List<ReviewExamAnswerModel> answerModelList = new ArrayList<>();
                    answerModelList.add(answerModel);
                    examQuestionModel.setAnswerList(answerModelList);

                    // Put the question in the dictionary
                    examQuestionModelList.add(examQuestionModel);
                }
            }
        }
        // Return the list of Question Exam Model
        return examQuestionModelList;
    }

    /**
     * Convert the log of the exam fill to the question list of exam to review
     * @param logExamenFillList The log exam fill list from database
     * @return The model object list with the question of the exam to review
     */
    private List<ReviewExamQuestionModel> convertLogExamFillListToReviewExamQuestionModelList(List<LogExamenFill> logExamenFillList){

        // Initialize the Auxiliar Map of Exam Question
        List<ReviewExamQuestionModel> examQuestionModelList = new ArrayList<>();

        // Convert all log exams to question model
        if(logExamenFillList != null && logExamenFillList.size() > 0){
            for (LogExamenFill logExamenFill : logExamenFillList) {

                // Get the question id
                int questionId = logExamenFill.getPregunta().getIdpreg();

                // Get the answer database object and fill the asnwer model
                Respuesta respuesta = logExamenFill.getPregunta().getRespuestas().get(0);
                ReviewExamAnswerModel answerModel = new ReviewExamAnswerModel();
                answerModel.setAsnwerId(respuesta.getIdresp());
                answerModel.setText(respuesta.getTexto());
                answerModel.setAnsweredText(logExamenFill.getResp() != null ? logExamenFill.getResp() : "");
                answerModel.setRight(answerModel.getAnsweredText().trim().equalsIgnoreCase(answerModel.getText().trim()));
                answerModel.setChecked(logExamenFill.getResp() != null);

                // Check if the question has been added to the list
                if(examQuestionModelList.stream().anyMatch(x -> x.getQuestionId() == questionId)){

                    // Add ansert to the question in the list
                    ReviewExamQuestionModel question = examQuestionModelList.stream().filter(x -> x.getQuestionId() == questionId).findFirst().get();
                    question.getAnswerList().add(answerModel);

                }else{

                    // Get the question database object and convert it to Exam Question model
                    Pregunta pregunta = logExamenFill.getPregunta();
                    ReviewExamQuestionModel examQuestionModel = this.convertPreguntaToExamQuestionModel(pregunta, logExamenFill.getNivelConfianza() == 1);

                    // Initialize a list of answer model and add the answer and set it to the question model
                    List<ReviewExamAnswerModel> answerModelList = new ArrayList<>();
                    answerModelList.add(answerModel);
                    examQuestionModel.setAnswerList(answerModelList);

                    // Put the question in the dictionary
                    examQuestionModelList.add(examQuestionModel);
                }
            }
        }

        // Return the list of Question Exam Model
        return examQuestionModelList;
    }

    /**
     * Convert the question database object to model object
     * @param question The question database object
     * @param activeConfidenceLevel If the confidence level is active for this question
     * @return The question model
     */
    private ReviewExamQuestionModel convertPreguntaToExamQuestionModel(Pregunta question, boolean activeConfidenceLevel){

        // Initialize and fill the model object
        ReviewExamQuestionModel examQuestionModel = new ReviewExamQuestionModel();
        examQuestionModel.setQuestionId(question.getIdpreg());
        examQuestionModel.setStatement(question.getEnunciado());
        examQuestionModel.setComment(question.getComentario());
        examQuestionModel.setNumberCorrectAnswers(question.getNRespCorrectas());
        examQuestionModel.setActiveConfidenceLevel(activeConfidenceLevel);
        examQuestionModel.setType(question.getTipo());

        // Set the multimedia elements
        examQuestionModel.setMultimediaList(this.learnerMultimediaService.getMultimediaModelListFromDatabaseObjectList(question.getExtraPreguntas()));
        examQuestionModel.setCommentMultimediaList(this.learnerMultimediaService.getMultimediaModelListFromDatabaseObjectList(question.getExtraPreguntaComentarios()));

        // Return the question model
        return examQuestionModel;
    }

}
