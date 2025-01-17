package com.dipl.abha.dto;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;

@SqlResultSetMapping(name = "PatientIdDtoMapping", classes = {
		@ConstructorResult(targetClass = com.dipl.abha.dto.PatientIdDto.class, columns = {
				@ColumnResult(name = "patient_id", type = String.class)
				 }) })
@Entity
public class PatientIdDtoMapping {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
}
