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
import com.itest.model.DoneExamHeader;
import com.itest.repository.ExamenRepository;
import com.itest.service.LearnerManagementService;
import com.itest.service.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("learnerManagementServiceImpl")
public class LearnerManagementServiceImpl implements LearnerManagementService {

    @Autowired
    @Qualifier("examenRepository")
    private ExamenRepository examenRepository;

    @Autowired
    @Qualifier("userManagementServiceImpl")
    private UserManagementService userManagementService;

    @Autowired
    @Qualifier("examenConverter")
    private ExamenConverter examenConverter;

    public List<DoneExamHeader> GetDoneExamsHeader(int groupId){
        // Get the done exams from database
        List<Examen> doneExamList = this.examenRepository.findDoneExams(userManagementService.getUserIdOfCurrentUser(), groupId);

        // Convert the entity object list to model object list
        return this.examenConverter.convertExamenListToDoneExamHeaderList(doneExamList);
    }

}
