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

import com.itest.constant.LanguageConstant;
import com.itest.service.TranslationService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.LocaleResolver;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

@Service("translationServiceImpl")
public class TranslationServiceImpl implements TranslationService{

    private final Log LOG = LogFactory.getLog(TranslationServiceImpl.class);

    @Autowired
    private MessageSource messageSource;

    @Autowired
    LocaleResolver localeResolver;

    private MessageSourceAccessor accessor;

    @PostConstruct
    private void init() {
        this.accessor = new MessageSourceAccessor(this.messageSource);
    }

    // Get the translated message by the resource identifier
    public String getMessage(String messageResource) {
        return this.accessor.getMessage(messageResource, messageResource);
    }

    // Set the application locale from language id
    public void setLocale(int languageId, HttpServletRequest request, HttpServletResponse response){
        try{
            // Set the locale with the culture id
            if(languageId == LanguageConstant.SPANISH_ID){
                this.localeResolver.setLocale(request, response, LanguageConstant.SPANSIH_LOCALE);
            }else if(languageId == LanguageConstant.ENGLISH_ID){
                this.localeResolver.setLocale(request, response, LanguageConstant.ENGLISH_LOCALE);
            }else{
                throw new Exception("There is not a Locale with LanguageId = "+ languageId);
            }
        }catch(Exception exc){
            // Log the exception
            LOG.error("Error setting the locale. Exception: " + exc.getMessage());
        }
    }

    // Get the current culture identifier
    public int getCurrentLanguageId(){
        // The culture id to return (Spanish for default)
        int cultureId = LanguageConstant.SPANISH_ID;

        try{
            // Get the current locate from the context
            Locale locale = LocaleContextHolder.getLocale();

            // Set the culture id from locale
            if(locale.getLanguage().equals(LanguageConstant.SPANISH_LANGUAGE)){
                cultureId = LanguageConstant.SPANISH_ID;
            }else if(locale.getLanguage().equals(LanguageConstant.ENGLISH_LANGUAGE)){
                cultureId = LanguageConstant.ENGLISH_ID;
            }
        }catch(Exception exc){
            // Log the exception
            LOG.error("Error getting the LanguageId of current user. Exception: " + exc.getMessage());
        }

        // Return the culture id
        return cultureId;
    }
}
