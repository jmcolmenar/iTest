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

public class NewExamModel {

    private int examId;

    private String subjectName;

    private String examTitle;

    private int examTime;

    private boolean showNumberRightAnswers;

    private boolean activeConfidenceLevel;

    private List<NewExamQuestionModel> questionList;

    public int getExamId() {
        return examId;
    }

    public void setExamId(int examId) {
        this.examId = examId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getExamTitle() {
        return examTitle;
    }

    public void setExamTitle(String examTitle) {
        this.examTitle = examTitle;
    }

    public int getExamTime() {
        return examTime;
    }

    public void setExamTime(int examTime) {
        this.examTime = examTime;
    }

    public boolean isShowNumberRightAnswers() {
        return showNumberRightAnswers;
    }

    public void setShowNumberRightAnswers(boolean showNumberRightAnswers) {
        this.showNumberRightAnswers = showNumberRightAnswers;
    }

    public boolean isActiveConfidenceLevel() {
        return activeConfidenceLevel;
    }

    public void setActiveConfidenceLevel(boolean activeConfidenceLevel) {
        this.activeConfidenceLevel = activeConfidenceLevel;
    }

    public List<NewExamQuestionModel> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<NewExamQuestionModel> questionList) {
        this.questionList = questionList;
    }
}
