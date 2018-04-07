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

        // Return the question model list
        return questionModelList;
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

                // TODO: Set the question score
                examQuestionModel.setScore("0");

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
        doneExam.setTime(this.formatterComponent.formatMillisecondsToHoursMinutesAndSeconds(calificacion.getFechaFin().getTime() - calificacion.getFechaIni().getTime()));

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
