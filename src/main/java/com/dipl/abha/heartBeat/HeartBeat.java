package com.dipl.abha.heartBeat;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HeartBeat{
    public LocalDateTime timestamp;
    public String status;
    public Error error;
}
