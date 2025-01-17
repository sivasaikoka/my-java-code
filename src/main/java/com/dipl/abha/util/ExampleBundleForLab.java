package com.dipl.abha.util;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Composition;
import org.hl7.fhir.r4.model.ContactPoint;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.DiagnosticReport;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.InstantType;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.Narrative;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.Quantity;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.ServiceRequest;
import org.hl7.fhir.r4.model.Signature;
import org.hl7.fhir.r4.model.Specimen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dipl.abha.controllers.NDHMM2Controller;
import com.dipl.abha.dto.AccountProfile;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ca.uhn.fhir.context.FhirContext;

@Service
public class ExampleBundleForLab {

	@Autowired
	private ObjectMapper objMapper;

	private static final Logger LOGGER = LoggerFactory.getLogger(NDHMM2Controller.class);

	public String labBundle() throws JsonProcessingException {
		String patientName = "shivasaketh";

//		LocalDate date1 = LocalDate.of(Integer.parseInt(profile.getYearOfBirth()),
//				Integer.parseInt((profile.getMonthOfBirth() == null || profile.getMonthOfBirth().isEmpty()) ? "01"
//						: profile.getMonthOfBirth()),
//				Integer.parseInt((profile.getDayOfBirth() == null || profile.getDayOfBirth().isEmpty()) ? "01"
//						: profile.getDayOfBirth()));
		// LOGGER.info("=============> {} labResultId " + labResultId);
//		DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//		String dateOfBirth1 = date1.format(formatter1);

		String isoDate = "2024-09-20T15:47:51.575369";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
		LocalDateTime dateTime1 = LocalDateTime.parse(isoDate, formatter);
		Instant instant = dateTime1.atZone(ZoneId.systemDefault()).toInstant();

		List<LabResultFetchDtoC> LabResultFetchDtoCs = new ArrayList<>();

		LabResultFetchDtoCs.add(new LabResultFetchDtoC("11000000002393", "LBILL-767-2024246", "HAEMATOLOGY", "CBC",
				"Absolute Eosinophil Count (AEC)", "300", "eosinophils/µL",
				"/vidmedfiles/BENEFICIARY/55/TestResults/55_1726121031705_55_LBResultReport.pdf", null));
		LabResultFetchDtoCs.add(new LabResultFetchDtoC("11000000002393", "LBILL-767-2024246", "HAEMATOLOGY", "CBC",
				"Absolute Basophil Count (ABC)", "150", "basophils/µL",
				"/vidmedfiles/BENEFICIARY/55/TestResults/55_1726121031705_55_LBResultReport.pdf", null));
		LabResultFetchDtoCs.add(new LabResultFetchDtoC("11000000002393", "LBILL-767-2024246", "HAEMATOLOGY", "CBC",
				"Mean Platelet Volume (MPV)", "11", "fL",
				"/vidmedfiles/BENEFICIARY/55/TestResults/55_1726121031705_55_LBResultReport.pdf", null));
		LabResultFetchDtoCs.add(new LabResultFetchDtoC("11000000002390", "LBILL-767-2024246", "BIO CHEMISTRY",
				"Thyroid profile test", "Triiodothyronine (T3)", "2", "ng/ml",
				"/vidmedfiles/BENEFICIARY/55/TestResults/55_1726120936748_55_LBResultReport.pdf", null));
//		LabResultFetchDtoCs.add(new LabResultFetchDtoC("11000000002392", "LBILL-767-2024246", "BIO CHEMISTRY", "Bilirubin", "Bilirubin Total", "1", "mg/dl", "/vidmedfiles/BENEFICIARY/55/TestResults/55_1726120883176_55_LBResultReport.pdf", null));
//		LabResultFetchDtoCs.add(new LabResultFetchDtoC("11000000002394", "LBILL-767-2024246", "BIO CHEMISTRY", "Blood sugar Random", "Blood Sugar Random", "130", "mg/dl", "/vidmedfiles/BENEFICIARY/55/TestResults/55_1726120896375_55_LBResultReport.pdf", null));
//		LabResultFetchDtoCs.add(new LabResultFetchDtoC("11000000002391", "LBILL-767-2024246", "BIO CHEMISTRY", "Lipid profile test", "Cholesterol", "130", "mg/dl", "/vidmedfiles/BENEFICIARY/55/TestResults/55_1726120919220_55_LBResultReport.pdf", null));
		LabResultFetchDtoCs.add(new LabResultFetchDtoC("11000000002390", "LBILL-767-2024246", "BIO CHEMISTRY",
				"Thyroid profile test", "Thyroxine (T4)", "60", "ng/ml",
				"/vidmedfiles/BENEFICIARY/55/TestResults/55_1726120936748_55_LBResultReport.pdf", null));
//		LabResultFetchDtoCs.add(new LabResultFetchDtoC("11000000002391", "LBILL-767-2024246", "BIO CHEMISTRY", "Lipid profile test", "HDL", "30", "mg/dl", "/vidmedfiles/BENEFICIARY/55/TestResults/55_1726120919220_55_LBResultReport.pdf", null));
//		LabResultFetchDtoCs.add(new LabResultFetchDtoC("11000000002391", "LBILL-767-2024246", "BIO CHEMISTRY", "Lipid profile test", "Triglycerides", "210", "mg/dl", "/vidmedfiles/BENEFICIARY/55/TestResults/55_1726120919220_55_LBResultReport.pdf", null));
//		LabResultFetchDtoCs.add(new LabResultFetchDtoC("11000000002391", "LBILL-767-2024246", "BIO CHEMISTRY", "Lipid profile test", "VLDL", "45", "mg/dl", "/vidmedfiles/BENEFICIARY/55/TestResults/55_1726120919220_55_LBResultReport.pdf", null));

	
		//LOGGER.info("=============> {}" + objMapper.writeValueAsString(groupedByServiceId));

		Bundle bundle = new Bundle();
		bundle.setId(UUID.randomUUID().toString());

		String practitionerId = UUID.randomUUID().toString();

		Meta meta = new Meta();
		meta.setVersionId("1");
		String formattedDate = "2024-09-20 15:47:51.575";
		formattedDate = formattedDate.replace(" ", "T") + "+05:30";
		meta.setLastUpdatedElement(new InstantType(formattedDate));
		meta.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/DocumentBundle");

		Coding coding = new Coding();
		coding.setSystem("http://terminology.hl7.org/CodeSystem/v3-Confidentiality");
		coding.setCode("V");
		coding.setDisplay("very restricted");
		meta.addSecurity(coding);
		bundle.setMeta(meta);

		Identifier identifier = new Identifier();
		identifier.setSystem("http://hip.in");
		identifier.setValue(UUID.randomUUID().toString());
		bundle.setIdentifier(identifier);

		bundle.setType(Bundle.BundleType.DOCUMENT);

		// Convert Instant to Timestamp
		Timestamp timestamp = Timestamp.from(instant);

		bundle.setTimestamp(timestamp);

		Composition composition = new Composition();
		composition.setId("FHIR120");

		Meta compositionMeta = new Meta();
		compositionMeta.setVersionId("1");
		compositionMeta.setLastUpdated(timestamp);
		compositionMeta.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/DiagnosticReportRecord");
		composition.setMeta(compositionMeta);

		composition.setLanguage("en-IN");

		Narrative text = new Narrative();
		text.setStatus(Narrative.NarrativeStatus.GENERATED);
		text.setDivAsString("<div xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en-IN\" lang=\"en-IN\">"
				+ "<p><b>Generated Narrative: Composition</b><a name=\"f1ab8bba-a0ed-476a-a902-a1e08517020b\"> </a></p>"
				+ "<div style=\"display: inline-block; background-color: #d9e0e7; padding: 6px; margin: 4px; border: 1px solid #8da1b4; border-radius: 5px; line-height: 60%\">"
				+ "<p style=\"margin-bottom: 0px\">Resource Composition &quot;f1ab8bba-a0ed-476a-a902-a1e08517020b&quot; Version &quot;1&quot; Updated &quot;2020-07-09 15:32:26+0530&quot; "
				+ "(Language &quot;en-IN&quot;) </p><p style=\"margin-bottom: 0px\">Profile: <a href=\"StructureDefinition-DiagnosticReportRecord.html\">DiagnosticReportRecord</a></p>"
				+ "</div><p><b>identifier</b>: <code>https://ndhm.in/phr</code>/645bb0c3-ff7e-4123-bef5-3852a4784813</p>"
				+ "<p><b>status</b>: final</p><p><b>type</b>: Diagnostic Report- Lab <span style=\"background: LightGoldenRodYellow; margin: 4px; border: 1px solid khaki\"> "
				+ "(<a href=\"https://browser.ihtsdotools.org/\">SNOMED CT</a>#721981007 &quot;Diagnostic studies report&quot;)</span></p>"
				+ "<p><b>date</b>: 2017-05-27 11:46:09+0530</p>" + "<p><b>author</b>: <a href=\"Practitioner_"
				+ practitionerId + "\">See above (urn:Practitioner:" + practitionerId + ": " + "Abhi" + ")</a></p>"
				+ "<p><b>title</b>: Diagnostic Report- Lab</p></div>");
		composition.setText(text);

		Identifier compositionIdentifier = new Identifier();
		compositionIdentifier.setSystem("https://ndhm.in/phr");
		compositionIdentifier.setValue(UUID.randomUUID().toString());
		composition.setIdentifier(compositionIdentifier);
		composition.setStatus(Composition.CompositionStatus.FINAL);

		CodeableConcept type = new CodeableConcept();
		Coding compositionCoding = new Coding();
		compositionCoding.setSystem("http://snomed.info/sct");
		compositionCoding.setCode("721981007");
		compositionCoding.setDisplay("Diagnostic studies report");
		type.addCoding(compositionCoding);
		type.setText("Diagnostic Report- Lab");
		composition.setType(type);

		String patientId = UUID.randomUUID().toString();
		Reference subject = new Reference();
		subject.setReference("urn:Patient:" + patientId);
		subject.setDisplay(patientName);
		composition.setSubject(subject);
		composition.setDate(timestamp);

		Reference author = new Reference();
		author.setReference("urn:Practitioner:" + practitionerId);
		author.setDisplay("Abhi");
		composition.addAuthor(author);
		composition.setTitle("Diagnostic Report- Lab");

		String organizationId = UUID.randomUUID().toString();
		Reference organization = new Reference();
		organization.setReference("urn:Organization:" + organizationId);
		organization.setDisplay("Hari Labs");
		composition.setCustodian(organization);

		String diagnosticReporId1 = "urn:DiagnosticReport:5b8edaad-475f-4d26-8244-0467cc0c8c0f";
		Composition.SectionComponent section = new Composition.SectionComponent();
		section.setTitle("DiagnosticReport");
		CodeableConcept codeableConcept = new CodeableConcept();
		Coding codeableConceptCoding = new Coding();
		codeableConceptCoding.setSystem("http://snomed.info/sct");
		codeableConceptCoding.setCode("261004008");
		codeableConceptCoding.setDisplay("Diagnostic");
		codeableConcept.addCoding(codeableConceptCoding);
		section.setCode(codeableConcept);

		Reference diagnosticReporReference = new Reference();
		diagnosticReporReference.setReference(diagnosticReporId1);
		diagnosticReporReference.setType("DiagnosticReport");
		section.addEntry(diagnosticReporReference);
		composition.addSection(section);

		String diagnosticReporId2 = "urn:DiagnosticReport:c71bb758-aeaf-40dd-ad7e-bd38b6df4e02";
		

		Reference diagnosticReporReference2 = new Reference();
		diagnosticReporReference2.setReference(diagnosticReporId2);
		diagnosticReporReference2.setType("DiagnosticReport");
		section.addEntry(diagnosticReporReference2);
		

		String compositionID = UUID.randomUUID().toString();
		Bundle.BundleEntryComponent entryComposition = new Bundle.BundleEntryComponent();
		entryComposition.setFullUrl("urn:composition:" + compositionID);
		entryComposition.setResource(composition);
		bundle.addEntry(entryComposition);

		Bundle.BundleEntryComponent entryPatient = new Bundle.BundleEntryComponent();
		entryPatient.setFullUrl("urn:Patient:" + patientId);
		entryPatient.setResource(ExampleBundleForLab.buildPatientObject(patientId, "20245121542154", "10020",
				patientName, "7661068719", "m", "2001-09-18", "Diagnostic Report- Lab"));
		bundle.addEntry(entryPatient);

		Bundle.BundleEntryComponent entryPractitioner = new Bundle.BundleEntryComponent();
		entryPractitioner.setFullUrl("urn:Practitioner:" + practitionerId);
		entryPractitioner.setResource(BundleForLab.buildDoctorObject(practitionerId, "Abhi", "Diagnostic Report- Lab"));
		bundle.addEntry(entryPractitioner);

		Bundle.BundleEntryComponent entryOrganization = new Bundle.BundleEntryComponent();
		entryOrganization.setFullUrl("urn:Organization:" + organizationId);
		entryOrganization.setResource(BundleForLab.createOrganization("urn:Organization:" + organizationId, "Hari Labs",
				"8632762237", "abhi.labs@gmail.com"));
		bundle.addEntry(entryOrganization);

		String[] obIs = { "urn:uuid:33ec33a8-5378-4f77-87cc-81ce161cab6f",
				"urn:uuid:7f080c22-0b63-4d3c-9d7f-588729288fbd", "urn:uuid:16425add-3878-4a6c-a659-a50782be8fa7" };

		String[] obIs2 = { "urn:Obseravation:7b2761ce-cb84-436e-9cdb-cf493ce10283",
				"urn:Obseravation:bb9ebe6c-b87b-435b-909e-e65e38958915",
				"urn:Obseravation:f59cae0b-3c8f-4ee6-8501-532a4835f3fa" };
		
		String[] obseravationTypes = { "Observation/Cholesterol", "Observation/triglyceride",
				"Observation/Cholesterol-in-hdl" };

		String specimenId = "urn:Specimen:" + UUID.randomUUID().toString();

		String ServiceRequest = "urn:ServiceRequest:d6248c73-d35f-414b-aa91-500547ff207e";

		Bundle.BundleEntryComponent entryDiagnosticReport = new Bundle.BundleEntryComponent();
		entryDiagnosticReport.setFullUrl(diagnosticReporId1);

		entryDiagnosticReport.setResource(this.buildDiagnosticReportObject("Diagnostic Report- Lab", "5b8edaad-475f-4d26-8244-0467cc0c8c0f", patientName,
				"urn:Patient:" + patientId, "Abhi", "urn:Practitioner:" + practitionerId, "Hari Lab",
				"urn:Organization:" + organizationId, obIs, obseravationTypes, specimenId,
				ServiceRequest, "...".getBytes(), "/dsad", "Hematology"));
		bundle.addEntry(entryDiagnosticReport);
		
		Bundle.BundleEntryComponent entryDiagnosticReport1 = new Bundle.BundleEntryComponent();
		entryDiagnosticReport1.setFullUrl(diagnosticReporId2);

		entryDiagnosticReport1.setResource(this.buildDiagnosticReportObject("Diagnostic Report- Lab", "5b8edaad-475f-4d26-8244-0467cc0c8c0f", patientName,
				"urn:Patient:" + patientId, "Abhi", "urn:Practitioner:" + practitionerId, "Hari Lab",
				"urn:Organization:" + organizationId, obIs2, obseravationTypes, specimenId,
				ServiceRequest, "...".getBytes(), "/dsad", "BIO CHEMISTRY"));
		bundle.addEntry(entryDiagnosticReport1);

		String observationDiv = "<div xmlns=\"http://www.w3.org/1999/xhtml\"><p><b>Generated Narrative: Observation</b>"
				+ "<a name=\"33ec33a8-5378-4f77-87cc-81ce161cab6f\"> </a></p>"
				+ "<div style=\"display: inline-block; background-color: #d9e0e7; padding: 6px; margin: 4px; border: 1px solid #8da1b4; border-radius: 5px; line-height: 60%\">"
				+ "<p style=\"margin-bottom: 0px\">Resource Observation &quot;33ec33a8-5378-4f77-87cc-81ce161cab6f&quot;</p>"
				+ "<p style=\"margin-bottom: 0px\">Profile: <a href=\"StructureDefinition-Observation.html\">Observation</a></p>"
				+ "</div><p><b>status</b>: final</p>" + "<p><b>code</b>: Cholesterol [Mass/volume] in Serum or Plasma"
				+ " <span style=\"background: LightGoldenRodYellow; margin: 4px; border: 1px solid khaki\">"
				+ " (<a href=\"https://loinc.org/\">LOINC</a>#2093-3)</span></p>"
				+ "<p><b>subject</b>: <a href=\"Patient_" + patientId + "\">See above (urn:Patient:" + patientId + ": "
				+ patientName + ")</a></p>" + "<p><b>effective</b>: 2020-09-29</p>"
				+ "<p><b>performer</b>: <a href=\"Organization_" + organizationId + "\">See above (urn:Organization:"
				+ organizationId + ": " + "Hari Labs" + ")</a></p>"
				+ "<p><b>value</b>: 156 mg/dL<span style=\"background: LightGoldenRodYellow\">"
				+ " (Details: SNOMED CT code 258797006 = 'milligram/decilitre')</span></p>"
				+ "<h3>ReferenceRanges</h3><table class=\"grid\"><tr><td style=\"display: none\">-</td>"
				+ "<td><b>High</b></td></tr><tr><td style=\"display: none\">*</td><td>200.0 mg/dL"
				+ "<span style=\"background: LightGoldenRodYellow\"> (Details: SNOMED CT code 258797006 = 'milligram/decilitre')</span></td></tr></table></div>";

		     int j=0;
					for (String Id : obIs) {
						Bundle.BundleEntryComponent entryObservation = new Bundle.BundleEntryComponent();
						entryObservation.setFullUrl(Id);
						entryObservation.setResource(ExampleBundleForLab.createObservation(Id,
								"https://nrces.in/ndhm/fhir/r4/StructureDefinition/Observation", observationDiv,
								"http://loinc.org", "2093-3", "Cholesterol [Mass/volume] in Serum or Plasma",
								"urn:Patient:" + patientId, patientName, "2024-09-10",
								"urn:Organization:" + organizationId, "Hari Labs",obseravationTypes[j]));
						bundle.addEntry(entryObservation);
						j++;
						}
					
		String specimenDiv = "<div xmlns=\"http://www.w3.org/1999/xhtml\"><p><b>Generated Narrative: Specimen</b>"
				+ "<a name=\"361b2a51-a3cf-4923-8c58-41141ce95ed5\"> </a></p>"
				+ "<div style=\"display: inline-block; background-color: #d9e0e7; padding: 6px; margin: 4px; border: 1px solid #8da1b4; border-radius: 5px; line-height: 60%\">"
				+ "<p style=\"margin-bottom: 0px\">Resource Specimen &quot;361b2a51-a3cf-4923-8c58-41141ce95ed5&quot;</p>"
				+ "<p style=\"margin-bottom: 0px\">Profile: <a href=\"StructureDefinition-Specimen.html\">Specimen</a></p>"
				+ "</div><p><b>type</b>: Serum specimen <span style=\"background: LightGoldenRodYellow; margin: 4px; border: 1px solid khaki\">"
				+ " (<a href=\"https://browser.ihtsdotools.org/\">SNOMED CT</a>#119364003)</span></p>"
				+ "<p><b>subject</b>: <a href=\"Patient_" + patientId + "\">See above (urn:Patient:" + patientId + ": "
				+ patientName + ")</a></p>" + "<p><b>receivedTime</b>: 2015-07-08 06:40:17+0000</p>"
				+ "<h3>Collections</h3><table class=\"grid\"><tr><td style=\"display: none\">-</td><td><b>Collected[x]</b></td></tr>"
				+ "<tr><td style=\"display: none\">*</td><td>2020-07-08 06:40:17+0000</td></tr></table></div>";
		Bundle.BundleEntryComponent entrySpecimen = new Bundle.BundleEntryComponent();
		entrySpecimen.setFullUrl(specimenId);
		entrySpecimen.setResource(ExampleBundleForLab.buildSpecimen(specimenDiv, specimenId, "urn:Patient:" + patientId,
				patientName, "2015-07-08T06:40:17Z"));
		bundle.addEntry(entrySpecimen);

		String serviceRequestdiv = "<div xmlns=\"http://www.w3.org/1999/xhtml\"><p><b>Generated Narrative: ServiceRequest</b>"
				+ "<a name=\"3875cb2d-c589-41d8-87c2-1f488e743184\"> </a></p>"
				+ "<div style=\"display: inline-block; background-color: #d9e0e7; padding: 6px; margin: 4px; border: 1px solid #8da1b4; border-radius: 5px; line-height: 60%\">"
				+ "<p style=\"margin-bottom: 0px\">Resource ServiceRequest &quot;3875cb2d-c589-41d8-87c2-1f488e743184&quot;</p>"
				+ "<p style=\"margin-bottom: 0px\">Profile: <a href=\"StructureDefinition-ServiceRequest.html\">ServiceRequest</a></p>"
				+ "</div><p><b>status</b>: active</p><p><b>intent</b>: original-order</p>"
				+ "<p><b>code</b>: Lipid Panel <span style=\"background: LightGoldenRodYellow; margin: 4px; border: 1px solid khaki\">"
				+ " (<a href=\"https://browser.ihtsdotools.org/\">SNOMED CT</a>#16254007)</span></p>"
				+ "<p><b>subject</b>: <a href=\"Patient_" + patientId + "\">See above (urn:Patient:" + patientId + ": "
				+ patientName + ")</a></p>" + "<p><b>occurrence</b>: 2020-07-08 09:33:27+0700</p>"
				+ "<p><b>requester</b>: <a href=\"Practitioner_" + practitionerId + "\">See above (urn:Practitioner:"
				+ practitionerId + ": " + "Abhi" + ")</a></p></div>";
		
			Bundle.BundleEntryComponent entryServiceRequest = new Bundle.BundleEntryComponent();
			entryServiceRequest.setFullUrl(ServiceRequest);
			entryServiceRequest.setResource(
					ExampleBundleForLab.buildServiceRequest(ServiceRequest, serviceRequestdiv, "urn:Patient:" + patientId,
							patientName, "2024-09-10T09:33:27+07:00", "Abhi", "urn:Practitioner:" + practitionerId));
			bundle.addEntry(entryServiceRequest);

	

		Signature signature = new Signature();

		Coding typeCoding = new Coding().setSystem("urn:iso-astm:E1762-95:2013").setCode("1.2.840.10065.1.12.1.1")
				.setDisplay("Author's Signature");
		signature.addType(typeCoding);

		signature.setWhenElement(new InstantType("2020-07-09T07:42:33+10:00"));

		Reference whoReference = new Reference("urn:Practitioner:" + practitionerId);
		whoReference.setDisplay("Abhi");
		signature.setWho(whoReference);

		signature.setSigFormat("image/jpeg");

		signature.setData("Author signatere image".getBytes());

		bundle.setSignature(signature);

		FhirContext ctx = FhirContext.forR4();
		String jsonString = ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle);

		//LOGGER.info("=============>  observationIds {}  " + observationIds);

		return jsonString;

	}

	private static ServiceRequest buildServiceRequest(String serviceRequestId, String serviceRequestdiv,
			String patientId, String patientName, String DateTime, String practitionerName, String practitionerId) {
		ServiceRequest serviceRequest = new ServiceRequest();

		serviceRequest.setId("3875cb2d-c589-41d8-87c2-1f488e743184");

		Meta meta = new Meta();
		meta.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/ServiceRequest");
		serviceRequest.setMeta(meta);

		Narrative text = new Narrative();
		text.setStatus(Narrative.NarrativeStatus.GENERATED);
		String xhtmlContent = serviceRequestdiv;
		text.setDivAsString(xhtmlContent);
		serviceRequest.setText(text);

		serviceRequest.setStatus(ServiceRequest.ServiceRequestStatus.ACTIVE);

		serviceRequest.setIntent(ServiceRequest.ServiceRequestIntent.ORIGINALORDER);

		CodeableConcept code = new CodeableConcept();
		code.addCoding(new Coding("http://snomed.info/sct", "16254007", "Lipid Panel"));
		serviceRequest.setCode(code);

		serviceRequest.setSubject(new Reference(patientId).setDisplay(patientName));

		serviceRequest.setOccurrence(DateTimeType.now());

		// serviceRequest.setOccurrence(new DateTimeType("2020-07-08T09:33:27+07:00"));

		serviceRequest.setRequester(new Reference(practitionerId).setDisplay(practitionerName));
		return serviceRequest;
	}

	private static Specimen buildSpecimen(String specimenDiv, String specimenId, String patientId, String patientName,
			String collectedDateTime) {
		Specimen specimen = new Specimen();

		String id = specimenId.replace("urn:Specimen:", "");
		specimen.setId(id);

		Meta meta = new Meta();
		meta.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/Specimen");
		specimen.setMeta(meta);

		Narrative text = new Narrative();
		text.setStatus(Narrative.NarrativeStatus.GENERATED);
		String xhtmlContent = specimenDiv;
		text.setDivAsString(xhtmlContent);
		specimen.setText(text);

		CodeableConcept type = new CodeableConcept();
		type.addCoding(new Coding("http://snomed.info/sct", "119364003", "Serum specimen"));
		specimen.setType(type);

		specimen.setSubject(new Reference(patientId).setDisplay(patientName));

		specimen.setReceivedTime(parseDate(collectedDateTime));

		Specimen.SpecimenCollectionComponent collection = new Specimen.SpecimenCollectionComponent();
		collection.setCollected(new DateTimeType(collectedDateTime));
		specimen.setCollection(collection);

		return specimen;
	}

	private static Date parseDate(String dateTimeString) {
		try {
			return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(dateTimeString);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static Observation createObservation(String id, String profileUrl, String div, String link, String code,
			String display, String patientId, String patientName, String date, String organizationId, String labName,String test) {
		Observation observation = new Observation();

		String obseravationId = id.replace("urn:uuid:", "");
		observation.setId(obseravationId);

		Meta meta = new Meta();
		meta.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/Observation");
		observation.setMeta(meta);

		Narrative text = new Narrative();
		text.setStatus(Narrative.NarrativeStatus.GENERATED);
		String xhtmlContent = div;
		text.setDivAsString(xhtmlContent);
		observation.setText(text);

		observation.setStatus(Observation.ObservationStatus.FINAL);

		CodeableConcept codeableConcept = new CodeableConcept();
		codeableConcept.addCoding(new Coding(link, code, display));
		codeableConcept.setText("");
		observation.setCode(codeableConcept);

		observation.setSubject(new Reference(patientId).setDisplay(patientName));

		observation.setEffective(new org.hl7.fhir.r4.model.DateTimeType(date));

		observation.addPerformer(new Reference(organizationId).setDisplay(labName));

		Quantity valueQuantity = new Quantity();
		
			valueQuantity.setValue(new BigDecimal("25"));
		
			
		

		
			valueQuantity.setUnit("mg/dl");
		

		valueQuantity.setSystem("http://snomed.info/sct");
		valueQuantity.setCode("258797006");
		observation.setValue(valueQuantity);

		Observation.ObservationReferenceRangeComponent referenceRange = new Observation.ObservationReferenceRangeComponent();
		Quantity high = new Quantity();
		high.setValue(200.0);
		high.setUnit("mg/dL");
		high.setSystem("http://snomed.info/sct");
		high.setCode("258797006");
		referenceRange.setHigh(high);
		observation.addReferenceRange(referenceRange);

		return observation;
	}

	public static Organization createOrganization(String id, String name, String contactNo, String email) {

		Organization organization = new Organization();

		organization.setId(id);

		organization.getMeta().addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/Organization");

		Identifier identifier = new Identifier();
		CodeableConcept identifierType = new CodeableConcept();
		identifierType.addCoding(new Coding().setSystem("http://terminology.hl7.org/CodeSystem/v2-0203").setCode("PRN")
				.setDisplay("Provider number"));
		identifier.setType(identifierType);
		identifier.setSystem("https://facility.ndhm.gov.in");
		identifier.setValue("4567823");
		organization.addIdentifier(identifier);

		organization.setName(name);

		ContactPoint telecomPhone = new ContactPoint();
		telecomPhone.setSystem(ContactPoint.ContactPointSystem.PHONE);
		telecomPhone.setValue("+91 " + contactNo);
		telecomPhone.setUse(ContactPoint.ContactPointUse.WORK);
		organization.addTelecom(telecomPhone);

		ContactPoint telecomEmail = new ContactPoint();
		telecomEmail.setSystem(ContactPoint.ContactPointSystem.EMAIL);
		telecomEmail.setValue(email);
		telecomEmail.setUse(ContactPoint.ContactPointUse.WORK);
		organization.addTelecom(telecomEmail);

		return organization;
	}

	@SuppressWarnings("null")
	private Resource buildDiagnosticReportObject(String reportType, String id, String patientName, String patientId,
			String practitionerName, String practitionerId, String clinicName, String clinicId,
			String[] observationIds, String[] obseravationTypes, String specimenId,
			String serviceRequestId, byte[] fileByte, String prescirptionPath, String k) {
		DiagnosticReport diagnosticReport = new DiagnosticReport();
		diagnosticReport.setId(id);

		Meta meta = new Meta();
		meta.setVersionId("1");
		meta.setLastUpdated(new Date());
		meta.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/DiagnosticReportLab");
		diagnosticReport.setMeta(meta);
		String serviceRequestId1 = serviceRequestId.replace("urn:ServiceRequest:", "");
		Narrative narrative = new Narrative();
		narrative.setStatus(Narrative.NarrativeStatus.GENERATED);
		narrative.setDivAsString(
				"<div xmlns=\"http://www.w3.org/1999/xhtml\"><p><b>Generated Narrative: DiagnosticReport</b>"
						+ "<a name=\"690adbcf-8d17-4206-bfaa-c7de730d06d7\"> </a></p>"
						+ "<div style=\"display: inline-block; background-color: #d9e0e7; padding: 6px; margin: 4px; border: 1px solid #8da1b4; border-radius: 5px; line-height: 60%\">"
						+ "<p style=\"margin-bottom: 0px\">Resource DiagnosticReport &quot;690adbcf-8d17-4206-bfaa-c7de730d06d7&quot; Version &quot;1&quot; Updated &quot;2020-07-09 15:32:26+0530&quot; </p>"
						+ "<p style=\"margin-bottom: 0px\">Profile: <a href=\"StructureDefinition-DiagnosticReportLab.html\">DiagnosticReportLab</a></p></div>"
						+ "<p><b>identifier</b>: <code>https://xyz.com/lab/reports</code>/5234342</p>"
						+ "<p><b>basedOn</b>: <a href=\"ServiceRequest_" + serviceRequestId1
						+ "\">See above (urn:ServiceRequest:" + serviceRequestId1 + ": ServiceRequest)</a></p>"
						+ "<p><b>status</b>: final</p>"
						+ "<p><b>category</b>: Hematology service <span style=\"background: LightGoldenRodYellow; margin: 4px; border: 1px solid khaki\"> (<a href=\"https://browser.ihtsdotools.org/\">SNOMED CT</a>#708196005)</span></p>"
						+ "<p><b>code</b>: Lipid 1996 panel <span style=\"background: LightGoldenRodYellow; margin: 4px; border: 1px solid khaki\"> (<a href=\"https://loinc.org/\">LOINC</a>#24331-1 \"Lipid 1996 panel - Serum or Plasma\")</span></p>"
						+ "<p><b>subject</b>: <a href=\"Patient_" + patientId.replace("urn:Patient:", "")
						+ "\">See above (" + patientId + ": " + patientName + ")</a></p>"
						+ "<p><b>issued</b>: 10-Jul-2020, 6:15:33 am</p>"
						+ "<p><b>performer</b>: <a href=\"Organization_" + clinicId.replace("urn:Organization:", "")
						+ "\">See above (" + clinicId + ": " + clinicName + ")</a></p>"
						+ "<p><b>resultsInterpreter</b>: <a href=\"Practitioner_"
						+ practitionerId.replace("urn:Practitioner", "") + "\">See above (" + practitionerId + ": "
						+ practitionerName + ")</a></p>" + "<p><b>specimen</b>: <a href=\"Specimen_"
						+ specimenId.replace("urn:Specimen:", "") + "\">See above (" + specimenId
						+ ": Specimen)</a></p>"
						+ "<p><b>conclusion</b>: Elevated cholesterol/high density lipoprotein ratio</p>"
						+ "<p><b>conclusionCode</b>: Elevated cholesterol/high density lipoprotein ratio <span style=\"background: LightGoldenRodYellow; margin: 4px; border: 1px solid khaki\"> (<a href=\"https://browser.ihtsdotools.org/\">SNOMED CT</a>#439953004)</span></p>"
						+ "<h3>PresentedForms</h3><table class=\"grid\"><tr><td style=\"display: none\">-</td><td><b>ContentType</b></td><td><b>Language</b></td><td><b>Data</b></td><td><b>Title</b></td></tr><tr><td style=\"display: none\">*</td><td>application/pdf</td><td>en-IN</td><td>(base64 data - 80793 bytes)</td><td>Diagnostic Report</td></tr></table></div>");
		diagnosticReport.setText(narrative);

		Identifier identifier = new Identifier();
		identifier.setSystem("https://xyz.com/lab/reports");
		identifier.setValue("5234342");
		diagnosticReport.addIdentifier(identifier);

		Reference basedOn = new Reference();
		basedOn.setReference(serviceRequestId);
		basedOn.setDisplay("ServiceRequest");
		diagnosticReport.addBasedOn(basedOn);

		diagnosticReport.setStatus(DiagnosticReport.DiagnosticReportStatus.FINAL);

		CodeableConcept category = new CodeableConcept();
		Coding categoryCoding = new Coding();
		categoryCoding.setSystem("http://snomed.info/sct");
		categoryCoding.setCode("708196005");
		categoryCoding.setDisplay("Hematology service");
		category.addCoding(categoryCoding);
		diagnosticReport.addCategory(category);

		Reference patient = new Reference();
		patient.setReference(patientId);
		patient.setDisplay(patientName);
		diagnosticReport.setSubject(patient);

		diagnosticReport.setIssued(new Date());

		Reference clinic = new Reference();
		clinic.setReference(clinicId);
		clinic.setDisplay(clinicName);
		diagnosticReport.addPerformer(clinic);

		Reference interpreter = new Reference();
		interpreter.setReference(practitionerId);
		interpreter.setDisplay(practitionerName);
		diagnosticReport.addResultsInterpreter(interpreter);

		Reference specimen = new Reference();
		specimen.setReference(specimenId);
		specimen.setDisplay("Specimen");
		diagnosticReport.addSpecimen(specimen);
		String serviceName = "";

		CodeableConcept code = new CodeableConcept();
		Coding codeCoding = new Coding();
		codeCoding.setSystem("http://loinc.org");
		codeCoding.setCode("24331-1");
		codeCoding.setDisplay("Lipid 1996 panel - Serum or Plasma");
		code.addCoding(codeCoding);
		code.setText(k);
		diagnosticReport.setCode(code);

		List<Reference> references = null;
		if (observationIds != null && obseravationTypes != null) {
			references=new ArrayList<>();
			int i=0;
			for (String observationId : observationIds) {
				Reference result = new Reference();
				result.setReference("urn:Obseravation:" + observationId);
				result.setDisplay(obseravationTypes[i]);
				references.add(result);
			
				diagnosticReport.setConclusion("  ..... ");
				i++;
			}
			diagnosticReport.setResult(references);
		}

//		Attachment attachment = new Attachment();
//		attachment.setContentType("application/pdf");
//		attachment.setLanguage("en-IN");
//		attachment.setTitle(serviceName);
//		LOGGER.info("==========>  {} " + prescirptionPath + documentPath);
//		String fullPath = prescirptionPath + documentPath;
//		 byte[] fileByte1 = webHookService.convertUrlDocToBytes(fullPath);
//		// LOGGER.info("==========> {fileByte} "+fileByte1);
//		attachment.setData(fileByte1);
//		diagnosticReport.addPresentedForm(attachment);

		CodeableConcept conclusionCode = new CodeableConcept();
		Coding conclusionCoding = new Coding();
		conclusionCoding.setSystem("http://snomed.info/sct");
		conclusionCoding.setCode("439953004");
		conclusionCoding.setDisplay("Elevated cholesterol/high density lipoprotein ratio");
		conclusionCode.addCoding(conclusionCoding);
		diagnosticReport.addConclusionCode(conclusionCode);

		return diagnosticReport;
	}

	public static Practitioner buildDoctorObject(String id, String doctorName, String recordType) {
		Practitioner practitioner = new Practitioner();

		practitioner.setId(id);

		Meta meta = new Meta();
		meta.setVersionId("1");
		meta.setLastUpdated(new Date());
		meta.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/Practitioner");
		practitioner.setMeta(meta);

		Narrative text = new Narrative();
		text.setStatus(Narrative.NarrativeStatus.GENERATED);
		text.setDivAsString("<div xmlns='http://www.w3.org/1999/xhtml'>" + recordType.toUpperCase() + "</div>");
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

	private static Patient buildPatientObject(String patientId, String abhaId, String interactoinId, String patientName,
			String mobileNo, String gender, String dob, String recordType) {

		Patient patient = new Patient();

		patient.setId(patientId);

		Meta meta = new Meta();
		meta.setVersionId("1");
		meta.setLastUpdated(new Date());
		meta.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/Patient");
		patient.setMeta(meta);

		String genderToAddInDiv = "";
		if (gender.equalsIgnoreCase("m")) {
			genderToAddInDiv = "male";
		} else if (gender.equalsIgnoreCase("f")) {
			genderToAddInDiv = "female";
		}
		Narrative text = new Narrative();
		text.setStatus(Narrative.NarrativeStatus.GENERATED);
		text.setDivAsString(
				"<div xmlns=\"http://www.w3.org/1999/xhtml\"><p><b>Generated Narrative: Patient</b><a name=\"f8d9e19e-5598-428c-ae03-b1cd44968b41\"> </a></p>"
						+ "<div style=\"display: inline-block; background-color: #d9e0e7; padding: 6px; margin: 4px; border: 1px solid #8da1b4; border-radius: 5px; line-height: 60%\">"
						+ "<p style=\"margin-bottom: 0px\">Resource Patient &quot;f8d9e19e-5598-428c-ae03-b1cd44968b41&quot; Version &quot;1&quot; Updated &quot;2020-07-09 14:58:58+0530&quot;</p>"
						+ "<p style=\"margin-bottom: 0px\">Profile: <a href=\"StructureDefinition-Patient.html\">Patient</a></p></div>"
						+ "<p><b>identifier</b>: Medical record number/" + abhaId + "</p>" + "<p><b>name</b>:"
						+ patientName + "</p><p><b>telecom</b>: <a href=\"tel:" + mobileNo + "\">" + mobileNo
						+ "</a></p>" + "<p><b>gender</b>: " + genderToAddInDiv + "</p><p><b>birthDate</b>: " + dob
						+ "</p></div>");
		patient.setText(text);

		Identifier mrIdentifier = new Identifier();
		Coding mrCoding = new Coding();
		mrCoding.setSystem("http://terminology.hl7.org/CodeSystem/v2-0203");
		mrCoding.setCode("MR");
		mrCoding.setDisplay("Medical record number");
		mrIdentifier.setType(new CodeableConcept().addCoding(mrCoding));
		mrIdentifier.setSystem("https://healthid.ndhm.gov.in");
		mrIdentifier.setValue(interactoinId);

		patient.addIdentifier(mrIdentifier);

		HumanName name = new HumanName();
		name.setText(patientName);
		patient.addName(name);

		ContactPoint telecom = new ContactPoint();
		telecom.setSystem(ContactPoint.ContactPointSystem.PHONE);
		telecom.setValue(mobileNo);
		telecom.setUse(ContactPoint.ContactPointUse.HOME);
		patient.addTelecom(telecom);

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

	public static void main(String[] args) throws Exception {
		AccountProfile profile = new AccountProfile();
		profile.setHealthIdNumber("24554554544");
		profile.setDayOfBirth("18");
		profile.setMonthOfBirth("09");
		profile.setYearOfBirth("2001");
		profile.setGender("m");

		String patientName = "Jane Doe";
		String prescriptionPath = "/path/to/prescription.pdf";

		ExampleBundleForLab exampleBundleForLab = new ExampleBundleForLab();
		List<LabResultFetchDtoC> LabResultFetchDtoCs = new ArrayList<>();

		LabResultFetchDtoCs.add(new LabResultFetchDtoC("11000000002393", "LBILL-767-2024246", "HAEMATOLOGY", "CBC",
				"Absolute Eosinophil Count (AEC)", "300", "eosinophils/µL",
				"/vidmedfiles/BENEFICIARY/55/TestResults/55_1726121031705_55_LBResultReport.pdf", null));
		LabResultFetchDtoCs.add(new LabResultFetchDtoC("11000000002393", "LBILL-767-2024246", "HAEMATOLOGY", "CBC",
				"Absolute Basophil Count (ABC)", "150", "basophils/µL",
				"/vidmedfiles/BENEFICIARY/55/TestResults/55_1726121031705_55_LBResultReport.pdf", null));
		LabResultFetchDtoCs.add(new LabResultFetchDtoC("11000000002393", "LBILL-767-2024246", "HAEMATOLOGY", "CBC",
				"Mean Platelet Volume (MPV)", "11", "fL",
				"/vidmedfiles/BENEFICIARY/55/TestResults/55_1726121031705_55_LBResultReport.pdf", null));
		LabResultFetchDtoCs.add(new LabResultFetchDtoC("11000000002390", "LBILL-767-2024246", "BIO CHEMISTRY",
				"Thyroid profile test", "Triiodothyronine (T3)", "2", "ng/ml",
				"/vidmedfiles/BENEFICIARY/55/TestResults/55_1726120936748_55_LBResultReport.pdf", null));
//		LabResultFetchDtoCs.add(new LabResultFetchDtoC("11000000002392", "LBILL-767-2024246", "BIO CHEMISTRY", "Bilirubin", "Bilirubin Total", "1", "mg/dl", "/vidmedfiles/BENEFICIARY/55/TestResults/55_1726120883176_55_LBResultReport.pdf", null));
//		LabResultFetchDtoCs.add(new LabResultFetchDtoC("11000000002394", "LBILL-767-2024246", "BIO CHEMISTRY", "Blood sugar Random", "Blood Sugar Random", "130", "mg/dl", "/vidmedfiles/BENEFICIARY/55/TestResults/55_1726120896375_55_LBResultReport.pdf", null));
//		LabResultFetchDtoCs.add(new LabResultFetchDtoC("11000000002391", "LBILL-767-2024246", "BIO CHEMISTRY", "Lipid profile test", "Cholesterol", "130", "mg/dl", "/vidmedfiles/BENEFICIARY/55/TestResults/55_1726120919220_55_LBResultReport.pdf", null));
		LabResultFetchDtoCs.add(new LabResultFetchDtoC("11000000002390", "LBILL-767-2024246", "BIO CHEMISTRY",
				"Thyroid profile test", "Thyroxine (T4)", "60", "ng/ml",
				"/vidmedfiles/BENEFICIARY/55/TestResults/55_1726120936748_55_LBResultReport.pdf", null));
//		LabResultFetchDtoCs.add(new LabResultFetchDtoC("11000000002391", "LBILL-767-2024246", "BIO CHEMISTRY", "Lipid profile test", "HDL", "30", "mg/dl", "/vidmedfiles/BENEFICIARY/55/TestResults/55_1726120919220_55_LBResultReport.pdf", null));
//		LabResultFetchDtoCs.add(new LabResultFetchDtoC("11000000002391", "LBILL-767-2024246", "BIO CHEMISTRY", "Lipid profile test", "Triglycerides", "210", "mg/dl", "/vidmedfiles/BENEFICIARY/55/TestResults/55_1726120919220_55_LBResultReport.pdf", null));
//		LabResultFetchDtoCs.add(new LabResultFetchDtoC("11000000002391", "LBILL-767-2024246", "BIO CHEMISTRY", "Lipid profile test", "VLDL", "45", "mg/dl", "/vidmedfiles/BENEFICIARY/55/TestResults/55_1726120919220_55_LBResultReport.pdf", null));
		for(int i=0;i<LabResultFetchDtoCs.size();i++) {
			 System.out.println(LabResultFetchDtoCs.size());
		}
  
	}
}
