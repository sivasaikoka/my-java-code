package com.dipl.abha.dto;

import java.time.LocalDateTime;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;

@SqlResultSetMapping(name = "RequestResultSetDtoNewMapping", classes = {
		@ConstructorResult(targetClass = com.dipl.abha.dto.RequestResultSetDtoNew.class, columns = {
				@ColumnResult(name = "patient_id", type = String.class),
				@ColumnResult(name = "patient_display", type = String.class),
				@ColumnResult(name = "patient_referencenumber", type = String.class),
				@ColumnResult(name = "doctor", type = String.class),
				@ColumnResult(name = "carecontexts_referencenumber", type = String.class),
				@ColumnResult(name = "file_path", type = String.class),
				@ColumnResult(name = "carecontexts_display", type = String.class),
				@ColumnResult(name = "report_type", type = String.class),
				@ColumnResult(name = "consultation_date", type = LocalDateTime.class),
				@ColumnResult(name = "clinic_name", type = String.class),
				@ColumnResult(name = "result_id",type = String.class)}) }

)
@Entity
public class RequestResultSetDtoNewMapping {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
}
