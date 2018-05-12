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
package com.itest.repository;

import com.itest.entity.LogExamen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository("logExamenRepository")
public interface LogExamenRepository extends JpaRepository<LogExamen, Serializable> {

    @Query("select " +
            "   log " +
            "from " +
            "   LogExamen log " +
            "where" +
            "   log.examenes.idexam = :examid " +
            "   and log.usuarios.idusu = :userid " +
            "order by " +
            "   log.idlogexams")
    List<LogExamen> findByExamIdAndUserIdOrderById(@Param("examid") int examId, @Param("userid") int userId);

    @Query("select " +
            "   log " +
            "from " +
            "   LogExamen log " +
            "where" +
            "   log.examenes.idexam = :examid " +
            "   and log.usuarios.idusu = :userid " +
            "   and log.preguntas.idpreg = :questionid ")
    List<LogExamen> findByExamIdAndUserIdAndQuestionId(@Param("examid") int examId, @Param("userid") int userId, @Param("questionid") int questionId);

}
