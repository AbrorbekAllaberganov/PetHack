package com.example.PetHack.service.impl;

import com.example.PetHack.exceptions.BadRequest;
import com.example.PetHack.payload.Result;
import com.example.PetHack.repository.ParentRepository;
import com.example.PetHack.security.SecurityUtils;
import com.example.PetHack.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final SecurityUtils securityUtils;
    private final ParentRepository parentRepository;

    @Override
    public Result getMe() {
        try{
            Optional<String> currentUser = securityUtils.getCurrentUser();
            if (currentUser.isEmpty())
                throw new BadRequest("User not found");
            return Result.success(parentRepository.findByUserName(currentUser.get()));
        }catch (Exception e){
            return Result.exception(e);
        }
    }
}
