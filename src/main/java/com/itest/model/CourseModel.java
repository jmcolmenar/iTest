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

public class CourseModel {

    private String year;

    private List<SubjectModel> subjectList;

    public CourseModel(String year, List<SubjectModel> subjectList) {
        this.year = year;
        this.subjectList = subjectList;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public List<SubjectModel> getSubjectList() {
        return subjectList;
    }

    public void setSubjectList(List<SubjectModel> subjectList) {
        this.subjectList = subjectList;
    }
}
