package com.dipl.abha.patient.entities;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@MappedSuperclass
public class BaseEntity {

//	@Id
//	@Column(name = "id", nullable = false, unique = true)
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	public long id;

	@Column(updatable = false, name = "created_date")
	@CreationTimestamp
	private LocalDateTime createdDate = LocalDateTime.now();

	@Column(insertable = false, name = "modified_date")
	@UpdateTimestamp
	private LocalDateTime modifiedDate;

	@Column(updatable = false, name = "created_by")
	private Long createdBy;

	@Column(insertable = false, name = "modified_by")
	private Long modifiedBy;

	private Integer status;

}
