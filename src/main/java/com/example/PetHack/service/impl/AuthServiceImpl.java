package com.example.PetHack.service.impl;

import com.example.PetHack.entity.Parent;
import com.example.PetHack.exceptions.BadRequest;
import com.example.PetHack.exceptions.UnAuthorized;
import com.example.PetHack.payload.LoginPayload;
import com.example.PetHack.payload.RegisterPayload;
import com.example.PetHack.payload.Result;
import com.example.PetHack.repository.ParentRepository;
import com.example.PetHack.security.JwtTokenProvider;
import com.example.PetHack.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
    private final ParentRepository parentRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserEmailServiceImpl userEmailService;

    @Override
    public Result loginUser(LoginPayload loginPayload) {
        Parent parent = parentRepository.findByUserName(loginPayload.getUserName());
        if (parent==null){
            return Result.exception(new UnAuthorized("Неверный логин или пароль"));
        }

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginPayload.getUserName(), loginPayload.getPassword()));
        String token = jwtTokenProvider.createToken(parent.getUserName(), parent.getRoles());

        if (token == null) {
            return Result.exception(new BadRequest("Token is not created"));
        }

        Map<String, Object> login = new HashMap<>();
        login.put("token", token);
        login.put("username", parent.getUserName());
        login.put("success", true);
        return Result.success(login);
    }


}
