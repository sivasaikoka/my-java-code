package com.dipl.abha.dto;
import java.time.LocalDateTime;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;

@SqlResultSetMapping(name = "AbhaRegistrationDtoMapping", classes = {
		@ConstructorResult(targetClass = com.dipl.abha.dto.NotifyResultSetDto.class, columns = {
				@ColumnResult(name = "id", type = Long.class),
				@ColumnResult(name = "tenant_id", type = Integer.class),
				@ColumnResult(name = "api_type", type = String.class),
				@ColumnResult(name = "abha_address", type = String.class),
				@ColumnResult(name = "abha_no", type = String.class),
				@ColumnResult(name = "abha_profile", type = String.class),
				@ColumnResult(name = "patient_id", type = Long.class),
				@ColumnResult(name = "gender", type = String.class),
				@ColumnResult(name = "year_of_birth", type = Long.class),
				@ColumnResult(name = "mobile_no", type = String.class),
				@ColumnResult(name = "abha_card_path", type = String.class),
				@ColumnResult(name = "is_abha_linked", type = String.class),
				@ColumnResult(name = "created_on", type = LocalDateTime.class),
				@ColumnResult(name = "mrn_no", type = String.class),
				@ColumnResult(name = "full_name", type = String.class)}) })
@Entity
public class AbhaRegistrationDtoMapping {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
}
