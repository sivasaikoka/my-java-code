package com.dipl.abha.m2.authnotifydirectmodepayload;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DirectModeAuth{
    public String requestId;
    public LocalDateTime timestamp;
    public Auth auth;
   
}

