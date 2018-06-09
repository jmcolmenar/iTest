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

public class ReviewExamQuestionModel {

    private int questionId;

    private String statement;

    private String comment;

    private String score;

    private int numberCorrectAnswers;

    private boolean activeConfidenceLevel;

    private int type;

    private List<ReviewExamAnswerModel> answerList;

    private List<MultimediaElementModel> multimediaList;

    private List<MultimediaElementModel> commentMultimediaList;

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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<ReviewExamAnswerModel> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(List<ReviewExamAnswerModel> answerList) {
        this.answerList = answerList;
    }

    public List<MultimediaElementModel> getMultimediaList() {
        return multimediaList;
    }

    public void setMultimediaList(List<MultimediaElementModel> multimediaList) {
        this.multimediaList = multimediaList;
    }

    public void addMultimedia(MultimediaElementModel multimedia) {
        this.multimediaList.add(multimedia);
    }

    public List<MultimediaElementModel> getCommentMultimediaList() {
        return commentMultimediaList;
    }

    public void setCommentMultimediaList(List<MultimediaElementModel> commentMultimediaList) {
        this.commentMultimediaList = commentMultimediaList;
    }

    public void addCommentMultimedia(MultimediaElementModel commentMultimedia) {
        this.commentMultimediaList.add(commentMultimedia);
    }
}
