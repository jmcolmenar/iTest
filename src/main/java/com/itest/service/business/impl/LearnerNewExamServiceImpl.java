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
import com.itest.constant.QuestionVisibilityConstant;
import com.itest.entity.*;
import com.itest.model.*;
import com.itest.repository.*;
import com.itest.service.business.LearnerExamService;
import com.itest.service.business.LearnerMultimediaService;
import com.itest.service.business.LearnerNewExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.itest.constant.QuestionTypeConstant.SHORT_ANSWER;
import static com.itest.constant.QuestionTypeConstant.TEST;

@Service("learnerNewExamServiceImpl")
public class LearnerNewExamServiceImpl implements LearnerNewExamService {

    @Autowired
    @Qualifier("learnerExamServiceImpl")
    private LearnerExamService learnerExamService;

    @Autowired
    @Qualifier("learnerMultimediaServiceImpl")
    private LearnerMultimediaService learnerMultimediaService;

    @Autowired
    @Qualifier("examenRepository")
    private ExamenRepository examenRepository;

    @Autowired
    @Qualifier("calificacionRepository")
    private CalificacionRepository calificacionRepository;

    @Autowired
    @Qualifier("usuarioRepository")
    private UsuarioRepository usuarioRepository;

    @Autowired
    @Qualifier("preguntaRepository")
    private PreguntaRepository preguntaRepository;

    @Autowired
    @Qualifier("respuestaRepository")
    private RespuestaRepository respuestaRepository;

    @Autowired
    @Qualifier("logExamenRepository")
    private LogExamenRepository logExamenRepository;

    @Autowired
    @Qualifier("logExamenFillRepository")
    private LogExamenFillRepository logExamenFillRepository;

    @Autowired
    @Qualifier("formatterComponent")
    private FormatterComponent formatterComponent;

    /**
     * Check if the exam is already started by the learner
     * @param learnerId The learner identifier
     * @param examId The exam identifier
     * @return Whether the exam is started or not
     */
    public boolean isExamAlreadyStarted(int learnerId, int examId){
        // Find the qualifiaction of exam by user (When a new exam is generated a empty qualification is created in database)
        Calificacion qualification = this.calificacionRepository.findByUserIdAndExamId(learnerId, examId);

        // Check if there is a qualification of the exam by user and the end date has not been set
        boolean isExamAlreadyStarted = qualification != null && qualification.getFechaFin().getTime() == new Date(0).getTime();

        // Return the boolean indicating whether the exam is already started or not
        return isExamAlreadyStarted;
    }

    /**
     * Check if the exam is already done by the learner
     * @param learnerId The learner identifier
     * @param examId The exam identifier
     * @return Whether the exam is already done or not
     */
    public boolean isExamAlreadyDonde(int learnerId, int examId){

        // Find the qualifiaction of exam by user (When a new exam is generated a empty qualification is created in database)
        Calificacion qualification = this.calificacionRepository.findByUserIdAndExamId(learnerId, examId);

        // Check if there is a qualification of the exam by user and the end date has been set
        boolean isExamAlreadyDonde = qualification != null && qualification.getFechaFin().getTime() != new Date(0).getTime();

        // Return the boolean indicating whether the exam is already done or not
        return isExamAlreadyDonde;
    }

    /**
     * Check if the exam to start is out of time
     * @param examId The exam identifier
     * @return Whether the exam to start is out of time or not
     */
    public boolean isExamToStartOutOfTime(int examId){

        // Find the exam
        Examen exam = this.examenRepository.findOne(examId);

        // Check if the exam end date is before than now
        boolean isExamOutOfTime = exam != null && exam.getFechaFin() != null && exam.getFechaFin().getTime() <= System.currentTimeMillis();

        // Return the boolean indicating whether the exam is out of time or not
        return isExamOutOfTime;
    }

    /**
     * Check if the exam to end is out of time
     * @param examId The exam identifier
     * @param learnerId The learner identifier
     * @param examEndDate The exam end date
     * @return Whether the exam to end is out of time or not
     */
    public boolean isExamToEndOutOfTime(int examId, int learnerId, Date examEndDate){

        // Find the qualification of exam and exam from database
        Calificacion calif = this.calificacionRepository.findByUserIdAndExamId(learnerId, examId);
        Examen exam = this.examenRepository.findOne(examId);

        // Get the date when the learner starts the exam
        Date examStartDate = calif.getFechaIni();
        long examDuration = exam.getDuracion() * 60 * 1000; // In milliseconds
        long extraTime = 10 * 1000; // 10 seconds of extra time

        // Check if the exam has ended out of time
        boolean isEndedOutOfTime = false;
        if(examEndDate.getTime() > (examStartDate.getTime() + examDuration + extraTime)){
            isEndedOutOfTime = true;
        }

        // Return if the exam is ended out of time
        return isEndedOutOfTime;
    }

    /**
     * Generate a new exam to perform by the learner
     * This method is executed insida a transaction. If the method launch an exception the transaction will be rollback
     * @param learnerId The learner identifier
     * @param examId The exam identifier
     * @param ip The ip of the learner
     * @return The generated exam for user
     */
    @Transactional(rollbackFor = Exception.class)
    public NewExamModel generateNewExamForLearner(int learnerId, int examId, String ip){

        // Initialize the new exam model object
        NewExamModel newExamModel = new NewExamModel();

        // Get the exam and learner from database
        Examen exam = this.examenRepository.findOne(examId);
        Usuario learner = this.usuarioRepository.findOne(learnerId);

        // Insert a new empty score for the exam
        Date startDate = new Date(System.currentTimeMillis());
        this.insertEmptyScoreForNewExam(learner, exam, startDate, ip);

        // Set the exam details
        newExamModel.setExamTitle(exam.getTitulo());
        newExamModel.setExamId(examId);
        newExamModel.setSubjectName(this.learnerExamService.getSubjectNameFromExam(examId));
        newExamModel.setShowNumberRightAnswers(exam.getMuestraNumCorr() == 1);
        newExamModel.setExamTime(exam.getDuracion());
        newExamModel.setActiveConfidenceLevel(exam.getNivelConfianza() == 1);

        // Check if the exam time must be lower than configured exam duration (Because the exam end time is before end of exam duration)
        double remainingTimeToEndInSeconds = (exam.getFechaFin().getTime() - System.currentTimeMillis()) / 1000.0;
        if(remainingTimeToEndInSeconds < exam.getDuracion() * 60.0){
            // Set the exam time to the remaning time to finish (Round to up the remaining time in minutes)
            newExamModel.setExamTime((int)Math.ceil(remainingTimeToEndInSeconds / 60.0));
        }

        // Get the list of new exam question model
        List<NewExamQuestionModel> examQuestionModelList = this.getQuestionsForNewExam(exam, startDate);

        // Set the question list to the exam model
        newExamModel.setQuestionList(examQuestionModelList);

        // Update the questions and anser in as "used in an exam" in database and for each answer create a new log exam in database (With empty score)
        this.updateQuestionsAndAnswersAsUsedAndAddLogForEachAnswer(examQuestionModelList, exam, learner, startDate);

        // Return the new exam model
        return newExamModel;
    }

    /**
     * Update the answered question of the exam in database
     * @param examId The exam identifier
     * @param learnerId The learner identifier
     * @param questionList The answered questions of the exam
     */
    public void updateAnsweredQuestionsInDatabase(int examId, int learnerId, List<NewExamQuestionModel> questionList){

        // Update the answered questions in database
        for(NewExamQuestionModel question : questionList){
            for(NewExamAnswerModel answer : question.getAnswerList()){

                // The fields of log exam entity to update(The fields can be updated or not - Optional)
                boolean updateAnswerInDatabase = false;
                Boolean checked = null;
                Boolean activeConfidenceLevel = null;
                Date answerTime = null;
                String shortAnswerText = null;

                // Check if confidence level is active in the question
                if(question.isActiveConfidenceLevel()){
                    // The log exam entity for this answer will be updated in database
                    updateAnswerInDatabase = true;

                    // Set the confidence level field
                    activeConfidenceLevel = true;
                }

                // Check if the learner has answered this questions (Depending on question type)
                if(question.getType() == TEST){

                    // Check if the answer is checked
                    if(answer.isChecked()){
                        // The log exam entity for this answer will be updated in database
                        updateAnswerInDatabase = true;

                        // Set the checked and answer time fields
                        checked = true;
                        answerTime = answer.getAnswerTime();
                    }

                }else if(question.getType() == SHORT_ANSWER){

                    // The short answers always are updated in database
                    updateAnswerInDatabase = true;

                    // Check if the user has checked the short answer
                    if(answer.isChecked()){
                        shortAnswerText = answer.getText();
                    }else{
                        shortAnswerText = null;
                    }
                }

                // Check if the answer have to update in database
                if(updateAnswerInDatabase){

                    // Check the type of question
                    if(question.getType() == TEST){

                        // Update the log exam entity in database
                        this.insertOrUpdateLogExam(learnerId, examId, question.getQuestionId(), answer.getAsnwerId(), checked, activeConfidenceLevel, answerTime);

                    }else if(question.getType() == SHORT_ANSWER){

                        // Update the log exam fill entity in database
                        this.insertOrUpdateLogExamFill(learnerId, examId, question.getQuestionId(), shortAnswerText, activeConfidenceLevel, answerTime);
                    }
                }
            }
        }
    }

    /**
     * Calculates the exam score and update it in database
     * @param examId The exam identifer
     * @param learnerId The learner identifier
     * @param questionList The question list of the exam
     * @param examEndDate The exam end date
     */
    public void calculateExamScore(int examId, int learnerId, List<NewExamQuestionModel> questionList, Date examEndDate){

        // Variable with the exam score
        double examScore  = 0;

        // Calculate the score for each question
        for(NewExamQuestionModel question : questionList){

            // Check the type of question
            double questionScore = 0.0;
            if(question.getType() == TEST){

                // Calculate the question score of test type
                questionScore = this.learnerExamService.calculateTestQuestionScore(learnerId, examId, question.getQuestionId(), questionList.size(), true);

            }else if(question.getType() == SHORT_ANSWER){

                // Calculate the question score of short answer type
                questionScore = this.learnerExamService.calculateShortAnswerQuestionScore(learnerId, examId, question.getQuestionId(), questionList.size(), true);
            }

            // Add the question score to exam score
            examScore += questionScore;
        }

        // Update the score in database
        Calificacion calif = this.calificacionRepository.findByUserIdAndExamId(learnerId, examId);
        long examDurationInMilliseconds = examEndDate.getTime() - calif.getFechaIni().getTime();
        calif.setTiempo((int)Math.ceil(examDurationInMilliseconds / (60.0 * 1000.0)));
        calif.setFechaFin(examEndDate);
        calif.setNota(new BigDecimal(examScore));
        this.calificacionRepository.save(calif);
    }

    /**
     * Get the exam score information
     * @param examId The exam identifier
     * @param learnerId The learner identifier
     * @return The model object holding the exam score information
     */
    public ExamScoreInfoModel getExamScoreInfo(int examId, int learnerId){

        // Get the exam and qualification from database
        Examen exam = this.examenRepository.findOne(examId);
        Calificacion qualification = this.calificacionRepository.findByUserIdAndExamId(learnerId, examId);

        // Initialize the model object and fill it
        ExamScoreInfoModel examScoreInfoModel = new ExamScoreInfoModel();
        examScoreInfoModel.setExamId(examId);
        examScoreInfoModel.setExamTitle(exam.getTitulo());
        examScoreInfoModel.setSubjectName(this.learnerExamService.getSubjectNameFromExam(examId));
        examScoreInfoModel.setScore(this.formatterComponent.formatNumberWithTwoDecimals(qualification.getNota()));
        examScoreInfoModel.setMaxScore(this.formatterComponent.formatNumberWithTwoDecimals(exam.getNotaMax()));
        if(exam.getRevActiva() == 1){
            examScoreInfoModel.setAvailableReview(true);
            examScoreInfoModel.setReviewStartDate(this.formatterComponent.formatDateToString(exam.getFechaIniRev()));
            examScoreInfoModel.setReviewEndDate(this.formatterComponent.formatDateToString(exam.getFechaFinRev()));
        }else{
            examScoreInfoModel.setAvailableReview(false);
        }

        // Return the model object holding the exam score info
        return examScoreInfoModel;
    }

    /**
     * Get the questions for the new exam
     * @param exam The new exam database object
     * @param examStartDate The start date of new exam
     * @return The question list for the new exam
     */
    private List<NewExamQuestionModel> getQuestionsForNewExam(Examen exam, Date examStartDate){

        // Get the exam details
        int examGroupId = exam.getGrupo().getIdgrupo();
        int examInstitutionId = exam.getGrupo().getCentro().getIdcentro();
        int examVisibility = exam.getVisibilidad();

        // Initialize the list of new exam question model
        List<NewExamQuestionModel> examQuestionModelList = new ArrayList<>();

        // Through all available themes of the exam to select random questions
        for (TemaExamen examTheme : exam.getTemasExamenes()) {

            // Get the configuration parameters of the theme
            int minDifficulty = examTheme.getDificultadMin();
            int maxDifficulty = examTheme.getDificultadMax();
            int numberQuestions = examTheme.getNPregs();
            int numberAnswersPerQuestion = examTheme.getNRespXPreg();

            // Get the question list of the theme
            List<Pregunta> questionsList = examTheme.getTema().getPreguntas();

            // Add random questions of the exam until the maximum number of questions
            Random randomizer = new Random();
            int addedQuestionsCounter = 0;
            while(addedQuestionsCounter < numberQuestions){

                // Get random question
                Pregunta question = questionsList.get(randomizer.nextInt(questionsList.size()));

                // Check the visibility of the question
                boolean isVisible = false;
                if(examVisibility == QuestionVisibilityConstant.GROUP){
                    isVisible = question.getGrupo().getIdgrupo() == examGroupId;
                }else if (examVisibility == QuestionVisibilityConstant.INSTITUTION){
                    isVisible = question.getGrupo().getCentro().getIdcentro() == examInstitutionId;
                }

                // Check if the question is allowed in order to add to exam
                boolean isActive = question.getActiva() == 1;
                boolean isAllowedDifficulty = question.getDificultad() >= minDifficulty && question.getDificultad() <= maxDifficulty;
                if(isVisible && isActive && isAllowedDifficulty){

                    // Convert the question database object to question model object and add to the list
                    NewExamQuestionModel questionModel = this.convertPreguntaToNewExamQuestionModel(question);
                    examQuestionModelList.add(questionModel);

                    // Increment the added questions counter
                    addedQuestionsCounter++;

                    // Initilize the list of added answer to the question
                    questionModel.setAnswerList(new ArrayList<>());

                    // Get the right and remaining answers for the question
                    List<Respuesta> rightAnswers = question.getRespuestas().stream().filter(respuesta -> respuesta.getActiva() == 1 && respuesta.getSolucion() == 1).collect(Collectors.toList());
                    List<Respuesta> remainingAsnwers = question.getRespuestas().stream().filter(respuesta -> respuesta.getActiva() == 1 && respuesta.getSolucion() != 1).collect(Collectors.toList());

                    // Add all right question to the added answer for question list
                    rightAnswers.forEach(respuesta -> {
                        // Convert the answer to model and add to the list
                        NewExamAnswerModel answerModel = this.convertRespuestaToNewExamQuestionAnswerModel(respuesta, examStartDate, questionModel.getType());
                        questionModel.addAnswerToAnswerList(answerModel);
                    });

                    // Add random answers from remaining answers to the questions until maximum number of answer per question
                    int addedAnswersToQuestionCounter = rightAnswers.size();
                    while(addedAnswersToQuestionCounter < numberAnswersPerQuestion && remainingAsnwers.size() > 0){

                        // Get a random answer
                        Respuesta answer = remainingAsnwers.get(randomizer.nextInt(remainingAsnwers.size()));

                        // Convert the answer to model and add to the list
                        NewExamAnswerModel answerModel = this.convertRespuestaToNewExamQuestionAnswerModel(answer, examStartDate, questionModel.getType());
                        questionModel.addAnswerToAnswerList(answerModel);

                        // Increment the added answers counter
                        addedAnswersToQuestionCounter++;

                        // Remove the answer of the remaining asnwers list
                        remainingAsnwers.remove(answer);
                    }

                    // Shuffle the answers
                    Collections.shuffle(questionModel.getAnswerList());
                }

                // Remove the question from the list
                questionsList.remove(question);
            }
        }

        // shuffle the questions
        Collections.shuffle(examQuestionModelList);

        // Return the question list of the new exam
        return examQuestionModelList;
    }

    /**
     * Insert an empty score for the new exam
     * @param learner The learner database object
     * @param exam The exam entity
     */
    private void insertEmptyScoreForNewExam(Usuario learner, Examen exam, Date startDate, String ip){

        // Create the calificacion entity
        Calificacion calificacion = new Calificacion();
        calificacion.setFechaIni(startDate);
        calificacion.setFechaFin(new Date(0));
        calificacion.setNota(new BigDecimal(0));
        calificacion.setTiempo(0);
        calificacion.setIp(ip);
        calificacion.setUsuario(learner);
        calificacion.setExamen(exam);

        // Insert in database
        calificacionRepository.save(calificacion);
    }

    /**
     * Update all questions and answers as "used in an exam" in database
     * Furthermore, for each answer is created a new log exam in database (without checked the answer)
     * @param examQuestionModelList The question model list
     * @param exam The exam database object
     * @param learner The learner database object
     * @param startDateExam The start date of the exam
     */
    private void updateQuestionsAndAnswersAsUsedAndAddLogForEachAnswer(List<NewExamQuestionModel> examQuestionModelList, Examen exam, Usuario learner, Date startDateExam){

        // Update all questions and answers
        for(NewExamQuestionModel questionModel : examQuestionModelList){

            // Update field of question
            Pregunta question = this.preguntaRepository.findOne(questionModel.getQuestionId());
            question.setUsedInExam(1);
            this.preguntaRepository.save(question);

            for(NewExamAnswerModel answerModel : questionModel.getAnswerList()){

                // Update field of answer
                Respuesta answer = this.respuestaRepository.findOne(answerModel.getAsnwerId());
                answer.setUsedInExamQuestion(1);
                this.respuestaRepository.save(answer);

                // For each answer create a log exam entity in database (Without checked the answer)
                if(question.getTipo() == TEST){

                    // Create a new answer for question of test type
                    LogExamen logExam = new LogExamen();
                    logExam.setExamen(exam);
                    logExam.setUsuario(learner);
                    logExam.setPregunta(question);
                    logExam.setRespuesta(answer);
                    logExam.setMarcada(0);
                    logExam.setPuntos(new BigDecimal(0));
                    logExam.setHoraResp(startDateExam);
                    this.logExamenRepository.save(logExam);

                }else if(question.getTipo() == SHORT_ANSWER){

                    // Create a new answer for question of short answer type
                    LogExamenFill logExamenFill = new LogExamenFill();
                    logExamenFill.setExamen(exam);
                    logExamenFill.setUsuario(learner);
                    logExamenFill.setPregunta(question);
                    logExamenFill.setResp(null);
                    logExamenFill.setPuntos(new BigDecimal(0));
                    logExamenFill.setHoraResp(startDateExam);
                    this.logExamenFillRepository.save(logExamenFill);
                }
            }
        }
    }

    /**
     * Convert a question database object to question model object
     * @param question The question database object
     * @return The question model object
     */
    private NewExamQuestionModel convertPreguntaToNewExamQuestionModel(Pregunta question){

        // Initialize and fill the model object
        NewExamQuestionModel newExamQuestionModel = new NewExamQuestionModel();
        newExamQuestionModel.setQuestionId(question.getIdpreg());
        newExamQuestionModel.setStatement(question.getEnunciado());
        newExamQuestionModel.setNumberCorrectAnswers(question.getNRespCorrectas());
        newExamQuestionModel.setActiveConfidenceLevel(false);
        newExamQuestionModel.setType(question.getTipo());
        newExamQuestionModel.setQuestionMultimediaList(this.learnerMultimediaService.getMultimediaModelListFromDatabaseObjectList(question.getExtraPreguntas()));

        // Return the model object
        return newExamQuestionModel;
    }

    /**
     * Convert a answer database object to question model object
     * @param answer The answer database object
     * @param examStartDate The start date of new exam.
     * @param questionType The type of the question: Test or Short Answer.
     * @return The answer model object
     */
    private NewExamAnswerModel convertRespuestaToNewExamQuestionAnswerModel(Respuesta answer, Date examStartDate, int questionType){

        // Initialize the model object
        NewExamAnswerModel newExamAnswerModel = new NewExamAnswerModel();

        // Check the type of question
        if(questionType == TEST){

            // Fill the answer model of test question
            newExamAnswerModel.setAsnwerId(answer.getIdresp());
            newExamAnswerModel.setText(answer.getTexto());
            newExamAnswerModel.setChecked(false);
            newExamAnswerModel.setAnswerTime(examStartDate);
            newExamAnswerModel.setMultimediaList(this.learnerMultimediaService.getMultimediaModelListFromDatabaseObjectList(answer.getExtraRespuestas()));

        }else if(questionType == SHORT_ANSWER){

            // Fill the answer model of short anserw question
            newExamAnswerModel.setAsnwerId(answer.getIdresp());
            newExamAnswerModel.setText("");
            newExamAnswerModel.setChecked(false);
            newExamAnswerModel.setAnswerTime(examStartDate);
        }

        // Return the model object
        return newExamAnswerModel;
    }

    /**
     * Insert or update the log exam entity in database
     * @param learnerId The learner identifier
     * @param examId The exam identifier
     * @param questionId The question identifier
     * @param answerId The answer identifier
     * @param checked If the answer is checked (Optional value to insert or update)
     * @param confidenceLevel If the question has checked as confidence level (Optional value to insert or update)
     * @param answerTime The answer time (Optional value to insert or update)
     */
    private void insertOrUpdateLogExam(int learnerId, int examId, int questionId, int answerId, Boolean checked, Boolean confidenceLevel, Date answerTime){
        // Try to get the Log Exam
        LogExamen logExam = this.logExamenRepository.findByExamIdAndUserIdAndQuestionIdAndAnswerId(examId, learnerId, questionId, answerId);

        // Check if the log exam is in database
        if(logExam != null){
            // Set the fields
            if(checked != null){
                logExam.setMarcada(checked ? 1 : 0);
            }
            if(confidenceLevel != null){
                logExam.setNivelConfianza(confidenceLevel ? 1 : 0);
            }
            if(answerTime != null){
                logExam.setHoraResp(answerTime);
            }
        }else{
            // Crate a new log exam
            logExam = new LogExamen();
            logExam.setUsuario(this.usuarioRepository.findOne(learnerId));
            logExam.setExamen(this.examenRepository.findOne(examId));
            logExam.setPregunta(this.preguntaRepository.findOne(questionId));
            logExam.setRespuesta(this.respuestaRepository.findOne(answerId));
            logExam.setPuntos(new BigDecimal(0));
            if(checked != null){
                logExam.setMarcada(checked ? 1 : 0);
            }else{
                logExam.setMarcada(0);
            }
            if(confidenceLevel != null){
                logExam.setNivelConfianza(confidenceLevel ? 1 : 0);
            }else{
                logExam.setNivelConfianza(0);
            }
            if(answerTime != null){
                logExam.setHoraResp(answerTime);
            }else{
                logExam.setHoraResp(new Date(System.currentTimeMillis()));
            }
        }

        // Insert or update the log exam in database
        this.logExamenRepository.save(logExam);
    }

    /**
     * Insert or update the log exam fill entity in database
     * @param learnerId The learner identifier
     * @param examId The exam identifier
     * @param questionId The question identifier
     * @param answerText The answer text
     * @param confidenceLevel If the question has checked as confidence level (Optional value to insert or update)
     * @param answerTime The answer time (Optional value to insert or update)
     */
    private void insertOrUpdateLogExamFill(int learnerId, int examId, int questionId, String answerText, Boolean confidenceLevel, Date answerTime){
        // Try to get the Log Exam
        LogExamenFill logExamFill = this.logExamenFillRepository.findByExamIdAndUserIdAndQuestionId(examId, learnerId, questionId);

        // Check if the log exam is in database
        if(logExamFill != null){
            // Set the fields
            if(confidenceLevel != null){
                logExamFill.setNivelConfianza(confidenceLevel ? 1 : 0);
            }
            if(answerTime != null){
                logExamFill.setHoraResp(answerTime);
            }
            logExamFill.setResp(answerText);
        }else{
            // Crate a new log exam
            logExamFill = new LogExamenFill();
            logExamFill.setUsuario(this.usuarioRepository.findOne(learnerId));
            logExamFill.setExamen(this.examenRepository.findOne(examId));
            logExamFill.setPregunta(this.preguntaRepository.findOne(questionId));
            logExamFill.setResp(answerText);
            logExamFill.setPuntos(new BigDecimal(0));
            if(confidenceLevel != null){
                logExamFill.setNivelConfianza(confidenceLevel ? 1 : 0);
            }else{
                logExamFill.setNivelConfianza(0);
            }
            if(answerTime != null){
                logExamFill.setHoraResp(answerTime);
            }else{
                logExamFill.setHoraResp(new Date(System.currentTimeMillis()));
            }
        }

        // Insert or update the log exam fill in database
        this.logExamenFillRepository.save(logExamFill);
    }
}
