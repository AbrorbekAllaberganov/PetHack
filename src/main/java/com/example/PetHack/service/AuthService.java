package com.example.PetHack.service;

import com.example.PetHack.payload.LoginPayload;
import com.example.PetHack.payload.RegisterPayload;
import com.example.PetHack.payload.Result;

public interface AuthService {
    Result loginUser(LoginPayload loginPayload);
}
