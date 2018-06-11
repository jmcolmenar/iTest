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

import com.itest.component.EmailComponent;
import com.itest.entity.RecuperacionPassword;
import com.itest.entity.Usuario;
import com.itest.repository.RecuperacionPasswordRepository;
import com.itest.repository.UsuarioRepository;
import com.itest.service.business.RetrievePasswordService;
import com.itest.service.business.TranslationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

import static com.itest.constant.AppUrlConstant.APP_URL;

@Service("retrievePasswordServiceImpl")
public class RetrievePasswordServiceImpl implements RetrievePasswordService {

    @Autowired
    @Qualifier("usuarioRepository")
    private UsuarioRepository usuarioRepository;

    @Autowired
    @Qualifier("recuperacionPasswordRepository")
    private RecuperacionPasswordRepository recuperacionPasswordRepository;

    @Autowired
    @Qualifier("translationServiceImpl")
    private TranslationService translationService;

    @Autowired
    private EmailComponent emailComponent;

    /**
     * Generate a new token for the user to retrieve the password
     * @param username The username
     * @param email The email of user
     * @return String with an error message if the token has not been generated. Otherwise, null
     */
    public String generateTokenToRetrievePassword(String username, String email){

        // Variables with the possible errors
        boolean userDataIncorrect = false;
        boolean userEmailIncorrect = false;
        boolean errorInsertedTokenInDatabase = false;
        boolean errorSendingEmailWithToken = false;

        // Check the username and email are not null
        if(username != null && email != null){

            // Get the user from database
            Usuario user = this.usuarioRepository.findByUsuario(username);

            // Check the user exist in database
            if(user != null){

                // Check the user email is the same that the received
                if(user.getEmail() != null && user.getEmail().equalsIgnoreCase(email)){

                    // Generate a new token
                    String newToken = UUID.randomUUID().toString();

                    // Insert the token in database
                    boolean isTokenInserted = this.insertTokenToRetrievePasswordInDatabase(newToken, user);

                    // Check if the token is inserted in database
                    if(isTokenInserted){

                        // Send an email with the token to retrieve the password
                        boolean isTokendSent = this.sendEmailWithTokenToRetrievePassword(newToken, email);

                        // Check if has an error sending the email with the token
                        if(!isTokendSent){

                            // The token cannot be sent to the user email
                            errorSendingEmailWithToken = true;

                            // Delete the token from database
                            this.deleteTokenFromDatabase(newToken);
                        }

                    }else{
                        // The token can not be inserted in database
                        errorInsertedTokenInDatabase = true;
                    }
                }else{
                    // The email to retrieve the password is not the same that the user email
                    userEmailIncorrect = true;
                }
            }
            else{
                // The user data is not correct to retrieve the password
                userDataIncorrect = true;
            }
        }
        else{
            // The user data is not correct to retrieve the password
            userDataIncorrect = true;
        }

        // Check if there are errors generating the token to return an error message (null if there are no errors)
        if(userDataIncorrect){

            // The user data is not correct to retrieve the password
            return translationService.getMessage("retrievePassword.errorIncorrectUser");

        }else if(userEmailIncorrect){

            // The email to retrieve the password is not the same that the user email
            return translationService.getMessage("retrievePassword.errorIncorrectEmail");

        }else if(errorInsertedTokenInDatabase || errorSendingEmailWithToken){

            // The token has not been generated
            return translationService.getMessage("retrievePassword.errorGeneratingToken");

        }else{

            // The toke is generated successfully
            return null;
        }
    }

    /**
     * Check if the token is valid to retrieve the user password
     * @param token The token to retrieve the user password
     * @return Error message because the token is invalid. Null if the token is valid
     */
    public String isValidToken(String token){

        // Variables to check if the token is valid
        boolean isValidToken = false;
        boolean hasBeenUsed = false;
        boolean hasExpired = false;

        // Check the token is not null
        if(token != null){

            // Check the token exists in database
            RecuperacionPassword recuperacionPassword = this.recuperacionPasswordRepository.findByToken(token);
            if(recuperacionPassword != null){

                // Get the expiration date and changed password date
                Date expirationDate = recuperacionPassword.getFechaCaducidad();
                Date changedPasswordDate = recuperacionPassword.getFechaCambio();

                // Check if the token is valid
                if(changedPasswordDate == null && expirationDate != null && expirationDate.after(new Date())){

                    // The token is valid
                    isValidToken = true;

                }else{

                    // Check the token has not expirad and has not been used
                    if(changedPasswordDate != null){

                        // The token has been used
                        hasBeenUsed = true;
                    }
                    else if(expirationDate != null && expirationDate.before(new Date())){

                        // The token has expired
                        hasExpired = true;
                    }
                }
            }
        }

        // Check if the token is valid
        if(isValidToken){
            // The token is valid
            return null;

        }else{
            // Check becuase the token is invalid
            if(hasExpired){

                // The token has expired
                return this.translationService.getMessage("retrievePassword.expiredToken");

            }else if(hasBeenUsed){

                // The token has already been used
                return this.translationService.getMessage("retrievePassword.alreadyUsedToken");

            }else{

                // The token is not valid
                return this.translationService.getMessage("retrievePassword.errorUrlToken");
            }
        }
    }

    /**
     * Get the user database object from token to retrieve the password
     * @param token The token to retrieve the password
     * @return The user database object from token
     */
    public Usuario getUserFromTokenToRetrievePassword(String token){

        // Get the entity to retrieve the password by token
        RecuperacionPassword recuperacionPassword = this.recuperacionPasswordRepository.findByToken(token);

        // Check the entity to retrieve the password is in database
        if(recuperacionPassword != null){

            // Return the user from the token
            return recuperacionPassword.getUsuario();

        }else{

            // The entity to retrieve the password by token is not in database
            return null;
        }
    }

    /**
     * Update the token as used in database
     * @param token The used token
     */
    public void UpdateUsedToken(String token){

        // Get the entity to retrieve the password by used token
        RecuperacionPassword recuperacionPassword = this.recuperacionPasswordRepository.findByToken(token);

        // Check the token exists in database
        if(recuperacionPassword != null){

            // Set the change password date to now
            recuperacionPassword.setFechaCambio(new Date());

            // Update the entity in database
            this.recuperacionPasswordRepository.save(recuperacionPassword);
        }
    }

    /**
     * Insert the new generated token to retrieve the user password in database
     * @param newToken The new generated token
     * @param user The current user
     * @return If the token has been inserted in database
     */
    private boolean insertTokenToRetrievePasswordInDatabase(String newToken, Usuario user) {

        // Variable to check if the token has been inserted in database
        boolean isInserted;

        // Check if the token exist in database
        RecuperacionPassword recuperacionPassword = this.recuperacionPasswordRepository.findByToken(newToken);
        if(recuperacionPassword != null){

            // The token is already in database
            isInserted = false;

        }else{

            // Calculates the now date and expiration date (2 hours)
            long nowMilliseconds = System.currentTimeMillis();
            Date now = new Date(nowMilliseconds);
            Date expirationDate = new Date(nowMilliseconds + (1000 * 60 * 60 * 2));

            // Create a new entity to insert in database the new token
            recuperacionPassword = new RecuperacionPassword();
            recuperacionPassword.setUsuario(user);
            recuperacionPassword.setToken(newToken);
            recuperacionPassword.setFechaInsert(now);
            recuperacionPassword.setFechaCaducidad(expirationDate);

            try{

                // Insert the entity in database
                this.recuperacionPasswordRepository.save(recuperacionPassword);

                // The token is inserted
                isInserted = true;

            }catch (Exception exc){

                // The token cannot be inserted in database
                isInserted = false;
            }
        }

        // Return if the token is inserted in database
        return isInserted;
    }

    /**
     * Send an email to the user to retrieve the password through a generated token
     * @param newToken The new generated token to retrieve the password
     * @param email The email of user
     * @return If the email has been sent
     */
    private boolean sendEmailWithTokenToRetrievePassword(String newToken, String email) {

        // Set the subject and message
        String subject = "iTest - Recuperación de contraseña";
        String message = "Pinche sobre el siguiente enlace para cambiar su contraseña: " + APP_URL + "retrievePassword/retrieve?token=" + newToken;

        boolean isSent;
        try{
            // Send the email to the user
            this.emailComponent.sendEmail(email, subject, message);

            // The email is sent
            isSent = true;

        }catch (Exception exc){

            // Has en error sending the email
            isSent = false;
        }

        // Return if the email is sent
        return isSent;
    }

    /**
     * Delete the token to retrieve the password from database
     * @param token The token to delete
     */
    private void deleteTokenFromDatabase(String token){

        // Find the token to etrieve the password from database
        RecuperacionPassword recuperacionPassword = this.recuperacionPasswordRepository.findByToken(token);

        // Check if the token is in database
        if(recuperacionPassword != null){

            // Delete the token to retrieve the password from database
            this.recuperacionPasswordRepository.delete(recuperacionPassword);
        }
    }

}
