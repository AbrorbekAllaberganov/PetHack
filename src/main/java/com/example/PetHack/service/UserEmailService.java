package com.example.PetHack.service;

import com.example.PetHack.entity.UserEmail;
import com.example.PetHack.payload.RegisterPayload;
import com.example.PetHack.payload.Result;

public interface UserEmailService {
    Result saveUserByEmailCode(RegisterPayload registerPayload);

    String generateEmailCode();

    void sendCodeToEmail(String email, String code);

    Result checkEmailCode(String email,String code);
}
