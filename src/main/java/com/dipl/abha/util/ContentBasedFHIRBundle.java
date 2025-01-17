package com.dipl.abha.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hl7.fhir.r4.model.Appointment;
import org.hl7.fhir.r4.model.Appointment.AppointmentParticipantComponent;
import org.hl7.fhir.r4.model.Appointment.AppointmentStatus;
import org.hl7.fhir.r4.model.Binary;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Composition;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.ContactPoint;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.Dosage;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.InstantType;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.Narrative;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Period;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.Quantity;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.ServiceRequest;
import org.springframework.stereotype.Service;

import ca.uhn.fhir.context.FhirContext;

@Service
public class ContentBasedFHIRBundle {

	public String fhirBundle(byte[] file) throws ParseException {
//	public static void main(String[] args) throws ParseException {
		// Create a Bundle resource
		Bundle bundle = new Bundle();
		// Set the ID
		bundle.setId("FHIR120");
		// Create and set the Meta element
		Meta meta = new Meta();
		meta.setVersionId("1");
		meta.setLastUpdatedElement(new InstantType("2024-07-30T07:14:09.381Z"));
		meta.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/DocumentBundle");
		Coding securityCoding = new Coding();
		securityCoding.setSystem("http://terminology.hl7.org/CodeSystem/v3-Confidentiality");
		securityCoding.setCode("V");
		securityCoding.setDisplay("very restricted");
		meta.addSecurity(securityCoding);
		bundle.setMeta(meta);
		// Set the identifier
		Identifier identifier = new Identifier();
		identifier.setSystem("http://hip.in");
		identifier.setValue("305fecc2-4ba2-46cc-9ccd-efa755aff51d");
		bundle.setIdentifier(identifier);
		// Set the type to document
		bundle.setType(Bundle.BundleType.DOCUMENT);
		// Set the timestamp to null (not necessary to set as it's null by default)
		// bundle.setTimestamp(null);
		// Set the identifier
		Identifier identifierOne = new Identifier();
		identifierOne.setSystem("http://hip.in");
		identifierOne.setValue("305fecc2-4ba2-46cc-9ccd-efa755aff51d");
		bundle.setIdentifier(identifierOne);
		// Set the type to document
		bundle.setType(Bundle.BundleType.DOCUMENT);
		// Create a Composition resource
		Composition composition = new Composition();
		composition.setId("1");
		Meta compositionMeta = new Meta();
		compositionMeta.setVersionId("1");
		compositionMeta.setLastUpdated(new Date());
		compositionMeta.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/OPConsultRecord");
		composition.setMeta(compositionMeta);
		composition.setLanguage("en-IN");
		Narrative text = new Narrative();
		text.setStatus(Narrative.NarrativeStatus.GENERATED);
		text.setDivAsString("<div xmlns='http://www.w3.org/1999/xhtml'>Generated Narrative with Details</div>");
		composition.setText(text);
		Identifier compositionIdentifier = new Identifier();
		compositionIdentifier.setSystem("https://ndhm.in/phr");
		compositionIdentifier.setValue("305fecc2-4ba2-46cc-9ccd-efa755aff51d");
		composition.setIdentifier(compositionIdentifier);
		composition.setStatus(Composition.CompositionStatus.FINAL);
		CodeableConcept type = new CodeableConcept();
		Coding typeCoding = new Coding();
		typeCoding.setSystem("http://snomed.info/sct");
		typeCoding.setCode("371530004");
		typeCoding.setDisplay("Clinical Consultation Record");
		type.addCoding(typeCoding);
		type.setText("Clinical Consultation Record");
		composition.setType(type);
		Reference subject = new Reference();
		subject.setReference("Patient/patient1");
		subject.setDisplay("NHA TEST- UHID No.18");
		composition.setSubject(subject);
		Reference encounter = new Reference();
		encounter.setReference("Encounter/encounter");
		encounter.setDisplay("NHA TEST- UHID No.18");
		composition.setEncounter(encounter);
		composition.setDate(new Date());
		Reference author = new Reference();
		author.setReference("Practitioner/doctor1");
		author.setDisplay("Dr. LOKESH SHARMA");
		composition.addAuthor(author);
		composition.setTitle("Consultation Report");
		Reference custodian = new Reference();
		custodian.setReference("Practitioner/doctor1");
		custodian.setDisplay("Dr. LOKESH SHARMA");
		composition.setCustodian(custodian);
		// Add sections
		composition.addSection(
				createSection("Chief Complaints", "http://snomed.info/sct", "422843007", "Chief Complaint Section",
						List.of(createEntry("Complaints/chief-complaints", "Chief Complaint : NHA TEST- UHID No.18"))));

		composition.addSection(createSection("Vital Signs", null, null, null,
				List.of(createEntry("Observation/pulse-rate"), createEntry("Observation/sp-o2"),
						createEntry("Observation/body-temperature"), createEntry("Observation/Systolic-blood-pressure"),
						createEntry("Observation/Diastolic-blood-pressure"),
						createEntry("Observation/BloodGlucose"), createEntry("Observation/body-height"),
						createEntry("Observation/body-weight"))));
		composition.addSection(createSection("Follow Up", "http://snomed.info/sct", "736271009", "Outpatient care plan",
				List.of(createEntry("Appointment/appointment", "Appointment: NHA TEST- UHID No.18"))));
		composition.addSection(createSection("Investigation Advice", "http://snomed.info/sct", "721963009",
				"Investigation Advice", List.of(createEntry("Investigation Advice/service", "Investigation Advice"))));
		composition.addSection(
				createSection("Medications", "http://snomed.info/sct", "721912009", "Medication summary document",
						List.of(createEntry("MedicationRequest/1"), createEntry("MedicationRequest/2"),
								createEntry("MedicationRequest/3")
								)));
		
		// Add the Composition resource to the bundle
		Bundle.BundleEntryComponent entryComposition = new Bundle.BundleEntryComponent();
		entryComposition.setFullUrl("Composition/1");
		entryComposition.setResource(composition);
		bundle.addEntry(entryComposition);
		Bundle.BundleEntryComponent entryPatient = new Bundle.BundleEntryComponent();
		entryPatient.setFullUrl("Patient/patient11");
		entryPatient.setResource(ContentBasedFHIRBundle.buildPatientObject("patient1", "91-0002-0003-0004", "10020", "Abhinay",
				"8132290207", "male", "1996-02-07"));
		bundle.addEntry(entryPatient);
		Bundle.BundleEntryComponent entryPractitioner = new Bundle.BundleEntryComponent();
		entryPractitioner.setFullUrl("Practitioner/doctor1");
		entryPractitioner.setResource(ContentBasedFHIRBundle.buildDoctorObject("vara prasad", "Prescription"));
		bundle.addEntry(entryPractitioner);
		Bundle.BundleEntryComponent entryEncounter = new Bundle.BundleEntryComponent();
		entryEncounter.setFullUrl("Encounter/encounter");
		entryEncounter.setResource(ContentBasedFHIRBundle.buildEncounter(subject, "1996-02-07"));
		bundle.addEntry(entryEncounter);
		Bundle.BundleEntryComponent observation1 = new Bundle.BundleEntryComponent();
		observation1.setFullUrl("Observation/pulse-rate");
		observation1.setResource(ContentBasedFHIRBundle.buildObservationPulseRate(98));
		bundle.addEntry();
		Bundle.BundleEntryComponent observation2 = new Bundle.BundleEntryComponent();
		observation2.setFullUrl("Observation/sp-o2");
		observation2.setResource(ContentBasedFHIRBundle.buildObservationOxygenSaturation(98));
		bundle.addEntry(observation2);
		Bundle.BundleEntryComponent observation3 = new Bundle.BundleEntryComponent();
		observation3.setFullUrl("Observation/body-temperature");
		observation3.setResource(ContentBasedFHIRBundle.observationBodyTemperature(98));
		bundle.addEntry(observation3);
		Bundle.BundleEntryComponent observation4 = new Bundle.BundleEntryComponent();
		observation4.setFullUrl("Observation/Systolic-blood-pressure");
		observation4.setResource(ContentBasedFHIRBundle.observationSystolicBloodPressure(110));
		bundle.addEntry(observation4);
		Bundle.BundleEntryComponent observation5 = new Bundle.BundleEntryComponent();
		observation5.setFullUrl("Observation/Diastolic-blood-pressure");
		observation5.setResource(ContentBasedFHIRBundle.observationDiastolicBloodPressure(56));
		bundle.addEntry(observation5);
		Bundle.BundleEntryComponent observation6 = new Bundle.BundleEntryComponent();
		observation6.setFullUrl("Observation/sp-o2");
		observation6.setResource(ContentBasedFHIRBundle.observationBloodGlucose(57));
		bundle.addEntry(observation6);
		Bundle.BundleEntryComponent observation7 = new Bundle.BundleEntryComponent();
		observation7.setFullUrl("Observation/body-height");
		observation7.setResource(ContentBasedFHIRBundle.observationBodyHeight(176));
		bundle.addEntry(observation7);
		Bundle.BundleEntryComponent observation8 = new Bundle.BundleEntryComponent();
		observation8.setFullUrl("Observation/body-weight");
		observation8.setResource(ContentBasedFHIRBundle.observationBodyWeight(76));
		bundle.addEntry(observation8);
		Bundle.BundleEntryComponent chiefComplaintsEntry = new Bundle.BundleEntryComponent();
		chiefComplaintsEntry.setFullUrl("Complaints/chief-complaints");
		chiefComplaintsEntry.setResource(ContentBasedFHIRBundle.buildChiefComplaints());
		bundle.addEntry(chiefComplaintsEntry);
		Bundle.BundleEntryComponent appointment = new Bundle.BundleEntryComponent();
		appointment.setFullUrl("Appointment/appointment");
		appointment.setResource(ContentBasedFHIRBundle.Appointment());
		bundle.addEntry(appointment);
		Bundle.BundleEntryComponent labServices = new Bundle.BundleEntryComponent();
		labServices.setFullUrl("Investigation Advice/service");
		labServices.setResource(ContentBasedFHIRBundle.createServiceRequest());
		bundle.addEntry(labServices);
		Binary binary = new Binary();
		binary.setId("2044f347-c58c-4237-b7f4-8c713b79f471");
		Meta binaryMeta = new Meta();
		binaryMeta.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/Binary");
		binary.setMeta(binaryMeta);
		binary.setContentType("application/pdf");

		// Bundle Entry for Binary
		BundleEntryComponent entryBinary = new BundleEntryComponent();
		entryBinary.setFullUrl("Binary/2044f347-c58c-4237-b7f4-8c713b79f471");
		entryBinary.setResource(binary);
		bundle.addEntry(entryBinary);

		MedicationRequest medicationRequest1 = createMedicationRequest(1,
				"DOXCEF 200MG TABLET Drug : Cefpodoxime 200 MG", "384977007",
				"DOXCEF 200MG TABLET Drug : Cefpodoxime 200 MG", "Patient/patient1", "NHA TEST", "2024-07-30",
				"Practitioner/doctor1", "Dr. LOKESH SHARMA", "386661006", "DENGUE FEVER",
				"(Dosage: 1-0-1) Instruction: with milk", "26643006", "Oral Route");

		Bundle.BundleEntryComponent medReq1 = new Bundle.BundleEntryComponent();
		medReq1.setFullUrl("MedicationRequest/1");
		medReq1.setResource(medicationRequest1);
		bundle.addEntry(medReq1);

		MedicationRequest medicationRequest2 = createMedicationRequest(2,
				"MEDROL 16MG TABLET Drug : Methylprednisolone 16 MG", "384977007",
				"MEDROL 16MG TABLET Drug : Methylprednisolone 16 MG", "Patient/patient1", "NHA TEST", "2024-07-30",
				"Practitioner/doctor1", "Dr. LOKESH SHARMA", "386661006", "DENGUE FEVER",
				"(Dosage: 1-0-0) Instruction: with warm water", "26643006", "Oral Route");

		Bundle.BundleEntryComponent medReq2 = new Bundle.BundleEntryComponent();
		medReq2.setFullUrl("MedicationRequest/2");
		medReq2.setResource(medicationRequest2);
		bundle.addEntry(medReq2);

		MedicationRequest medicationRequest3 = createMedicationRequest(3, "LEECOP Drug : CITRAZIN", "384977007",
				"LEECOP Drug : CITRAZIN", "Patient/patient1", "NHA TEST", "2024-07-30", "Practitioner/doctor1", "Dr. LOKESH SHARMA",
				"386661006", "DENGUE FEVER", "(Dosage: 0-0-1) Instruction: after food only", "26643006", "Oral Route");

		Bundle.BundleEntryComponent medReq3 = new Bundle.BundleEntryComponent();
		medReq3.setFullUrl("MedicationRequest/3");
		medReq3.setResource(medicationRequest3);
		bundle.addEntry(medReq3);

//		Binary binary1 = createBinaryResource("OP Consultation62",
//				"https://nrces.in/ndhm/fhir/r4/StructureDefinition/Binary", "application/pdf", "sdfasdfsda");
//		Bundle.BundleEntryComponent binaryEntry = new Bundle.BundleEntryComponent();
//		binaryEntry.setFullUrl("Binary/OP Consultation62");
//		binaryEntry.setResource(binary1);
//		bundle.addEntry(binaryEntry);

		// You can now use the created MedicationRequest objects as needed

		// Serialize to JSON for output (optional)
		FhirContext ctx = FhirContext.forR4();
		String jsonString = ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle);
		System.out.println(jsonString);
		return jsonString;
	}

	private static Reference createEntry(String reference) {
		return new Reference(reference);
	}

	private static Reference createEntry(String reference, String display) {
		Reference ref = new Reference(reference);
		ref.setDisplay(display);
		return ref;
	}

	private static Composition.SectionComponent createSection(String title, String codeSystem, String code,
			String display, List<Reference> entries) {
		Composition.SectionComponent section = new Composition.SectionComponent();
		section.setTitle(title);
		if (codeSystem != null && code != null && display != null) {
			CodeableConcept codeableConcept = new CodeableConcept();
			Coding coding = new Coding();
			coding.setSystem(codeSystem);
			coding.setCode(code);
			coding.setDisplay(display);
			codeableConcept.addCoding(coding);
			section.setCode(codeableConcept);
		}
		for (Reference entry : entries) {
			section.addEntry(entry);
		}
		return section;
	}

	private static Patient buildPatientObject(String patientId, String abhaId, String interactoinId, String patientName,
			String mobileNo, String gender, String dob) {
		// Create a Patient resource
		Patient patient = new Patient();

		// Set the ID
		patient.setId(patientId);

		// Create and set the Meta element
		Meta meta = new Meta();
		meta.setVersionId("1");
		meta.setLastUpdated(new Date());
		meta.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/Patient");
		patient.setMeta(meta);

		// Set the identifier elements
		Identifier mrIdentifier = new Identifier();
		Coding mrCoding = new Coding();
		mrCoding.setSystem("http://terminology.hl7.org/CodeSystem/v2-0203");
		mrCoding.setCode("MR");
		mrCoding.setDisplay("Medical record number");
		mrIdentifier.setType(new CodeableConcept().addCoding(mrCoding));
		mrIdentifier.setSystem("https://healthid.ndhm.gov.in");
		mrIdentifier.setValue(interactoinId);

		Identifier abhaIdentifier = new Identifier();
		Coding abhaCoding = new Coding();
		abhaCoding.setSystem("https://nrces.in/ndhm/fhir/r4/CodeSystem/ndhm-identifier-type-code");
		abhaCoding.setCode("ABHA");
		abhaCoding.setDisplay("Ayushman Bharat Health Account (ABHA) ID");
		abhaIdentifier.setType(new CodeableConcept().addCoding(abhaCoding));
		abhaIdentifier.setSystem("https://healthid.abdm.gov.in");
		abhaIdentifier.setValue(abhaId);

		patient.addIdentifier(mrIdentifier);
		patient.addIdentifier(abhaIdentifier);

		// Set the name element
		HumanName name = new HumanName();
		name.setText(patientName);
		patient.addName(name);

		// Set the telecom element
		ContactPoint telecom = new ContactPoint();
		telecom.setSystem(ContactPoint.ContactPointSystem.PHONE);
		telecom.setValue(mobileNo);
		telecom.setUse(ContactPoint.ContactPointUse.HOME);
		patient.addTelecom(telecom);

		// Set the gender

		if (gender.equalsIgnoreCase("male")) {
			patient.setGender(Enumerations.AdministrativeGender.MALE);
		} else if (gender.equalsIgnoreCase("female")) {
			patient.setGender(Enumerations.AdministrativeGender.FEMALE);
		} else {
			patient.setGender(Enumerations.AdministrativeGender.OTHER);
		}
		patient.setBirthDateElement(new DateType(dob));
		return patient;
	}

	private static Practitioner buildDoctorObject(String doctorName, String recordType) {
		Practitioner practitioner = new Practitioner();

		// Set the ID
		practitioner.setId("doctor1");

		// Create and set the Meta element
		Meta meta = new Meta();
		meta.setVersionId("1");
		meta.setLastUpdated(new Date());
		meta.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/Practitioner");
		practitioner.setMeta(meta);

		// Set the narrative text
		Narrative text = new Narrative();
		text.setStatus(Narrative.NarrativeStatus.GENERATED);
		text.setDivAsString("<div xmlns='http://www.w3.org/1999/xhtml'>" + recordType.toUpperCase() + "</div>");
		practitioner.setText(text);

		// Set the identifier elements
		Identifier identifier = new Identifier();
		Coding identifierCoding = new Coding();
		identifierCoding.setSystem("http://terminology.hl7.org/CodeSystem/v2-0203");
		identifierCoding.setCode("MD");
		identifierCoding.setDisplay("Medical License number");
		identifier.setType(new CodeableConcept().addCoding(identifierCoding));
		identifier.setSystem("https://nhpr.abdm.gov.in");
		identifier.setValue("123445");
		practitioner.addIdentifier(identifier);

		// Set the name element
		HumanName name = new HumanName();
		name.setText(doctorName);
		practitioner.addName(name);
		return practitioner;
	}

	private static Encounter buildEncounter(Reference patientSubject, String visitDate) throws ParseException {
		// Create an Encounter resource
		Encounter encounter = new Encounter();

		// Set the ID
		encounter.setId("encounter");

		// Create and set the Meta element
		Meta meta = new Meta();
		meta.setLastUpdated(new Date());
		meta.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/Encounter");
		encounter.setMeta(meta);

		// Set the narrative text
		Narrative text = new Narrative();
		text.setStatus(Narrative.NarrativeStatus.GENERATED);
		text.setDivAsString("<div xmlns='http://www.w3.org/1999/xhtml'>Generated Narrative</div>");
		encounter.setText(text);

		// Set the identifier element
		Identifier identifier = new Identifier();
		identifier.setSystem("https://ndhm.in");
		identifier.setValue("S100");
		encounter.addIdentifier(identifier);

		// Set the status
		encounter.setStatus(Encounter.EncounterStatus.FINISHED);

		// Set the class element
		Coding encounterClass = new Coding();
		encounterClass.setSystem("http://terminology.hl7.org/CodeSystem/v3-ActCode");
		encounterClass.setCode("VR");
		encounterClass.setDisplay("virtual");
		encounter.setClass_(encounterClass);

		// Set the subject element
		encounter.setSubject(patientSubject);

		// Set the period element
		Period period = new Period();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = dateFormat.parse(visitDate);
		period.setStart(date);
		encounter.setPeriod(period);

		// Set the diagnosis element
		Encounter.DiagnosisComponent diagnosis = new Encounter.DiagnosisComponent();
		Reference conditionReference = new Reference();
		conditionReference.setReference("Complaints/chief-complaints");
		conditionReference.setDisplay("Chief Complaint : NHA TEST- UHID No.18");
		diagnosis.setCondition(conditionReference);

		Coding diagnosisUseCoding = new Coding();
		diagnosisUseCoding.setSystem("http://snomed.info/sct");
		diagnosisUseCoding.setCode("33962009");
		diagnosisUseCoding.setDisplay("Chief Complain");
		diagnosis.setUse(new CodeableConcept().addCoding(diagnosisUseCoding));
		encounter.addDiagnosis(diagnosis);
		return encounter;
	}

	private static Condition buildChiefComplaints() {
		// Create a Condition resource
		Condition condition = new Condition();

		// Set the ID
		condition.setId("chief-complaints");

		// Create and set the Meta element
		Meta meta = new Meta();
		meta.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/Condition");
		condition.setMeta(meta);

		// Set the narrative text
		Narrative text = new Narrative();
		text.setStatus(Narrative.NarrativeStatus.GENERATED);
		text.setDivAsString("<div xmlns='http://www.w3.org/1999/xhtml'>Generated Narrative</div>");
		condition.setText(text);

		// Set the clinical status
		CodeableConcept clinicalStatus = new CodeableConcept();
		Coding clinicalStatusCoding = new Coding();
		clinicalStatusCoding.setSystem("http://snomed.info/sct");
		clinicalStatusCoding.setCode("active");
		clinicalStatusCoding.setDisplay("Active");
		clinicalStatus.addCoding(clinicalStatusCoding);
		condition.setClinicalStatus(clinicalStatus);

		// Set the condition code
		CodeableConcept code = new CodeableConcept();
		Coding codeCoding = new Coding();
		codeCoding.setSystem("http://snomed.info/sct");
		codeCoding.setCode("297142003");
		codeCoding.setDisplay("DENGUE FEVER");
		code.addCoding(codeCoding);
		code.setText("DENGUE FEVER");
		condition.setCode(code);

		// Set the subject element
		Reference subject = new Reference();
		subject.setReference("Patient/patient1");
		subject.setDisplay("NHA TEST- UHID No.18");
		condition.setSubject(subject);

		return condition;
	}

	private static Observation createObservation(String id, String profile, String divText, String codeSystem,
			String code, String codeDisplay, String subjectReference, double value, String unit, String unitSystem,
			String unitCode) {
		Observation observation = new Observation();
		observation.setId(id);

		Meta meta = new Meta();
		meta.addProfile(profile);
		observation.setMeta(meta);

		Narrative text = new Narrative();
		text.setStatus(Narrative.NarrativeStatus.GENERATED);
		text.setDivAsString(divText);
		observation.setText(text);

		observation.setStatus(Observation.ObservationStatus.FINAL);

		CodeableConcept category = new CodeableConcept();
		Coding categoryCoding = new Coding();
		categoryCoding.setSystem("http://terminology.hl7.org/CodeSystem/observation-category");
		categoryCoding.setCode("vital-signs");
		categoryCoding.setDisplay("Vital Signs");
		category.addCoding(categoryCoding);
		category.setText("Vital Signs");
		observation.addCategory(category);

		CodeableConcept observationCode = new CodeableConcept();
		Coding codeCoding = new Coding();
		codeCoding.setSystem(codeSystem);
		codeCoding.setCode(code);
		codeCoding.setDisplay(codeDisplay);
		observationCode.addCoding(codeCoding);
		observationCode.setText(codeDisplay);
		observation.setCode(observationCode);

		Reference subject = new Reference();
		subject.setReference(subjectReference);
		observation.setSubject(subject);

		observation.setEffective(new DateTimeType("2024-07-30T07:14:09.381Z"));

		Quantity valueQuantity = new Quantity();
		valueQuantity.setValue(value);
		valueQuantity.setUnit(unit);
		valueQuantity.setSystem(unitSystem);
		valueQuantity.setCode(unitCode);
		observation.setValue(valueQuantity);

		return observation;
	}

	public static Appointment Appointment() {
		Appointment appointment = new Appointment();
		appointment.setId("appointment");
		Meta meta = new Meta();
		meta.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/Appointment");
		appointment.setMeta(meta);
		Narrative text = new Narrative();
		text.setStatusAsString("generated");
		text.setDivAsString("<div xmlns='http://www.w3.org/1999/xhtml'>Appointment</div>");
		appointment.setText(text);
		appointment.setStatus(AppointmentStatus.BOOKED);
		Coding serviceCategoryCoding = new Coding();
		serviceCategoryCoding.setSystem("http://snomed.info/sct");
		serviceCategoryCoding.setCode("408443003");
		serviceCategoryCoding.setDisplay("General medical practice");
		CodeableConcept serviceCategory = new CodeableConcept();
		serviceCategory.addCoding(serviceCategoryCoding);
		appointment.addServiceCategory(serviceCategory);
		Coding serviceTypeCoding = new Coding();
		serviceTypeCoding.setSystem("http://snomed.info/sct");
		serviceTypeCoding.setCode("11429006");
		serviceTypeCoding.setDisplay("Consultation");
		CodeableConcept serviceType = new CodeableConcept();
		serviceType.addCoding(serviceTypeCoding);
		appointment.addServiceType(serviceType);
		Coding appointmentTypeCoding = new Coding();
		appointmentTypeCoding.setSystem("http://snomed.info/sct");
		appointmentTypeCoding.setCode("185389009");
		appointmentTypeCoding.setDisplay("Follow-up visit");
		CodeableConcept appointmentType = new CodeableConcept();
		appointmentType.addCoding(appointmentTypeCoding);
		appointment.setAppointmentType(appointmentType);
		Reference reasonReference = new Reference();
		reasonReference.setReference("Complaints/chief-complaints");
		reasonReference.setDisplay("Complaints");
		appointment.addReasonReference(reasonReference);
		appointment.setDescription("Re-Visit with the Follow-Up Adviced Lab Reports :BLOOD GLUCOSE(FASTING)");
		appointment.setStartElement(new InstantType("2024-08-01T04:30:00.380Z"));
		appointment.setEndElement(new InstantType("2024-08-01T08:30:00.380Z"));
		appointment.setCreatedElement(new DateTimeType("2024-07-30T07:14:09.381Z"));
		Reference basedOnReference = new Reference();
		basedOnReference.setReference("Investigation Advice/service");
		basedOnReference.setDisplay("Investigation Advice");
		appointment.addBasedOn(basedOnReference);
		AppointmentParticipantComponent patientParticipant = new AppointmentParticipantComponent();
		Reference patientReference = new Reference();
		patientReference.setReference("Patient/patient1");
		patientReference.setDisplay(" NHA TEST");
		patientParticipant.setActor(patientReference);
		patientParticipant.setStatus(Appointment.ParticipationStatus.ACCEPTED);
		appointment.addParticipant(patientParticipant);
		AppointmentParticipantComponent practitionerParticipant = new AppointmentParticipantComponent();
		Reference practitionerReference = new Reference();
		practitionerReference.setReference("Practitioner/doctor1");
		practitionerReference.setDisplay("Dr. LOKESH SHARMA");
		practitionerParticipant.setActor(practitionerReference);
		practitionerParticipant.setStatus(Appointment.ParticipationStatus.ACCEPTED);
		appointment.addParticipant(practitionerParticipant);
		return appointment;
	}

	public static ServiceRequest createServiceRequest() {
		ServiceRequest serviceRequest = new ServiceRequest();
		serviceRequest.setId("service");
		Meta meta = new Meta();
		meta.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/ServiceRequest");
		serviceRequest.setMeta(meta);
		Narrative text = new Narrative();
		text.setStatusAsString("generated");
		text.setDivAsString("<div xmlns='http://www.w3.org/1999/xhtml'>Generated Narrative</div>");
		serviceRequest.setText(text);
		serviceRequest.setStatus(ServiceRequest.ServiceRequestStatus.ACTIVE);
		serviceRequest.setIntent(ServiceRequest.ServiceRequestIntent.ORDER);
		Coding categoryCoding = new Coding();
		categoryCoding.setSystem("http://snomed.info/sct");
		categoryCoding.setCode("108252007");
		categoryCoding.setDisplay("Laboratory Procedure");
		CodeableConcept category = new CodeableConcept();
		category.addCoding(categoryCoding);
		serviceRequest.addCategory(category);
		Coding codeCoding = new Coding();
		codeCoding.setSystem("http://snomed.info/sct");
		codeCoding.setCode("35774004");
		codeCoding.setDisplay("Blood Profile");
		CodeableConcept code = new CodeableConcept();
		code.addCoding(codeCoding);
		code.setText("LIPID PROFILE");
		serviceRequest.setCode(code);
		Reference subjectReference = new Reference();
		subjectReference.setReference("Patient/patient1");
		subjectReference.setDisplay(" NHA TEST- UHID No.18");
		serviceRequest.setSubject(subjectReference);
		serviceRequest.setAuthoredOnElement(new DateTimeType("2024-07-30T07:14:09.381Z"));
		Reference requesterReference = new Reference();
		requesterReference.setReference("Practitioner/doctor1");
		requesterReference.setDisplay("Consultant");
		serviceRequest.setRequester(requesterReference);
		return serviceRequest;
	}

	public static MedicationRequest createMedicationRequest(int id, String medicationText, String medicationCode,
			String medicationDisplay, String patientReference, String patientDisplay, String authoredOn,
			String requesterReference, String requesterDisplay, String reasonCode, String reasonDisplay,
			String dosageText, String routeCode, String routeDisplay) {
		MedicationRequest medicationRequest = new MedicationRequest();
		medicationRequest.setId(Integer.toString(id));
		Meta meta = new Meta();
		meta.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/MedicationRequest");
		medicationRequest.setMeta(meta);
		Narrative text = new Narrative();
		text.setStatusAsString("generated");
		text.setDivAsString(
				"<div xmlns='http://www.w3.org/1999/xhtml'>Generated Narrative with Details MedicationRequest </div>");
		medicationRequest.setText(text);
		medicationRequest.setStatus(MedicationRequest.MedicationRequestStatus.ACTIVE);
		medicationRequest.setIntent(MedicationRequest.MedicationRequestIntent.ORDER);
		Coding medicationCoding = new Coding();
		medicationCoding.setSystem("http://snomed.info/sct");
		medicationCoding.setCode(medicationCode);
		medicationCoding.setDisplay(medicationDisplay);
		CodeableConcept medicationCodeableConcept = new CodeableConcept();
		medicationCodeableConcept.addCoding(medicationCoding);
		medicationCodeableConcept.setText(medicationText);
		medicationRequest.setMedication(medicationCodeableConcept);
		Reference subjectReference = new Reference();
		subjectReference.setReference(patientReference);
		subjectReference.setDisplay(patientDisplay);
		medicationRequest.setSubject(subjectReference);
		medicationRequest.setAuthoredOnElement(new DateTimeType(authoredOn));
		Reference requesterReferenceObj = new Reference();
		requesterReferenceObj.setReference(requesterReference);
		requesterReferenceObj.setDisplay(requesterDisplay);
		medicationRequest.setRequester(requesterReferenceObj);
		Coding reasonCoding = new Coding();
		reasonCoding.setSystem("http://snomed.info/sct");
		reasonCoding.setCode(reasonCode);
		reasonCoding.setDisplay(reasonDisplay);
		CodeableConcept reasonCodeableConcept = new CodeableConcept();
		reasonCodeableConcept.addCoding(reasonCoding);
		medicationRequest.addReasonCode(reasonCodeableConcept);
		Dosage dosage = new Dosage();
		dosage.setText(dosageText);
		Coding routeCoding = new Coding();
		routeCoding.setSystem("http://snomed.info/sct");
		routeCoding.setCode(routeCode);
		routeCoding.setDisplay(routeDisplay);
		CodeableConcept route = new CodeableConcept();
		route.addCoding(routeCoding);
		dosage.setRoute(route);
		medicationRequest.addDosageInstruction(dosage);
		return medicationRequest;
	}

	public static Binary createBinaryResource(String id, String profile, String contentType, String data) {
		Binary binary = new Binary();
		binary.setId(id);
		Meta meta = new Meta();
		meta.addProfile(profile);
		binary.setMeta(meta);
		binary.setContentType(contentType);
		binary.setData(data.getBytes());
		return binary;
	}

	public static Observation buildObservationPulseRate(int pulseRate) {
		return createObservation("pulse-rate",
				"https://nrces.in/ndhm/fhir/r4/StructureDefinition/ObservationVitalSigns",
				"<div xmlns='http://www.w3.org/1999/xhtml'>Generated Narrative with Details pulse-rate</div>",
				"http://snomed.info/sct", "364075005", "Heart rate", "Patient/patient1", pulseRate, "beats/minute",
				"http://unitsofmeasure.org", "258983007");
	}

	public static Observation observationSystolicBloodPressure(int systolicRange) {
		return createObservation("Systolic-blood-pressure",
				"https://nrces.in/ndhm/fhir/r4/StructureDefinition/ObservationVitalSigns",
				"<div xmlns='http://www.w3.org/1999/xhtml'>Generated Narrative with Details Systolic-blood-pressure</div>",
				"http://snomed.info/sct", "364075005", "Systolic Blood Pressure", "Patient/patient1", systolicRange, "mmHg",
				"http://unitsofmeasure.org", "mm[Hg]");
	}

	public static Observation observationDiastolicBloodPressure(int booldPressure) {
		return createObservation("Diastolic-blood-pressure",
				"https://nrces.in/ndhm/fhir/r4/StructureDefinition/ObservationVitalSigns",
				"<div xmlns='http://www.w3.org/1999/xhtml'>Generated Narrative with Details Diastolic-blood-pressure</div>",
				"http://snomed.info/sct", "364075005", "Diastolic Blood Pressure", "Patient/patient1", booldPressure, "mmHg",
				"http://unitsofmeasure.org", "mm[Hg]");
	}

	public static Observation observationBodyTemperature(double bodyTemperature) {
		return createObservation("body-temperature",
				"https://nrces.in/ndhm/fhir/r4/StructureDefinition/ObservationVitalSigns",
				"<div xmlns='http://www.w3.org/1999/xhtml'>Generated Narrative with Details body-temperature</div>",
				"http://snomed.info/sct", "246508008", "Body surface temperature", "Patient/patient1", bodyTemperature, "Cel",
				"http://unitsofmeasure.org", "{Cel or degF}");
	}

	public static Observation observationBodyHeight(int bodyHeight) {
		return createObservation("body-height",
				"https://nrces.in/ndhm/fhir/r4/StructureDefinition/ObservationVitalSigns",
				"<div xmlns='http://www.w3.org/1999/xhtml'>Generated Narrative with Details Body height</div>",
				"http://snomed.info/sct", "50373000", "Body height", "Patient/patient1", bodyHeight, "cm",
				"http://unitsofmeasure.org", "{[in_us],cm,m}");
	}

	public static Observation observationBodyWeight(int bodyWeight) {
		return createObservation("body-weight",
				"https://nrces.in/ndhm/fhir/r4/StructureDefinition/ObservationVitalSigns",
				"<div xmlns='http://www.w3.org/1999/xhtml'>Generated Narrative with Details Body weight</div>",
				"http://snomed.info/sct", "248345008", "Body weight", "Patient/patient1", bodyWeight, "kg",
				"http://unitsofmeasure.org", "{[lb_av],kg,g}");
	}

	public static Observation observationBloodGlucose(int glucoseValue) {
		return createObservation("BloodGlucose",
				"https://nrces.in/ndhm/fhir/r4/StructureDefinition/ObservationVitalSigns",
				"<div xmlns='http://www.w3.org/1999/xhtml'>Generated Narrative with Details BloodGlucose</div>",
				"http://snomed.info/sct", "33747003", "Glucose [Mass/volume] in Blood", "Patient/patient1", glucoseValue,
				"mg/dL", "http://unitsofmeasure.org", "mg/dL");
	}

	public static Observation buildObservationOxygenSaturation(int spO2) {
		return createObservation("sp-o2", "https://nrces.in/ndhm/fhir/r4/StructureDefinition/ObservationVitalSigns",
				"<div xmlns='http://www.w3.org/1999/xhtml'>Generated Narrative with Details oxygen-saturation</div>",
				"http://snomed.info/sct", "250554003", "SpO2 measurement", "Patient/patient1", spO2, "Percentage Unit",
				"http://unitsofmeasure.org", "415067009");
	}

}
