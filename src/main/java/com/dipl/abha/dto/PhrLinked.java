package com.dipl.abha.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhrLinked {

private String phrAddress;
private Boolean preferred;
private String update_token;


}
