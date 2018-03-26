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

import com.itest.entity.LogExamen;
import com.itest.entity.Pregunta;
import com.itest.entity.Respuesta;
import com.itest.model.ExamQuestionAnswerModel;
import com.itest.model.ExamQuestionModel;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("logExamenConverter")
public class LogExamenConverter {

    public List<ExamQuestionModel> convertLogExamListToExamQuestionModelList(List<LogExamen> logExamenList){

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

                // Get the answer database object and fill the asnwer model
                Respuesta respuesta = logExamen.getRespuestas();
                ExamQuestionAnswerModel examQuestionAnswerModel = new ExamQuestionAnswerModel();
                examQuestionAnswerModel.setAsnwerId(respuesta.getIdresp());
                examQuestionAnswerModel.setText(respuesta.getTexto());
                examQuestionAnswerModel.setRight(respuesta.getSolucion() == 1);
                examQuestionAnswerModel.setValue(respuesta.getValor());
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

}
