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

import com.fasterxml.jackson.annotation.JsonFormat;
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
@Table(name = "call_back_api_response")
@XmlRootElement
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CallBackApiResponse {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id")
	@JsonProperty("id")
	private Long id;
	@Column(name = "request_id")
	@JsonProperty("request_id")
	private String requestId;
	@Column(name = "api_type")
	@JsonProperty("api_type")
	private String apiType;
	@Column(name = "response")
	@JsonProperty("response")
	@Type(type = "com.dipl.abha.util.JsonNodeUserType")
	private JsonNode response;
	@Column(name = "health_id")
	@JsonProperty("health_id")
	private String healthId;
	@Column(name = "request")
	@JsonProperty("request")
	@Type(type = "com.dipl.abha.util.JsonNodeUserType")
	private JsonNode request;
	@Column(name = "created_on", updatable = false)
	@JsonProperty("created_on")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@CreationTimestamp
	private LocalDateTime createdOn;
	@Basic(optional = false)
	@JsonProperty("is_active")
	@Column(name = "is_active")
	private Boolean isActive;
	@Basic(optional = false)
	@JsonProperty("is_deleted")
	@Column(name = "is_deleted")
	private Boolean isDeleted;
	@JsonProperty("concentid")
	@Column(name = "concentid")
	private String concentId;
	@JsonProperty("patient_ref_no")
	@Column(name = "patient_ref_no")
	private String patientRefNo;
	@JsonProperty("care_context_added_by")
	@Column(name = "care_context_added_by")
	private String careContextAddedBy;
	@JsonProperty("transactionId")
	@Column(name = "transaction_id")
	private String transactionId;
	@JsonProperty("interaction_id")
	@Column(name = "interaction_id")
	private Long interactionId;
}
