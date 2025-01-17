package com.dipl.abha.uhi.dto;

import java.util.Date;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;

@SqlResultSetMapping(name = "DoctorDetailsMappingSlotsDto", classes = {
        @ConstructorResult(targetClass = DoctorDetailsSlotsDto.class, columns = {
                @ColumnResult(name = "doctor_name", type = String.class),
                @ColumnResult(name = "doctor_id", type = Long.class),
                @ColumnResult(name = "from_date", type = Date.class),
                @ColumnResult(name = "to_date", type = Date.class),
                @ColumnResult(name = "available_slot_id", type = String.class),
                @ColumnResult(name = "time_slot", type = String.class),
                @ColumnResult(name = "hpr_id", type = String.class),
                }) })
@Entity
public class DoctorDetailsMappingSlotsDto {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

}
