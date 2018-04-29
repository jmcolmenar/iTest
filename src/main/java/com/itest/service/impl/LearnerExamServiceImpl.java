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

import com.itest.component.FormatterComponent;
import com.itest.entity.*;
import com.itest.model.*;
import com.itest.repository.ExamenRepository;
import com.itest.repository.LogExamenRepository;
import com.itest.service.LearnerExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("learnerExamServiceImpl")
public class LearnerExamServiceImpl implements LearnerExamService {

    @Autowired
    @Qualifier("examenRepository")
    private ExamenRepository examenRepository;

    @Autowired
    @Qualifier("logExamenRepository")
    private LogExamenRepository logExamenRepository;

    @Autowired
    @Qualifier("formatterComponent")
    private FormatterComponent formatterComponent;

    /**
     * Get the list of available exams of current subject for the learner
     * @param groupId Group identifier corresponding to the current subject
     * @param learnerId User identifier of the learner
     * @return The list of available exams
     */
    public List<ExamExtraInfoModel> getAvailableExamsList(int groupId, int learnerId){
        // Get the list of available exams from database
        List<Examen> examsList = this.examenRepository.findAvailableExams(learnerId, groupId);

        // Convert the list of entity objects to model objects
        List<ExamExtraInfoModel> availableExamModelList = this.convertExamListToExamExtraInfoModelList(examsList);

        // Return the available exam model list
        return availableExamModelList;
    }

    /**
     * Get the list of next exams of current subject for the learner
     * @param groupId Group identifier corresponding to the current subject
     * @param learnerId User identifier of the learner
     * @return The list of next exams
     */
    public List<ExamExtraInfoModel> getNextExamsList(int groupId, int learnerId){
        // Get the list of next exams from database
        List<Examen> examsList = this.examenRepository.findNextExams(learnerId, groupId);

        // Convert the list of entity objects to model objects
        List<ExamExtraInfoModel> nextExamModelList = this.convertExamListToExamExtraInfoModelList(examsList);

        // Return the next exam model list
        return nextExamModelList;
    }

    /**
     * Get the list of done exams of current subject for the learner
     * @param groupId Group identifier corresponding to the current subject
     * @param learnerId User identifier of the learner
     * @return The list of done exams
     */
    public List<DoneExamInfoModel> getDoneExamsList(int groupId, int learnerId){

        // Get the done exams from database
        List<Examen> examList = this.examenRepository.findDoneExams(learnerId, groupId);

        // Convert the entity object list to model object list
        List<DoneExamInfoModel> doneExamModelList = this.convertExamenListToDoneExamInfoModelList(examList);

        // Return the done exam model list
        return doneExamModelList;
    }

    /**
     * Get the done exam info corresponding to the learner
     * @param examId The exam identifier
     * @param learnerId The user identifier
     * @return The done exam by the user
     */
    public DoneExamInfoModel getDoneExam(int examId, int learnerId){

        // Get the exam by id
        Examen exam = this.examenRepository.findDoneExamByUserIdAndExamId(learnerId, examId);

        // Convert the exam database object to model object
        DoneExamInfoModel doneExamModel = this.convertExamenToDoneExamInfoModel(exam);

        // Return the done exam model
        return doneExamModel;
    }

    /**
     * Get the subject name from an exam
     * @param examId The exam identifier
     * @return The subject name
     */
    public String getSubjectNameFromExam(int examId){

        // Get the exam by id
        Examen exam = this.examenRepository.findOne(examId);

        // Return the subject name
        return exam.getGrupos().getAsignaturas().getNombre();
    }

    /**
     * Get the questions of the exam to review by the learner
     * @param examId Exam identifier to review
     * @param learnerId User identifier of the learner
     * @return The list of exam questions to review
     */
    public List<ExamQuestionModel> getExamQuestionsToReviewList(int examId, int learnerId){

        // Get the logs of the exam to review
        List<LogExamen> logExamList = this.logExamenRepository.findByExamIdAndUserIdOrderById(examId, learnerId);

        // Convert the database objects to model objects
        List<ExamQuestionModel> questionModelList = this.convertLogExamListToExamQuestionModelList(logExamList);

        // Get the current exam from database
        Examen exam = this.examenRepository.findOne(examId);

        // Get the score for each question
        for (ExamQuestionModel question : questionModelList) {
            double score = this.getQuestionScore(question, exam, questionModelList.size());
            String scoreAsString = this.formatterComponent.formatNumberWithTwoDecimals(score);
            question.setScore(scoreAsString.equals("0.00") ? "0" : scoreAsString);
        }

        // Return the question model list
        return questionModelList;
    }

    /**
     * Method to calculate the question score based in current exam parameters
     * @param question The question
     * @param exam The current exam database object
     * @param numberQuestionsOfCurrentExam The number of question in the current exam
     * @return The score of the question
     */
    private double getQuestionScore(ExamQuestionModel question, Examen exam, int numberQuestionsOfCurrentExam) {
        // The question score variable
        double questionScore = 0;

        // Exam information variables
        boolean isPartialCorrection = exam.getCorrParcial() == 1;
        double questionFailedPenalty = exam.getPPregFallada();
        double questionNotAnsweredPenalty = exam.getPPregNoResp();
        double answerFailedPenalty = exam.getPRespFallada();
        boolean isConfidenceLevel = exam.getNivelConfianza() == 1;
        double rewardConfidenceLevel = exam.getRNivelConfianza();
        double penaltyConfidenceLevel = exam.getPNivelConfianza();

        // Question information variables
        double maxScorePerQuestion = (double)exam.getNotaMax() / (double)numberQuestionsOfCurrentExam;
        double minQuestionScore = maxScorePerQuestion * exam.getCotaCalifPreg();
        int numberCorrectAnswers = question.getNumberCorrectAnswers();
        int numberCheckedCorrectAnswers = (int)question.getAnswerList().stream().filter(a -> a.isChecked() && a.isRight()).count();
        int numberCheckedIncorrectAnswers = (int)question.getAnswerList().stream().filter(a -> a.isChecked() && !a.isRight()).count();

        /* Start the question score calculation */

        // Check if the question has any solution
        if(numberCorrectAnswers == 0){

            // Check if the learner has not answered
            if(numberCheckedCorrectAnswers == 0 && numberCheckedIncorrectAnswers == 0){

                // Max score for question when the learner has not answered
                questionScore = maxScorePerQuestion;

            }else{

                // Apply the penalty for question failed
                questionScore = -(maxScorePerQuestion * questionFailedPenalty);
            }
        }else{

            // Check if the question has not answered
            if(numberCheckedCorrectAnswers == 0 && numberCheckedIncorrectAnswers == 0){

                // Apply the penalty for question not answer
                questionScore = -(maxScorePerQuestion * questionNotAnsweredPenalty);

            }else{

                // Check if the exam has partial correction
                if(isPartialCorrection){

                    // Check if the learner has all answers right
                    if(numberCheckedCorrectAnswers == numberCorrectAnswers && numberCheckedIncorrectAnswers == 0){

                        // Set the max grade per question
                        questionScore = maxScorePerQuestion;

                    }else{

                        // Increment the right answers
                        questionScore += (maxScorePerQuestion / numberCorrectAnswers) * numberCheckedCorrectAnswers;

                        // Substract the failed answers
                        questionScore -= (maxScorePerQuestion * answerFailedPenalty) * numberCheckedIncorrectAnswers;

                        // Check the question score is not less than minimum question score
                        if(questionScore <  minQuestionScore){

                            // The question score will be the minimum
                            questionScore = minQuestionScore;
                        }
                    }

                }else{

                    // When the exam has no partial correction, the question will be only right when the learner has all answers right
                    if(numberCheckedCorrectAnswers == numberCorrectAnswers && numberCheckedIncorrectAnswers == 0){

                        // Set the max grade per question
                        questionScore = maxScorePerQuestion;

                    }else{

                        // Apply the question failed penalty
                        questionScore = -(maxScorePerQuestion * questionFailedPenalty);
                    }
                }
            }
        }

        // Apply the confidence level criteria
        if(isConfidenceLevel){

            // Check if the learner has all answers right
            if(numberCheckedCorrectAnswers == numberCorrectAnswers && numberCheckedIncorrectAnswers == 0){

                // Apply the confidence level reward when the confidence level is active in the question
                if(question.isActiveConfidenceLevel()) {
                    questionScore += maxScorePerQuestion * rewardConfidenceLevel;
                }

            }else{

                // Apply the confidence level penalty when the confidence level is active in the question
                if(question.isActiveConfidenceLevel()){
                    questionScore -= maxScorePerQuestion * penaltyConfidenceLevel;
                }
            }
        }

        // Check if the question score is negative zero to set as positive zero (In order to display in web page)
        if(questionScore == -0.0){
            questionScore = 0.0;
        }

        // Return the question score
        return questionScore;
    }

    private List<ExamQuestionModel> convertLogExamListToExamQuestionModelList(List<LogExamen> logExamenList){

        // Initialize the Auxiliar Map of Exam Question
        Map<Integer, List<ExamQuestionAnswerModel>> examQuestionMapAux = new HashMap<>();

        // Initialize the Exam Question model list
        List<ExamQuestionModel> examQuestionModelList = new ArrayList<>();

        if(logExamenList != null && logExamenList.size() > 0){
            for (LogExamen logExamen : logExamenList) {

                // Get the question database object and fill the Exam Question model
                Pregunta pregunta = logExamen.getPreguntas();
                ExamQuestionModel examQuestionModel = new ExamQuestionModel();
                examQuestionModel.setQuestionId(pregunta.getIdpreg());
                examQuestionModel.setStatement(pregunta.getEnunciado());
                examQuestionModel.setComment(pregunta.getComentario());
                examQuestionModel.setNumberCorrectAnswers(pregunta.getNRespCorrectas());
                examQuestionModel.setActiveConfidenceLevel(logExamen.getNivelConfianza() == 1);

                // Get the answer database object and fill the asnwer model
                Respuesta respuesta = logExamen.getRespuestas();
                ExamQuestionAnswerModel examQuestionAnswerModel = new ExamQuestionAnswerModel();
                examQuestionAnswerModel.setAsnwerId(respuesta.getIdresp());
                examQuestionAnswerModel.setText(respuesta.getTexto());
                examQuestionAnswerModel.setRight(respuesta.getSolucion() == 1);
                examQuestionAnswerModel.setChecked(logExamen.getMarcada() == 1);

                // Check if the question has been added to the auxiliar map
                if(examQuestionMapAux.containsKey(pregunta.getIdpreg())){

                    // Add the answer to the answer list
                    examQuestionMapAux.get(pregunta.getIdpreg()).add(examQuestionAnswerModel);

                }else{

                    // Add the question model to the list
                    examQuestionModelList.add(examQuestionModel);

                    // Initialize a list of answer model and add the answer
                    List<ExamQuestionAnswerModel> examQuestionAnswerModelList = new ArrayList<>();
                    examQuestionAnswerModelList.add(examQuestionAnswerModel);

                    // Put the answer list in the dictionary associated by the question
                    examQuestionMapAux.put(pregunta.getIdpreg(), examQuestionAnswerModelList);
                }
            }
        }

        // Fill the question objects with the answer list from the map
        for (ExamQuestionModel questionModel : examQuestionModelList) {
            questionModel.setAnswerList(examQuestionMapAux.get(questionModel.getQuestionId()));
        }

        // Return the list of Question Exam Model
        return examQuestionModelList;
    }


    private DoneExamInfoModel convertExamenToDoneExamInfoModel(Examen exam){
        // Initialize and fill the model object
        DoneExamInfoModel doneExam = new DoneExamInfoModel();
        doneExam.setExamId(exam.getIdexam());
        doneExam.setExamName(exam.getTitulo());
        doneExam.setMaxScore(this.formatterComponent.formatNumberWithTwoDecimals(exam.getNotaMax()));

        // Get the "Calificacion" object corresponding to the user (It should not be null and only one)
        Calificacion calificacion = exam.getCalifs().stream().findFirst().get();
        doneExam.setScore(this.formatterComponent.formatNumberWithTwoDecimals(calificacion.getNota()));
        doneExam.setStartDate(this.formatterComponent.formatDateToString(calificacion.getFechaIni()));
        doneExam.setEndDate(this.formatterComponent.formatDateToString(calificacion.getFechaFin()));
        if(calificacion.getFechaFin().getTime() >= calificacion.getFechaIni().getTime()){
            doneExam.setTime(this.formatterComponent.formatMillisecondsToHoursMinutesAndSeconds(calificacion.getFechaFin().getTime() - calificacion.getFechaIni().getTime()));
        }else{
            doneExam.setTime(this.formatterComponent.formatMillisecondsToHoursMinutesAndSeconds(0));
        }

        // Check if the review is available
        Date now = new Date();
        boolean isAvailableReview = exam.getRevActiva() == 1 // Active review
                && exam.getFechaIni().before(now) && exam.getFechaFin().after(now) // Review date period
                && calificacion.getFechaFin().before(now); // The exam has finished
        doneExam.setAvailableReview(isAvailableReview);

        // Return the exam model
        return doneExam;
    }

    private List<DoneExamInfoModel> convertExamenListToDoneExamInfoModelList(List<Examen> examenList){

        // Initialize the Donde Exam Info Model list
        List<DoneExamInfoModel> doneExamInfoModelList = new ArrayList<>();

        // Check the Examen list is not empty
        if(examenList != null && !examenList.isEmpty()){

            for(Examen exam : examenList){
                // Convert the done exam to exam model
                DoneExamInfoModel doneExam = this.convertExamenToDoneExamInfoModel(exam);

                // Add DoneExamModel object to the list
                doneExamInfoModelList.add(doneExam);
            }
        }

        // Return the Donde Exam Info Model list
        return doneExamInfoModelList;
    }

    private List<ExamExtraInfoModel> convertExamListToExamExtraInfoModelList(List<Examen> examenList){

        // Initialize the Exam Extra Info Model list
        List<ExamExtraInfoModel> examExtraInfoModelList = new ArrayList<>();

        // Check the Examen list is not empty
        if(examenList != null && !examenList.isEmpty()){

            for(Examen exam : examenList){

                // Initialize and fill the model object
                ExamExtraInfoModel examExtraInfo = new ExamExtraInfoModel();
                examExtraInfo.setExamId(exam.getIdexam());
                examExtraInfo.setExamName(exam.getTitulo());
                examExtraInfo.setStartDate(this.formatterComponent.formatDateToString(exam.getFechaIni()));
                examExtraInfo.setEndDate(this.formatterComponent.formatDateToString(exam.getFechaFin()));
                examExtraInfo.setMaxScore(this.formatterComponent.formatNumberWithTwoDecimals(exam.getNotaMax()));
                examExtraInfo.setActiveReview(exam.getRevActiva() == 1);
                examExtraInfo.setStartReviewDate(this.formatterComponent.formatDateToString(exam.getFechaIniRev()));
                examExtraInfo.setEndReviewDate(this.formatterComponent.formatDateToString(exam.getFechaFinRev()));
                examExtraInfo.setExamTime(exam.getDuracion());
                examExtraInfo.setQuestionsNumber(exam.getTemasExam().stream().mapToInt(TemaExamen::getNPregs).sum());
                examExtraInfo.setShowRightAnswersNumber(exam.getMuestraNumCorr() == 1);

                // Set partial correction
                double penaltyFailedQuestion = (exam.getNotaMax() / examExtraInfo.getQuestionsNumber()) * exam.getPPregFallada();
                double penaltyNotAnsweredQuestion = (exam.getNotaMax() / examExtraInfo.getQuestionsNumber()) * exam.getPPregNoResp();
                ExamPartialCorrectionInfoModel partialCorrectionModel = new ExamPartialCorrectionInfoModel();
                partialCorrectionModel.setActivePartialCorrection(exam.getCorrParcial() == 1);
                partialCorrectionModel.setPenaltyFailedQuestion(this.formatterComponent.formatNumberWithTwoDecimals(penaltyFailedQuestion));
                partialCorrectionModel.setPenaltyNotAnsweredQuestion(this.formatterComponent.formatNumberWithTwoDecimals(penaltyNotAnsweredQuestion));
                examExtraInfo.setPartialCorrection(partialCorrectionModel);

                // Set confidence level
                double penaltyConfidenceLevel = (exam.getNotaMax() / examExtraInfo.getQuestionsNumber()) * exam.getPNivelConfianza();
                double rewardConfidenceLevel = (exam.getNotaMax() / examExtraInfo.getQuestionsNumber()) * exam.getRNivelConfianza();
                ExamConfidenceLevelInfoModel confidenceLevelModel = new ExamConfidenceLevelInfoModel();
                confidenceLevelModel.setActiveConfidenceLevel(exam.getNivelConfianza() == 1);
                confidenceLevelModel.setPenalty(this.formatterComponent.formatNumberWithTwoDecimals(penaltyConfidenceLevel));
                confidenceLevelModel.setReward(this.formatterComponent.formatNumberWithTwoDecimals(rewardConfidenceLevel));
                examExtraInfo.setConfidenceLevel(confidenceLevelModel);

                // Add exam model object to the list
                examExtraInfoModelList.add(examExtraInfo);
            }
        }

        // Return the Exam Extra Info Model list
        return examExtraInfoModelList;
    }
}
