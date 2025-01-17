package com.dipl.abha.uhi.entities;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessagesEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "content_id")
    private String contentId;

    @Column(name = "sender")
    private String sender;

    @Column(name = "receiver")
    private String receiver;

    @Column(name = "time")
    private LocalDateTime time;

    @Column(name = "consumer_url")
    private String consumerUrl;

    @Column(name = "provider_url")
    private String providerUrl;

    @Column(name = "content_value", length = 50000)
    private String contentValue;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "content_url", length = 50000)
    private String contentUrl;

}
