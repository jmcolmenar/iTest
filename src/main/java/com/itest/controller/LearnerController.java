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

import com.itest.model.request.GetExamToReviewRequest;
import com.itest.model.request.GetExamsInfoRequest;
import com.itest.model.request.GetTutorsToSendEmailRequest;
import com.itest.model.response.GetCoursesResponse;
import com.itest.model.response.GetExamToReviewResponse;
import com.itest.model.response.GetExamsInfoResponse;
import com.itest.model.response.GetTutorsToSendEmailResponse;
import com.itest.service.LearnerManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/learner/")
public class LearnerController {

    @Autowired
    @Qualifier("learnerManagementServiceImpl")
    private LearnerManagementService learnerManagementService;

    @GetMapping("/getCourses")
    public GetCoursesResponse getCourses(){

        // Call to the service to get the course list of current user
        GetCoursesResponse response = this.learnerManagementService.getCourseList();

        // Return the response
        return response;
    }

    @PostMapping("/getExamsInfo")
    public GetExamsInfoResponse getExamsInfo(@RequestBody GetExamsInfoRequest request){

        // Call to the service to get the exams info
        GetExamsInfoResponse response = this.learnerManagementService.getExamsInfo(request);

        // Return the response
        return response;
    }

    @PostMapping("/getExamToReview")
    public GetExamToReviewResponse getExamToReview(@RequestBody GetExamToReviewRequest request){

        // Cal to the service to get the exam info to review
        GetExamToReviewResponse response = this.learnerManagementService.getExamToReview(request);

        // Return the response
        return response;
    }

    @PostMapping("/getTutorsToSendEmail")
    public GetTutorsToSendEmailResponse getTutorsToSendEmailResponse(@RequestBody GetTutorsToSendEmailRequest request){

        // Call to the service to get the tutors to send an email
        GetTutorsToSendEmailResponse response = this.learnerManagementService.getTutorsToSendEmail(request);

        // Return the response
        return response;
    }

}
