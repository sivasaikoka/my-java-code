package com.dipl.abha.util;


import java.util.Date;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Composition;
import org.hl7.fhir.r4.model.ContactPoint;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.Narrative;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.utilities.xhtml.XhtmlNode;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Service
public class NewFHIR {
	 @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    public static class BundleWrapper {
//        @JsonIgnore
        private Bundle bundle;

        public BundleWrapper(Bundle bundle) {
            this.bundle = bundle;
        }

        public Bundle getBundle() {
            return bundle;
        }

        public void setBundle(Bundle bundle) {
            this.bundle = bundle;
        }
    }

    public static void main(String[] args) throws JsonProcessingException {

        // Create a new Bundle
        Bundle bundle = new Bundle();
        bundle.setId("Prescription-example-06");
        Meta meta = new Meta().setVersionId("1").setLastUpdated(new Date())
                .addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/DocumentBundle");

        // Add a security label to the Meta component
        Coding security = new Coding().setSystem("http://terminology.hl7.org/CodeSystem/v3-Confidentiality").setCode("V")
                .setDisplay("very restricted");
        meta.addSecurity(security);

        // Set the Meta component to the Bundle
        bundle.setMeta(meta);
        bundle.setIdentifier(new Identifier().setSystem("http://hip.in").setValue("bc3c6c57-2053-4d0e-ac40-139ccccff645"));
        bundle.setType(Bundle.BundleType.DOCUMENT);
        bundle.setTimestamp(new Date());

        // Add Composition resource
        Composition composition = new Composition();
        composition.setId("f1ab8bba-a0ed-476a-a902-a1e08517020b");

        // Create a new Meta component
        Meta meta1 = new Meta().setVersionId("1").setLastUpdated(new Date())
                .addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/PrescriptionRecord");

        // Set the Meta component to the Composition
        composition.setMeta(meta1);

        // Set other properties
        composition.setLanguage("en-IN");

        // Set the Narrative text
        XhtmlNode div = new XhtmlNode();
        div.setValue(
                "<div xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en-IN\" lang=\"en-IN\"><p><b>Generated Narrative: Composition</b><a name=\"f1ab8bba-a0ed-476a-a902-a1e08517020b\"> </a></p><div style=\"display: inline-block; background-color: #d9e0e7; padding: 6px; margin: 4px; border: 1px solid #8da1b4; border-radius: 5px; line-height: 60%\"><p style=\"margin-bottom: 0px\">Resource Composition &quot;f1ab8bba-a0ed-476a-a902-a1e08517020b&quot; Version &quot;1&quot; Updated &quot;2020-07-09 15:32:26+0530&quot;  (Language &quot;en-IN&quot;) </p><p style=\"margin-bottom: 0px\">Profile: <a href=\"StructureDefinition-PrescriptionRecord.html\">PrescriptionRecord</a></p></div><p><b>identifier</b>: <code>https://ndhm.in/phr</code>/645bb0c3-ff7e-4123-bef5-3852a4784813</p><p><b>status</b>: final</p><p><b>type</b>: Prescription record <span style=\"background: LightGoldenRodYellow; margin: 4px; border: 1px solid khaki\"> (<a href=\"https://browser.ihtsdotools.org/\">SNOMED CT</a>#440545006)</span></p><p><b>date</b>: 2017-05-27 11:46:09+0530</p><p><b>author</b>: <a href=\"#Practitioner_dc36f7c9-7ea5-4984-a02e-7102c215db17\">See above (urn:uuid:dc36f7c9-7ea5-4984-a02e-7102c215db17: Practitioner)</a></p><p><b>title</b>: Prescription record</p></div>");
        Narrative narrative = new Narrative().setStatus(Narrative.NarrativeStatus.GENERATED).setDiv(div);
        composition.setText(narrative);

        // Set the Identifier
        composition.setIdentifier(
                new Identifier().setSystem("https://ndhm.in/phr").setValue("645bb0c3-ff7e-4123-bef5-3852a4784813"));

        // Set the Status
        composition.setStatus(Composition.CompositionStatus.FINAL);

        // Set the Type
        composition.setType(new CodeableConcept().addCoding(new Coding().setSystem("http://snomed.info/sct")
                .setCode("440545006").setDisplay("Prescription record")));

        // Set the Date
        composition.setDate(new Date());

        // Add an Author
        composition.addAuthor(new Reference("urn:uuid:dc36f7c9-7ea5-4984-a02e-7102c215db17").setDisplay("Practitioner"));

        // Set the Title
        composition.setTitle("Prescription record");
        // Add sections, entries, etc. to composition

        // Create a new Patient
        Patient patient = new Patient();
        patient.setId("f8d9e19e-5598-428c-ae03-b1cd44968b41");

        // Create a new Meta component
        Meta meta2 = new Meta().setVersionId("1").setLastUpdated(new Date())
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
                + "</div>" + "<p><b>identifier</b>: Medical record number/22-7225-4829-5255</p>"
                + "<p><b>name</b>: ABC</p>" + "<p><b>telecom</b>: <a href=\"tel:+919818512600\">+919818512600</a></p>"
                + "<p><b>gender</b>: male</p>" + "<p><b>birthDate</b>: 1981-01-12</p>" + "</div>";
        div1.setValue(xhtmlString);
        Narrative narrative1 = new Narrative().setStatus(Narrative.NarrativeStatus.GENERATED).setDiv(div1);
        patient.setText(narrative1);

        // Set the Identifier
        patient.addIdentifier(new Identifier().setSystem("https://ndhm.in/phr").setValue("22-7225-4829-5255")
                .setType(new CodeableConcept().addCoding(new Coding().setSystem("http://terminology.hl7.org/CodeSystem/v2-0203")
                        .setCode("MR").setDisplay("Medical record number"))));

        // Set the Name
        patient.addName(new HumanName().setText("ABC").addGiven("ABC"));

        // Set the Telecom
        patient.addTelecom(
                new ContactPoint().setSystem(ContactPoint.ContactPointSystem.PHONE).setValue("+919818512600"));

        // Set the Gender
        patient.setGender(Enumerations.AdministrativeGender.MALE);

        // Set the Birth Date
        patient.setBirthDate(new Date());

        // Add the Composition and Patient to the Bundle
        bundle.addEntry(new BundleEntryComponent().setResource(composition));
        bundle.addEntry(new BundleEntryComponent().setResource(patient));

        BundleWrapper bundleWrapper = new BundleWrapper(bundle);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        // Serialize BundleWrapper to JSON
        String bundleJson = objectMapper.writeValueAsString(bundleWrapper);

        System.out.println(bundleJson);
    }
}