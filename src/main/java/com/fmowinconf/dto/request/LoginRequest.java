package com.fmowinconf.dto.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String ficha;
    private String password;
}