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
package com.itest.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;

@Component
public class EmailComponent {

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private JavaMailSender sender;

    /**
     * Send an email
     * @param to The recipient direction
     * @param subject The subject of email
     * @param message The message of email
     * @throws Exception The possible exception
     */
    public void sendEmail(String to, String subject, String message) throws Exception{
        // Create the message object
        MimeMessage mimeMessage = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

        // Fill the receipt, subject and message
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(message);

        // Send the email
        this.sender.send(mimeMessage);
    }


}
