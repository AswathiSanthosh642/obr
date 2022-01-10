package com.sayone.obr.service.impl;

import com.sayone.obr.exception.UserServiceException;
import com.sayone.obr.exception.WishlistErrors;
import com.sayone.obr.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailsender;
    private final static Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);


    /**
     * Constructor.
     * //         * @param mailSender JavaMailSender.
     */


    public EmailServiceImpl(JavaMailSender mailsender) {
        this.mailsender = mailsender;
    }

    @Override
    @Async
    public Boolean send(String emailId, String email) throws Exception {

        MimeMessage mimeMessage = mailsender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        helper.setText(email, true);
        helper.setTo(emailId);
        if (!emailId.matches("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$"))
            throw new UserServiceException(WishlistErrors.EMAIL_NOT_VALID.getErrorMessage());
        helper.setSubject("obr message");
        helper.setFrom("aswathisinro78@gmail.com");
        mailsender.send(mimeMessage);
        return true;

    }
}
