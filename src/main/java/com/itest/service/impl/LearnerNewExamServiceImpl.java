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

import com.itest.constant.QuestionVisibilityConstant;
import com.itest.entity.*;
import com.itest.model.ExamQuestionModel;
import com.itest.model.NewExamModel;
import com.itest.model.NewExamQuestionAnswerModel;
import com.itest.model.NewExamQuestionModel;
import com.itest.repository.*;
import com.itest.service.LearnerNewExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service("learnerNewExamServiceImpl")
public class LearnerNewExamServiceImpl implements LearnerNewExamService {

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

    /**
     * Check if the exam is already done by the learner
     * @param learnerId The learner identifier
     * @param examId The exam identifier
     * @return Whether the exam is already done or not
     */
    public boolean isExamAlreadyDonde(int learnerId, int examId){

        // Find the done exam by user
        Examen exam = this.examenRepository.findDoneExamByUserIdAndExamId(learnerId, examId);

        // Check if there is a done exam by user in database
        boolean isExamAlreadyDonde = exam != null && exam.getFechaFin() != null;

        // Return the boolean indicating whether the exam is already done or not
        return isExamAlreadyDonde;
    }

    /**
     * Generate a new exam to perform by the learner
     * This method is executed insida a transaction. If the method launch an exception the transaction will be rollback
     * @param learnerId The learner identifier
     * @param examId The exam identifier
     * @return The generated exam for user
     */
    @Transactional(rollbackFor = Exception.class)
    public NewExamModel generateNewExamForLearner(int learnerId, int examId){

        // Initialize the new exam model object
        NewExamModel newExamModel = new NewExamModel();

        // Get the exam
        Examen exam = this.examenRepository.findOne(examId);

        // Insert a new empty score for the exam
        this.insertNewEmptyScoreForExam(learnerId, exam);

        // Get the list of new exam question model
        List<NewExamQuestionModel> examQuestionModelList = this.getQuestionsForNewExam(exam,learnerId);

        // Set the question list to the exam model
        newExamModel.setQuestionList(examQuestionModelList);

        // Update the field of database from question and answers indicating the question and answer has been used in an exam
        examQuestionModelList.forEach(question -> {

            // Update field of question
            Pregunta pregunta = this.preguntaRepository.findOne(question.getQuestionId());
            pregunta.setUsedInExam((byte) 1);
            this.preguntaRepository.save(pregunta);

            // Update field of all answers of question
            question.getAnswerList().forEach(answer -> {

                // Update field of answer
                Respuesta respuesta = this.respuestaRepository.findOne(answer.getAsnwerId());
                respuesta.setUsedInExamQuestion((byte) 1);
                this.respuestaRepository.save(respuesta);

                // TODO: Insert a new log exam register in database
            });

        });

        // Return the new exam model
        return newExamModel;
    }

    /**
     * Get the questions for the new exam
     * @param exam The new exam database object
     * @param learnerId The learner identifier
     * @return The question list for the new exam
     */
    private List<NewExamQuestionModel> getQuestionsForNewExam(Examen exam, int learnerId){

        // Get the exam details
        int examGroupId = exam.getGrupos().getIdgrupo();
        int examInstitutionId = exam.getGrupos().getCentros().getIdcentro();
        int examVisibility = exam.getVisibilidad();

        // Insert a new empty score for the exam
        this.insertNewEmptyScoreForExam(learnerId, exam);

        // Initialize the list of new exam question model
        List<NewExamQuestionModel> examQuestionModelList = new ArrayList<>();

        // Through all available themes of the exam to select random questions
        for (TemaExamen examTheme : exam.getTemasExam()) {

            // Get the configuration parameters of the theme
            int minDifficulty = examTheme.getDificultadMin();
            int maxDifficulty = examTheme.getDificultadMax();
            int numberQuestions = examTheme.getNPregs();
            int numberAnswersPerQuestion = examTheme.getNRespXPreg();

            // Get the question list of the theme
            List<Pregunta> questionsList = examTheme.getTemas().getPreguntas();

            // Add random questions of the exam until the maximum number of questions
            Random randomizer = new Random(learnerId * exam.getIdexam());
            int addedQuestionsCounter = 0;
            while(addedQuestionsCounter < numberQuestions){

                // Get random question
                Pregunta question = questionsList.get(randomizer.nextInt(questionsList.size()));

                // Check the visibility of the question
                boolean isVisible = false;
                if(examVisibility == QuestionVisibilityConstant.GROUP){
                    isVisible = question.getGrupos().getIdgrupo() == examGroupId;
                }else if (examVisibility == QuestionVisibilityConstant.INSTITUTION){
                    isVisible = question.getGrupos().getCentros().getIdcentro() == examInstitutionId;
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
                        NewExamQuestionAnswerModel answerModel = this.convertRespuestaToNewExamQuestionAnswerModel(respuesta);
                        questionModel.addAnswerToAnswerList(answerModel);
                    });

                    // Add random answers from remaining answers to the questions until maximum number of answer per question
                    int addedAnswersToQuestionCounter = rightAnswers.size();
                    while(addedAnswersToQuestionCounter < numberAnswersPerQuestion && remainingAsnwers.size() > 0){

                        // Get a random answer
                        Respuesta answer = remainingAsnwers.get(randomizer.nextInt(remainingAsnwers.size()));

                        // Convert the answer to model and add to the list
                        NewExamQuestionAnswerModel answerModel = this.convertRespuestaToNewExamQuestionAnswerModel(answer);
                        questionModel.addAnswerToAnswerList(answerModel);

                        // Increment the added answers counter
                        addedAnswersToQuestionCounter++;

                        // Remove the answer of the remaining asnwers list
                        remainingAsnwers.remove(answer);
                    }

                    // Add question to the added question list for the exam
                    examQuestionModelList.add(questionModel);
                }

                // Remove the question from the list
                questionsList.remove(question);
            }
        }

        // Return the question list of the new exam
        return examQuestionModelList;
    }

    /**
     * Insert a new score for the exam with the minimum details
     * @param userId The user identifier
     * @param exam The exam entity
     */
    private void insertNewEmptyScoreForExam(int userId, Examen exam){

        // Create the calificacion entity
        Calificacion calificacion = new Calificacion();
        calificacion.setFechaIni(new Date(System.currentTimeMillis()));
        calificacion.setFechaFin(null);
        calificacion.setNota(null);
        calificacion.setTiempo(0);
        calificacion.setIp("IP"); //TODO
        calificacion.setUsuarios(this.usuarioRepository.findOne(userId));
        calificacion.setExamenes(exam);

        // Insert in database
        calificacionRepository.save(calificacion);
    }

    private NewExamQuestionModel convertPreguntaToNewExamQuestionModel(Pregunta pregunta){

        // Initialize and fill the model object
        NewExamQuestionModel newExamQuestionModel = new NewExamQuestionModel();
        newExamQuestionModel.setQuestionId(pregunta.getIdpreg());
        newExamQuestionModel.setStatement(pregunta.getEnunciado());
        newExamQuestionModel.setComment(pregunta.getComentario());
        newExamQuestionModel.setNumberCorrectAnswers(pregunta.getNRespCorrectas());

        // Return the model object
        return newExamQuestionModel;
    }

    private NewExamQuestionAnswerModel convertRespuestaToNewExamQuestionAnswerModel(Respuesta respuesta){

        // Initialize and fill the model object
        NewExamQuestionAnswerModel newExamQuestionAnswerModel = new NewExamQuestionAnswerModel();
        newExamQuestionAnswerModel.setAsnwerId(respuesta.getIdresp());
        newExamQuestionAnswerModel.setText(respuesta.getTexto());
        newExamQuestionAnswerModel.setChecked(false);

        // Return the model object
        return newExamQuestionAnswerModel;
    }

}
