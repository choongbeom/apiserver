package com.cbkim.apiserver.service;

import java.util.Random;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpSession;

import com.cbkim.apiserver.model.MailHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    
    @Value("${spring.mail.username}")
    private String fromEmail;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private JavaMailSenderImpl javaMailSenderImpl;

    public boolean sendEmail(HttpSession session, String email) throws Exception{
        MailHandler mailHandler = new MailHandler(javaMailSenderImpl);
        Random random = new Random(System.currentTimeMillis());
        
        int result = 100000 + random.nextInt(900000);

        // 메일 받는 사람
        mailHandler.setTo(email);

        // 보내는 사람
        mailHandler.setFrom(fromEmail);

        // 제목
        mailHandler.setSubject("회원가입 인증번호 입니다.");

        // html layout
        String htmlContent = "<p>인증번호: " + result + "<p>";
        mailHandler.setText(htmlContent, true);
        mailHandler.send();
        
        session.setAttribute(email, result);
        session.setMaxInactiveInterval(3*60); // 3분동안 요청이 없을 경우 세션 제거 (초단위)

        return true;
    }

    public boolean emailCertification(HttpSession session, String email, int inputCode) throws Exception {
        
        boolean result = false;
        if (session.getAttribute(email) != null) {
            int generationCode = (int)session.getAttribute(email);

            if (generationCode == inputCode) {
                result = true;
            }
            else {
                result = false;
            }
        }
        else {
            throw new Exception("인증 시간을 초과하였습니다. 다시 인증하시기 바랍니다.");
        }

        return result;       
    }

}
