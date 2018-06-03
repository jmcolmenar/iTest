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
package com.itest.service.business.impl;

import com.itest.constant.MultimediaPathConstant;
import com.itest.constant.MultimediaTypeConstant;
import com.itest.entity.ExtraPregunta;
import com.itest.entity.ExtraPreguntaComentario;
import com.itest.entity.ExtraRespuesta;
import com.itest.model.MultimediaElementModel;
import com.itest.service.business.LearnerMultimediaService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.itest.constant.MultimediaMeasureUnitConstant.*;

@Service("learnerMultimediaServiceImpl")
public class LearnerMultimediaServiceImpl implements LearnerMultimediaService {

    /**
     * Get the Multimedia Element Model list from the database object list
     * @param databaseMultimediaList The database object list with the information of multimedia elements
     * @return The model object list ith the multimedia info
     */
    public List<MultimediaElementModel> getMultimediaModelListFromDatabaseObjectList(List databaseMultimediaList){

        // Initialize the list of Multimedia model
        List<MultimediaElementModel> multimediaElementModelList = new ArrayList<>();

        // Check if the list of database multimedia object is not empty
        if(databaseMultimediaList != null && databaseMultimediaList.size() > 0){

            // Convert all database object to model objects
            for(Object databaseObject : databaseMultimediaList){

                // Convert to model object
                MultimediaElementModel multimediaElemModel = this.convertDatabaseObjectToMultimediaElementModelObject(databaseObject);

                // Add the model object to list if it is not null
                if(multimediaElemModel != null){
                    multimediaElementModelList.add(multimediaElemModel);
                }
            }
        }

        // Return the multimedia model list
        return multimediaElementModelList;
    }

    /**
     * Convert a database object with multimedia information to model object
     * @param databaseObject The database object with multimedia information
     * @return The model object with multimedia information
     */
    private MultimediaElementModel convertDatabaseObjectToMultimediaElementModelObject(Object databaseObject) {

        // Check if the type of database class is not correct
        if(databaseObject.getClass() != ExtraPregunta.class && databaseObject.getClass() != ExtraPreguntaComentario.class && databaseObject.getClass() != ExtraRespuesta.class){

            // Return a null object because the type of the database object is not correct
            return null;
        }

        // Get the object with the multimedia info from the database object
        MultimediaInfo multimediaInfo = this.getMultimediaElementModelFromDatabaseEntity(databaseObject);

        // Initialize and fill the multimedia element model
        MultimediaElementModel multimediaElementModel = new MultimediaElementModel();
        multimediaElementModel.setType(multimediaInfo.getType());
        multimediaElementModel.setGeogebraType(multimediaInfo.getGeogebraType());

        // Fill the width and height
        String width = multimediaInfo.getWidth();
        String heigh = multimediaInfo.getHeight();
        multimediaElementModel.setId(multimediaInfo.getId());
        multimediaElementModel.setWidth(width);
        multimediaElementModel.setMeasureUnitWidth(this.calculateMeasureUnit(width));
        multimediaElementModel.setHeight(heigh);
        multimediaElementModel.setMeasureUnitHeight(this.calculateMeasureUnit(heigh));

        // Check the type of multimedia element to fill the path of multimedia and extension element
        String path = multimediaInfo.getPath();
        if(multimediaInfo.getType() == MultimediaTypeConstant.YOUTUBE){
            multimediaElementModel.setPath(MultimediaPathConstant.YOUTUBE + path);
        }else{
            multimediaElementModel.setPath(MultimediaPathConstant.MEDIA_FILE + path);
            multimediaElementModel.setExtension(path.substring(path.lastIndexOf('.') + 1).toLowerCase());
        }

        // Return the multimedia element model
        return multimediaElementModel;

    }

    /**
     * Calculates the measure unit from a value of width or height
     * @param value The value of width or height of the multimedia element
     * @return The measure unit of width or height
     */
    private int calculateMeasureUnit(String value) {

        // Calculates the measure unit from width/height value
        int measureUnitType;
        if(value == null || value.trim().isEmpty()){

            measureUnitType = NONE;

        }else if(value.equals(SIZE_AUTO) || value.equals(SIZE_SMALL) || value.equals(SIZE_MEDIUM) || value.equals(SIZE_BIG)){

            measureUnitType = SIZE;

        }else if(value.endsWith(PERCENT_SYMBOL)){

            measureUnitType = PERCENT;

        }else{

            measureUnitType = PIXEL;
        }

        // Return the measure unit
        return measureUnitType;
    }


    /**
     * Get the Multimedia info from the database object
     * @param databaseMultimediaElement The database object with the multimedia information
     * @return The object with the multimedia info
     */
    private MultimediaInfo getMultimediaElementModelFromDatabaseEntity(Object databaseMultimediaElement){

        // Initialize the Multimedia Info object
        MultimediaInfo multimediaInfo = new MultimediaInfo();

        // Get the multimeda info from database object
        if(databaseMultimediaElement.getClass() == ExtraPregunta.class){

            // Fill the Multimedia Info object from database object
            ExtraPregunta extraPregunta = (ExtraPregunta)databaseMultimediaElement;
            multimediaInfo.setId(extraPregunta.getIdextrap());
            multimediaInfo.setPath(extraPregunta.getRuta());
            multimediaInfo.setType(extraPregunta.getTipo());
            multimediaInfo.setWidth(extraPregunta.getAncho());
            multimediaInfo.setHeight(extraPregunta.getAlto());
            multimediaInfo.setGeogebraType(extraPregunta.getGeogebraType());

        }else if(databaseMultimediaElement.getClass() == ExtraPreguntaComentario.class){

            // Fill the Multimedia Info object from database object
            ExtraPreguntaComentario extraPreguntaComentario = (ExtraPreguntaComentario)databaseMultimediaElement;
            multimediaInfo.setId(extraPreguntaComentario.getIdextrapcom());
            multimediaInfo.setPath(extraPreguntaComentario.getRuta());
            multimediaInfo.setType(extraPreguntaComentario.getTipo());
            multimediaInfo.setWidth(extraPreguntaComentario.getAncho());
            multimediaInfo.setHeight(extraPreguntaComentario.getAlto());

        } else if(databaseMultimediaElement.getClass() == ExtraRespuesta.class){

            // Fill the Multimedia Info object from database object
            ExtraRespuesta extraRespuesta = (ExtraRespuesta)databaseMultimediaElement;
            multimediaInfo.setId(extraRespuesta.getIdextrar());
            multimediaInfo.setPath(extraRespuesta.getRuta());
            multimediaInfo.setType(extraRespuesta.getTipo());
            multimediaInfo.setWidth(extraRespuesta.getAncho());
            multimediaInfo.setHeight(extraRespuesta.getAlto());

        }

        // Return the multimedia info object
        return multimediaInfo;
    }

    /**
     * Private class holding the multimedia information fields of the common entities in database
     */
    private class MultimediaInfo {
        private int id;
        private String path;
        private int type;
        private String width;
        private String height;
        private int geogebraType;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        String getPath() {
            return path;
        }

        void setPath(String path) {
            this.path = path;
        }

        int getType() {
            return type;
        }

        void setType(int type) {
            this.type = type;
        }

        String getWidth() {
            return width;
        }

        void setWidth(String width) {
            this.width = width;
        }

        String getHeight() {
            return height;
        }

        void setHeight(String height) {
            this.height = height;
        }

        int getGeogebraType() {
            return geogebraType;
        }

        void setGeogebraType(int geogebraType) {
            this.geogebraType = geogebraType;
        }
    }

}
