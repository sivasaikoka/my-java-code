package com.dipl.abha.util;
import java.util.Date;

import org.hl7.fhir.r4.model.Binary;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Composition;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.ContactPoint;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.Narrative;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.utilities.xhtml.XhtmlNode;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;

@Service
public class FHIRBundleBuilderNew {
	
	public static void main(String[] args) throws JsonProcessingException {
		
//    public String fhirBundle(byte[] file) {
        Bundle bundle = new Bundle();
//        bundle.setId("Prescription-example-06");
        Meta meta = new Meta()
                .setVersionId("1")
                .setLastUpdated(new Date())
                .addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/DocumentBundle");

            // Add a security label to the Meta component
            Coding security = new Coding()
                .setSystem("http://terminology.hl7.org/CodeSystem/v3-Confidentiality")
                .setCode("V")
                .setDisplay("very restricted");
            meta.addSecurity(security);

            // Set the Meta component to the Bundle
            bundle.setMeta(meta);
        bundle.setIdentifier(new Identifier()
            .setSystem("http://hip.in")
            .setValue("bc3c6c57-2053-4d0e-ac40-139ccccff645"));
        bundle.setType(Bundle.BundleType.DOCUMENT);
        bundle.setTimestamp(new Date());

        // Add Composition resource
        Composition composition = new Composition();
//        composition.setId("f1ab8bba-a0ed-476a-a902-a1e08517020b");

        // Create a new Meta component
        Meta meta1 = new Meta()
            .setVersionId("1")
            .setLastUpdated(new Date())
            .addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/PrescriptionRecord");

        // Set the Meta component to the Composition
        composition.setMeta(meta1);

        // Set other properties
        composition.setLanguage("en-IN");

        // Set the Narrative text
        XhtmlNode div = new XhtmlNode();
        div.setValue("<div xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en-IN\" lang=\"en-IN\"><p><b>Generated Narrative: Composition</b><a name=\"f1ab8bba-a0ed-476a-a902-a1e08517020b\"> </a></p><div style=\"display: inline-block; background-color: #d9e0e7; padding: 6px; margin: 4px; border: 1px solid #8da1b4; border-radius: 5px; line-height: 60%\"><p style=\"margin-bottom: 0px\">Resource Composition &quot;f1ab8bba-a0ed-476a-a902-a1e08517020b&quot; Version &quot;1&quot; Updated &quot;2020-07-09 15:32:26+0530&quot;  (Language &quot;en-IN&quot;) </p><p style=\"margin-bottom: 0px\">Profile: <a href=\"StructureDefinition-PrescriptionRecord.html\">PrescriptionRecord</a></p></div><p><b>identifier</b>: <code>https://ndhm.in/phr</code>/645bb0c3-ff7e-4123-bef5-3852a4784813</p><p><b>status</b>: final</p><p><b>type</b>: Prescription record <span style=\"background: LightGoldenRodYellow; margin: 4px; border: 1px solid khaki\"> (<a href=\"https://browser.ihtsdotools.org/\">SNOMED CT</a>#440545006)</span></p><p><b>date</b>: 2017-05-27 11:46:09+0530</p><p><b>author</b>: <a href=\"#Practitioner_dc36f7c9-7ea5-4984-a02e-7102c215db17\">See above (Practitioner:dc36f7c9-7ea5-4984-a02e-7102c215db17: Practitioner)</a></p><p><b>title</b>: Prescription record</p></div>");
        Narrative narrative = new Narrative()
            .setStatus(Narrative.NarrativeStatus.GENERATED)
            .setDiv(div);
        composition.setText(narrative);

        // Set the Identifier
        composition.setIdentifier(new Identifier()
            .setSystem("https://ndhm.in/phr")
            .setValue("645bb0c3-ff7e-4123-bef5-3852a4784813"));

        // Set the Status
        composition.setStatus(Composition.CompositionStatus.FINAL);

        // Set the Type
        composition.setType(new CodeableConcept()
            .addCoding(new Coding()
                .setSystem("http://snomed.info/sct")
                .setCode("440545006")
                .setDisplay("Prescription record")));

        // Set the Date
        composition.setDate(new Date());

        // Add an Author
        composition.addAuthor(new Reference("Practitioner/dc36f7c9-7ea5-4984-a02e-7102c215db17")
            .setDisplay("Practitioner"));

        // Set the Title
        composition.setTitle("Prescription record");
        // Add sections, entries, etc. to composition

        // Create a new Patient
        Patient patient = new Patient();
//        patient.setId("f8d9e19e-5598-428c-ae03-b1cd44968b41");

        // Create a new Meta component
        Meta meta2 = new Meta()
            .setVersionId("1")
            .setLastUpdated(new Date())
            .addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/Patient");

        // Set the Meta component to the Patient
        patient.setMeta(meta1);

        // Set the Narrative text
        
        XhtmlNode div1 = new XhtmlNode();
        
        String xhtmlString = "<div xmlns=\"http://www.w3.org/1999/xhtml\">"
        	    + "<p><b>Generated Narrative: Patient</b><a name=\"f8d9e19e-5598-428c-ae03-b1cd44968b41\"> </a></p>"
        	    + "<div style=\"display: inline-block; background-color: #d9e0e7; padding: 6px; margin: 4px; border: 1px solid #8da1b4; border-radius: 5px; line-height: 60%\">"
        	    + "<p style=\"margin-bottom: 0px\">Resource Patient &quot;f8d9e19e-5598-428c-ae03-b1cd44968b41&quot; Version &quot;1&quot; Updated &quot;2020-07-09 14:58:58+0530&quot; </p>"
        	    + "<p style=\"margin-bottom: 0px\">Profile: <a href=\"StructureDefinition-Patient.html\">Patient</a></p>"
        	    + "</div>"
        	    + "<p><b>identifier</b>: Medical record number/22-7225-4829-5255</p>"
        	    + "<p><b>name</b>: ABC</p>"
        	    + "<p><b>telecom</b>: <a href=\"tel:+919818512600\">+919818512600</a></p>"
        	    + "<p><b>gender</b>: male</p>"
        	    + "<p><b>birthDate</b>: 1981-01-12</p>"
        	    + "</div>";
        div1.setValue(xhtmlString);
        Narrative narrative1 = new Narrative()
            .setStatus(Narrative.NarrativeStatus.GENERATED)
            .setDiv(div1);
        patient.setText(narrative1);

        // Set other properties
        patient.addIdentifier(new Identifier()
            .setSystem("https://healthid.ndhm.gov.in")
            .setValue("22-7225-4829-5255"));
        patient.addName(new HumanName().setText("ABC"));
        patient.addTelecom(new ContactPoint()
            .setSystem(ContactPoint.ContactPointSystem.PHONE)
            .setValue("+919818512600")
            .setUse(ContactPoint.ContactPointUse.HOME));
        patient.setGender(Enumerations.AdministrativeGender.MALE);
        patient.setBirthDate(java.sql.Date.valueOf("1981-01-12"));

        // Add Practitioner resource
     // Create a new Practitioner
        Practitioner practitioner = new Practitioner();
//        practitioner.setId("dc36f7c9-7ea5-4984-a02e-7102c215db17");

        // Create a new Meta component
        Meta meta3 = new Meta()
            .setVersionId("1")
            .setLastUpdated(new Date())
            .addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/Practitioner");

        // Set the Meta component to the Practitioner
        practitioner.setMeta(meta3);

        // Set the Narrative text
        XhtmlNode div2 = new XhtmlNode();
        String xhtmlString1 = "<div xmlns=\"http://www.w3.org/1999/xhtml\">"
        	    + "<p><b>Generated Narrative: Patient</b><a name=\"f8d9e19e-5598-428c-ae03-b1cd44968b41\"> </a></p>"
        	    + "<div style=\"display: inline-block; background-color: #d9e0e7; padding: 6px; margin: 4px; border: 1px solid #8da1b4; border-radius: 5px; line-height: 60%\">"
        	    + "<p style=\"margin-bottom: 0px\">Resource Patient &quot;f8d9e19e-5598-428c-ae03-b1cd44968b41&quot; Version &quot;1&quot; Updated &quot;2020-07-09 14:58:58+0530&quot; </p>"
        	    + "<p style=\"margin-bottom: 0px\">Profile: <a href=\"StructureDefinition-Patient.html\">Patient</a></p>"
        	    + "</div>"
        	    + "<p><b>identifier</b>: Medical record number/22-7225-4829-5255</p>"
        	    + "<p><b>name</b>: ABC</p>"
        	    + "<p><b>telecom</b>: <a href=\"tel:+919818512600\">+919818512600</a></p>"
        	    + "<p><b>gender</b>: male</p>"
        	    + "<p><b>birthDate</b>: 1981-01-12</p>"
        	    + "</div>";        
        div.setValue(xhtmlString1);
        Narrative narrative2 = new Narrative()
            .setStatus(Narrative.NarrativeStatus.GENERATED)
            .setDiv(div2);
        practitioner.setText(narrative2);

        // Set other properties
        practitioner.addIdentifier(new Identifier()
            .setSystem("https://doctor.ndhm.gov.in")
            .setValue("21-1521-3828-3227"));
        practitioner.addName(new HumanName().setText("Dr. DEF"));

        // Add MedicationRequest resources
        MedicationRequest medRequest1 = new MedicationRequest();
//        medRequest1.setId("bceda2f2-484f-45f3-8bf1-f8bcd7631f50");

        // Create a new Meta component
        Meta meta4 = new Meta()
            .addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/MedicationRequest");

        // Set the Meta component to the MedicationRequest
        medRequest1.setMeta(meta4);

        // Set the status and intent
        medRequest1.setStatus(MedicationRequest.MedicationRequestStatus.ACTIVE);
        medRequest1.setIntent(MedicationRequest.MedicationRequestIntent.ORDER);

        // Set the medication
        CodeableConcept medicationCodeableConcept = new CodeableConcept()
            .addCoding(new Coding()
                .setSystem("http://snomed.info/sct")
                .setCode("1145423002")
                .setDisplay("Azithromycin 250 mg oral tablet"));
        medRequest1.setMedication(medicationCodeableConcept); // Use setMedication() instead of setMedicationCodeableConcept()

        // Set subject
        medRequest1.setSubject(new Reference("MedicationRequest/f8d9e19e-5598-428c-ae03-b1cd44968b41").setDisplay("ABC"));
        medRequest1.setAuthoredOn(new Date());
        medRequest1.setRequester(new Reference("MedicationRequest/dc36f7c9-7ea5-4984-a02e-7102c215db17").setDisplay("Dr. DEF"));

        // Add reason code and reference
        medRequest1.addReasonCode(new CodeableConcept()
            .addCoding(new Coding()
                .setSystem("http://snomed.info/sct")
                .setCode("11840006")
                .setDisplay("Traveler's diarrhea")));
        medRequest1.addReasonReference(new Reference("MedicationRequest/b650b103-5eee-43c3-b53a-228872e77488").setDisplay("Condition"));

        // Add dosage instructions

        MedicationRequest medRequest2 = new MedicationRequest();
//        medRequest2.setId("c74e22c8-55b6-49c1-a891-ca5e3cd3a99c");

        // Create a new Meta component
        Meta meta5 = new Meta()
            .addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/MedicationRequest");

        // Set the Meta component to the MedicationRequest
        medRequest2.setMeta(meta5);

        // Set the status and intent
        medRequest2.setStatus(MedicationRequest.MedicationRequestStatus.ACTIVE);
        medRequest2.setIntent(MedicationRequest.MedicationRequestIntent.ORDER);

        // Set the medication
        CodeableConcept medicationCodeableConcept1 = new CodeableConcept()
            .setText("Paracetamol 500mg Oral Tab"); // Ensure correct spelling of Paracetamol
        medRequest2.setMedication(medicationCodeableConcept1); // Use setMedication() instead of setMedicationCodeableConcept()

        // Set subject
        medRequest2.setSubject(new Reference("MedicationRequest/f8d9e19e-5598-428c-ae03-b1cd44968b41").setDisplay("ABC"));
        
        // Set authored on date
        medRequest2.setAuthoredOn(new Date()); // Use Date object, or convert string to date as needed

        // Set requester
        medRequest2.setRequester(new Reference("MedicationRequest/dc36f7c9-7ea5-4984-a02e-7102c215db17").setDisplay("Dr. DEF"));

        // Add reason code and reference
        medRequest2.addReasonCode(new CodeableConcept()
            .addCoding(new Coding()
                .setSystem("http://snomed.info/sct")
                .setCode("789400009")
                .setDisplay("Ross River disease")));
        medRequest2.addReasonReference(new Reference("MedicationRequest/b650b103-5eee-43c3-b53a-228872e77488").setDisplay("Condition"));

        // Add dosage instructions

        // Add Condition resource
        // Create a new Condition
        Condition condition = new Condition();
//        condition.setId("b650b103-5eee-43c3-b53a-228872e77488");

        // Create a new Meta component
        Meta meta6 = new Meta()
            .setVersionId("1")
            .setLastUpdated(new Date())
            .addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/Condition");

        // Set the Meta component to the Condition
        condition.setMeta(meta);

        // Set the Narrative text
        XhtmlNode div4 = new XhtmlNode();
        String xhtmlString2 = "<div xmlns=\"http://www.w3.org/1999/xhtml\">"
        	    + "<p><b>Generated Narrative: MedicationRequest</b><a name=\"bceda2f2-484f-45f3-8bf1-f8bcd7631f50\"> </a></p>"
        	    + "<div style=\"display: inline-block; background-color: #d9e0e7; padding: 6px; margin: 4px; border: 1px solid #8da1b4; border-radius: 5px; line-height: 60%\">"
        	    + "<p style=\"margin-bottom: 0px\">Resource MedicationRequest &quot;bceda2f2-484f-45f3-8bf1-f8bcd7631f50&quot; </p>"
        	    + "<p style=\"margin-bottom: 0px\">Profile: <a href=\"StructureDefinition-MedicationRequest.html\">MedicationRequest</a></p>"
        	    + "</div>"
        	    + "<p><b>status</b>: active</p>"
        	    + "<p><b>intent</b>: order</p>"
        	    + "<p><b>medication</b>: Azithromycin 250 mg oral tablet <span style=\"background: LightGoldenRodYellow; margin: 4px; border: 1px solid khaki\"> (<a href=\"https://browser.ihtsdotools.org/\">SNOMED CT</a>#1145423002)</span></p>"
        	    + "<p><b>subject</b>: <a href=\"#Patient_f8d9e19e-5598-428c-ae03-b1cd44968b41\">See above (MedicationRequest/f8d9e19e-5598-428c-ae03-b1cd44968b41: ABC)</a></p>"
        	    + "<p><b>authoredOn</b>: 2020-07-09</p>"
        	    + "<p><b>requester</b>: <a href=\"#Practitioner_dc36f7c9-7ea5-4984-a02e-7102c215db17\">See above (MedicationRequest/dc36f7c9-7ea5-4984-a02e-7102c215db17: Dr. DEF)</a></p>"
        	    + "<p><b>reasonCode</b>: Traveler's diarrhea <span style=\"background: LightGoldenRodYellow; margin: 4px; border: 1px solid khaki\"> (<a href=\"https://browser.ihtsdotools.org/\">SNOMED CT</a>#11840006)</span></p>"
        	    + "<p><b>reasonReference</b>: <a href=\"#Condition_b650b103-5eee-43c3-b53a-228872e77488\">See above (MedicationRequest/b650b103-5eee-43c3-b53a-228872e77488: Condition)</a></p>"
        	    + "<h3>DosageInstructions</h3>"
        	    + "<table class=\"grid\">"
        	    + "<tr><td style=\"display: none\">-</td><td><b>Text</b></td><td><b>AdditionalInstruction</b></td><td><b>Timing</b></td><td><b>Route</b></td><td><b>Method</b></td></tr>"
        	    + "<tr><td style=\"display: none\">*</td><td>One tablet at once</td><td>With or after food <span style=\"background: LightGoldenRodYellow; margin: 4px; border: 1px solid khaki\"> (<a href=\"https://browser.ihtsdotools.org/\">SNOMED CT</a>#311504000)</span></td><td>Once per 1 days</td><td>Oral Route <span style=\"background: LightGoldenRodYellow; margin: 4px; border: 1px solid khaki\"> (<a href=\"https://browser.ihtsdotools.org/\">SNOMED CT</a>#26643006)</span></td><td>Swallow <span style=\"background: LightGoldenRodYellow; margin: 4px; border: 1px solid khaki\"> (<a href=\"https://browser.ihtsdotools.org/\">SNOMED CT</a>#421521009)</span></td></tr>"
        	    + "</table></div>";
        div.setValue(xhtmlString2);
        Narrative narrative4 = new Narrative()
            .setStatus(Narrative.NarrativeStatus.GENERATED)
            .setDiv(div4);
        condition.setText(narrative4);

        // Set other properties
        condition.addIdentifier(new Identifier()
            .setSystem("https://condition.ndhm.gov.in")
            .setValue("945f5c5e-82c6-4359-9d2b-7c30ecb05143"));
        condition.setSubject(new Reference("MedicationRequest/f8d9e19e-5598-428c-ae03-b1cd44968b41").setDisplay("ABC"));
        condition.setCode(new CodeableConcept()
            .addCoding(new Coding()
                .setSystem("http://snomed.info/sct")
                .setCode("11840006")
                .setDisplay("Traveler's diarrhea")));

        // Set the verification status
//        condition.setVerificationStatus(Condition.ConditionVerificationStatus.CONFIRMED); // Use the correct enumeration

        CodeableConcept verificationStatus = new CodeableConcept()
                .addCoding(new Coding()
                    .setSystem("http://hl7.org/fhir/condition-ver-status")
                    .setCode("confirmed") // Use the appropriate code here
                    .setDisplay("Confirmed"));
        
        condition.setVerificationStatus(verificationStatus);
     
        // Add Binary resource
        Binary binary = new Binary();
//        binary.setId("0de317eb-bd58-438c-9b92-22b8307c934d");
        binary.setContentType("application/pdf");
//        binary.setData(Base64.getEncoder().encode("".getBytes()));

        // Add resources to bundle
        bundle.addEntry(new BundleEntryComponent().setFullUrl("Composition/" + composition.getId()).setResource(composition));
        bundle.addEntry(new BundleEntryComponent().setFullUrl("Patient/" + patient.getId()).setResource(patient));
        bundle.addEntry(new BundleEntryComponent().setFullUrl("Practitioner/" + practitioner.getId()).setResource(practitioner));
        bundle.addEntry(new BundleEntryComponent().setFullUrl("MedicationRequest/" + medRequest1.getId()).setResource(medRequest1));
        bundle.addEntry(new BundleEntryComponent().setFullUrl("MedicationRequest/" + medRequest2.getId()).setResource(medRequest2));
        bundle.addEntry(new BundleEntryComponent().setFullUrl("Condition/" + condition.getId()).setResource(condition));
        bundle.addEntry(new BundleEntryComponent().setFullUrl("Binary/" + binary.getId()).setResource(binary));

//        JSONObject json = new JSONObject(bundle);
//        
//        System.out.println(json+"");
        
//        BundleWrapper bundleWrapper = new BundleWrapper(bundle);
//        
//        // Print or use the bundle
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT); // For pretty printing

        try {
            String jsonString = objectMapper.writeValueAsString(bundle);
            System.out.println(jsonString);
            IParser jsonParser = FhirContext.forR4().newJsonParser();
            jsonParser.setPrettyPrint(true);
            String bundleJson = jsonParser.encodeResourceToString(bundle);
            System.out.println(bundleJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
