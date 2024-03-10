package com.example.PetHack.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminRequest {
    private String userName;
    private String email;
    private String password;
}
