package com.example.PetHack.service.impl;

import com.example.PetHack.exceptions.BadRequest;
import com.example.PetHack.payload.Result;
import com.example.PetHack.repository.AdminRepository;
import com.example.PetHack.security.SecurityUtils;
import com.example.PetHack.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final SecurityUtils securityUtils;
    private final AdminRepository adminRepository;

    @Override
    public Result getMe() {
        try{
            Optional<String> currentUser = securityUtils.getCurrentUser();
            if (currentUser.isEmpty())
                throw new BadRequest("User not found");
            return Result.success(adminRepository.findByParent_UserName(currentUser.get()));
        }catch (Exception e){
            return Result.exception(e);
        }
    }
}
