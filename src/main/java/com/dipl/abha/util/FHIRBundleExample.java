package com.dipl.abha.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

import org.hl7.fhir.r4.model.Binary;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.Bundle.BundleType;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Composition;
import org.hl7.fhir.r4.model.ContactPoint;
import org.hl7.fhir.r4.model.Dosage;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Identifier.IdentifierUse;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.Narrative;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.Quantity;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Timing;
import org.springframework.stereotype.Service;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;

@Service
public class FHIRBundleExample {

	public String fhirBundle(String docName, String patientName, String visitDate, String fileType,
			String interactionId, String docRef, String fileTypeNew, byte[] file, String medicationString,
			String medDirectionString, String chiefComplains) throws ParseException {
//    	public static void main(String[] args) {
		String visitDate1 = visitDate.replace("T", " ");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = dateFormat.parse(visitDate1);

		// Create the bundle
		Bundle bundle = new Bundle();
		bundle.setId(interactionId);
		bundle.setType(BundleType.DOCUMENT);
		bundle.setTimestamp(date);

		// Meta
		Meta bundleMeta = new Meta();
		bundleMeta.setVersionId("1");
		bundleMeta.setLastUpdated(date);
		bundleMeta.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/DocumentBundle");
		bundleMeta.addSecurity().setSystem("http://terminology.hl7.org/CodeSystem/v3-Confidentiality").setCode("V")
				.setDisplay("very restricted");
		bundle.setMeta(bundleMeta);

		// Identifier
		Identifier bundleIdentifier = new Identifier();
		bundleIdentifier.setSystem("http://hip.in");
		bundleIdentifier.setValue("bc3c6c57-2053-4d0e-ac40-139ccccff645");
		bundle.setIdentifier(bundleIdentifier);

		// Composition resource
		Composition composition = new Composition();
		composition.setId("f1ab8bba-a0ed-476a-a902-a1e08517020b");
		Meta compositionMeta = new Meta();
		compositionMeta.setVersionId("1");
		compositionMeta.setLastUpdated(new Date());
		compositionMeta.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/PrescriptionRecord");
		composition.setMeta(compositionMeta);
		composition.setLanguage("en-IN");
		composition.getText().setStatus(Narrative.NarrativeStatus.GENERATED);

		composition.getText()
				.setDivAsString("<div xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en-IN\" lang=\"en-IN\">"
						+ "<p><b>Generated Narrative: Composition</b>"
						+ "<a name=\"f1ab8bba-a0ed-476a-a902-a1e08517020b\"> </a>"
						+ "</p><div style=\"display: inline-block; background-color: #d9e0e7; padding: 6px; margin: 4px; border: 1px solid #8da1b4; border-radius: 5px; line-height: 60%\">"
						+ "<p style=\"margin-bottom: 0px\">Resource Composition &quot;f1ab8bba-a0ed-476a-a902-a1e08517020b&quot; Version &quot;1&quot; Updated &quot;2020-07-09 15:32:26+0530&quot;  (Language &quot;en-IN&quot;) </p>"
						+ "<p style=\"margin-bottom: 0px\">Profile: <a href=\"StructureDefinition-PrescriptionRecord.html\">PrescriptionRecord</a></p></div>"
						+ "<p><b>identifier</b>: <code>https://ndhm.in/phr</code>/645bb0c3-ff7e-4123-bef5-3852a4784813</p>"
						+ "<p><b>status</b>: final</p>"
						+ "<p><b>type</b>: Prescription record <span style=\"background: LightGoldenRodYellow; margin: 4px; border: 1px solid khaki\"> (<a href=\"https://browser.ihtsdotools.org/\">SNOMED CT</a>#440545006)</span></p>"
						+ "<p><b>date</b>: "+visitDate1.toString()+"</p><p><b>author</b>: <a href=\"#Practitioner_dc36f7c9-7ea5-4984-a02e-7102c215db17\">See above (Practitioner/dc36f7c9-7ea5-4984-a02e-7102c215db17: Practitioner)</a></p>"
						+ "<p><b>title</b>: Prescription record</p></div>");

		// Composition Identifier
		Identifier compositionIdentifier = new Identifier();
		compositionIdentifier.setSystem("https://ndhm.in/phr");
		compositionIdentifier.setValue("645bb0c3-ff7e-4123-bef5-3852a4784813");
		composition.setIdentifier(compositionIdentifier);

		composition.setStatus(Composition.CompositionStatus.FINAL);

		CodeableConcept type = new CodeableConcept();
		type.addCoding().setSystem("http://snomed.info/sct").setCode("440545006").setDisplay("Prescription record");
		type.setText("Prescription record");
		composition.setType(type);

		Reference subject = new Reference("Patient/f8d9e19e-5598-428c-ae03-b1cd44968b41");
		subject.setDisplay("Patient");
		composition.setSubject(subject);

		composition.setDate(new Date());

		Reference author = new Reference("Practitioner/dc36f7c9-7ea5-4984-a02e-7102c215db17");
		author.setDisplay(docName);
		composition.addAuthor(author);

		composition.setTitle(fileType);

		// Composition Section
		Composition.SectionComponent section = new Composition.SectionComponent();
		section.setTitle(fileTypeNew);
		CodeableConcept sectionCode = new CodeableConcept();
		sectionCode.addCoding().setSystem("http://snomed.info/sct").setCode("440545006").setDisplay(fileType);
		section.setCode(sectionCode);

//        section.addEntry(new Reference("MedicationRequest/bceda2f2-484f-45f3-8bf1-f8bcd7631f50"));
//        section.addEntry(new Reference("MedicationRequest/c74e22c8-55b6-49c1-a891-ca5e3cd3a99c"));
		section.addEntry(new Reference("Binary/2044f347-c58c-4237-b7f4-8c713b79f471"));

		composition.addSection(section);

		// Bundle Entry for Composition
		BundleEntryComponent entryComposition = new BundleEntryComponent();
		entryComposition.setFullUrl("Composition/f1ab8bba-a0ed-476a-a902-a1e08517020b");
		entryComposition.setResource(composition);
		bundle.addEntry(entryComposition);

		// Patient resource
		Patient patient = new Patient();
		patient.setId("f8d9e19e-5598-428c-ae03-b1cd44968b41");
		Meta patientMeta = new Meta();
		patientMeta.setVersionId("1");
		patientMeta.setLastUpdated(new Date());
		patientMeta.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/Patient");
		patient.setMeta(patientMeta);

		patient.getText().setStatus(Narrative.NarrativeStatus.GENERATED);
//        patient.getText().setDivAsString("<div xmlns=\\\"http://www.w3.org/1999/xhtml\\\"><p><b>Generated Narrative: Patient</b><a name=\\\"f8d9e19e-5598-428c-ae03-b1cd44968b41\\\"> </a></p><div style=\\\"display: inline-block; background-color: #d9e0e7; padding: 6px; margin: 4px; border: 1px solid #8da1b4; border-radius: 5px; line-height: 60%\\\"><p style=\\\"margin-bottom: 0px\\\">Resource Patient &quot;f8d9e19e-5598-428c-ae03-b1cd44968b41&quot; Version &quot;1&quot; Updated &quot;2020-07-09 14:58:58+0530&quot; </p><p style=\\\"margin-bottom: 0px\\\">Profile: <a href=\\\"StructureDefinition-Patient.html\\\">Patient</a></p></div><p><b>identifier</b>: Medical record number/22-7225-4829-5255</p><p><b>name</b>: ABC</p><p><b>telecom</b>: <a href=\\\"tel:+919818512600\\\">+919818512600</a></p><p><b>gender</b>: male</p><p><b>birthDate</b>: 1981-01-12</p></div>");

		patient.getText().setDivAsString("<div xmlns=\"http://www.w3.org/1999/xhtml\">"
				+ "<p><b>Generated Narrative: Patient</b><a name=\"f8d9e19e-5598-428c-ae03-b1cd44968b41\"> </a></p>"
				+ "<div style=\"display: inline-block; background-color: #d9e0e7; padding: 6px; margin: 4px; border: 1px solid #8da1b4; border-radius: 5px; line-height: 60%\">"
				+ "<p style=\"margin-bottom: 0px\">Resource Patient &quot;f8d9e19e-5598-428c-ae03-b1cd44968b41&quot; Version &quot;1&quot; Updated &quot;2020-07-09 14:58:58+0530&quot; </p>"
				+ "<p style=\"margin-bottom: 0px\">Profile: <a href=\"StructureDefinition-Patient.html\">Patient</a></p>"
				+ "</div>" + "<p><b>identifier</b>: Medical record number/22-7225-4829-5255</p>" + "<p><b>name</b>:"
				+ patientName + "</p>" + "<p><b>telecom</b>: <a href=\"tel:+919818512600\">+919818512600</a></p>"
				+ "<p><b>gender</b>: male</p>" + "<p><b>birthDate</b>: 1981-01-12</p>" + "</div>");

		Identifier patientIdentifier = new Identifier();
		patientIdentifier.setUse(IdentifierUse.USUAL);
		patientIdentifier.setType(
				new CodeableConcept().addCoding(new Coding().setSystem("http://terminology.hl7.org/CodeSystem/v2-0203")
						.setCode("MR").setDisplay("Medical record number")));
		patientIdentifier.setSystem("https://healthid.ndhm.gov.in");
		patientIdentifier.setValue("22-7225-4829-5255");
		patient.addIdentifier(patientIdentifier);

		HumanName patientNameObj = new HumanName();
		patientNameObj.setText(patientName);
		patient.addName(patientNameObj);

		ContactPoint telecom = new ContactPoint();
		telecom.setSystem(ContactPoint.ContactPointSystem.PHONE);
		telecom.setValue("+919818512600");
		telecom.setUse(ContactPoint.ContactPointUse.HOME);
		patient.addTelecom(telecom);

//        patient.setGender(Enumerations.AdministrativeGender.MALE);
//        patient.setBirthDate(new Date(81, 0, 12));

		// Bundle Entry for Patient
		BundleEntryComponent entryPatient = new BundleEntryComponent();
		entryPatient.setFullUrl("Patient/f8d9e19e-5598-428c-ae03-b1cd44968b41");
		entryPatient.setResource(patient);
		bundle.addEntry(entryPatient);

		// Practitioner resource
		Practitioner practitioner = new Practitioner();
		practitioner.setId("dc36f7c9-7ea5-4984-a02e-7102c215db17");
		Meta practitionerMeta = new Meta();
		practitionerMeta.setVersionId("1");
		practitionerMeta.setLastUpdated(new Date());
		practitionerMeta.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/Practitioner");
		practitioner.setMeta(practitionerMeta);

		practitioner.getText().setStatus(Narrative.NarrativeStatus.GENERATED);
//        practitioner.getText().setDivAsString("<div xmlns=\\\"http://www.w3.org/1999/xhtml\\\"><p><b>Generated Narrative: Practitioner</b><a name=\\\"dc36f7c9-7ea5-4984-a02e-7102c215db17\\\"> </a></p><div style=\\\"display: inline-block; background-color: #d9e0e7; padding: 6px; margin: 4px; border: 1px solid #8da1b4; border-radius: 5px; line-height: 60%\\\"><p style=\\\"margin-bottom: 0px\\\">Resource Practitioner &quot;dc36f7c9-7ea5-4984-a02e-7102c215db17&quot; Version &quot;1&quot; Updated &quot;2019-05-29 14:58:58+0530&quot; </p><p style=\\\"margin-bottom: 0px\\\">Profile: <a href=\\\"StructureDefinition-Practitioner.html\\\">Practitioner</a></p></div><p><b>identifier</b>: Medical License number/21-1521-3828-3227</p><p><b>name</b>: Dr. DEF</p></div>");
		practitioner.getText().setDivAsString("<div xmlns=\"http://www.w3.org/1999/xhtml\">"
				+ "<p><b>Generated Narrative: Practitioner</b><a name=\"dc36f7c9-7ea5-4984-a02e-7102c215db17\"> </a></p>"
				+ "<div style=\"display: inline-block; background-color: #d9e0e7; padding: 6px; margin: 4px; border: 1px solid #8da1b4; border-radius: 5px; line-height: 60%\">"
				+ "<p style=\"margin-bottom: 0px\">Resource Practitioner &quot;dc36f7c9-7ea5-4984-a02e-7102c215db17&quot; Version &quot;1&quot; Updated &quot;2019-05-29 14:58:58+0530&quot; </p>"
				+ "<p style=\"margin-bottom: 0px\">Profile: <a href=\"StructureDefinition-Practitioner.html\">Practitioner</a></p>"
				+ "</div>" + "<p><b>identifier</b>: Medical License number/21-1521-3828-3227</p>" + "<p><b>name</b>: "
				+ docName + "</p>" + "</div>");
		Identifier practitionerIdentifier = new Identifier();
		practitionerIdentifier.setUse(IdentifierUse.USUAL);
		practitionerIdentifier.setType(
				new CodeableConcept().addCoding(new Coding().setSystem("http://terminology.hl7.org/CodeSystem/v2-0203")
						.setCode("MD").setDisplay("Medical License number")));
		practitionerIdentifier.setSystem("https://doctor.ndhm.gov.in");
		practitionerIdentifier.setValue("21-1521-3828-3227");
		practitioner.addIdentifier(practitionerIdentifier);

		HumanName practitionerName = new HumanName();
		practitionerName.setText(docName);
		practitioner.addName(practitionerName);

		// Bundle Entry for Practitioner
		BundleEntryComponent entryPractitioner = new BundleEntryComponent();
		entryPractitioner.setFullUrl("Practitioner/dc36f7c9-7ea5-4984-a02e-7102c215db17");
		entryPractitioner.setResource(practitioner);
		bundle.addEntry(entryPractitioner);

		// MedicationRequest resource 1
		MedicationRequest medicationRequest1 = new MedicationRequest();
		medicationRequest1.setId("bceda2f2-484f-45f3-8bf1-f8bcd7631f50");
		Meta medicationRequest1Meta = new Meta();
		medicationRequest1Meta.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/MedicationRequest");
		medicationRequest1.setMeta(medicationRequest1Meta);

		medicationRequest1.getText().setStatus(Narrative.NarrativeStatus.GENERATED);
//        medicationRequest1.getText().setDivAsString("<div xmlns=\\\"http://www.w3.org/1999/xhtml\\\"><p><b>Generated Narrative: MedicationRequest</b><a name=\\\"bceda2f2-484f-45f3-8bf1-f8bcd7631f50\\\"> </a></p><div style=\\\"display: inline-block; background-color: #d9e0e7; padding: 6px; margin: 4px; border: 1px solid #8da1b4; border-radius: 5px; line-height: 60%\\\"><p style=\\\"margin-bottom: 0px\\\">Resource MedicationRequest &quot;bceda2f2-484f-45f3-8bf1-f8bcd7631f50&quot; </p><p style=\\\"margin-bottom: 0px\\\">Profile: <a href=\\\"StructureDefinition-MedicationRequest.html\\\">MedicationRequest</a></p></div><p><b>status</b>: active</p><p><b>intent</b>: order</p><p><b>medication</b>: Azithromycin 250 mg oral tablet <span style=\\\"background: LightGoldenRodYellow; margin: 4px; border: 1px solid khaki\\\"> (<a href=\\\"https://browser.ihtsdotools.org/\\\">SNOMED CT</a>#1145423002)</span></p><p><b>subject</b>: <a href=\\\"#Patient_f8d9e19e-5598-428c-ae03-b1cd44968b41\\\">See above (Patient/f8d9e19e-5598-428c-ae03-b1cd44968b41: ABC)</a></p><p><b>authoredOn</b>: 2020-07-09</p><p><b>requester</b>: <a href=\\\"#Practitioner_dc36f7c9-7ea5-4984-a02e-7102c215db17\\\">See above (Practitioner/dc36f7c9-7ea5-4984-a02e-7102c215db17: Dr. DEF)</a></p><p><b>reasonCode</b>: Traveler's diarrhea <span style=\\\"background: LightGoldenRodYellow; margin: 4px; border: 1px solid khaki\\\"> (<a href=\\\"https://browser.ihtsdotools.org/\\\">SNOMED CT</a>#11840006)</span></p><p><b>reasonReference</b>: <a href=\\\"#Condition_b650b103-5eee-43c3-b53a-228872e77488\\\">See above (Condition/b650b103-5eee-43c3-b53a-228872e77488: Condition)</a></p><h3>DosageInstructions</h3><table class=\\\"grid\\\"><tr><td style=\\\"display: none\\\">-</td><td><b>Text</b></td><td><b>AdditionalInstruction</b></td><td><b>Timing</b></td><td><b>Route</b></td><td><b>Method</b></td></tr><tr><td style=\\\"display: none\\\">*</td><td>One tablet at once</td><td>With or after food <span style=\\\"background: LightGoldenRodYellow; margin: 4px; border: 1px solid khaki\\\"> (<a href=\\\"https://browser.ihtsdotools.org/\\\">SNOMED CT</a>#311504000)</span></td><td>Once per 1 days</td><td>Oral Route <span style=\\\"background: LightGoldenRodYellow; margin: 4px; border: 1px solid khaki\\\"> (<a href=\\\"https://browser.ihtsdotools.org/\\\">SNOMED CT</a>#26643006)</span></td><td>Swallow <span style=\\\"background: LightGoldenRodYellow; margin: 4px; border: 1px solid khaki\\\"> (<a href=\\\"https://browser.ihtsdotools.org/\\\">SNOMED CT</a>#421521009)</span></td></tr></table></div>");
		medicationRequest1.getText().setDivAsString("<div xmlns=\"http://www.w3.org/1999/xhtml\">"
				+ "<p><b>Generated Narrative: MedicationRequest</b><a name=\"bceda2f2-484f-45f3-8bf1-f8bcd7631f50\"> </a></p>"
				+ "<div style=\"display: inline-block; background-color: #d9e0e7; padding: 6px; margin: 4px; border: 1px solid #8da1b4; border-radius: 5px; line-height: 60%\">"
				+ "<p style=\"margin-bottom: 0px\">Resource MedicationRequest &quot;bceda2f2-484f-45f3-8bf1-f8bcd7631f50&quot; </p>"
				+ "<p style=\"margin-bottom: 0px\">Profile: <a href=\"StructureDefinition-MedicationRequest.html\">MedicationRequest</a></p>"
				+ "</div>" + "<p><b>status</b>: active</p>" + "<p><b>intent</b>: order</p>"
				+ "<p><b>medication</b>: Azithromycin 250 mg oral tablet <span style=\"background: LightGoldenRodYellow; margin: 4px; border: 1px solid khaki\"> (<a href=\"https://browser.ihtsdotools.org/\">SNOMED CT</a>#1145423002)</span></p>"
				+ "<p><b>subject</b>: <a href=\"#Patient_f8d9e19e-5598-428c-ae03-b1cd44968b41\">See above (Patient/f8d9e19e-5598-428c-ae03-b1cd44968b41: ABC)</a></p>"
				+ "<p><b>authoredOn</b>: 2020-07-09</p>"
				+ "<p><b>requester</b>: <a href=\"#Practitioner_dc36f7c9-7ea5-4984-a02e-7102c215db17\">See above (Practitioner/dc36f7c9-7ea5-4984-a02e-7102c215db17: "+docName+")</a></p>"
				+ "<p><b>reasonCode</b>: Traveler's diarrhea <span style=\"background: LightGoldenRodYellow; margin: 4px; border: 1px solid khaki\"> (<a href=\"https://browser.ihtsdotools.org/\">SNOMED CT</a>#11840006)</span></p>"
				+ "<p><b>reasonReference</b>: <a href=\"#Condition_b650b103-5eee-43c3-b53a-228872e77488\">See above (Condition/b650b103-5eee-43c3-b53a-228872e77488: Condition)</a></p>"
				+ "<h3>DosageInstructions</h3>" + "<table class=\"grid\">"
				+ "<tr><td style=\"display: none\">-</td><td><b>Text</b></td><td><b>AdditionalInstruction</b></td><td><b>Timing</b></td><td><b>Route</b></td><td><b>Method</b></td></tr>"
				+ "<tr><td style=\"display: none\">*</td><td>One tablet at once</td><td>With or after food <span style=\"background: LightGoldenRodYellow; margin: 4px; border: 1px solid khaki\"> (<a href=\"https://browser.ihtsdotools.org/\">SNOMED CT</a>#311504000)</span></td><td>Once per 1 days</td><td>Oral Route <span style=\"background: LightGoldenRodYellow; margin: 4px; border: 1px solid khaki\"> (<a href=\"https://browser.ihtsdotools.org/\">SNOMED CT</a>#26643006)</span></td><td>Swallow <span style=\"background: LightGoldenRodYellow; margin: 4px; border: 1px solid khaki\"> (<a href=\"https://browser.ihtsdotools.org/\">SNOMED CT</a>#421521009)</span></td></tr>"
				+ "</table></div>");
		Identifier medicationRequest1Identifier = new Identifier();
		medicationRequest1Identifier.setSystem("https://ndhm.in");
		medicationRequest1Identifier.setValue("98ce9b82-2686-4eb9-9822-45808a88eb95");
		medicationRequest1.addIdentifier(medicationRequest1Identifier);

		medicationRequest1.setStatus(MedicationRequest.MedicationRequestStatus.ACTIVE);
		medicationRequest1.setIntent(MedicationRequest.MedicationRequestIntent.ORDER);

		CodeableConcept medication1 = new CodeableConcept();
		medication1.addCoding().setSystem("http://www.nhp.gov.in").setCode("1000747")
				.setDisplay("Paracetamol 500 MG Oral Tablet");
		medicationRequest1.setMedication(medication1);

		Reference medicationRequest1Subject = new Reference("Patient/f8d9e19e-5598-428c-ae03-b1cd44968b41");
		medicationRequest1Subject.setDisplay("Patient");
		medicationRequest1.setSubject(medicationRequest1Subject);

		medicationRequest1.setAuthoredOn(new Date());

		Reference medicationRequest1Requester = new Reference("Practitioner/dc36f7c9-7ea5-4984-a02e-7102c215db17");
		medicationRequest1Requester.setDisplay("Practitioner");
		medicationRequest1.setRequester(medicationRequest1Requester);

		Dosage dosageInstruction1 = new Dosage();
		dosageInstruction1.getTiming().getRepeat().setFrequency(3).setPeriod(1).setPeriodUnit(Timing.UnitsOfTime.D);
		dosageInstruction1.setAsNeeded(new BooleanType(true));
		dosageInstruction1.getRoute().addCoding().setSystem("http://snomed.info/sct").setCode("26643006")
				.setDisplay("Oral Route");
		dosageInstruction1.setDoseAndRate(Collections.singletonList(
				new Dosage.DosageDoseAndRateComponent().setDose(new Quantity().setValue(1).setUnit("tablet"))));
		medicationRequest1.addDosageInstruction(dosageInstruction1);

		// Bundle Entry for MedicationRequest 1
		BundleEntryComponent entryMedicationRequest1 = new BundleEntryComponent();
		entryMedicationRequest1.setFullUrl("MedicationRequest/bceda2f2-484f-45f3-8bf1-f8bcd7631f50");
		entryMedicationRequest1.setResource(medicationRequest1);
		bundle.addEntry(entryMedicationRequest1);

		// MedicationRequest resource 2
		MedicationRequest medicationRequest2 = new MedicationRequest();
		medicationRequest2.setId("c74e22c8-55b6-49c1-a891-ca5e3cd3a99c");
		Meta medicationRequest2Meta = new Meta();
		medicationRequest2Meta.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/MedicationRequest");
		medicationRequest2.setMeta(medicationRequest2Meta);

		medicationRequest2.getText().setStatus(Narrative.NarrativeStatus.GENERATED);
//        medicationRequest2.getText().setDivAsString("<div xmlns=\\\"http://www.w3.org/1999/xhtml\\\"><p><b>Generated Narrative: MedicationRequest</b><a name=\\\"c74e22c8-55b6-49c1-a891-ca5e3cd3a99c\\\"> </a></p><div style=\\\"display: inline-block; background-color: #d9e0e7; padding: 6px; margin: 4px; border: 1px solid #8da1b4; border-radius: 5px; line-height: 60%\\\"><p style=\\\"margin-bottom: 0px\\\">Resource MedicationRequest &quot;c74e22c8-55b6-49c1-a891-ca5e3cd3a99c&quot; </p><p style=\\\"margin-bottom: 0px\\\">Profile: <a href=\\\"StructureDefinition-MedicationRequest.html\\\">MedicationRequest</a></p></div><p><b>status</b>: active</p><p><b>intent</b>: order</p><p><b>medication</b>: Paracetemol 500mg Oral Tab <span style=\\\"background: LightGoldenRodYellow; margin: 4px; border: 1px solid khaki\\\"> ()</span></p><p><b>subject</b>: <a href=\\\"#Patient_f8d9e19e-5598-428c-ae03-b1cd44968b41\\\">See above (Patient/f8d9e19e-5598-428c-ae03-b1cd44968b41: ABC)</a></p><p><b>authoredOn</b>: 2020-07-09</p><p><b>requester</b>: <a href=\\\"#Practitioner_dc36f7c9-7ea5-4984-a02e-7102c215db17\\\">See above (Practitioner:dc36f7c9-7ea5-4984-a02e-7102c215db17: Dr. DEF)</a></p><p><b>reasonCode</b>: Ross River disease <span style=\\\"background: LightGoldenRodYellow; margin: 4px; border: 1px solid khaki\\\"> (<a href=\\\"https://browser.ihtsdotools.org/\\\">SNOMED CT</a>#789400009)</span></p><p><b>reasonReference</b>: <a href=\\\"#Condition_b650b103-5eee-43c3-b53a-228872e77488\\\">See above (Condition/b650b103-5eee-43c3-b53a-228872e77488: Condition)</a></p><h3>DosageInstructions</h3><table class=\\\"grid\\\"><tr><td style=\\\"display: none\\\">-</td><td><b>Text</b></td></tr><tr><td style=\\\"display: none\\\">*</td><td>Take two tablets orally with or after meal once a day</td></tr></table></div>");

		medicationRequest2.getText().setDivAsString("<div xmlns=\"http://www.w3.org/1999/xhtml\">"
				+ "<p><b>Generated Narrative: MedicationRequest</b><a name=\"c74e22c8-55b6-49c1-a891-ca5e3cd3a99c\"> </a></p>"
				+ "<div style=\"display: inline-block; background-color: #d9e0e7; padding: 6px; margin: 4px; border: 1px solid #8da1b4; border-radius: 5px; line-height: 60%\">"
				+ "<p style=\"margin-bottom: 0px\">Resource MedicationRequest &quot;c74e22c8-55b6-49c1-a891-ca5e3cd3a99c&quot; </p>"
				+ "<p style=\"margin-bottom: 0px\">Profile: <a href=\"StructureDefinition-MedicationRequest.html\">MedicationRequest</a></p>"
				+ "</div>" + "<p><b>status</b>: active</p>" + "<p><b>intent</b>: order</p>"
				+ "<p><b>medication</b>: Paracetamol 500mg Oral Tab <span style=\"background: LightGoldenRodYellow; margin: 4px; border: 1px solid khaki\"> ()</span></p>"
				+ "<p><b>subject</b>: <a href=\"#Patient_f8d9e19e-5598-428c-ae03-b1cd44968b41\">See above (Patient/f8d9e19e-5598-428c-ae03-b1cd44968b41: ABC)</a></p>"
				+ "<p><b>authoredOn</b>: 2020-07-09</p>"
				+ "<p><b>requester</b>: <a href=\"#Practitioner_dc36f7c9-7ea5-4984-a02e-7102c215db17\">See above (Practitioner/dc36f7c9-7ea5-4984-a02e-7102c215db17: Dr. DEF)</a></p>"
				+ "<p><b>reasonCode</b>: Ross River disease <span style=\"background: LightGoldenRodYellow; margin: 4px; border: 1px solid khaki\"> (<a href=\"https://browser.ihtsdotools.org/\">SNOMED CT</a>#789400009)</span></p>"
				+ "<p><b>reasonReference</b>: <a href=\"#Condition_b650b103-5eee-43c3-b53a-228872e77488\">See above (Condition/b650b103-5eee-43c3-b53a-228872e77488: Condition)</a></p>"
				+ "<h3>DosageInstructions</h3>" + "<table class=\"grid\">"
				+ "<tr><td style=\"display: none\">-</td><td><b>Text</b></td></tr>"
				+ "<tr><td style=\"display: none\">*</td><td>Take two tablets orally with or after meal once a day</td></tr>"
				+ "</table></div>");
		Identifier medicationRequest2Identifier = new Identifier();
		medicationRequest2Identifier.setSystem("https://ndhm.in");
		medicationRequest2Identifier.setValue("bd2e603d-cbc2-4e12-bde8-dc94b967bb53");
		medicationRequest2.addIdentifier(medicationRequest2Identifier);

		medicationRequest2.setStatus(MedicationRequest.MedicationRequestStatus.ACTIVE);
		medicationRequest2.setIntent(MedicationRequest.MedicationRequestIntent.ORDER);

		CodeableConcept medication2 = new CodeableConcept();
		medication2.addCoding().setSystem("http://www.nhp.gov.in").setCode("1000391")
				.setDisplay("Pantoprazole Sodium 40 MG Oral Tablet");
		medicationRequest2.setMedication(medication2);

		Reference medicationRequest2Subject = new Reference("Patient/f8d9e19e-5598-428c-ae03-b1cd44968b41");
		medicationRequest2Subject.setDisplay("Patient");
		medicationRequest2.setSubject(medicationRequest2Subject);

		medicationRequest2.setAuthoredOn(new Date());

		Reference medicationRequest2Requester = new Reference("Practitioner/dc36f7c9-7ea5-4984-a02e-7102c215db17");
		medicationRequest2Requester.setDisplay("Practitioner");
		medicationRequest2.setRequester(medicationRequest2Requester);

		Dosage dosageInstruction2 = new Dosage();
		dosageInstruction2.getTiming().getRepeat().setFrequency(1).setPeriod(1).setPeriodUnit(Timing.UnitsOfTime.D);
		dosageInstruction2.setAsNeeded(new BooleanType(false));
		dosageInstruction2.getRoute().addCoding().setSystem("http://snomed.info/sct").setCode("26643006")
				.setDisplay("Oral Route");
		dosageInstruction2.setDoseAndRate(Collections.singletonList(
				new Dosage.DosageDoseAndRateComponent().setDose(new Quantity().setValue(1).setUnit("tablet"))));
		medicationRequest2.addDosageInstruction(dosageInstruction2);

		// Bundle Entry for MedicationRequest 2
		BundleEntryComponent entryMedicationRequest2 = new BundleEntryComponent();
		entryMedicationRequest2.setFullUrl("MedicationRequest/c74e22c8-55b6-49c1-a891-ca5e3cd3a99c");
		entryMedicationRequest2.setResource(medicationRequest2);
		bundle.addEntry(entryMedicationRequest2);

		// Binary resource
		Binary binary = new Binary();
		binary.setId("2044f347-c58c-4237-b7f4-8c713b79f471");
		Meta binaryMeta = new Meta();
		binaryMeta.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/Binary");
		binary.setMeta(binaryMeta);

		binary.setContentType("application/pdf");
		binary.setContent(file);

		// Bundle Entry for Binary
		BundleEntryComponent entryBinary = new BundleEntryComponent();
		entryBinary.setFullUrl("Binary/2044f347-c58c-4237-b7f4-8c713b79f471");
		entryBinary.setResource(binary);
		bundle.addEntry(entryBinary);

		// Print the bundle as JSON
		IParser jsonParser = FhirContext.forR4().newJsonParser();
		jsonParser.setPrettyPrint(true);
		String bundleJson = jsonParser.encodeResourceToString(bundle);
//        System.out.println(bundleJson);
		return bundleJson;
	}
}
