package com.dipl.abha.uhi.dto;

import java.util.Date;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;

@SqlResultSetMapping(name = "HprDoctorMappingDetailsDto", classes = {
        @ConstructorResult(targetClass = HprDoctorMappingDto.class, columns = {
        		 @ColumnResult(name = "language_id", type = Integer.class),
                 @ColumnResult(name = "language_name", type = String.class),
                @ColumnResult(name = "doctor_id", type = Long.class),
                @ColumnResult(name = "doctor_name", type = String.class),
                @ColumnResult(name = "hpr_id", type = String.class),
                @ColumnResult(name = "birth_date", type = Date.class),
                @ColumnResult(name = "age", type = Integer.class),
                @ColumnResult(name = "from_date", type = Date.class),
                @ColumnResult(name = "to_date", type = Date.class),
                @ColumnResult(name = "gender", type = String.class),
                @ColumnResult(name = "mobile", type = String.class),
                @ColumnResult(name = "mci_number", type = String.class),
                @ColumnResult(name = "qualification_id", type = Integer.class),
                @ColumnResult(name = "qualification", type = String.class),
                @ColumnResult(name = "specialization_id", type = Integer.class),
                @ColumnResult(name = "department_name", type = String.class),
                @ColumnResult(name = "years_experience", type = Integer.class),
                @ColumnResult(name = "audio_consultation_price", type = Long.class),
                @ColumnResult(name = "video_consultation_price", type = Long.class),
                @ColumnResult(name = "state_id", type = Integer.class),
                @ColumnResult(name = "state_name", type = String.class),
                @ColumnResult(name = "district_id", type = Integer.class),
                @ColumnResult(name = "district_name", type = String.class),
                @ColumnResult(name = "document_path", type = String.class),
                @ColumnResult(name = "hospital_id", type = Integer.class),
                @ColumnResult(name = "hospital_name", type = String.class),
                @ColumnResult(name = "country", type = String.class),
                @ColumnResult(name = "country_code", type = String.class),
                @ColumnResult(name = "city_id", type = Integer.class),
                @ColumnResult(name = "city_name", type = String.class),
        		 @ColumnResult(name = "city_code", type = String.class),
        		 @ColumnResult(name = "hfr_id", type = String.class),
                 @ColumnResult(name = "hspa_name", type = String.class),
         		 @ColumnResult(name = "hspa_long_desc", type = String.class)
        		 
        		 
        		 
        		 
                
                }) })
@Entity
public class HprDoctorMappingDetailsDto {

	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
}
