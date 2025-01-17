package com.dipl.abha.entities;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "abdm_request_mapping")
@XmlRootElement
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ABDMRequestMapping {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "request_id")
	@JsonProperty("request_id")
	private String requestId;
	
	@JsonProperty("consent_id")
	@Column(name = "consent_id")
	private String concentId;
	
	@JsonProperty("request_url")
	@Column(name = "request_url")
	private String url;
	
	@Column(name = "created_date", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date createdOn;
	
}
