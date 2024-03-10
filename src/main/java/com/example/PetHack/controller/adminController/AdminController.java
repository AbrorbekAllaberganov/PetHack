package com.example.PetHack.controller.adminController;

import com.example.PetHack.payload.Result;
import com.example.PetHack.service.impl.AdminServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin(value = "*", maxAge = 3600L)
public class AdminController {
    private final AdminServiceImpl adminService;


    @GetMapping
    public ResponseEntity<Result> getMe(){
        Result result= adminService.getMe();
        return ResponseEntity.status(result.isStatus()?200:409).body(result);
    }

}
