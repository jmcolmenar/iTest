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
package com.itest.controller;

import com.itest.model.CourseModel;
import com.itest.model.DoneExamHeader;
import com.itest.service.LearnerManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/learner/")
public class LearnerController {

    @Autowired
    @Qualifier("learnerManagementServiceImpl")
    private LearnerManagementService learnerManagementService;

    @GetMapping("/getCourses")
    public List<CourseModel> getCourses(){

        // Get the course list of current user
        List<CourseModel> courseList = this.learnerManagementService.getCourseList();

        // Return the course list
        return courseList;
    }

    @PostMapping("/getDoneExams")
    public List<DoneExamHeader> getDoneExams(@RequestParam(value = "groupId") int groupId){

        // Get the Done examen Header by a group
        List<DoneExamHeader> doneExamsHeaderList = this.learnerManagementService.getDoneExamsHeader(groupId);

        // Return the list of Done Exam Header
        return doneExamsHeaderList;
    }


}
