package com.dipl.abha.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VitalHistory {
	private String temperature;
    private String pulse;
    private String systolic;
    private String diastolic;
    private String hemoglobin;
    private String rbsk;
    private String oxigen_count;
    private String diabetic_value;
    private String height;
    private String weight;
}
