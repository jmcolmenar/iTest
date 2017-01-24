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

import com.itest.converter.MatriculaConverter;
import com.itest.entity.Matricula;
import com.itest.model.CourseModel;
import com.itest.repository.MatriculaRepository;
import com.itest.service.CourseManagementService;
import com.itest.service.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service("courseManagementServiceImpl")
public class CourseManagementServiceImpl implements CourseManagementService {

    @Autowired
    @Qualifier("matriculaRepository")
    private MatriculaRepository matriculaRepository;

    @Autowired
    @Qualifier("userManagementServiceImpl")
    private UserManagementService userManagementService;

    @Autowired
    @Qualifier("matriculaConverter")
    private MatriculaConverter matriculaConverter;


    @Override
    public List<CourseModel> getCourseOfUser() {

        try{
            // Get the user identifier of current user
            int userId = this.userManagementService.getUserIdOfCurrentUser();

            // Get the Matricula table list of current user
            List<Matricula> matriculaList = this.matriculaRepository.selectMatriculaListByUsernameOfUsuario(userId);

            // Convert the Matricula objects list to Course model
            List<CourseModel> courseModelList = this.matriculaConverter.convertMatriculaListToCourseModelList(matriculaList);

            // Order the list by year
            Collections.sort(courseModelList, (o1, o2) -> o1.getYear().compareTo(o2.getYear()));

            // Return the course model list
            return courseModelList;
        }catch(Exception exc){
            // TODO: Log the exception
            return new ArrayList<>();
        }
    }
}
