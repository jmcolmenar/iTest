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
package com.itest.service.impl;

import com.itest.converter.ExamenConverter;
import com.itest.entity.Examen;
import com.itest.model.DoneExamInfoModel;
import com.itest.model.ExamExtraInfoModel;
import com.itest.repository.ExamenRepository;
import com.itest.service.LearnerExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("learnerExamServiceImpl")
public class LearnerExamServiceImpl implements LearnerExamService {

    @Autowired
    @Qualifier("examenRepository")
    private ExamenRepository examenRepository;

    @Autowired
    @Qualifier("examenConverter")
    private ExamenConverter examenConverter;

    /**
     * Get the list of available exams of current subject for the learner
     * @param groupId Group identifier corresponding to the current subject
     * @param learnerId User identifier of the learner
     * @return The list of available exams
     */
    public List<ExamExtraInfoModel> getAvailableExamsList(int groupId, int learnerId){
        // TODO: Get the list of available exams
        List<ExamExtraInfoModel> availableExamModelList = new ArrayList();
        return availableExamModelList;
    }

    /**
     * Get the list of next exams of current subject for the learner
     * @param groupId Group identifier corresponding to the current subject
     * @param learnerId User identifier of the learner
     * @return The list of next exams
     */
    public List<ExamExtraInfoModel> getNextExamsList(int groupId, int learnerId){
        // TODO: Get the list of next exams
        List<ExamExtraInfoModel> nextExamModelList = new ArrayList();
        return nextExamModelList;
    }

    /**
     * Get the list of done exams of current subject for the learner
     * @param groupId Group identifier corresponding to the current subject
     * @param learnerId User identifier of the learner
     * @return The list of done exams
     */
    public List<DoneExamInfoModel> getDoneExamsList(int groupId, int learnerId){

        // Get the done exams from database and user id
        List<Examen> examList = this.examenRepository.findDoneExams(learnerId, groupId);

        // Convert the entity object list to model object list
        List<DoneExamInfoModel> doneExamModelList = this.examenConverter.convertExamenListToDoneExamInfoModelList(examList, learnerId);

        // Return the done exam model list
        return doneExamModelList;
    }
}
