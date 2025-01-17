package com.dipl.abha.dto;
import java.time.LocalDateTime;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;

@SqlResultSetMapping(name = "RequestResultSetDtoMapping", classes = {
		@ConstructorResult(targetClass = com.dipl.abha.dto.RequestResultSetDto.class, columns = {
				@ColumnResult(name = "patient_referencenumber", type = String.class),
				@ColumnResult(name = "patient_display", type = String.class),
				@ColumnResult(name = "patient_id", type = String.class),
				@ColumnResult(name = "carecontexts_referencenumber", type = String.class),
				@ColumnResult(name = "result_id", type = String.class),
				@ColumnResult(name = "carecontexts_display", type = String.class),
				@ColumnResult(name = "report_type", type = String.class),
				@ColumnResult(name = "consultation_date", type = LocalDateTime.class),
				@ColumnResult(name = "doctor", type = String.class),
				@ColumnResult(name = "file_path", type = String.class),
				@ColumnResult(name = "chief_complaints", type = String.class),
				@ColumnResult(name = "meds", type = String.class),
				@ColumnResult(name = "med_directions", type = String.class),
				@ColumnResult(name = "visit_date",type = String.class),
				@ColumnResult(name = "follow_up_date", type = String.class),
				@ColumnResult(name = "visit_start_time", type = String.class),
				@ColumnResult(name = "visit_end_time", type = String.class),
				@ColumnResult(name = "vital_history", type = String.class),
				@ColumnResult(name = "investigation", type = String.class),
				@ColumnResult(name = "clinic_name", type = String.class) }) })
@Entity
public class RequestResultSetDtoMapping {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id; 
}
