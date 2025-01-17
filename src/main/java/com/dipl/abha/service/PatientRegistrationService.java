//package com.dipl.abha.service;
//import static java.util.Objects.isNull;
//import static java.util.Objects.nonNull;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//import java.util.stream.Collectors;
//import java.util.stream.IntStream;
//
//import javax.transaction.Transactional;
//
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.HttpEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.dipl.abha.dto.PatientRegistrationDTO;
//import com.dipl.abha.patient.dto.PatientDemographics;
//import com.dipl.abha.patient.entities.Center;
//import com.dipl.abha.patient.entities.PatientAddress;
//import com.dipl.abha.patient.entities.PatientCreditDetail;
//import com.dipl.abha.patient.entities.PatientDemography;
//import com.dipl.abha.patient.entities.PatientGuarantor;
//import com.dipl.abha.patient.entities.PatientGuarantorPool;
//import com.dipl.abha.patient.exceptions.DataUpdateException;
//import com.dipl.abha.patient.exceptions.RecordsNotFoundException;
//import com.dipl.abha.patient.model.NewBornBabyPayload;
//import com.dipl.abha.patient.model.PatientAbhaPayload;
//import com.dipl.abha.patient.model.PatientAddressPayload;
//import com.dipl.abha.patient.model.PatientCreditDetailsPayload;
//import com.dipl.abha.patient.model.PatientGuaratorPayload;
//import com.dipl.abha.patient.model.PatientGuaratorPoolPayload;
//import com.dipl.abha.patient.model.PatientJackson.CheckAvaliblityPayload;
//import com.dipl.abha.patient.model.PatientJackson.SearchFilters;
//import com.dipl.abha.patient.model.PatientRegistrationDto;
//import com.dipl.abha.patient.model.VMedBeneficiaryDTO;
//import com.dipl.abha.repositories.CenterRepository;
//import com.dipl.abha.repository.GuaratorPoolRepository;
//import com.dipl.abha.repository.GuaratorRepository;
//import com.dipl.abha.repository.PatientAddressRepository;
//import com.dipl.abha.repository.PatientCreditDetailsRepository;
//import com.dipl.abha.repository.PatientRegistrationRepository;
//import com.dipl.abha.util.JsonPayloadUtility;
//import com.dipl.abha.util.OtherUtil;
//import com.dipl.abha.util.PageRequestUtil;
//import com.dipl.abha.util.ResponseBean;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.JsonMappingException;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import lombok.Synchronized;
//import lombok.extern.slf4j.Slf4j;
//
//@Service
//@Slf4j
//public class PatientRegistrationService {
//
//	@Value("${VIEW_FILE_URL}")
//	private String viewFileUrl;
//
//	@Value("${VMED_PRO_URL}")
//	private String VMED_PRO_URL;
//
//	@Value("${VMED_PR_END_URL}")
//	private String VMED_PR_END_URL;
//
//	@Autowired
//	private PatientRegistrationRepository patientRegistrationRepository;
//	@Autowired
//	private PatientAddressRepository patientAddressRepository;
//	@Autowired
//	private GuaratorRepository guaratorRepository;
//	@Autowired
//	private GuaratorPoolRepository guaratorPoolRepository;
//	@Autowired
//	private PatientCreditDetailsRepository creditDetailsRepository;
//	@Autowired
//	private FileUploadService fileUploadService;
//	@Autowired
//	private CenterRepository centerRepository;
//
//	@Autowired
//	private ModelMapper modelMapper;
//
//	public String admitPatient(PatientDemographics patientPayload, MultipartFile profilePic)
//			throws DataUpdateException {
//		Long userId = OtherUtil.getUserId();
//		Long orgId = OtherUtil.getOrgId();
//		Long centerId = OtherUtil.getCenterId();
//		log.info("patientPayload : {}", JsonPayloadUtility.payloadReturn(patientPayload));
//
////		String MRRNO = this.getMrnNumberNew(patientPayload.getLastName());
////		if (MRRNO == null) {
////			throw new DataUpdateException("MRN is not generated");
////		}
////		log.info("MRRNO==getMrnNumberNew==={}", MRRNO);
//
//		PatientDemography patientDemography = this.savePatientRegistration(patientPayload, profilePic, userId, orgId,
//				centerId);
//
//		Thread referalThread = new Thread(() -> {
//			try {
//				this.sendToVMedRegistation(patientPayload, patientDemography);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		});
//		referalThread.start();
//
//		return patientDemography.getMrnNo();
//
//	}
//
//	public String getMrnNumberNew(String firstName) {
//		String firstChar = !firstName.isEmpty() && firstName != null ? firstName.toUpperCase().substring(0, 1) : "Z";
//		log.info("{} {} {} {} {}", OtherUtil.getCenterId(), OtherUtil.getOrgId(), MRN_NO, LocalDateTime.now(),
//				firstChar);
//
//		Optional<Center> centrOpt = centerRepository.findByIdAndOrgId(OtherUtil.getCenterId(), OtherUtil.getOrgId());
//		if (!centrOpt.isPresent()) {
//			return null;
//		}
////		Optional<Organisation> orgOpt = organisationRepository.findById(OtherUtil.getOrgId());
////		List<Financialyear> finList = financialYearRepository.findByFY(LocalDate.now(), OtherUtil.getOrgId());
////		List<OrganizationConfig> orgList = organizationConfigRepository.findByOrgId(OtherUtil.getOrgId());
////		String format= "";
////		if(orgList.size()>0) {
////			format =orgList.get(0).getMrnNo();
////		}
//
//		String ccCode = "";
//		if (centrOpt.isPresent()) {
//			ccCode = centrOpt.get().getCode();
//		}
//		String CODENOCHECK = ccCode + "0";
//
//		if (firstChar != null) {
//			CODENOCHECK = ccCode + firstChar;
//		}
//		log.info("CODENOCHECK====={}", CODENOCHECK);
//
//		String mrnoSr = patientRegistrationRepository.generateMrnoNewR(CODENOCHECK + "%");
//		log.info("mrnoSr=1===={}", mrnoSr);
//
////		String mrnoSr = patientRegistrationRepository.generateMrnoNew(CODENOCHECK + "%");
////		log.info("mrnoSr==2==={}", mrnoSr);
//		return CODENOCHECK + mrnoSr;
//
//	}
//
//	@Transactional(rollbackOn = DataUpdateException.class)
//	@Synchronized
//	private PatientDemography savePatientRegistration(PatientDemographics patientPayload, MultipartFile profilePic,
//			Long userId, Long orgId, Long centerId) {
//
//		PatientDemography patientDemography = this.addDemoGraphyDetailsAlongWithProfilePic(patientPayload, profilePic,
//				userId, orgId, centerId);
//		if (nonNull(patientDemography) && nonNull(patientDemography.getId())) {
//			this.savePatientAddressAndGuarantor(patientPayload, patientDemography.getId(), userId, orgId, centerId);
//		}
//		return patientDemography;
//	}
//
//	/**
//	 * 
//	 * @param patientPayload
//	 * @param profilePic
//	 * @param userId
//	 * @param centerId
//	 * @param orgId
//	 * @param mRRNO
//	 * @return
//	 */
//	private PatientDemography addDemoGraphyDetailsAlongWithProfilePic(PatientDemographics patientPayload,
//			MultipartFile profilePic, Long userId, Long orgId, Long centerId) throws DataUpdateException {
//		PatientDemography patientDemography = null;
//		if (patientPayload.getId() != null && patientPayload.getId() > 0) {
//			Optional<PatientDemography> existingpatientDemographyRecord = patientRegistrationRepository
//					.findById(patientPayload.getId());
//			if (existingpatientDemographyRecord.isPresent()) {
//				patientDemography = existingpatientDemographyRecord.get();
//				patientDemography = this.setPatientDemoGraphyObj(patientDemography, patientPayload, orgId, centerId);
//				patientDemography.setModifiedBy(userId);
//			}
//		} else {
//			patientDemography = new PatientDemography();
//			patientDemography = this.setPatientDemoGraphyObj(patientDemography, patientPayload, orgId, centerId);
//			patientDemography.setCreatedDate(LocalDateTime.now());
//			patientDemography.setCreatedBy(userId);
//
//			String MRRNO = this.getMrnNumberNew(patientPayload.getLastName());
//			if (MRRNO == null) {
//				throw new DataUpdateException("MRN is not generated, center is empty");
//			}
//			log.info("MRRNO==getMrnNumberNew==={}", MRRNO);
//			patientDemography.setMrnNo(MRRNO);
//		}
//
//		patientDemography.setStatus(patientPayload.getStatus());
//		patientDemography = patientRegistrationRepository.save(patientDemography);
//		if (nonNull(patientDemography.getReferralId()) && patientDemography.getReferralId() != 0L) {
//			patientRegistrationRepository.updateHimsPatientIdInReferral(patientPayload.getReferralId(),
//					patientDemography.getId());
//		}
//		return patientDemography;
//	}
//
//	/**
//	 * @param patientPayloadx
//	 * @param patientId
//	 * @param userId
//	 * @param centerId
//	 * @param orgId
//	 * @throws DataUpdateException
//	 */
//
//	private void savePatientAddressAndGuarantor(PatientRegistrationDto patientPayload, Long patientId, Long userId,
//			Long orgId, Long centerId) throws DataUpdateException {
//		List<PatientAddressPayload> patientAddressPayloads = Arrays.asList(patientPayload.getPatientAddress(),
//				patientPayload.getSecPatAddress());
//
//		this.addPatientAddress(patientAddressPayloads, patientId, userId, centerId);
//
//		List<PatientGuaratorPayload> patientGuaratorsAddressPayload = Arrays.asList(patientPayload.getGuarator(),
//				patientPayload.getSecGuarator());
//
//		List<PatientGuaratorPoolPayload> patientGuaratorsPoolPayloads = Arrays.asList(patientPayload.getGarAddress(),
//				patientPayload.getSecGarAddress());
//
//		List<Long> guratorIds = this.addPatientGuaratorAddress(patientGuaratorsAddressPayload, patientId, userId);
//		if (nonNull(guratorIds)) {
//			IntStream.range(0, guratorIds.size())
//					.forEach(i -> patientGuaratorsPoolPayloads.get(i).setGuarantorId(guratorIds.get(i)));
//		}
//		this.addPatientGuaratorPool(patientGuaratorsPoolPayloads, patientId, userId);
//		this.addPatientCreditDetails(patientPayload.getCreditDetails(), patientId, userId, orgId);
//
//	}
//
//	public void sendToVMedRegistation(PatientRegistrationDto patientPayload, PatientDemography patientDemography) {
//		try {
//			log.info("vmed registration start patientPayload.getId() {} patientDemography.getId() {}",
//					patientPayload.getId(), patientDemography.getId());
//			if (!patientPayload.getIsEmergency().equals("Y")
//					&& (patientPayload.getId() == null || patientPayload.getId() == 0L)) {
//				RestTemplate restTemplate = new RestTemplate();
////				String resourceUrl = "https://vmedproqaretail.dhanushinfotech.com/registration/beneficiary/registration?TENANT_ID=72";
//				log.info("{}{}", VMED_PRO_URL, VMED_PR_END_URL);
//				VMedBeneficiaryDTO vMedBeneficiaryPayload = modelMapper.map(patientDemography,
//						VMedBeneficiaryDTO.class);
//				vMedBeneficiaryPayload.setMobile(patientPayload.getPatientAddress().getMobile1());
//				vMedBeneficiaryPayload.setEmail(patientPayload.getPatientAddress().getEmail());
//				JsonPayloadUtility.payloadPrint(vMedBeneficiaryPayload);
//				HttpEntity<VMedBeneficiaryDTO> requestPayload = new HttpEntity<VMedBeneficiaryDTO>(
//						vMedBeneficiaryPayload);
//				log.info("requestPayload {}", requestPayload);
//				ResponseBean response = restTemplate.postForObject(VMED_PRO_URL + VMED_PR_END_URL, requestPayload,
//						ResponseBean.class);
//				log.info("response {}", response);
//				Map<String, Object> data = (Map<String, Object>) response.getData();
//				JsonPayloadUtility.payloadPrint(data);
//				patientRegistrationRepository.updateReferralIdInPatientDemograpy(
//						Long.valueOf(data.get("id").toString()), patientDemography.getId());
//				log.info("vmed registration end {}", patientPayload.getId());
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
////			throw new DataUpdateException("Patient Registration has failed to save ");
//		}
//	}
//
//	/**
//	 * 
//	 * @param creditDetails
//	 * @param patientId
//	 * @param userId
//	 * @param centerId
//	 * @param orgId
//	 * @throws DataUpdateException
//	 */
//	private void addPatientCreditDetails(List<PatientCreditDetailsPayload> creditDetails, Long patientId, Long userId,
//			Long orgId) throws DataUpdateException {
//		if (creditDetails.isEmpty())
//			return;
//		List<PatientCreditDetail> credit = new ArrayList<>();
//
//		for (PatientCreditDetailsPayload creditPayloads : creditDetails) {
//			if (isNull(credit)) {
//				continue;
//			}
//			PatientCreditDetail patientCreditDetail = null;
//			if (creditPayloads.getId() != null && creditPayloads.getId() > 0) {
//				Optional<PatientCreditDetail> existingRecord = creditDetailsRepository.findById(creditPayloads.getId());
//				if (existingRecord.isPresent()) {
//					patientCreditDetail = existingRecord.get();
//					patientCreditDetail = this.setPatientCreditDetailObj(patientCreditDetail, creditPayloads, orgId);
//					patientCreditDetail.setModifiedBy(userId);
//					patientCreditDetail.setModifiedDate(LocalDateTime.now());
//				}
//			} else {
//				patientCreditDetail = new PatientCreditDetail();
//				patientCreditDetail = this.setPatientCreditDetailObj(patientCreditDetail, creditPayloads, orgId);
//				patientCreditDetail.setCreatedBy(userId);
//				patientCreditDetail.setCreatedDate(LocalDateTime.now());
//			}
//			patientCreditDetail.setPatientId(patientId);
//			patientCreditDetail.setStatus(creditPayloads.getStatus());
//			credit.add(patientCreditDetail);
//		}
//		if (!credit.isEmpty()) {
//			creditDetailsRepository.saveAll(credit);
//		}
//	}
//
//	private PatientCreditDetail setPatientCreditDetailObj(PatientCreditDetail patientCreditDetail,
//			PatientCreditDetailsPayload creditPayloads, Long orgId) {
//		return patientCreditDetail.toBuilder().cmpschmemberId(creditPayloads.getCmpschmemberId())
//				.companyschemeId(creditPayloads.getCompanySchemeId())
//				.creditCompanyId(creditPayloads.getCreditCompanyId()).isPrimary(creditPayloads.getIsPrimary())
//				.orgId(orgId).transferFlag(creditPayloads.getTransferFlag()).build();
//	}
//
//	/**
//	 * 
//	 * @param patientGuaratorsPoolPayloads
//	 * @param patientId
//	 * @param userId
//	 * @param centerId
//	 * @param orgId
//	 * @throws DataUpdateException
//	 */
//	private void addPatientGuaratorPool(List<PatientGuaratorPoolPayload> patientGuaratorsPoolPayloads, Long patientId,
//			Long userId) throws DataUpdateException {
//		if (patientGuaratorsPoolPayloads.isEmpty())
//			return;
//		List<PatientGuarantorPool> guratorPool = new ArrayList<>();
//		for (PatientGuaratorPoolPayload guratorPoolPayloads : patientGuaratorsPoolPayloads) {
//
//			if (guratorPoolPayloads != null) {
//				PatientGuarantorPool patientGuaratorPool = null;
//				if (guratorPoolPayloads.getId() != null && guratorPoolPayloads.getId() > 0) {
//					Optional<PatientGuarantorPool> existingRecord = guaratorPoolRepository
//							.findById(guratorPoolPayloads.getId());
//					if (existingRecord.isPresent()) {
//						patientGuaratorPool = existingRecord.get();
//						patientGuaratorPool = setPatientGuaratorPoolObj(patientGuaratorPool, guratorPoolPayloads);
//						patientGuaratorPool.setModifiedBy(userId);
//						patientGuaratorPool.setModifiedDate(LocalDateTime.now());
//					}
//				} else {
//					patientGuaratorPool = new PatientGuarantorPool();
//					patientGuaratorPool = setPatientGuaratorPoolObj(patientGuaratorPool, guratorPoolPayloads);
//					patientGuaratorPool.setCreatedBy(userId);
//					patientGuaratorPool.setCreatedDate(LocalDateTime.now());
//					patientGuaratorPool.setGuarantorId(guratorPoolPayloads.getGuarantorId());
//
//				}
//				patientGuaratorPool.setPatientId(patientId);
//				patientGuaratorPool.setStatus(guratorPoolPayloads.getStatus());
//				guratorPool.add(patientGuaratorPool);
//			}
//		}
//		if (!guratorPool.isEmpty()) {
//			guaratorPoolRepository.saveAll(guratorPool);
//		}
//	}
//
//	private PatientGuarantorPool setPatientGuaratorPoolObj(PatientGuarantorPool patientGuaratorPool,
//			PatientGuaratorPoolPayload guratorPoolPayloads) {
//		return patientGuaratorPool.toBuilder().address1(guratorPoolPayloads.getAddress1())
//				.address2(guratorPoolPayloads.getAddress2()).transferFlag(guratorPoolPayloads.getTransferFlag())
//				.areaId(guratorPoolPayloads.getAreaId()).countryId(guratorPoolPayloads.getCountryId())
//				.stateId(guratorPoolPayloads.getStateId()).cityId(guratorPoolPayloads.getCityId())
//				.districtId(guratorPoolPayloads.getDistrictId()).email(guratorPoolPayloads.getEmail())
//				.sendEmail(guratorPoolPayloads.getSendEmail()).sendSms(guratorPoolPayloads.getSendSms())
//				.fax(guratorPoolPayloads.getFax()).zip(guratorPoolPayloads.getZip())
//				.webaddress(guratorPoolPayloads.getWebAddress()).password(guratorPoolPayloads.getPassword())
//				.primaryGuarantor(guratorPoolPayloads.getPrimaryGuarantor())
//				.officePhone(guratorPoolPayloads.getOfficePhone()).mobile1(guratorPoolPayloads.getMobile1())
//				.mobile2(guratorPoolPayloads.getMobile2()).homePhone(guratorPoolPayloads.getHomePhone())
//				.guarantorTypeId(guratorPoolPayloads.getGuarantorTypeId()).build();
//	}
//
//	/**
//	 * 
//	 * @param patientGuaratorsAddressPayload
//	 * @param patientId
//	 * @param userId
//	 * @param centerId
//	 * @param orgId
//	 * @return
//	 * @throws DataUpdateException
//	 */
//	private List<Long> addPatientGuaratorAddress(List<PatientGuaratorPayload> patientGuaratorsAddressPayload,
//			Long patientId, Long userId) throws DataUpdateException {
//		if (patientGuaratorsAddressPayload.isEmpty())
//			return null;
//		List<PatientGuarantor> gurator = new ArrayList<>();
////		System.out.println(patientGuaratorsAddressPayload);
//		for (PatientGuaratorPayload guratorPayloads : patientGuaratorsAddressPayload) {
//
//			PatientGuarantor patientGuarator = null;
//			if (guratorPayloads != null) {
//				if (guratorPayloads.getId() != null && guratorPayloads.getId() > 0) {
//					Optional<PatientGuarantor> existingRecord = guaratorRepository.findById(guratorPayloads.getId());
//					if (existingRecord.isPresent()) {
//						patientGuarator = existingRecord.get();
//						patientGuarator = setPatientGuarantorObj(patientGuarator, guratorPayloads);
//						patientGuarator.setModifiedBy(userId);
//						patientGuarator.setModifiedDate(LocalDateTime.now());
//					}
//				} else {
//					patientGuarator = new PatientGuarantor();
//					patientGuarator = setPatientGuarantorObj(patientGuarator, guratorPayloads);
//					patientGuarator.setCreatedBy(userId);
//					patientGuarator.setCreatedDate(LocalDateTime.now());
//				}
//				patientGuarator.setStatus(guratorPayloads.getStatus());
//				gurator.add(patientGuarator);
//			}
//		}
//		if (!gurator.isEmpty()) {
//			List<PatientGuarantor> gurators = guaratorRepository.saveAll(gurator);
//			if (nonNull(gurators) && !gurators.isEmpty()) {
//				return gurator.stream().map(PatientGuarantor::getId).collect(Collectors.toList());
//			}
//		}
//		return null;
//	}
//
//	private PatientGuarantor setPatientGuarantorObj(PatientGuarantor patientGuarator,
//			PatientGuaratorPayload guratorPayloads) {
//		return patientGuarator.toBuilder().guarantorName(guratorPayloads.getGuarantorName())
//				.guarantorRelationshipid(guratorPayloads.getGuarantorRelationshipId())
//				.transferFlag(guratorPayloads.getTransferFlag()).address(guratorPayloads.getAddress()).build();
//	}
//
//	/**
//	 * 
//	 * @param addressPayloads
//	 * @param patientId
//	 * @param userId
//	 * @param centerId
//	 * @param orgId
//	 * @throws DataUpdateException
//	 */
//	private void addPatientAddress(List<PatientAddressPayload> addressPayloads, Long patientId, Long userId,
//			Long centerId) throws DataUpdateException {
//		if (addressPayloads.isEmpty())
//			return;
//		List<PatientAddress> address = new ArrayList<>();
//		for (PatientAddressPayload addr : addressPayloads) {
//			if (isNull(addr)) {
//				continue;
//			}
//			PatientAddress patientAddress = null;
//			if (addr.getId() != null && addr.getId() > 0) {
//				Optional<PatientAddress> existingRecord = patientAddressRepository.findById(addr.getId());
//				if (existingRecord.isPresent()) {
//					patientAddress = existingRecord.get();
//					patientAddress = setPatientAddressObj(patientAddress, addr, centerId);
//					patientAddress.setModifiedBy(userId);
//					patientAddress.setModifiedDate(LocalDateTime.now());
//				}
//			} else {
//				patientAddress = new PatientAddress();
//				patientAddress = setPatientAddressObj(patientAddress, addr, centerId);
//				patientAddress.setCreatedBy(userId);
//				patientAddress.setCreatedDate(LocalDateTime.now());
//				patientAddress.setPatientId(patientId);
//			}
//			patientAddress.setStatus(addr.getStatus());
//			if (patientAddress.getAddressTypeId() == null || patientAddress.getAddressTypeId() == 0) {
//				patientAddress.setAddressTypeId(1);
//			}
//			address.add(patientAddress);
//		}
//		if (!address.isEmpty()) {
//			patientAddressRepository.saveAll(address);
//		}
//	}
//
//	private PatientAddress setPatientAddressObj(PatientAddress patientAddress, PatientAddressPayload addr,
//			Long centerId) {
//		return patientAddress.toBuilder().address1(addr.getAddress1()).address2(addr.getAddress2())
//				.countryId(addr.getCountryId()).stateId(addr.getStateId()).districtId(addr.getDistrictId())
//				.cityId(addr.getCityId()).areaId(addr.getAreaId()).email(addr.getEmail())
//				.transferFlag(addr.getTransferFlag()).sendEmail(addr.getSendEmail()).sendSms(addr.getSendSms())
//				.fax(addr.getFax()).mobile1(addr.getMobile1()).zip(addr.getZip()).mobile2(addr.getMobile2())
//				.homePhone(addr.getHomePhone()).officePhone(addr.getOfficePhone())
//				.addressTypeId(addr.getAddressTypeId()).primaryAddress(addr.getPrimaryAddress()).centerId(centerId)
//				.build();
//	}
//
//	private PatientDemography setPatientDemoGraphyObj(PatientDemography patientDemography,
//			PatientRegistrationDto patientPayload, Long orgId, Long centerId) {
//		return patientDemography.toBuilder().genderId(patientPayload.getGenderId())
//				.employeeId(patientPayload.getEmployeeId()).dob(patientPayload.getDob()).age(patientPayload.getAge())
//				.casetypeId(patientPayload.getCasetypeId()).educationId(patientPayload.getEducationId())
//				.identificationCard(patientPayload.getIdentificationCard()).famliySize(patientPayload.getFamliySize())
//				.patientCategoryId(patientPayload.getPatientCategoryId())
//				.maritalstatus(patientPayload.getMaritalStatus()).treatingDoctorId(patientPayload.getTreatingDoctorId())
//				.tariffCategory(patientPayload.getTariffCategory()).uhid(patientPayload.getUhid())
//				.salutationId(patientPayload.getSalutationId()).religionId(patientPayload.getReligionId())
//				.relationshipId(patientPayload.getRelationshipId()).paymentTypeId(patientPayload.getPaymentTypeId())
//				.patientType(patientPayload.getPatientType()).patientsourceId(patientPayload.getPatientSourceId())
//				.bloodGroup(patientPayload.getBloodGroup()).occupationId(patientPayload.getOccupationId())
//				.nhifCardno(patientPayload.getNhifCardNo()).transferFlag(patientPayload.getTransferFlag())
//				.nationality(patientPayload.getNationality()).orgId(orgId).centerId(centerId)
//				.patientFirstName(patientPayload.getFirstName()).patientLastName(patientPayload.getLastName())
//				.patientMiddleName(patientPayload.getMiddleName()).referralId(patientPayload.getReferralId())
//				.nationalHealthId(patientPayload.getNationalHealthId())
//				.nationalHealthNumber(patientPayload.getNationalHealthNumber())
//				.nationalHealthResponse(patientPayload.getNationalHealthResponse())
//				.isAbhaLinked(patientPayload.getIsAbhaLinked())
//				.canViewInOtherCenters(patientPayload.getCanViewInOtherCenters())
//				.patientsourcedtls(patientPayload.getPatientSourcedTls())
//				.identificationTypeId(patientPayload.getIdentificationTypeId())
//				.incomegroupId(patientPayload.getIncomegroupId()).isEmergency(patientPayload.getIsEmergency())
//				.isTelemedicine(patientPayload.getIsTelemedicine())
//				.mayCallThisNumber(patientPayload.getMayCallThisNumber())
//				.mayLeaveMessage(patientPayload.getMayLeaveMessage()).raceId(patientPayload.getRaceId())
//				.organDonationStatus(patientPayload.getOrganDonationStatus())
//				.policeStation(patientPayload.getPoliceStation()).mlc(patientPayload.getMlc())
//				.hospitalConfirmation(patientPayload.getHospitalConfirmation()).declared(patientPayload.getDeclared())
//				.tribe(patientPayload.getTribe()).build();
//	}
//
//	/**
//	 * 
//	 * @param firstName
//	 * @return
//	 */
//	public String getMrnNumber(String firstName) {
//		String firstChar = !firstName.isEmpty() && firstName != null ? firstName.toUpperCase().substring(0, 1) : "Z";
//		log.info("{} {} {} {} {}", OtherUtil.getCenterId(), OtherUtil.getOrgId(), MRN_NO, LocalDateTime.now(),
//				firstChar);
//		return patientRegistrationRepository.generateMrno(OtherUtil.getCenterId(), OtherUtil.getOrgId(), MRN_NO,
//				firstChar);
//	}
//
//	public Map<String, Object> getPatientDetailsByIdOrMRNNumber(Long id, String mrnNumber) {
//		if (id != null && id > 0) {
//			Map<String, Object> pat = new HashMap<>(patientRegistrationRepository.findByPatientId2(id, "-1"));
//			return this.populateCreditDetails(this.populateGaurantorDetails(this.populateAddressDetails(pat, id), id),
//					id);
//		} else if (mrnNumber != null && !mrnNumber.isEmpty()) {
//			Map<String, Object> pat = new HashMap<>(patientRegistrationRepository.findByPatientId2(-1L, mrnNumber));
//			Long patientId = Long.valueOf(pat.get("patient_id").toString());
//			return populateCreditDetails(populateGaurantorDetails(populateAddressDetails(pat, patientId), patientId),
//					patientId);
//		}
//		return null;
//	}
//
//	public List<Map<String, Object>> checkPatientAvaliblity(CheckAvaliblityPayload payload)
//			throws RecordsNotFoundException {
//		return this.getCheckAvailabilityofPatient(payload);
//	}
//
//	private List<Map<String, Object>> getCheckAvailabilityofPatient(CheckAvaliblityPayload payload)
//			throws RecordsNotFoundException {
////		List<Map<String, Object>> list = null;
//		Long orgId = OtherUtil.getOrgId();
//		if (payload.getUhId() != null && !payload.getUhId().isEmpty()) {
//			return patientRegistrationRepository.findCheckAvailabilityByUHId(payload.getUhId(), orgId,
//					payload.getFirstName(), payload.getLastName());
//		} else if (payload.getIdentificationCard() != null && !payload.getIdentificationCard().isEmpty()
//				&& !"NA".equalsIgnoreCase(payload.getIdentificationCard()) && payload.getIdentificationTypeId() != null
//				&& payload.getIdentificationTypeId() > 0) {
//			return patientRegistrationRepository.findCheckAvailabilityByMRNIden(payload.getIdentificationCard(),
//					payload.getIdentificationTypeId(), orgId);
//		} else if (payload.getNationalHealthNumber() != null && !payload.getNationalHealthNumber().isEmpty()) {
//			return patientRegistrationRepository.findByNationalHealthId(payload.getNationalHealthNumber(), orgId);
//		} else {
//			return patientRegistrationRepository.findCheckAvailabilityByMRN("%" + payload.getFirstName() + "%",
//					"%" + payload.getLastName() + "%", orgId, payload.getDob());
//		}
//	}
//
//	/**
//	 * 
//	 * @param filters
//	 * @return
//	 */
//	public Page<List<Map<String, Object>>> getPatientInfoBySearchFilters(SearchFilters filters)
//			throws RecordsNotFoundException {
//		Long centerId = OtherUtil.getCenterId();
//		filters.setDefaultsIfEmpty();
//		Pageable pageable = pageRequestUtil.getPagableObj(filters.getPage(), filters.getSize());
//		return this.getPatientOrIPBedTransferPatientDetails(centerId, filters, pageable);
//	}
//
//	private Page<List<Map<String, Object>>> getPatientOrIPBedTransferPatientDetails(Long centerId,
//			SearchFilters filters, Pageable pageable) throws RecordsNotFoundException {
//		if ("PATIENTSERACH".equalsIgnoreCase(filters.getSerchType())) {
//			return patientRegistrationRepository.findAllPatients(pageable, filters.getMrNo(), filters.getFirstName(),
//					filters.getLastName(), filters.getDateOfBirths(), filters.getAge(), filters.getMobileNo(),
//					filters.getArea(), filters.getCompanyId(), filters.getCompanySchemeId(), OtherUtil.getOrgId());
//		} else if ("IPSEARCH".equalsIgnoreCase(filters.getSerchType())) {
//			return patientRegistrationRepository.findAllOpBedPatients(pageable, filters.getIpNo(), filters.getMrNo(),
//					filters.getFirstName(), filters.getLastName(), filters.getDateOfBirths(), filters.getAge(),
//					filters.getMobileNo(), filters.getArea(), filters.getCompanyName(), filters.getCompanySchemeName(),
//					filters.getConsultingDoctor(), OtherUtil.getCenterId(), filters.getMedicoLegalCase());
//		} else if ("MORTUARYIPSEARCH".equalsIgnoreCase(filters.getSerchType())) {
//			return patientRegistrationRepository.findAllIpBedPatients(pageable, filters.getIpNo(), filters.getMrNo(),
//					filters.getFirstName(), filters.getLastName(), filters.getDateOfBirths(), filters.getAge(),
//					filters.getMobileNo(), filters.getArea(), filters.getCompanyName(), filters.getCompanySchemeName(),
//					filters.getConsultingDoctor(), OtherUtil.getCenterId(), filters.getMedicoLegalCase());
//		} else if ("PACKAGEALLOCATION".equalsIgnoreCase(filters.getSerchType())) {
//			return patientRegistrationRepository.findAllPatientsByPackageId(pageable, filters.getIpNo(),
//					filters.getMrNo(), filters.getFirstName(), filters.getLastName(), filters.getDateOfBirths(),
//					filters.getAge(), filters.getMobileNo(), filters.getArea(), filters.getCompanyName(),
//					filters.getCompanySchemeName(), filters.getConsultingDoctor(), OtherUtil.getCenterId(),
//					filters.getMedicoLegalCase());
//		} else if ("IPDISCHARGESEARCH".equalsIgnoreCase(filters.getSerchType())) {
//			return patientRegistrationRepository.findAllPatientsDoneByIpdNofications(pageable, filters.getIpNo(),
//					filters.getMrNo(), filters.getFirstName(), filters.getLastName(), filters.getDateOfBirths(),
//					filters.getAge(), filters.getMobileNo(), filters.getArea(), filters.getCompanyName(),
//					filters.getCompanySchemeName(), filters.getConsultingDoctor(), OtherUtil.getCenterId());
//		}
//		return null;
//	}
//
//	public List<Map<String, Object>> getcreditSchemeId(Long schemeId) {
//		return patientRegistrationRepository.getDetailsBySchemeId(schemeId);
//	}
//
//	@Transactional(rollbackOn = DataUpdateException.class)
//	@Synchronized
//	public Long admitNewBornBaby(NewBornBabyPayload newBornBabyPayload)
//			throws JsonMappingException, JsonProcessingException {
//		if (nonNull(newBornBabyPayload.getParent_patientId())) {
//
////			Optional<PatientDemography> patientRegData = patientRegistrationRepository
////					.findById(newBornBabyPayload.getParent_patientId());
//			PatientAddress patientAdd = patientAddressRepository.findById(newBornBabyPayload.getParent_patientId())
//					.get();
//			PatientGuarantorPool poolAdd = guaratorPoolRepository
//					.findTop1ByPatientIdAndGuarantorTypeId(newBornBabyPayload.getParent_patientId(), 1).get();
//
//			PatientGuarantor guarantorAdd = guaratorRepository.findById(poolAdd.getGuarantorId()).get();
//
////			ObjectMapper obj = new ObjectMapper();
//			PatientAddressPayload patientAddress = modelMapper.map(patientAdd, PatientAddressPayload.class);
//			PatientGuaratorPayload guaratorAddress = modelMapper.map(guarantorAdd, PatientGuaratorPayload.class);
//			PatientGuaratorPoolPayload secGarAddress = modelMapper.map(poolAdd, PatientGuaratorPoolPayload.class);
//
//			PatientRegistrationDto patientPayload = PatientRegistrationDto.builder().age(newBornBabyPayload.getAge())
//					.dob(newBornBabyPayload.getDob()).firstName(newBornBabyPayload.getBabyFirstname())
//					.middleName(newBornBabyPayload.getBabyMiddlename() != null
//							&& !newBornBabyPayload.getBabyMiddlename().equalsIgnoreCase("")
//									? newBornBabyPayload.getBabyMiddlename()
//									: null)
//					.lastName(newBornBabyPayload.getBabyLastname()).salutationId(newBornBabyPayload.getSalutationId())
//					.genderId(newBornBabyPayload.getGenderId()).patientAddress(patientAddress).guarator(guaratorAddress)
//					.secGuarator(guaratorAddress).garAddress(secGarAddress).secGarAddress(secGarAddress)
//					.creditDetails(new ArrayList<>()).build();
//
////			System.out.println(patientPayload);
//			this.admitPatient(patientPayload, null);
//		}
//		return 1L;
//	}
//
//	private Map<String, Object> populateAddressDetails(Map<String, Object> object, Long patientId) {
//
//		object.put("patient_address", patientRegistrationRepository.findAddressDetailsByPatientId(patientId));
//
//		return object;
//	}
//
//	private Map<String, Object> populateGaurantorDetails(Map<String, Object> object, Long patientId) {
//
//		object.put("guarantor_address", patientRegistrationRepository.findGaurantorDetailsByPatientId(patientId));
//
//		return object;
//	}
//
//	private Map<String, Object> populateCreditDetails(Map<String, Object> object, Long patientId) {
//
//		object.put("credit_details", patientRegistrationRepository.findCreditDetailsByPatientId(patientId));
//
//		return object;
//	}
//
//	public void updatePatient(PatientAbhaPayload verificationDTO) {
//		ObjectMapper objMapper = new ObjectMapper();
//		JsonNode jsonNode = null;
//		try {
//			if (verificationDTO.getNationalResponse() != null && !verificationDTO.getNationalResponse().isEmpty())
//				jsonNode = objMapper.readTree(verificationDTO.getNationalResponse());
//		} catch (JsonMappingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (JsonProcessingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		Optional<PatientDemography> patient = patientRegistrationRepository.findByMrnNo(verificationDTO.getMrno());
//		if (patient.isPresent()) {
//			patient.get().setNationalHealthId(
//					(verificationDTO.getNationalHealthId() != null && !verificationDTO.getNationalHealthId().isEmpty())
//							? verificationDTO.getNationalHealthId()
//							: null);
//			patient.get()
//					.setNationalHealthNumber((verificationDTO.getNationalHealthNumber() != null
//							&& !verificationDTO.getNationalHealthNumber().isEmpty())
//									? verificationDTO.getNationalHealthNumber()
//									: null);
//			patient.get().setNationalHealthResponse(jsonNode);
//			patient.get().setIsAbhaLinked("1");
//			patientRegistrationRepository.save(patient.get());
//		}
//	}
//}
