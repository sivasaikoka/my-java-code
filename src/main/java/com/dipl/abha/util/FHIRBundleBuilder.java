package com.dipl.abha.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.hl7.fhir.r4.model.Attachment;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Composition;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.DocumentReference;
import org.hl7.fhir.r4.model.Dosage;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Medication;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Period;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.Reference;
import org.springframework.stereotype.Service;

import ca.uhn.fhir.context.FhirContext;

@Service
public class FHIRBundleBuilder {

	public String buildBundle(String docName, String patientName, String visitDate, String fileType,
			String interactionId, String docRef, String fileTypeNew, byte[] file, String medicationString,
			String medDirectionString, String chiefComplains) throws ParseException {
		FhirContext ctx = FhirContext.forR4();
		String visitDate1 = visitDate.replace("T", " ");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = dateFormat.parse(visitDate1);

		// Creating the bundle
		Bundle bundle = new Bundle();
		bundle.setId(interactionId);
		bundle.setType(Bundle.BundleType.DOCUMENT);
		bundle.setTimestamp(date);

		// Meta information
		Meta meta = new Meta();
		meta.setLastUpdated(date);
		bundle.setMeta(meta);

		// Identifier
		Identifier identifier = new Identifier();
		identifier.setSystem("https://www.max.in/bundle");
		identifier.setValue("2efc8643-743b-4559-9030-f07456970711");
		bundle.setIdentifier(identifier);

		// Entry list
		List<Bundle.BundleEntryComponent> entryList = new ArrayList<>();

		// Composition
		Composition composition = new Composition();
		composition.setId("23cbf24b-87be-424b-8a3c-f3aa32d6c777");
		composition.setStatus(Composition.CompositionStatus.FINAL);
		composition.setType(new CodeableConcept().addCoding(
				new Coding().setSystem("https://projecteka.in/sct").setCode("371530004").setDisplay(fileTypeNew)));
		composition.setSubject(new Reference("Patient/LIVNO15"));
		composition.setEncounter(new Reference("Encounter/3cf12305-4797-4880-820c-1af701521913"));
		composition.setDate(date);
		composition.setAuthor(List.of(new Reference("Practitioner/MAX1234")));
		composition.setTitle(fileType);

		// Composition sections
		List<Composition.SectionComponent> sectionList = new ArrayList<>();

		if (chiefComplains != null && !chiefComplains.isEmpty()) {
			// Chief Complaints section
			Composition.SectionComponent chiefComplaints = new Composition.SectionComponent();
			chiefComplaints.setTitle("Chief Complaints");
			chiefComplaints.setCode(new CodeableConcept().addCoding(new Coding().setSystem("https://projecteka.in/sct")
					.setCode("422843007").setDisplay("Chief Complaint Section")));
			List<String> chiefComp = Stream.of(chiefComplains.split(",", -1)).collect(Collectors.toList());
			List<Reference> chiefComplainsEntry = new ArrayList<>();
			chiefComp.forEach(s -> {
				String chiefCompId = UUID.randomUUID().toString();
				// Conditions (Chief Complaints)
				chiefComplainsEntry.add(new Reference("Condition/" + chiefCompId));
				Condition condition1 = new Condition();
				condition1.setId(chiefCompId);
				condition1.setClinicalStatus(new CodeableConcept()
						.addCoding(new Coding().setSystem("http://terminology.hl7.org/CodeSystem/condition-clinical")
								.setCode("active").setDisplay("active")));
				condition1.setCategory(List.of(new CodeableConcept()
						.addCoding(new Coding().setSystem("http://terminology.hl7.org/CodeSystem/condition-category")
								.setCode("problem-list-item").setDisplay("problem list"))));
				condition1.setCode(new CodeableConcept().setText(s));
				condition1.setSubject(new Reference("Patient/LIVNO15"));
				condition1.setOnset(new Period().setStart(date));
				condition1.setRecordedDate(date);
				// Adding Condition 1 to entry list
				entryList.add(new Bundle.BundleEntryComponent().setResource(condition1));
			});
			chiefComplaints.setEntry(chiefComplainsEntry);
			sectionList.add(chiefComplaints);
		}

		if (medicationString != null && !medicationString.isEmpty()) {
			// Prescription section
			Composition.SectionComponent prescription = new Composition.SectionComponent();
			prescription.setTitle("Prescription");
			prescription.setCode(new CodeableConcept().addCoding(new Coding().setSystem("https://projecteka.in/sct")
					.setCode("440545006").setDisplay("Prescription")));
//			prescription.setEntry(List.of(new Reference("MedicationRequest/a1f58b69-1e5e-4f2c-a291-0b5671a8f15c")));
			
			
			List<Reference> medReqReference = new ArrayList<>();
			List<String> medications = Stream.of(medicationString.split(",", -1)).collect(Collectors.toList());
			for (int i = 0; i < medications.size(); i++) {
				// Medication
				String medId = UUID.randomUUID().toString();
				Medication medication = new Medication();
				medication.setId(medId);
				medication.setCode(new CodeableConcept().addCoding(new Coding().setSystem("https://projecteka.in/act")
						.setCode("R05CB02").setDisplay(medications.get(i))));
				// Adding Medication to entry list
				entryList.add(new Bundle.BundleEntryComponent().setResource(medication));
				if (medDirectionString != null && medDirectionString != null) {
					List<String> medDirections = Stream.of(medicationString.split(",", -1))
							.collect(Collectors.toList());
					if (!medications.isEmpty() && !medDirections.isEmpty()) {
						// Medication Request
						String medDirectionsId = UUID.randomUUID().toString();
						MedicationRequest medicationRequest = new MedicationRequest();
						medicationRequest.setId(medDirectionsId);
						medicationRequest.setStatus(MedicationRequest.MedicationRequestStatus.ACTIVE);
						medicationRequest.setIntent(MedicationRequest.MedicationRequestIntent.ORDER);
						Reference medicationReference1 = new Reference("Medication/" + medId);
						medicationRequest.setMedication(medicationReference1); // Corrected line
						medicationRequest.setSubject(new Reference("Patient/LIVNO15"));
						medicationRequest.setAuthoredOn(date);
						medicationRequest.setRequester(new Reference("Practitioner/MAX1234"));
						medicationRequest
								.setDosageInstruction(List.of(new Dosage().setText("1 capsule 2 times a day")));
						medReqReference.add(new Reference("MedicationRequest/"+medDirectionsId));
						// Adding Medication Request to entry list
						entryList.add(new Bundle.BundleEntryComponent().setResource(medicationRequest));
					}
				}
			}
			prescription.setEntry(medReqReference);
			sectionList.add(prescription);

		}

		// Clinical consultation section
		Composition.SectionComponent clinicalConsultation = new Composition.SectionComponent();
		clinicalConsultation.setTitle(fileTypeNew);
		clinicalConsultation.setCode(new CodeableConcept().addCoding(
				new Coding().setSystem("https://projecteka.in/sct").setCode("371530004").setDisplay(fileType)));
		clinicalConsultation.setEntry(List.of(new Reference("DocumentReference/f39604fc-da47-4e09-abb2-e2f4551e5713")));
		sectionList.add(clinicalConsultation);

		composition.setSection(sectionList);

		// Adding Composition to entry list
		entryList.add(new Bundle.BundleEntryComponent().setResource(composition));

		// Practitioner
		Practitioner practitioner = new Practitioner();
		practitioner.setId("MAX1234");
		practitioner.addIdentifier().setSystem("https://www.mciindia.in/doctor").setValue("MAX1234");
		practitioner.addName().setText(docName).addPrefix("Dr").addSuffix("MD");

		// Adding Practitioner to entry list
		entryList.add(new Bundle.BundleEntryComponent().setResource(practitioner));

		// Patient
		Patient patient = new Patient();
		patient.setId("LIVNO15");
		patient.addName().setText(patientName);
//        patient.setGender(Enumerations.AdministrativeGender.MALE);

		// Adding Patient to entry list
		entryList.add(new Bundle.BundleEntryComponent().setResource(patient));

		// Encounter
		Encounter encounter = new Encounter();
		encounter.setId("3cf12305-4797-4880-820c-1af701521913");
		encounter.setStatus(Encounter.EncounterStatus.FINISHED);
		encounter.setClass_(new Coding().setSystem("http://terminology.hl7.org/CodeSystem/v3-ActCode").setCode("AMB")
				.setDisplay("Outpatient visit"));
		encounter.setSubject(new Reference("Patient/LIVNO15"));
		encounter.setPeriod(new Period().setStart(date));

		// Adding Encounter to entry list
		entryList.add(new Bundle.BundleEntryComponent().setResource(encounter));

		// Document Reference
		DocumentReference documentReference = new DocumentReference();
		documentReference.setId("f39604fc-da47-4e09-abb2-e2f4551e5713");
		documentReference.setStatus(Enumerations.DocumentReferenceStatus.CURRENT);
		documentReference.setType(new CodeableConcept().addCoding(
				new Coding().setSystem("https://projecteka.in/loinc").setCode("30954-2").setDisplay(fileType)));
		documentReference.setAuthor(List.of(new Reference("Practitioner/MAX1234")));
		documentReference.setContent(List.of(new DocumentReference.DocumentReferenceContentComponent()
				.setAttachment(new Attachment().setContentType("application/pdf").setData(file).setTitle(fileType))));

		// Adding Document Reference to entry list
		entryList.add(new Bundle.BundleEntryComponent().setResource(documentReference));
		bundle.setEntry(entryList);
		String json = ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle);
		System.out.println(json);
		return json;
	}

//	public static void main(String[] args) throws ParseException {
//		FHIRBundleBuilder builder = new FHIRBundleBuilder();
//		Bundle bundle = builder.buildBundle("ABHINAY", "Sravanthi", "2024-04-04T11:23:00", "Prescription", "100001",
//				"DOCREF", "PrescriptoinNew", null, "para,hello,hi,how,are,you", "a,b,c,d,e,f", "fever, caugh, cold");
//		FhirContext ctx = FhirContext.forR4();
//		String json = ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle);
//		System.out.println(json);
//	}
}
