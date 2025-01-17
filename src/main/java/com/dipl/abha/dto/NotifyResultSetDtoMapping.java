package com.dipl.abha.dto;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;

@SqlResultSetMapping(name = "NotifyResultSetDtoMapping", classes = {
		@ConstructorResult(targetClass = com.dipl.abha.dto.NotifyResultSetDto.class, columns = {
				@ColumnResult(name = "patient_referencenumber", type = String.class),
				@ColumnResult(name = "patient_display", type = String.class),
				@ColumnResult(name = "patient_id", type = String.class),
				@ColumnResult(name = "result_no", type = String.class),
				@ColumnResult(name = "carecontexts_referencenumber", type = String.class),
				@ColumnResult(name = "carecontexts_display", type = String.class), }) })
@Entity
public class NotifyResultSetDtoMapping {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
}