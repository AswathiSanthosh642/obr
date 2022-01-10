package com.sayone.obr.service;


import org.springframework.data.jpa.repository.JpaRepository;

import javax.mail.MessagingException;

public interface EmailService {
      Boolean send(String emailId, String email) throws Exception;


}
