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

public class ExamPartialCorrectionInfoModel {

    private boolean activePartialCorrection;

    private String penaltyNotAnsweredQuestion;

    private String penaltyFailedQuestion;

    public boolean isActivePartialCorrection() {
        return activePartialCorrection;
    }

    public void setActivePartialCorrection(boolean activePartialCorrection) {
        this.activePartialCorrection = activePartialCorrection;
    }

    public String getPenaltyNotAnsweredQuestion() {
        return penaltyNotAnsweredQuestion;
    }

    public void setPenaltyNotAnsweredQuestion(String penaltyNotAnsweredQuestion) {
        this.penaltyNotAnsweredQuestion = penaltyNotAnsweredQuestion;
    }

    public String getPenaltyFailedQuestion() {
        return penaltyFailedQuestion;
    }

    public void setPenaltyFailedQuestion(String penaltyFailedQuestion) {
        this.penaltyFailedQuestion = penaltyFailedQuestion;
    }
}
