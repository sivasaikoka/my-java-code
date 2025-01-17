package com.dipl.abha.entities;

import java.time.LocalDateTime;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "abha_logs")
@XmlRootElement
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ABHAlogs {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id")
	@JsonProperty("id")
	private Long id;
	@Column(name = "beneficiary_id")
	@JsonProperty("beneficiary_id")
	private Long beneficiaryId;
	@Column(name = "dependent_id")
	@JsonProperty("dependent_id")
	private Long dependentId;
	@Column(name = "health_id")
	@JsonProperty("health_id")
	private String healthId;
	@Column(name = "health_id_number")
	@JsonProperty("health_id_number")
	private String healthIdNumber;
	@Column(name = "is_deleted")
	@JsonProperty("is_deleted")
	private Boolean isDeleted;
	@Column(name = "is_active")
	@JsonProperty("is_active")
	private Boolean isActive;
	@CreationTimestamp
	@Column(name = "created_on", updatable = false)
	@JsonProperty("createdon")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime createdOn;
	@UpdateTimestamp
	@Column(name = "modified_on")
	@JsonProperty("modifiedon")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime modifiedOn;
	@Column(name = "ndhm_response")
	@JsonProperty("ndhm_response")
	@Type(type = "com.dipl.abha.util.JsonNodeUserType")
	private JsonNode ndhmResponse;
	@JsonIgnore
	@Column(name = "abha_status_id")
	@JsonProperty("abha_status_id")
	private Integer abhaStatusId;
	@JsonIgnore
	@Column(name = "encrypted_health_card")
	@JsonProperty("abha_status_id")
	private String pngBytes;
	@Column(name = "health_card_path")
	@JsonProperty("health_card_path")
	private String healthCardPath;
}
