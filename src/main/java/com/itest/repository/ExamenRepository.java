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

import com.itest.entity.Examen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository("examenRepository")
public interface ExamenRepository extends JpaRepository<Examen, Serializable> {

    @Query("select " +
            "   ex " +
            "from " +
            "   Examen ex " +
            "where ex.revActiva = 1 " +
            "   and ex.fechaIniRev <= current_date " +
            "   and ex.fechaFinRev >= current_date" +
            "   and :userid in (select excal.usuarios.idusu from ex.califs excal where excal.fechaIni <= current_date) " +
            "   and ex.grupos.idgrupo = :groupid")
    public abstract List<Examen> findDoneExams(@Param("userid") int userId, @Param("groupid") int groupId);
}
