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
import com.itest.constant.MultimediaPathConstant;
import com.itest.constant.MultimediaTypeConstant;
import com.itest.entity.*;
import com.itest.model.*;
import com.itest.repository.ExamenRepository;
import com.itest.repository.LogExamenRepository;
import com.itest.repository.PreguntaRepository;
import com.itest.service.business.LearnerExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service("learnerExamServiceImpl")
public class LearnerExamServiceImpl implements LearnerExamService {

    @Autowired
    @Qualifier("examenRepository")
    private ExamenRepository examenRepository;

    @Autowired
    @Qualifier("preguntaRepository")
    private PreguntaRepository preguntaRepository;

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
        return exam.getGrupo().getAsignatura().getNombre();
    }

    /**
     * Calculates the score of a question in an donde exam by a learner
     * @param learnerId The learner identifier
     * @param examId The exam identifier
     * @param questionId The question identifier
     * @param numberQuestionsOfCurrentExam The number of questions in the current exam
     * @param updateAnswerScoreInDatabase To check if the score for the correct and incorrect answers must be updated in database
     * @return The score of the question
     */
    public double calculateQuestionScore(int learnerId, int examId, int questionId, int numberQuestionsOfCurrentExam, boolean updateAnswerScoreInDatabase) {
        // The question score variable
        double questionScore = 0;

        // Exam information variables
        Examen exam = this.examenRepository.findOne(examId);
        boolean isPartialCorrection = exam.getCorrParcial() == 1;
        double questionFailedPenalty = exam.getPPregFallada();
        double questionNotAnsweredPenalty = exam.getPPregNoResp();
        double answerFailedPenalty = exam.getPRespFallada();
        boolean isConfidenceLevel = exam.getNivelConfianza() == 1;
        double rewardConfidenceLevel = exam.getRNivelConfianza();
        double penaltyConfidenceLevel = exam.getPNivelConfianza();

        // Question information variables
        Pregunta pregunta = this.preguntaRepository.findOne(questionId);
        double maxScorePerQuestion = (double)exam.getNotaMax() / (double)numberQuestionsOfCurrentExam;
        double minQuestionScore = maxScorePerQuestion * exam.getCotaCalifPreg();
        int numberCorrectAnswers = pregunta.getNRespCorrectas();

        // Get the asnwers of question answered by the user in order to get the number of correct and incorrect answers
        List<LogExamen> logExamList = this.logExamenRepository.findByExamIdAndUserIdAndQuestionId(examId, learnerId, questionId);
        List<LogExamen> checkedCorrectAnswers = logExamList.stream().filter(logExam -> logExam.getMarcada() == 1 && logExam.getRespuesta().getSolucion() == 1).collect(Collectors.toList());
        List<LogExamen> checkedIncorrectAnswers = logExamList.stream().filter(logExam -> logExam.getMarcada() == 1 && logExam.getRespuesta().getSolucion() == 0).collect(Collectors.toList());
        int numberCheckedCorrectAnswers = checkedCorrectAnswers.size();
        int numberCheckedIncorrectAnswers = checkedIncorrectAnswers.size();

        // Check if the learner has ckeched the confidence level in the question
        Optional<LogExamen> firstAnswer = logExamList.stream().findFirst(); // All answers of question has the "nivel_confianza" field set as the same
        boolean activeConfidenceLevelInQuestion = firstAnswer.isPresent() && firstAnswer.get().getNivelConfianza() == 1;

        // Variables to calculates the score of correct and incorrect answers
        boolean updateCorrectAnswers = false;
        double correctAnswersScore = 0.0;
        boolean updateIncorrectAnswers = false;
        double incorrectAnswersScore = 0.0;

        /* Start the question score calculation */

        // Check if the question has any solution
        if(numberCorrectAnswers == 0){

            // Check if the learner has not answered
            if(numberCheckedCorrectAnswers == 0 && numberCheckedIncorrectAnswers == 0){

                // Max score for question when the learner has not answered
                questionScore = maxScorePerQuestion;

            }else{

                // Check if the exam has partial correction
                if(isPartialCorrection){

                    // The question score will be the minimum
                    questionScore = minQuestionScore;

                }else{

                    // Apply the penalty for question failed
                    questionScore = -(maxScorePerQuestion * questionFailedPenalty);
                }

                // The answer score for incorrect answers will be the penalty divided between the answers
                updateIncorrectAnswers = true;
                incorrectAnswersScore = questionScore / numberCheckedIncorrectAnswers;
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

                        // All correct asnwers will have the maximum score
                        updateCorrectAnswers = true;
                        correctAnswersScore = questionScore / numberCheckedCorrectAnswers;

                    }else{

                        // Increment the right answers
                        questionScore += (maxScorePerQuestion / numberCorrectAnswers) * numberCheckedCorrectAnswers;

                        // Substract the failed answers
                        questionScore -= (maxScorePerQuestion * answerFailedPenalty) * numberCheckedIncorrectAnswers;

                        // Check the question score is not less than minimum question score
                        if(questionScore <  minQuestionScore){

                            // The question score will be the minimum
                            questionScore = minQuestionScore;

                            // The score of answers is divided between the correct and incorrect answers
                            updateCorrectAnswers = true;
                            updateIncorrectAnswers = true;
                            correctAnswersScore = questionScore / (numberCheckedCorrectAnswers + numberCheckedIncorrectAnswers);
                            incorrectAnswersScore = questionScore / (numberCheckedCorrectAnswers + numberCheckedIncorrectAnswers);

                        }else{

                            // Thesocre of correct answers will be the max score per question divided by the maximum number of correct answers
                            updateCorrectAnswers = true;
                            correctAnswersScore = maxScorePerQuestion / numberCorrectAnswers;

                            // The score of incorrect answers will be the pre-configured penalty per answer failed
                            updateIncorrectAnswers = true;
                            incorrectAnswersScore = - (maxScorePerQuestion * answerFailedPenalty);
                        }
                    }

                }else{

                    // When the exam has no partial correction, the question will be only right when the learner has all answers right
                    if(numberCheckedCorrectAnswers == numberCorrectAnswers && numberCheckedIncorrectAnswers == 0){

                        // Set the max grade per question
                        questionScore = maxScorePerQuestion;

                        // All correct asnwers will have the maximum score
                        updateCorrectAnswers = true;
                        correctAnswersScore = questionScore / numberCorrectAnswers;

                    }else{

                        // Apply the question failed penalty
                        questionScore = -(maxScorePerQuestion * questionFailedPenalty);

                        // The answer score for correct answers will be 0
                        updateCorrectAnswers = true;
                        correctAnswersScore = 0.0;

                        // The answer score for incorrect answers will be the penalty divided between the answers
                        if(numberCheckedIncorrectAnswers > 0){
                            updateIncorrectAnswers = true;
                            incorrectAnswersScore = questionScore / numberCheckedIncorrectAnswers;
                        }
                    }
                }
            }
        }

        // Apply the confidence level criteria
        if(isConfidenceLevel){

            // Check if the learner has all answers right
            if(numberCheckedCorrectAnswers == numberCorrectAnswers && numberCheckedIncorrectAnswers == 0){

                // Apply the confidence level reward when the confidence level is active in the question
                if(activeConfidenceLevelInQuestion) {
                    questionScore += maxScorePerQuestion * rewardConfidenceLevel;

                    // All correct asnwers will have the maximum score
                    if(numberCheckedCorrectAnswers > 0){
                        updateCorrectAnswers = true;
                        correctAnswersScore = questionScore / numberCheckedCorrectAnswers;
                    }
                }

            }else{

                // Apply the confidence level penalty when the confidence level is active in the question
                if(activeConfidenceLevelInQuestion){
                    questionScore -= maxScorePerQuestion * penaltyConfidenceLevel;

                    // The answer score for correct answers will be 0
                    updateCorrectAnswers = true;
                    correctAnswersScore = 0.0;

                    // The answer score for incorrect answers will be the penalty divided between the answers
                    if(numberCheckedIncorrectAnswers > 0){
                        updateIncorrectAnswers = true;
                        incorrectAnswersScore = questionScore / numberCheckedIncorrectAnswers;
                    }
                }
            }
        }

        // Check if the score for each answer must be updated in database
        if(updateAnswerScoreInDatabase){

            // Check if update the score for correct answer
            if(updateCorrectAnswers){
                for(LogExamen logExamen : checkedCorrectAnswers){
                    logExamen.setPuntos(new BigDecimal(correctAnswersScore));
                    this.logExamenRepository.save(logExamen);
                }
            }

            // Check if update the score for incorrect answer
            if(updateIncorrectAnswers){
                for(LogExamen logExamen : checkedIncorrectAnswers){
                    logExamen.setPuntos(new BigDecimal(incorrectAnswersScore));
                    this.logExamenRepository.save(logExamen);
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

    /**
     * Get the Multimedia Element Model object from the database object
     * @param databaseMultimediaElement The database object with the multimedia element information
     * @return The model object with the multimedia info
     */
    public MultimediaElementModel getMultimediaElementModelFromDatabaseEntity(Object databaseMultimediaElement){

        // Get the common fields of the multimedia element from the entities in database
        String ruta;
        int tipo;
        String ancho;
        String alto;
        int geogebraType = 0;
        if(databaseMultimediaElement.getClass() == ExtraPregunta.class){
            ruta = ((ExtraPregunta)databaseMultimediaElement).getRuta();
            tipo = ((ExtraPregunta)databaseMultimediaElement).getTipo();
            ancho = ((ExtraPregunta)databaseMultimediaElement).getAncho();
            alto = ((ExtraPregunta)databaseMultimediaElement).getAlto();
            geogebraType = ((ExtraPregunta)databaseMultimediaElement).getGeogebraType();
        }else if(databaseMultimediaElement.getClass() == ExtraPreguntaComentario.class){
            ruta = ((ExtraPreguntaComentario)databaseMultimediaElement).getRuta();
            tipo = ((ExtraPreguntaComentario)databaseMultimediaElement).getTipo();
            ancho = ((ExtraPreguntaComentario)databaseMultimediaElement).getAncho();
            alto = ((ExtraPreguntaComentario)databaseMultimediaElement).getAlto();
        } else if(databaseMultimediaElement.getClass() == ExtraRespuesta.class){
            ruta = ((ExtraRespuesta)databaseMultimediaElement).getRuta();
            tipo = ((ExtraRespuesta)databaseMultimediaElement).getTipo();
            ancho = ((ExtraRespuesta)databaseMultimediaElement).getAncho();
            alto = ((ExtraRespuesta)databaseMultimediaElement).getAlto();
        } else {
            return null;
        }

        // Initialize and fill the multimedia element model
        MultimediaElementModel multimediaElementModel = new MultimediaElementModel();
        multimediaElementModel.setType(tipo);
        multimediaElementModel.setWidht(ancho);
        multimediaElementModel.setHeight(alto);
        multimediaElementModel.setGeogebraType(geogebraType);
        if(tipo == MultimediaTypeConstant.YOUTUBE){
            multimediaElementModel.setPath(MultimediaPathConstant.YOUTUBE + ruta);
        }else{
            multimediaElementModel.setPath(MultimediaPathConstant.MEDIA_FILE + ruta);
            multimediaElementModel.setExtension(ruta.substring(ruta.lastIndexOf('.') + 1).toLowerCase());
        }

        // Return the multimedia element model
        return multimediaElementModel;
    }

    /**
     * Convert the exam from database to done exam model object
     * @param exam The exam from database
     * @return The done exam model
     */
    private DoneExamInfoModel convertExamenToDoneExamInfoModel(Examen exam){
        // Initialize and fill the model object
        DoneExamInfoModel doneExam = new DoneExamInfoModel();
        doneExam.setExamId(exam.getIdexam());
        doneExam.setExamName(exam.getTitulo());
        doneExam.setMaxScore(this.formatterComponent.formatNumberWithTwoDecimals(exam.getNotaMax()));

        // Get the "Calificacion" object corresponding to the user (It should not be null and only one)
        Calificacion calificacion = exam.getCalificaciones().stream().findFirst().get();
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

    /**
     * Convert the list of exams from database to done exam model object list
     * @param examenList The exams from database
     * @return The list of done exam model objects
     */
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

    /**
     * Convert the list of exams from database to exam extra info model list
     * @param examenList The exams from database
     * @return The list of exam extra info model objects
     */
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
                examExtraInfo.setQuestionsNumber(exam.getTemasExamenes().stream().mapToInt(TemaExamen::getNPregs).sum());
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
