package com.dipl.abha.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Period;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.Quantity;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.ServiceRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dipl.abha.dto.AccountProfile;
import com.dipl.abha.dto.OrgnizationRegistrationDto;
import com.dipl.abha.dto.VitalHistory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import ca.uhn.fhir.context.FhirContext;

@Service
public class SfhirB {

	private static final Logger LOGGER = LoggerFactory.getLogger(SfhirB.class);

	@Autowired
	private JdbcTemplateHelper jdbcTemplateHelper;

//	public static void main(String[] args) throws ParseException {
	public String buildFhirBundle(String doctorName, String chiefCompaints, String medication, String medDirections,
			String visitDate, String fileType, String visitEndTime, String visitStartTime, VitalHistory history,
			AccountProfile profile, byte[] fileByte, String ConsulstionDate, String allinvestigationAdvice,
			String clinicName, String followUpDate)
			throws ParseException, JsonMappingException, JsonProcessingException {
		
		LOGGER.info("===== followUpDate ======== ----->" + followUpDate);
		
		String dateTime = visitDate + "T" + visitStartTime + "Z";
		LOGGER.info("===== dateTime ======== ----->" + dateTime);
		System.out.println(dateTime);

		LOGGER.info("===== date ======== ----->" + ConsulstionDate);
		System.out.println(ConsulstionDate);
		String Date = ConsulstionDate.replace("T", " ");

		String isoDate = ConsulstionDate;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
		LocalDateTime dateTime1 = LocalDateTime.parse(isoDate, DateTimeFormatter.ISO_DATE_TIME);
		String formattedDate = dateTime1.format(formatter);

		OrgnizationRegistrationDto dto2 = new OrgnizationRegistrationDto();
		dto2.setOrgnizationContactNo("9912582285");
		dto2.setOrgnizationEmail("shiva@gmail.com");

		LocalDate date1 = LocalDate.of(Integer.parseInt(profile.getYearOfBirth()),
				Integer.parseInt((profile.getMonthOfBirth() == null || profile.getMonthOfBirth().isEmpty()) ? "01"
						: profile.getMonthOfBirth()),
				Integer.parseInt((profile.getDayOfBirth() == null || profile.getDayOfBirth().isEmpty()) ? "01"
						: profile.getDayOfBirth()));

		DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String dateOfBirth1 = date1.format(formatter1);

		// Create a Bundle resource
		Bundle bundle = new Bundle();
		// Set the ID
		String bundleId = UUID.randomUUID().toString();
		bundle.setId(bundleId);

		// Create and set the Meta element
		Meta meta = new Meta();
		meta.setVersionId("1");
		meta.setLastUpdatedElement(new InstantType(formattedDate));
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

		Instant instant = dateTime1.atZone(ZoneId.systemDefault()).toInstant();

		// Convert Instant to Timestamp
		Timestamp timestamp = Timestamp.from(instant);

		// In the Bundle need to add at least one Tiemstamp.
		bundle.setTimestamp(timestamp);

		// Set the identifier
		Identifier identifierOne = new Identifier();
		identifierOne.setSystem("http://hip.in");
		identifierOne.setValue("305fecc2-4ba2-46cc-9ccd-efa755aff51d");
		bundle.setIdentifier(identifierOne);

		// Set the type to document
		bundle.setType(Bundle.BundleType.DOCUMENT);

		// Create a Composition resource
		Composition composition = new Composition();
		composition.setId("FHIR120");

		Meta compositionMeta = new Meta();
		compositionMeta.setVersionId("1");
		compositionMeta.setLastUpdated(timestamp);
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
		typeCoding.setDisplay("Clinical consultation report");
		type.addCoding(typeCoding);
		type.setText("Clinical Consultation Record");
		composition.setType(type);

		String patientId = UUID.randomUUID().toString();
		Reference subject = new Reference();
		subject.setReference("urn:Patient:" + patientId);
		subject.setDisplay(profile.getFirstName() + " " + profile.getLastName());
		composition.setSubject(subject);

		String encountId = UUID.randomUUID().toString();
		Reference encounter = new Reference();
		encounter.setReference("urn:encounter:" + encountId);
		encounter.setDisplay("NHA TEST- UHID No.18");
		composition.setEncounter(encounter);
		composition.setDate(timestamp);

		String practitionerId = UUID.randomUUID().toString();
		Reference author = new Reference();
		author.setReference("urn:Practitioner:" + practitionerId);
		author.setDisplay(doctorName);
		composition.addAuthor(author);
		composition.setTitle("Consultation Report");

		Reference custodian = new Reference();
		custodian.setReference("urn:Practitioner:" + practitionerId);
		custodian.setDisplay(doctorName);
		composition.setCustodian(custodian);

		String organizationId = UUID.randomUUID().toString();
		Reference organization = new Reference();
		organization.setReference("urn:Organization:" + organizationId);
		organization.setDisplay(clinicName);
		composition.setCustodian(organization);
		List<String> compaintIds = null;

		if (chiefCompaints != null && !chiefCompaints.isEmpty()) {
			String[] split = chiefCompaints.split(",");
			if (split.length > 0) {
				compaintIds = new ArrayList<String>();
				List<Reference> referenceList = new ArrayList<Reference>();
				for (String s : split) {
					String complaintsId = UUID.randomUUID().toString();
					referenceList.add(createEntry("urn:ChiefComplaints:" + complaintsId, s));
					compaintIds.add(complaintsId);
				}
				composition.addSection(createSection("Chief Complaints", "http://snomed.info/sct", "422843007",
						"Chief complaint section", referenceList));

			}
		}

		List<Reference> vitalReferenceList = null;
		String diasBloodPresssureId = null;
		String pulseRateId = null;
		String spO2Id = null;
		String bodytempId = null;
		String sysBloodPresssureId = null;
		String bloodGlucoseId = null;
		String bodyHeightId = null;
		String bodyWeightId = null;
		if (history != null) {
			if (history.getDiastolic() != null && !history.getDiastolic().isEmpty()) {
				diasBloodPresssureId = UUID.randomUUID().toString();
				vitalReferenceList = this.checkVitalHistoryAndAdd(vitalReferenceList,
						"urn:DiastolicBloodPressure:" + diasBloodPresssureId);
			}

			if (history.getPulse() != null && !history.getPulse().isEmpty()) {
				pulseRateId = UUID.randomUUID().toString();
				vitalReferenceList = this.checkVitalHistoryAndAdd(vitalReferenceList, "urn:PulseRate:" + pulseRateId);
			}

			if (history.getOxigen_count() != null && !history.getOxigen_count().isEmpty()) {
				spO2Id = UUID.randomUUID().toString();
				vitalReferenceList = this.checkVitalHistoryAndAdd(vitalReferenceList, "urn:OxygenSaturation:" + spO2Id);
			}

			if (history.getTemperature() != null && !history.getTemperature().isEmpty()) {
				bodytempId = UUID.randomUUID().toString();
				vitalReferenceList = this.checkVitalHistoryAndAdd(vitalReferenceList,
						"urn:BodyTemperature:" + bodytempId);
			}

			if (history.getSystolic() != null && !history.getSystolic().isEmpty()) {
				sysBloodPresssureId = UUID.randomUUID().toString();
				vitalReferenceList = this.checkVitalHistoryAndAdd(vitalReferenceList,
						"urn:SystolicBloodPressure:" + sysBloodPresssureId);
			}

			if (history.getDiabetic_value() != null && !history.getDiabetic_value().isEmpty()) {
				bloodGlucoseId = UUID.randomUUID().toString();
				vitalReferenceList = this.checkVitalHistoryAndAdd(vitalReferenceList,
						"urn:BloodGlucose:" + bloodGlucoseId);
			}

			if (history.getHeight() != null && !history.getHeight().isEmpty()) {
				bodyHeightId = UUID.randomUUID().toString();
				vitalReferenceList = this.checkVitalHistoryAndAdd(vitalReferenceList, "urn:BodyHeight:" + bodyHeightId);
			}

			if (history.getWeight() != null && !history.getWeight().isEmpty()) {
				bodyWeightId = UUID.randomUUID().toString();
				vitalReferenceList = this.checkVitalHistoryAndAdd(vitalReferenceList, "urn:BodyWeight:" + bodyWeightId);
			}
			composition.addSection(createSection("Vital Signs", null, null, null, vitalReferenceList));
		}

		String appointmentId = UUID.randomUUID().toString();

		composition.addSection(
				createSection("Follow Up", "http://snomed.info/sct", "308273005", "Follow-up status (finding)",
						List.of(createEntry("urn:Appointment:" + appointmentId, "Appointment: NHA TEST- UHID No.18"))));

		List<String> investigationadviceIds = null;

		if (allinvestigationAdvice != null && !allinvestigationAdvice.isEmpty()) {
			String[] split = allinvestigationAdvice.split(",");
			if (split.length > 0) {
				investigationadviceIds = new ArrayList<String>();
				List<Reference> referenceList = new ArrayList<Reference>();
				for (String s : split) {
					String investigationadviceId = UUID.randomUUID().toString();
					referenceList.add(createEntry("urn:InvestigationAdvice:" + investigationadviceId, s));
					investigationadviceIds.add(investigationadviceId);
				}
				composition.addSection(createSection("Investigation Advice", "http://snomed.info/sct", "721963009",
						"Order document", referenceList));

			}
		}

		List<String> medicationStatementID = null;
		if (medication != null && !medication.isEmpty()) {
			String[] split = medication.split(",");
			if (split.length > 0) {
				List<Reference> referenceList = new ArrayList<Reference>();
				medicationStatementID = new ArrayList<String>();
				for (String s : split) {
					String m = UUID.randomUUID().toString();
					referenceList.add(createEntry("urn:MedicationStatement:" + m, s));
					medicationStatementID.add(m);
				}
				composition.addSection(createSection("Medications", "http://snomed.info/sct", "721912009",
						"Medication summary document", referenceList));
			}
		}

		Composition.SectionComponent section = new Composition.SectionComponent();
		section.setTitle(fileType);
		CodeableConcept sectionCode = new CodeableConcept();
		sectionCode.addCoding().setSystem("http://snomed.info/sct").setCode("440545006")
				.setDisplay("Prescription record (record artifact)");
		section.setCode(sectionCode);
		String binaryID = UUID.randomUUID().toString();
		section.addEntry(new Reference("urn:binary:" + binaryID));
		composition.addSection(section);

		// Add the Composition resource to the bundle
		String compositionID = UUID.randomUUID().toString();
		Bundle.BundleEntryComponent entryComposition = new Bundle.BundleEntryComponent();
		entryComposition.setFullUrl("urn:composition:" + compositionID);
		entryComposition.setResource(composition);
		bundle.addEntry(entryComposition);

		Bundle.BundleEntryComponent entryPractitioner = new Bundle.BundleEntryComponent();
		entryPractitioner.setFullUrl("urn:Practitioner:" + practitionerId);
		entryPractitioner.setResource(SfhirB.buildDoctorObject(doctorName, "Prescription"));
		bundle.addEntry(entryPractitioner);

		Bundle.BundleEntryComponent entryOrganization = new Bundle.BundleEntryComponent();
		entryOrganization.setFullUrl("urn:Organization:" + organizationId);
		entryOrganization
				.setResource(createOrganization(organizationId, clinicName, "7763985275", "dhanush@support.com"));
		bundle.addEntry(entryOrganization);

		Bundle.BundleEntryComponent entryPatient = new Bundle.BundleEntryComponent();
		entryPatient.setFullUrl("urn:Patient:" + patientId);
		entryPatient.setResource(SfhirB.buildPatientObject(patientId, profile.getHealthIdNumber(), "10020",
				profile.getFirstName() + "" + profile.getLastName(), profile.getMobile(), profile.getGender(),
				dateOfBirth1, "patient"));
		bundle.addEntry(entryPatient);

		Bundle.BundleEntryComponent entryEncounter = new Bundle.BundleEntryComponent();
		entryEncounter.setFullUrl("urn:encounter:" + encountId);
		entryEncounter.setResource(SfhirB.buildEncounter(subject, Date, encountId, compaintIds, timestamp));
		bundle.addEntry(entryEncounter);
		if (history != null) {
			if (history.getPulse() != null && !history.getPulse().isEmpty()) {
				Bundle.BundleEntryComponent observation1 = new Bundle.BundleEntryComponent();
				observation1.setFullUrl("urn:PulseRate:" + pulseRateId);
				observation1.setResource(
						SfhirB.buildObservationPulseRate(history.getPulse(), patientId, pulseRateId, dateTime));
				bundle.addEntry(observation1);
			}

			if (history.getOxigen_count() != null && !history.getOxigen_count().isEmpty()) {
				Bundle.BundleEntryComponent observation2 = new Bundle.BundleEntryComponent();
				observation2.setFullUrl("urn:OxygenSaturation:" + spO2Id);
				observation2.setResource(SfhirB.buildObservationOxygenSaturation(history.getOxigen_count(), patientId,
						spO2Id, dateTime));
				bundle.addEntry(observation2);
			}

			if (history.getTemperature() != null && !history.getTemperature().isEmpty()) {
				Bundle.BundleEntryComponent observation3 = new Bundle.BundleEntryComponent();
				observation3.setFullUrl("urn:BodyTemperature:" + bodytempId);
				observation3.setResource(
						SfhirB.observationBodyTemperature(history.getTemperature(), patientId, bodytempId, dateTime));
				bundle.addEntry(observation3);
			}

			if (history.getSystolic() != null && !history.getSystolic().isEmpty()) {
				Bundle.BundleEntryComponent observation4 = new Bundle.BundleEntryComponent();
				observation4.setFullUrl("urn:SystolicBloodPressure:" + sysBloodPresssureId);
				observation4.setResource(SfhirB.observationSystolicBloodPressure(history.getSystolic(), patientId,
						sysBloodPresssureId, dateTime));
				bundle.addEntry(observation4);
			}

			if (history.getDiastolic() != null && !history.getDiastolic().isEmpty()) {
				Bundle.BundleEntryComponent observation5 = new Bundle.BundleEntryComponent();
				observation5.setFullUrl("urn:DiastolicBloodPressure:" + diasBloodPresssureId);
				observation5.setResource(SfhirB.observationDiastolicBloodPressure(history.getDiastolic(), patientId,
						diasBloodPresssureId, dateTime));
				bundle.addEntry(observation5);
			}

			if (history.getDiabetic_value() != null && history.getDiabetic_value() != "") {
				Bundle.BundleEntryComponent observation6 = new Bundle.BundleEntryComponent();
				observation6.setFullUrl("urn:BloodGlucose:" + bloodGlucoseId);
				observation6.setResource(SfhirB.observationBloodGlucose(history.getDiabetic_value(), patientId,
						bloodGlucoseId, dateTime));
				bundle.addEntry(observation6);
			}

			if (history.getHeight() != null && !history.getHeight().isEmpty()) {
				Bundle.BundleEntryComponent observation7 = new Bundle.BundleEntryComponent();
				observation7.setFullUrl("urn:BodyHeight:" + bodyHeightId);
				observation7.setResource(
						SfhirB.observationBodyHeight(history.getHeight(), patientId, bodyHeightId, dateTime));
				bundle.addEntry(observation7);
			}

			if (history.getWeight() != null && !history.getWeight().isEmpty()) {
				Bundle.BundleEntryComponent observation8 = new Bundle.BundleEntryComponent();
				observation8.setFullUrl("urn:BodyWeight:" + bodyWeightId);
				observation8.setResource(
						SfhirB.observationBodyWeight(history.getWeight(), patientId, bodyWeightId, dateTime));
				bundle.addEntry(observation8);
			}
		}
		Map<String, String> chiefCompaintsMap = new HashMap<>();
		if (compaintIds != null && compaintIds.size() > 0) {
			String[] split = chiefCompaints.split(",");
			int i = 0;
			for (String s : compaintIds) {
				Bundle.BundleEntryComponent chiefComplaintsEntry = new Bundle.BundleEntryComponent();
				chiefComplaintsEntry.setFullUrl("urn:ChiefComplaints:" + s);
				chiefComplaintsEntry.setResource(SfhirB.buildChiefComplaints(patientId, s, split[i]));
				bundle.addEntry(chiefComplaintsEntry);
				chiefCompaintsMap.put(split[i], s);
				i++;
			}

		}

		String[] advice = allinvestigationAdvice.split(",");
		Map<String, String> investigationadvicesMap = new HashMap<>();
		if (advice != null && advice.length > 0) {
			if (investigationadviceIds != null) {
				int i = 0;
				for (String id : investigationadviceIds) {
					Bundle.BundleEntryComponent labServices = new Bundle.BundleEntryComponent();
					labServices.setFullUrl("urn:InvestigationAdvice:" + id);
					labServices.setResource(
							SfhirB.createServiceRequest(patientId, practitionerId, id, dateTime, advice[i]));
					bundle.addEntry(labServices);
					investigationadvicesMap.put(advice[i], id);
					i++;
				}

			}
		}

		if (followUpDate != null && !followUpDate.isEmpty()) {
			Bundle.BundleEntryComponent appointment = new Bundle.BundleEntryComponent();
			appointment.setFullUrl("urn:Appointment:" + appointmentId);
			appointment.setResource(SfhirB.Appointment(patientId, practitionerId, appointmentId, chiefCompaintsMap,
					investigationadvicesMap, doctorName, dateTime, visitStartTime, visitEndTime, followUpDate,
					profile.getFirstName() + " " + profile.getLastName()));
			bundle.addEntry(appointment);
		}

		// Bundle Entry for Binary
		BundleEntryComponent entryBinary = new BundleEntryComponent();
		entryBinary.setFullUrl("urn:binary:" + binaryID);
		Binary binary = new Binary();
		binary.setId(binaryID);
		Meta binaryMeta = new Meta();
		binaryMeta.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/Binary");
		binary.setMeta(binaryMeta);
		binary.setContentType("application/pdf");
		binary.setData(fileByte);
		entryBinary.setResource(binary);
		bundle.addEntry(entryBinary);

		if ((medication != null && !medication.isEmpty()) && (medDirections != null && !medDirections.isEmpty())) {
			String[] split = medication.split(",");
			String[] split1 = medDirections.split(",");
			if (split != null && split1 != null && split.length > 0 && split1.length > 0) {
				if (split.length == split1.length) {
					if (medicationStatementID != null) {
						for (int i = 0; i < medicationStatementID.size(); i++) {
							MedicationRequest medicationRequest1 = createMedicationRequest(medicationStatementID.get(i),
									split[i], "384977007", "Paracetamol", "urn:Patient:" + patientId, "NHA TEST",
									dateTime, "urn:Practitioner:" + practitionerId, doctorName, "386661006", "Fever",
									split1[i], "26643006", "Oral Route");
							Bundle.BundleEntryComponent medReq1 = new Bundle.BundleEntryComponent();
							medReq1.setFullUrl("urn:MedicationStatement:" + medicationStatementID.get(i));
							medReq1.setResource(medicationRequest1);
							bundle.addEntry(medReq1);
						}
					}
				}
			}
		}

		FhirContext ctx = FhirContext.forR4();
		String jsonString = ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle);
		System.out.println(jsonString);

		return jsonString;

	}

	private List<Reference> checkVitalHistoryAndAdd(List<Reference> vitalReferenceList, String referenceId) {

		if (vitalReferenceList != null) {
			vitalReferenceList.add(createEntry(referenceId));
		} else {
			vitalReferenceList = new ArrayList<>();
			vitalReferenceList.add(createEntry(referenceId));
		}

		return vitalReferenceList;
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

	public static Organization createOrganization(String id, String name, String contactNo, String email) {
		// Create Organization resource
		Organization organization = new Organization();

		// Set ID
		organization.setId(id);

		// Set profile metadata
		organization.getMeta().addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/Organization");

		// Set identifier
		Identifier identifier = new Identifier();
		CodeableConcept identifierType = new CodeableConcept();
		identifierType.addCoding(new Coding().setSystem("http://terminology.hl7.org/CodeSystem/v2-0203").setCode("PRN")
				.setDisplay("Provider number"));
		identifier.setType(identifierType);
		identifier.setSystem("https://facility.ndhm.gov.in");
		identifier.setValue("4567823");
		organization.addIdentifier(identifier);

		// Set name
		organization.setName(name);

		// Set telecom (phone)
		ContactPoint telecomPhone = new ContactPoint();
		telecomPhone.setSystem(ContactPoint.ContactPointSystem.PHONE);
		telecomPhone.setValue("+91 " + contactNo);
		telecomPhone.setUse(ContactPoint.ContactPointUse.WORK);
		organization.addTelecom(telecomPhone);

		// Set telecom (email)
		ContactPoint telecomEmail = new ContactPoint();
		telecomEmail.setSystem(ContactPoint.ContactPointSystem.EMAIL);
		telecomEmail.setValue(email);
		telecomEmail.setUse(ContactPoint.ContactPointUse.WORK);
		organization.addTelecom(telecomEmail);

		return organization;
	}

	private static Patient buildPatientObject(String patientId, String abhaId, String interactoinId, String patientName,
			String mobileNo, String gender, String dob, String recordType) {
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

		// Set the narrative text
		Narrative text = new Narrative();
		text.setStatus(Narrative.NarrativeStatus.GENERATED);
		text.setDivAsString("<div xmlns='http://www.w3.org/1999/xhtml'>" + recordType.toUpperCase() + "</div>");
		patient.setText(text);

		// Set the identifier elements
		Identifier mrIdentifier = new Identifier();
		Coding mrCoding = new Coding();
		mrCoding.setSystem("http://terminology.hl7.org/CodeSystem/v2-0203");
		mrCoding.setCode("MR");
		mrCoding.setDisplay("Medical record number");
		mrIdentifier.setType(new CodeableConcept().addCoding(mrCoding));
		mrIdentifier.setSystem("https://healthid.ndhm.gov.in");
		mrIdentifier.setValue(interactoinId);

		patient.addIdentifier(mrIdentifier);

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

		if (gender.equalsIgnoreCase("m")) {
			patient.setGender(Enumerations.AdministrativeGender.MALE);
		} else if (gender.equalsIgnoreCase("f")) {
			patient.setGender(Enumerations.AdministrativeGender.FEMALE);
		} else {
			patient.setGender(Enumerations.AdministrativeGender.OTHER);
		}
		patient.setBirthDateElement(new DateType(dob));

		return patient;
	}

	public static Practitioner buildDoctorObject(String doctorName, String recordType) {
		Practitioner practitioner = new Practitioner();

	
		practitioner.setId("1");

		
		Meta meta = new Meta();
		meta.setVersionId("1");
		meta.setLastUpdated(new Date());
		meta.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/Practitioner");
		practitioner.setMeta(meta);

	
		Narrative text = new Narrative();
		text.setStatus(Narrative.NarrativeStatus.GENERATED);
		text.setDivAsString("<div xmlns=\"http://www.w3.org/1999/xhtml\"><p><b>Generated Narrative: Practitioner</b><a name=\"dc36f7c9-7ea5-4984-a02e-7102c215db17\"> </a></p>"
                + "<div style=\"display: inline-block; background-color: #d9e0e7; padding: 6px; margin: 4px; border: 1px solid #8da1b4; border-radius: 5px; line-height: 60%\">"
                + "<p style=\"margin-bottom: 0px\">Resource Practitioner &quot;dc36f7c9-7ea5-4984-a02e-7102c215db17&quot; Version &quot;1&quot; Updated &quot;2019-05-29 14:58:58+0530&quot;</p>"
                + "<p style=\"margin-bottom: 0px\">Profile: <a href=\"StructureDefinition-Practitioner.html\">Practitioner</a></p></div>"
                + "<p><b>identifier</b>: Medical License number/21-1521-3828-3227</p>"
                + "<p><b>name</b>: "+doctorName+"</p></div>");
		practitioner.setText(text);

		
		Identifier identifier = new Identifier();
		Coding identifierCoding = new Coding();
		identifierCoding.setSystem("http://terminology.hl7.org/CodeSystem/v2-0203");
		identifierCoding.setCode("MD");
		identifierCoding.setDisplay("Medical License number");
		identifier.setType(new CodeableConcept().addCoding(identifierCoding));
		identifier.setSystem("https://nhpr.abdm.gov.in");
		identifier.setValue("123445");
		practitioner.addIdentifier(identifier);

		
		HumanName name = new HumanName();
		name.setText(doctorName);
		practitioner.addName(name);
		return practitioner;
	}

	private static Encounter buildEncounter(Reference patientSubject, String visitDate, String id, List<String> chef,
			Date date1) throws ParseException {
		// Create an Encounter resource
		Encounter encounter = new Encounter();

		// Set the ID
		encounter.setId(id);

		// Create and set the Meta element
		Meta meta = new Meta();
		meta.setLastUpdated(date1);
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
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date = dateFormat.parse(visitDate);
		period.setStart(date);
		encounter.setPeriod(period);

		// Set the diagnosis element
		Encounter.DiagnosisComponent diagnosis = new Encounter.DiagnosisComponent();

		if (chef != null) {
			for (String s : chef) {
				Reference conditionReference = new Reference();
				conditionReference.setReference("urn:ChiefComplaints:" + s);
				conditionReference.setDisplay("Chief Complaint : NHA TEST- UHID No.18");
				diagnosis.setCondition(conditionReference);
			}
		}

		Coding diagnosisUseCoding = new Coding();
		diagnosisUseCoding.setSystem("http://snomed.info/sct");
		diagnosisUseCoding.setCode("33962009");
		diagnosisUseCoding.setDisplay("Chief complaint");
		diagnosis.setUse(new CodeableConcept().addCoding(diagnosisUseCoding));
		encounter.addDiagnosis(diagnosis);

		return encounter;
	}

	private static Condition buildChiefComplaints(String patientId, String id, String complaintText) {
		// Create a Condition resource
		Condition condition = new Condition();

		// Set the ID
		condition.setId(id);

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
		clinicalStatusCoding.setSystem("http://terminology.hl7.org/CodeSystem/condition-clinical");
		clinicalStatusCoding.setCode("active");
		clinicalStatusCoding.setDisplay("Active");
		clinicalStatus.addCoding(clinicalStatusCoding);
		condition.setClinicalStatus(clinicalStatus);

		// Set the condition code
		CodeableConcept code = new CodeableConcept();
		Coding codeCoding = new Coding();
		codeCoding.setSystem("http://snomed.info/sct");
		codeCoding.setCode("38362002");
		codeCoding.setDisplay("DENGUE FEVER");
		code.addCoding(codeCoding);
		code.setText(complaintText);
		condition.setCode(code);

		// Set the subject element
		Reference subject = new Reference();
		subject.setReference("urn:Patient:" + patientId);
		subject.setDisplay("NHA TEST- UHID No.18");
		condition.setSubject(subject);

		return condition;
	}

	private static Observation createObservation(String id, String profile, String divText, String codeSystem,
			String code, String codeDisplay, String subjectReference, String value, String unit, String unitSystem,
			String unitCode, String dateTime) {

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

		observation.setEffective(new DateTimeType(dateTime));

		Quantity valueQuantity = new Quantity();
		valueQuantity.setValue(Integer.parseInt(value));
		valueQuantity.setUnit(unit);
		valueQuantity.setSystem(unitSystem);
		valueQuantity.setCode(unitCode);
		observation.setValue(valueQuantity);

		return observation;
	}

	public static Appointment Appointment(String patientId, String practitionerId, String id,
			Map<String, String> chiefCompaintsMap, Map<String, String> investigationadvicesMap, String doctorName,
			String dateTime, String visitStartTime, String visitEndTime, String visitDate, String PatientName) {

		Appointment appointment = new Appointment();
		appointment.setId(id);

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

		for (Map.Entry<String, String> entry : chiefCompaintsMap.entrySet()) {
			Reference reasonReference = new Reference();
			reasonReference.setReference("urn:ChiefComplaints:" + entry.getValue());
			reasonReference.setDisplay(entry.getKey());
			appointment.addReasonReference(reasonReference);
		}

		appointment.setDescription(null);
		appointment.setStartElement(new InstantType(SfhirB.subtractTheTime(visitDate + "T" + visitStartTime + "Z")));
		appointment.setEndElement(new InstantType(SfhirB.subtractTheTime(visitDate + "T" + visitEndTime + "Z")));

		appointment.setCreatedElement(new DateTimeType(dateTime));

		for (Map.Entry<String, String> entry : investigationadvicesMap.entrySet()) {
			Reference basedOnReference = new Reference();
			basedOnReference.setReference("urn:InvestigationAdvice:" + entry.getValue());
			basedOnReference.setDisplay(entry.getKey());
			appointment.addBasedOn(basedOnReference);
		}

		AppointmentParticipantComponent patientParticipant = new AppointmentParticipantComponent();
		Reference patientReference = new Reference();
		patientReference.setReference("urn:Patient:" + patientId);
		patientReference.setDisplay(PatientName);
		patientParticipant.setActor(patientReference);
		patientParticipant.setStatus(Appointment.ParticipationStatus.ACCEPTED);
		appointment.addParticipant(patientParticipant);

		AppointmentParticipantComponent practitionerParticipant = new AppointmentParticipantComponent();
		Reference practitionerReference = new Reference();
		practitionerReference.setReference("urn:Practitioner:" + practitionerId);
		practitionerReference.setDisplay(doctorName);
		practitionerParticipant.setActor(practitionerReference);
		practitionerParticipant.setStatus(Appointment.ParticipationStatus.ACCEPTED);
		appointment.addParticipant(practitionerParticipant);

		return appointment;
	}

	public static String subtractTheTime(String DateTime) {
		Instant instantTypeValue = Instant.parse(DateTime);

		// Subtract 5 hours and 30 minutes
		Instant adjustedInstant = instantTypeValue.minus(Duration.ofHours(5).plus(Duration.ofMinutes(30)));

		return adjustedInstant.toString();

	}

	public static ServiceRequest createServiceRequest(String patientId, String practitionerId, String id,
			String dateTime, String investigationAdvice) {

		ServiceRequest serviceRequest = new ServiceRequest();
		serviceRequest.setId(id);

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
		codeCoding.setDisplay("Complete blood count with white cell differential, manual");

		CodeableConcept code = new CodeableConcept();
		code.addCoding(codeCoding);
		code.setText(investigationAdvice);
		serviceRequest.setCode(code);

		Reference subjectReference = new Reference();
		subjectReference.setReference("urn:Patient:" + patientId);
		subjectReference.setDisplay(" NHA TEST- UHID No.18");
		serviceRequest.setSubject(subjectReference);

		// Format the date and time
		String isoDate = dateTime;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
		LocalDateTime dateTime1 = LocalDateTime.parse(isoDate, DateTimeFormatter.ISO_DATE_TIME);
		String formattedDate = dateTime1.format(formatter);

		// Set the authoredOn date
		serviceRequest.setAuthoredOnElement(new org.hl7.fhir.r4.model.DateTimeType(formattedDate));

		Reference requesterReference = new Reference();
		requesterReference.setReference("urn:Practitioner:" + practitionerId);
		requesterReference.setDisplay("Consultant");
		serviceRequest.setRequester(requesterReference);

		return serviceRequest;
	}

	public static MedicationRequest createMedicationRequest(String id, String medicationText, String medicationCode,
			String medicationDisplay, String patientReference, String patientDisplay, String dateTime,
			String practitionerId, String requesterDisplay, String reasonCode, String reasonDisplay, String dosageText,
			String routeCode, String routeDisplay) {

		MedicationRequest medicationRequest = new MedicationRequest();
		medicationRequest.setId(id);

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

		// Format the date and time
		String isoDate = dateTime;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
		LocalDateTime dateTime1 = LocalDateTime.parse(isoDate, DateTimeFormatter.ISO_DATE_TIME);
		String formattedDate = dateTime1.format(formatter);

		medicationRequest.setAuthoredOnElement(new DateTimeType(formattedDate));

		Reference requesterReferenceObj = new Reference();
		requesterReferenceObj.setReference(practitionerId);
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

	public static Observation buildObservationPulseRate(String pulseRate, String patientId, String id,
			String dateTime) {
		return createObservation(id, "https://nrces.in/ndhm/fhir/r4/StructureDefinition/ObservationVitalSigns",
				"<div xmlns='http://www.w3.org/1999/xhtml'>Generated Narrative with Details pulse-rate</div>",
				"http://snomed.info/sct", "364075005", "Heart rate", "urn:Patient:" + patientId, pulseRate,
				"beats/minute", "http://unitsofmeasure.org", "258983007", dateTime);
	}

	public static Observation observationSystolicBloodPressure(String systolicRange, String patientId, String id,
			String dateTime) {
		return createObservation(id, "https://nrces.in/ndhm/fhir/r4/StructureDefinition/ObservationVitalSigns",
				"<div xmlns='http://www.w3.org/1999/xhtml'>Generated Narrative with Details Systolic-blood-pressure</div>",
				"http://snomed.info/sct", "364075005", "Heart rate", "urn:Patient:" + patientId, systolicRange, "mmHg",
				"http://unitsofmeasure.org", "mm[Hg]", dateTime);
	}

	public static Observation observationDiastolicBloodPressure(String booldPressure, String patientId, String id,
			String dateTime) {
		return createObservation(id, "https://nrces.in/ndhm/fhir/r4/StructureDefinition/ObservationVitalSigns",
				"<div xmlns='http://www.w3.org/1999/xhtml'>Generated Narrative with Details Diastolic-blood-pressure</div>",
				"http://snomed.info/sct", "364075005", "Heart rate", "urn:Patient:" + patientId, booldPressure, "mmHg",
				"http://unitsofmeasure.org", "mm[Hg]", dateTime);
	}

	public static Observation observationBodyTemperature(String bodyTemperature, String patientId, String id,
			String dateTime) {
		return createObservation(id, "https://nrces.in/ndhm/fhir/r4/StructureDefinition/ObservationVitalSigns",
				"<div xmlns='http://www.w3.org/1999/xhtml'>Generated Narrative with Details body-temperature</div>",
				"http://snomed.info/sct", "246508008", "Temperature", "urn:Patient:" + patientId, bodyTemperature,
				"Cel", "http://unitsofmeasure.org", "{Cel or degF}", dateTime);
	}

	public static Observation observationBodyHeight(String bodyHeight, String patientId, String id, String dateTime) {
		return createObservation(id, "https://nrces.in/ndhm/fhir/r4/StructureDefinition/ObservationVitalSigns",
				"<div xmlns='http://www.w3.org/1999/xhtml'>Generated Narrative with Details Body height</div>",
				"http://loinc.org", "8302-2", "Body height", "urn:Patient:" + patientId, bodyHeight, "cm",
				"http://unitsofmeasure.org", "cm", dateTime);
	}

	public static Observation observationBodyWeight(String bodyWeight, String patientId, String id, String dateTime) {
		return createObservation(id, "https://nrces.in/ndhm/fhir/r4/StructureDefinition/ObservationVitalSigns",
				"<div xmlns='http://www.w3.org/1999/xhtml'>Generated Narrative with Details Body weight</div>",
				"http://snomed.info/sct", "248345008", "Body weight", "urn:Patient:" + patientId, bodyWeight, "kg",
				"http://unitsofmeasure.org", "{[lb_av],kg,g}", dateTime);
	}

	public static Observation observationBloodGlucose(String glucoseValue, String patientId, String id,
			String dateTime) {
		return createObservation(id, "https://nrces.in/ndhm/fhir/r4/StructureDefinition/ObservationVitalSigns",
				"<div xmlns='http://www.w3.org/1999/xhtml'>Generated Narrative with Details BloodGlucose</div>",
				"http://snomed.info/sct", "33747003", "Blood sugar level", "urn:Patient:" + patientId, glucoseValue,
				"mg/dL", "http://unitsofmeasure.org", "mg/dL", dateTime);
	}

	public static Observation buildObservationOxygenSaturation(String spO2, String patientId, String id,
			String dateTime) {
		return createObservation(id, "https://nrces.in/ndhm/fhir/r4/StructureDefinition/ObservationVitalSigns",
				"<div xmlns='http://www.w3.org/1999/xhtml'>Generated Narrative with Details oxygen-saturation</div>",
				"http://snomed.info/sct", "250554003", "SpO2 measurement", "urn:Patient:" + patientId, spO2,
				"Percentage Unit", "http://unitsofmeasure.org", "415067009", dateTime);
	}

	public static void main(String[] args) throws ParseException, JsonMappingException, JsonProcessingException {
		SfhirB obj = new SfhirB();
		VitalHistory history = new VitalHistory();
		history.setDiabetic_value("");
		history.setDiastolic("40");
		history.setHeight("");
		history.setPulse("");
		history.setTemperature("");
		history.setWeight("60");
		history.setSystolic("");
		history.setOxigen_count("30");

		byte[] data = "your-data-here".getBytes();

		AccountProfile accountProfile = new AccountProfile();
		accountProfile.setDayOfBirth("20");
		accountProfile.setMonthOfBirth("01");
		accountProfile.setYearOfBirth("2001");
		accountProfile.setFirstName("shiva");
		accountProfile.setLastName("saketh");
		accountProfile.setHealthIdNumber("245545456");
		accountProfile.setMobile("7661068719");
		accountProfile.setGender("m");

	obj.buildFhirBundle("dr.saketh", "cold,cough,something", "medi,dolo", "Afternoon,evening,noon", "2023-04-05",
			"Op", "19:06:42", "19:09:34", history, accountProfile, data, "2023-10-16T12:09", "lumba,jimba,kamba","Dhanush","2024-03-29");

		String visitDate = "2024-03-29";

		String visitStartTime = "12:45:59";

	}

}
