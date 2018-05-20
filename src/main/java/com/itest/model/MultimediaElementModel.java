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

import com.itest.constant.MultimediaMeasureUnitTypeConstant;

public class MultimediaElementModel {

    private String path;

    private int type;

    private String widht;

    private String height;

    private int measureUnitWidht;

    private int measureUnitHeight;

    private String extension;

    private int geogebraType;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getWidht() {
        return widht;
    }

    public void setWidht(String widht) {
        this.widht = widht;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public int getMeasureUnitWidht() {
        return measureUnitWidht;
    }

    public int getMeasureUnitHeight() {
        return measureUnitHeight;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public int getGeogebraType() {
        return geogebraType;
    }

    public void setGeogebraType(int geogebraType) {
        this.geogebraType = geogebraType;
    }

    private int calculateMeasureUnit(String value){
        int measureUnitType;
        if(value == null || value.trim().isEmpty()){
            measureUnitType = MultimediaMeasureUnitTypeConstant.NONE;
        }
        else if(value.equals("auto") || value.equals("small") || value.equals("medium") || value.equals("big")){
            measureUnitType = MultimediaMeasureUnitTypeConstant.SIZE;
        }else if(value.endsWith("%")){
            measureUnitType = MultimediaMeasureUnitTypeConstant.PERCENT;
        }else{
            measureUnitType = MultimediaMeasureUnitTypeConstant.PIXEL;
        }
        return measureUnitType;
    }
}
