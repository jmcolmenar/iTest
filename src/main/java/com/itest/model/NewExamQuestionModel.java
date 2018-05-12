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
package com.itest.model;

import java.util.List;

public class NewExamQuestionModel {

    private int questionId;

    private String statement;

    private int numberCorrectAnswers;

    private boolean activeConfidenceLevel;

    private List<NewExamAnswerModel> answerList;

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }

    public int getNumberCorrectAnswers() {
        return numberCorrectAnswers;
    }

    public void setNumberCorrectAnswers(int numberCorrectAnswers) {
        this.numberCorrectAnswers = numberCorrectAnswers;
    }

    public boolean isActiveConfidenceLevel() {
        return activeConfidenceLevel;
    }

    public void setActiveConfidenceLevel(boolean activeConfidenceLevel) {
        this.activeConfidenceLevel = activeConfidenceLevel;
    }

    public List<NewExamAnswerModel> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(List<NewExamAnswerModel> answerList) {
        this.answerList = answerList;
    }

    public void addAnswerToAnswerList(NewExamAnswerModel answer){
        this.answerList.add(answer);
    }
}
