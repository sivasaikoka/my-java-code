package com.dipl.abha.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

@Data
@MappedSuperclass
public class BaseEntity {
	

	@Column(name = "updated_on")
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date updatedOn;
	@Column(name = "created_on", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date createdOn;
	@Column(name = "updated_by")
	private Long updatedBy;
	@Column(name = "created_by")
	private Long createdBy;
	@Column(name = "is_deleted")
	private Boolean isDeleted = false;

}
