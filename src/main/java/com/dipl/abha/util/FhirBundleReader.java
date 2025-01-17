package com.dipl.abha.util;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hl7.fhir.r4.model.Appointment;
import org.hl7.fhir.r4.model.Binary;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.ServiceRequest;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.DataFormatException;

@Service
public class FhirBundleReader {

	public String reader(JsonNode decryptedDocument) {
		String fhir=decryptedDocument.toString();
		
		String resultJsonNode="";
		FhirContext ctx = FhirContext.forR4();

        ObjectMapper mapper=new ObjectMapper();
       
        List<Map<String, String>> extractedData = new ArrayList<>();

        try {
            // Parse the Bundle from the JSON string
        	 Bundle bundle = (Bundle) ctx.newJsonParser().parseResource(fhir);
            
            System.out.println("Bundle ID: " + bundle.getId());
            System.out.println("Bundle Type: " + bundle.getType());
            System.out.println("Last Updated: " + bundle.getMeta().getLastUpdated());

            // Iterate through the entries in the bundle
            for (Bundle.BundleEntryComponent entry : bundle.getEntry()) {
                Map<String, String> resourceData = new HashMap<>();
                
                // Check the resource type and extract necessary details
                if (entry.getResource() instanceof Patient) {
                    Patient patient = (Patient) entry.getResource();
                    String name = patient.getName().get(0).getText();
                    resourceData.put("ResourceType", "Patient");
                    resourceData.put("Name", name);
                    resourceData.put("Gender", patient.getGender().toCode());
                    resourceData.put("BirthDate", patient.getBirthDate() != null ? patient.getBirthDate().toString() : "N/A");

                } else if (entry.getResource() instanceof Practitioner) {
                    Practitioner practitioner = (Practitioner) entry.getResource();
                    String name = practitioner.getName().get(0).getText();
                    resourceData.put("ResourceType", "Practitioner");
                    resourceData.put("Name", name);

                } else if (entry.getResource() instanceof Organization) {
                    Organization organization = (Organization) entry.getResource();
                    resourceData.put("ResourceType", "Organization");
                    resourceData.put("Name", organization.getName());

                } else if (entry.getResource() instanceof Observation) {
                    Observation observation = (Observation) entry.getResource();
                    resourceData.put("ResourceType", "Observation");
                    resourceData.put("ObservationName", observation.getCode().getText());
                    resourceData.put("ObservationValue", observation.getValueQuantity().getValue().toString());
                    resourceData.put("ObservationUnits", observation.getValueQuantity().getUnit());

                } else if (entry.getResource() instanceof MedicationRequest) {
                    MedicationRequest medicationRequest = (MedicationRequest) entry.getResource();
                    resourceData.put("ResourceType", "MedicationRequest");
                    resourceData.put("MedicationName", medicationRequest.getMedicationCodeableConcept().getText());
                    resourceData.put("MedicationDosage", medicationRequest.getDosageInstruction().get(0).getText());

                } else if (entry.getResource() instanceof ServiceRequest) {
                    ServiceRequest serviceRequest = (ServiceRequest) entry.getResource();
                    resourceData.put("ResourceType", "Investigation");
                    resourceData.put("ServiceRequested", serviceRequest.getCode().getText());

                } else if (entry.getResource() instanceof Condition) {
                    Condition condition = (Condition) entry.getResource();
                    resourceData.put("ResourceType", "Chiefcomplaint");
                    resourceData.put("Condition", condition.getCode().getText());

                } else if (entry.getResource() instanceof Appointment) {
                    Appointment appointment = (Appointment) entry.getResource();
                    resourceData.put("ResourceType", "FollowUpDate");
                    resourceData.put("AppointmentDate", appointment.getStart() != null ? appointment.getStart().toString() : "N/A");
                }else if (entry.getResource() instanceof Binary) {
                    Binary binary = (Binary) entry.getResource(); // Use the resource from the entry
                    resourceData.put("ResourceType", "DocumentReference");
                    if (binary.getContent() != null) {
                        //String encodedData = Base64.getEncoder().encodeToString();
                        System.out.println(binary.getContent().toString());
                        System.out.println(binary.getContentAsBase64());
                        resourceData.put("BinaryData", binary.getContentAsBase64());
                    } else {
                        resourceData.put("BinaryData", "No Binary Content");
                    }
                }

                if (!resourceData.isEmpty()) {
                    extractedData.add(resourceData); // Store the extracted data
                }
                resultJsonNode=mapper.writeValueAsString(extractedData);
            }
        } catch (DataFormatException e) {
            System.err.println("Failed to parse JSON: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
		return resultJsonNode;
	}
  
}
