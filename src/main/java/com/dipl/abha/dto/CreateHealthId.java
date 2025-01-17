package com.dipl.abha.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateHealthId {
   private String email;
   private String firstName;
   private String healthId;
   private String lastName;
   private String middleName;
   private String password;
   private String profilePhoto;
   private String txnId;
}
