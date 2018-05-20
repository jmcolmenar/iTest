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

import java.util.Date;
import java.util.List;

public class NewExamAnswerModel {

    private int asnwerId;

    private String text;

    private boolean checked;

    private Date answerTime;

    private List<MultimediaElementModel> multimediaList;

    public int getAsnwerId() {
        return asnwerId;
    }

    public void setAsnwerId(int asnwerId) {
        this.asnwerId = asnwerId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public Date getAnswerTime() {
        return answerTime;
    }

    public void setAnswerTime(Date answerTime) {
        this.answerTime = answerTime;
    }

    public List<MultimediaElementModel> getMultimediaList() {
        return multimediaList;
    }

    public void setMultimediaList(List<MultimediaElementModel> multimediaList) {
        this.multimediaList = multimediaList;
    }

    public void addMultimedia(MultimediaElementModel multimediaModel) {
        this.multimediaList.add(multimediaModel);
    }
}
