package com.dipl.abha.m2.shareprofilepayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileSharePayload{
    public String requestId;
    public String timestamp;
    public Intent intent;
    public Location location;
    public ProfilePayload profile;
}