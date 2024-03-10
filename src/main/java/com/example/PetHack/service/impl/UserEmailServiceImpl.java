package com.example.PetHack.service.impl;

import com.example.PetHack.entity.Parent;
import com.example.PetHack.entity.Role;
import com.example.PetHack.entity.UserEmail;
import com.example.PetHack.exceptions.BadRequest;
import com.example.PetHack.payload.RegisterPayload;
import com.example.PetHack.payload.Result;
import com.example.PetHack.repository.ParentRepository;
import com.example.PetHack.repository.RoleRepository;
import com.example.PetHack.repository.UserEmailRepository;
import com.example.PetHack.service.UserEmailService;
import lombok.RequiredArgsConstructor;
import org.hibernate.id.UUIDGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserEmailServiceImpl implements UserEmailService {
    private final UserEmailRepository userEmailRepository;
    private final ParentRepository parentRepository;
    private final JavaMailSender javaMailSender;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override  // this method saves user to user_email table if user enter email code, this object is moved to parent table
    public Result saveUserByEmailCode(RegisterPayload registerPayload) {
        if (parentRepository.findByUserName(registerPayload.getUserName()) != null)
            return Result.exception(new BadRequest("This email is already registered"));
        else if (parentRepository.findByEmail(registerPayload.getEmail()) != null)
            return Result.exception(new BadRequest("This username is already registered"));

        try {
            // delete user_email if user has tried to register before
            if (userEmailRepository.existsByEmail(registerPayload.getEmail()))
                userEmailRepository.deleteUserEmailByEmail(registerPayload.getEmail());

            UserEmail userEmail = new UserEmail();
            userEmail.setUserName(registerPayload.getUserName());
            userEmail.setPassword(registerPayload.getPassword());
            userEmail.setEmail(registerPayload.getEmail());
            userEmail.setEmailCode(generateEmailCode());

            // save user_email to user_email table
            userEmailRepository.save(userEmail);

            sendCodeToEmail(userEmail.getEmail(), userEmail.getEmailCode());
            return Result.message("user is registered, but please confirm the email of user", true);
        } catch (Exception e) {
            return Result.exception(e);
        }
    }

    @Override // Generating email confirmation code for registration
    public String generateEmailCode() {
        Random rnd = new Random();
        int number = rnd.nextInt(9999);
        return String.format("%04d", number);
    }

    @Override // send code to email (this method requires toEmail - receiver's email, code - confirmation code)
    public void sendCodeToEmail(String toEmail, String code) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Email Confirm");
            message.setText("Code: " + code);
            javaMailSender.send(message);

        } catch (Exception e) {
            throw new BadRequest(e.getMessage());
        }
    }

    @Override // check the code which is entered by user
    public Result checkEmailCode(String email, String code) {
        UserEmail userEmail=userEmailRepository.findUserEmailByEmail(email);

        if (userEmail.getEmailCode().equals(code)){
            Parent parent=new Parent();
            parent.setEmail(userEmail.getEmail());
            parent.setPassword(passwordEncoder.encode(userEmail.getPassword()));
            parent.setUserName(userEmail.getUserName());

            parent.setRoles(Collections.singletonList(roleRepository.findByName("ROLE_USER")));


            parentRepository.save(parent); // if code is correct, the data of user is relocated to parent table

            if (userEmailRepository.existsByEmail(fromEmail))
                userEmailRepository.deleteUserEmailByEmail(email); // the user_email object is deleted after saving user data

            return Result.success(parent);
        }

        return Result.message("code is incorrect, please try again", true);
    }


}
