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
package com.itest.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/learner/partial/")
public class LearnerPartialViewController {

    @GetMapping("/courses")
    public String getCoursesPartialView(){
        return "learner/courses";
    }

    @GetMapping("/subject")
    public String getSubjectPartialView(){
        return "learner/subject";
    }

    @GetMapping("/reviewExam")
    public String getReviewExamPartialView(){
        return "learner/review_exam";
    }

    @GetMapping("/newExam")
    public String getNewExamPartialView(){
        return "learner/new_exam";
    }
}
