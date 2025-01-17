package com.dipl.abha.uhi.entities;

import javax.persistence.Column;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatUserModel {

	
	 @Id
	    @Column(name = "user_id")
	    private String userId;

	    @Column(name = "user_name")
	    private String userName;

	    @Column(name = "image", length = 50000)
	    private String image;
}
