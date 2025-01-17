package com.dipl.abha.patient.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ramchandra K
 *
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "centers")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Center  implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "center_id")
	private Long id;

	@Size(max = 100, message = "Description cannot be greater than 100")
	@Column(name = "center_name")
	private String centerName;

	private String centeralized;

	@Min(value = 1 , message = "Please provide Lab Center")
	@Column(name = "central_lab_center_id")
	private Long centralLabCenterId;

	@Size(max = 1, message = "Centralized cannot be greater than 1")
	private String centralized;

	@Size(max = 4, message = "Code cannot be greater than 4")
	private String code;

	@Size(max = 100, message = "Contact person cannot be greater than 100")
	private String contactperson;

	@Size(max = 12, message = "Contact person number cannot be greater than 12")
	@Column(name = "contactperson_phone")
	private String contactpersonPhone;

	@Column(name = "org_id")
	private Long orgId;


}