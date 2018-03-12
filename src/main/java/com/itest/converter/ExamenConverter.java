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
package com.itest.converter;

import com.itest.component.FormatterComponent;
import com.itest.entity.Calificacion;
import com.itest.entity.Examen;
import com.itest.entity.TemaExamen;
import com.itest.model.DoneExamInfoModel;
import com.itest.model.ExamConfidenceLevelInfoModel;
import com.itest.model.ExamExtraInfoModel;
import com.itest.model.ExamPartialCorrectionInfoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component("examenConverter")
public class ExamenConverter {

    @Autowired
    @Qualifier("formatterComponent")
    private FormatterComponent formatterComponent;

    public List<DoneExamInfoModel> convertExamenListToDoneExamInfoModelList(List<Examen> examenList){

        // Initialize the Donde Exam Info Model list
        List<DoneExamInfoModel> doneExamInfoModelList = new ArrayList<>();

        // Check the Examen list is not empty
        if(examenList != null && !examenList.isEmpty()){

            for(Examen exam : examenList){
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

                // Add DoneExamModel object to the list
                doneExamInfoModelList.add(doneExam);
            }
        }

        // Return the Donde Exam Info Model list
        return doneExamInfoModelList;
    }

    // TODO: Review this conversion
    public List<ExamExtraInfoModel> convertExamListToExamExtraInfoModelList(List<Examen> examenList){

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
