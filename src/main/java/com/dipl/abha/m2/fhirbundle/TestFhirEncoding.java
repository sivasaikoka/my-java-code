package com.dipl.abha.m2.fhirbundle;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


public class TestFhirEncoding {
	public static void main(String[] args) {
		
		ObjectMapper objMapper = new ObjectMapper();
		
		try {
			String content = new String(Files.readAllBytes(Paths.get("src/main/resources/fhirbundle.txt")));

			FhirBundle fhir = objMapper.readValue(content, FhirBundle.class);
//			fhir = objMapper.readValue(concent, FhirBundle.class);
			List<Entry> entry = fhir.getEntry();
			Entry entry1 =  entry.get(1);
			
			Resource resource = entry1.getResource();
			List<Name> name = resource.getName();
			name.get(0).setText("Hello");
			Entry entry2 = entry.get(2);
			Resource resource2 = entry2.getResource();
			resource2.setId("sdfsdljf");
			List<Name> name2 = resource2.getName();
			name2.get(0).setText("HELLO");
			
			Entry entry4 = entry.get(4);
			Resource resource4 = entry4.getResource();
			List<Content> content4 =resource4.getContent();
			content4.get(0).getAttachment().setData("jhfkjvhjdytfkufuguyguyfuykfvuv");
			String fhir1 = 	objMapper.writeValueAsString(fhir);
			System.out.println("JSON OBJECT OBJ MAPPER     "+fhir1);
			System.out.println("STRING     " + content);
			JsonNode jsonNode = objMapper.readTree(content);
			jsonNode.get("");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
