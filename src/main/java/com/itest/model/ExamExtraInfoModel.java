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

public class ExamExtraInfoModel extends ExamBasicInfoModel{

    private int questionsNumber;

    private int examTime;

    private ExamPartialCorrectionInfoModel partialCorrection;

    private boolean showRightAnswersNumber;

    private String maxScore;

    private boolean activeReview;

    private String startReviewDate;

    private String endReviewDate;

    private ExamConfidenceLevelInfoModel confidenceLevel;

    public int getQuestionsNumber() {
        return questionsNumber;
    }

    public void setQuestionsNumber(int questionsNumber) {
        this.questionsNumber = questionsNumber;
    }

    public int getExamTime() {
        return examTime;
    }

    public void setExamTime(int examTime) {
        this.examTime = examTime;
    }

    public ExamPartialCorrectionInfoModel getPartialCorrection() {
        return partialCorrection;
    }

    public void setPartialCorrection(ExamPartialCorrectionInfoModel partialCorrection) {
        this.partialCorrection = partialCorrection;
    }

    public boolean isShowRightAnswersNumber() {
        return showRightAnswersNumber;
    }

    public void setShowRightAnswersNumber(boolean showRightAnswersNumber) {
        this.showRightAnswersNumber = showRightAnswersNumber;
    }

    public String getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(String maxScore) {
        this.maxScore = maxScore;
    }

    public boolean isActiveReview() {
        return activeReview;
    }

    public void setActiveReview(boolean activeReview) {
        this.activeReview = activeReview;
    }

    public String getStartReviewDate() {
        return startReviewDate;
    }

    public void setStartReviewDate(String startReviewDate) {
        this.startReviewDate = startReviewDate;
    }

    public String getEndReviewDate() {
        return endReviewDate;
    }

    public void setEndReviewDate(String endReviewDate) {
        this.endReviewDate = endReviewDate;
    }

    public ExamConfidenceLevelInfoModel getConfidenceLevel() {
        return confidenceLevel;
    }

    public void setConfidenceLevel(ExamConfidenceLevelInfoModel confidenceLevel) {
        this.confidenceLevel = confidenceLevel;
    }
}
