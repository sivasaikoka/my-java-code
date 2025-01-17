package com.dipl.abha.uhi.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.commons.text.StrSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.dipl.abha.config.TenantContext;
import com.dipl.abha.dto.AbhaQueryTable;
import com.dipl.abha.dto.ResponseBean;
import com.dipl.abha.service.AllAbdmCentralDbSave;
import com.dipl.abha.service.ExtractResultSetService;
import com.dipl.abha.uhi.dto.Agent;
import com.dipl.abha.uhi.dto.Catalog;
import com.dipl.abha.uhi.dto.Category;
import com.dipl.abha.uhi.dto.City;
import com.dipl.abha.uhi.dto.Context;
import com.dipl.abha.uhi.dto.Country;
import com.dipl.abha.uhi.dto.Descriptor;
import com.dipl.abha.uhi.dto.DoctorDetailsSlotsDto;
import com.dipl.abha.uhi.dto.End;
import com.dipl.abha.uhi.dto.Fulfillment;
import com.dipl.abha.uhi.dto.HprDoctorMappingDto;
import com.dipl.abha.uhi.dto.Item;
import com.dipl.abha.uhi.dto.Location;
import com.dipl.abha.uhi.dto.Message;
import com.dipl.abha.uhi.dto.Price;
import com.dipl.abha.uhi.dto.Provider;
import com.dipl.abha.uhi.dto.RequestDto;
import com.dipl.abha.uhi.dto.SearchByDoctorSpeciality;
import com.dipl.abha.uhi.dto.SearchTypeDto;
import com.dipl.abha.uhi.dto.Slots;
import com.dipl.abha.uhi.dto.Start;
import com.dipl.abha.uhi.dto.Tags;
import com.dipl.abha.uhi.dto.Time;
import com.dipl.abha.util.ConstantUtil;
import com.dipl.abha.util.Crypt;
import com.dipl.abha.util.JdbcTemplateHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SearchService {

	@Autowired
	private UHIService uhiService;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private JdbcTemplateHelper jdbcTemplateHelper;

	@Autowired
	private ExtractResultSetService extractResultSetService;

	@Autowired
	private HSPABulidService bulidService;

	@Autowired
	private Crypt crypt;

	@Value("${ON_SEARCH}")
	private String on_search;

	@Value("${PROVIDER_URI}")
	private String providerUrl;

	@Value("${PROVIDER_ID}")
	private String providerId;

	@Value("${HSPA_SUBSCRIBER_ID}")
	private String hspaSubscriberId;

	@Value("${HSPA_PUBLIC_KEY_ID}")
	private String hspaPublicId;

	@Autowired
	private AllAbdmCentralDbSave abdmCentralDbSave;

	public ResponseEntity<?> findDoctorSpeacialityAndfacility(SearchByDoctorSpeciality search,
			List<HprDoctorMappingDto> Querydata) {
		ResponseEntity<?> dto = null;
		try {

			dto = this.OnSearch(this.BuildCatalog(Querydata), search.getContext());
			log.info("dto===========" + dto);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return dto;

	}

	public ResponseEntity<?> findDoctorName(SearchByDoctorSpeciality search, List<HprDoctorMappingDto> Querydata) {
		ResponseEntity<?> dto = null;
		try {

			dto = this.OnSearch(this.BuildCatalog(Querydata), search.getContext());
			log.info("dto===========" + dto);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return dto;
	}

	public ResponseEntity<?> findHprId(SearchByDoctorSpeciality search, List<HprDoctorMappingDto> Querydata) {
		ResponseEntity<?> dto = null;
		try {

			dto = this.OnSearch(this.BuildCatalog(Querydata), search.getContext());
			log.info("dto===========" + dto);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return dto;
	}

	public ResponseEntity<?> findDoctorDoctorNameAndSpeacialityAndFacility(SearchByDoctorSpeciality search,
			List<HprDoctorMappingDto> Querydata) {
		ResponseEntity<?> dto = null;
		try {

			dto = this.OnSearch(this.BuildCatalog(Querydata), search.getContext());
			log.info("dto===========" + dto);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return dto;
	}

	public ResponseEntity<?> findDoctorDoctorNameAndSpeaciality(SearchByDoctorSpeciality search,
			List<HprDoctorMappingDto> Querydata) {
		ResponseEntity<?> dto = null;
		try {

			dto = this.OnSearch(this.BuildCatalog(Querydata), search.getContext());
			log.info("dto===========" + dto);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return dto;
	}

	public ResponseEntity<?> findDoctorSpeaciality(SearchByDoctorSpeciality search,
			List<HprDoctorMappingDto> Querydata) {
		ResponseEntity<?> dto = null;
		try {

			dto = this.OnSearch(this.BuildCatalog(Querydata), search.getContext());
			log.info("dto===========" + dto);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return dto;
	}

	public Catalog BuildCatalog(List<HprDoctorMappingDto> querydata) {

		Catalog catalog = null;
		Provider provider = null;
		Category category = null;
		Fulfillment fulfillment = null;

		Agent agent = null;
		Tags tags = null;
		Item item = null;

		Start start = null;
		End end = null;

		Time time = null;

		try {
			catalog = new Catalog();
			Descriptor descriptor = new Descriptor();
			descriptor.setName(querydata.get(0).getHspaName());
			descriptor.setImages("HSPA Image");
			descriptor.setShort_desc(querydata.get(0).getHspaLongDesc());
			descriptor.setLong_desc(querydata.get(0).getHspaLongDesc());
			catalog.setDescriptor(descriptor);

			int i = 0;
			if (querydata != null && !querydata.isEmpty()) {
				Map<String, List<HprDoctorMappingDto>> GroupByByDoctorCategoryId = querydata.stream()
						.collect(Collectors.groupingBy(x -> x.getDepartmentName().toString()));

				log.info("GroupByByDoctorCategoryId==============>" + GroupByByDoctorCategoryId);
				List<Category> listOfcategories = new ArrayList<>();
				List<Fulfillment> listOffulfillments = new ArrayList<>();
				List<Item> listOfitems = new ArrayList<>();

				for (Entry<String, List<HprDoctorMappingDto>> sp : GroupByByDoctorCategoryId.entrySet()) {
					if (querydata.stream().filter(s -> s.getDepartmentName().equals(sp.getKey())).findAny()
							.isPresent()) {
						for (int j = 0; j < sp.getValue().size(); j++) {

							category = new Category();
							fulfillment = new Fulfillment();

							Descriptor categoryDescriptor = new Descriptor();
							category.setId(String.valueOf(j));
							categoryDescriptor.setCode(sp.getValue().get(j).getDepartmentName());
							categoryDescriptor.setName(sp.getValue().get(j).getDepartmentName());
							category.setDescriptor(categoryDescriptor);
							listOfcategories.add(category);
							fulfillment.setId(String.valueOf(j));
							fulfillment.setType("Online");
							time = new Time();
							time.setTimestamp(sp.getValue().get(j).getFromDate());
							start = new Start();
							start.setTime(time);
							fulfillment.setStart(start);
							time.setTimestamp(sp.getValue().get(j).getToDate());
							end = new End();
							end.setTime(time);
							fulfillment.setEnd(end);
							agent = new Agent();
							agent.setId(sp.getValue().get(j).getHprId());
							agent.setName(sp.getValue().get(j).getDoctorName());
							tags = new Tags();
							tags.setEducation(sp.getValue().get(j).getQualification());
							tags.setExperience(String.valueOf(sp.getValue().get(j).getYearsExperience()));
							tags.setHprid(sp.getValue().get(j).getHfrId());
							tags.setLanguages(sp.getValue().get(j).getLanguage_name());
							agent.setTags(tags);
							fulfillment.setAgent(agent);
							fulfillment.setTags(tags);
							listOffulfillments.add(fulfillment);
							Descriptor itemDescriptor1 = new Descriptor();
							itemDescriptor1.setName("consulation");
							Price price1 = new Price();
							price1.setCurrency("INR");
							price1.setValue(String.valueOf(sp.getValue().get(j).getVideoConsultationPrice()));
							item = new Item();
							item.setId(String.valueOf(j));
							item.setDescriptor(itemDescriptor1);
							item.setPrice(price1);
							item.setCategory_id(String.valueOf(j));
							item.setFulfillment_id(String.valueOf(j));
							listOfitems.add(item);

						}
					}
				}
				System.out.println("category========" + listOfcategories);

				System.out.println("listOffulfillments========" + listOffulfillments);

				System.out.println("listOfitems========" + listOfitems);
				provider = new Provider();
				provider.setCategories(listOfcategories);
				provider.setFulfillments(listOffulfillments);
				provider.setItems(listOfitems);

				log.info("provider=====================" + provider);
				Descriptor Providerdescriptor = new Descriptor();
				Providerdescriptor.setName(querydata.get(0).getHospitalName());
				Providerdescriptor.setShort_desc(querydata.get(0).getHospitalName());
				Providerdescriptor.setLong_desc(querydata.get(0).getHospitalName());
				provider.setId("1");
				provider.setDescriptor(Providerdescriptor);
				Location location = new Location();
				location.setId(String.valueOf(i + 1));
				location.setDescriptor(Providerdescriptor);
				City city = new City();
				city.setCode(querydata.get(0).getCityCode());
				city.setName(querydata.get(0).getCityName());
				location.setCity(city);
				Country country = new Country();
				country.setName(querydata.get(0).getCountry());
				country.setCode(querydata.get(0).getCountryCode());
				location.setCountry(country);
				location.setGps("");
				location.setAddress("hyderabad");
				provider.setLocation(location);
				System.out.println("providers=======================" + provider);
				catalog.setProviders(provider != null ? Arrays.asList(provider) : Collections.EMPTY_LIST);
				System.out.println("catalog=================" + catalog);
				System.out.println(" =====================" + objectMapper.writeValueAsString(catalog));
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return catalog;
	}

	public ResponseEntity<?> OnSearch(Catalog catalog, Context context) {
		RequestDto requestDto = new RequestDto();
		ResponseEntity<?> restData = null;
		Message message = null;
		ResponseBean bean = new ResponseBean();

		try {
			if (catalog != null) {
				context.setProvider_id(providerId);
				context.setProvider_uri(providerUrl);
				context.setAction("on_search");
				requestDto.setContext(context);
				log.info("context=============" + context);
				message = new Message();
				message.setCatalog(catalog);
				requestDto.setMessage(message);
				log.info("message=============" + message);
				String request = objectMapper.writeValueAsString(requestDto).replace(" ", "");
				log.info("Request============================" + request);
				log.info("=====================CALLING UHI GATEWAY======================");
				abdmCentralDbSave.saveHspaRequest("/on_search", requestDto.getContext().getTransaction_id(), null,
						request, null);
				restData = uhiService.checkRestCallSuccessOrNot(uhiService.restCallApi(
						crypt.generateAuthorizationParams(request, hspaSubscriberId, hspaPublicId), request,
						on_search));
			}

		} catch (Exception ex) {
			log.error("Hspa select method error: " + ex);
			bean.setStatus(HttpStatus.EXPECTATION_FAILED);
			bean.setMessage(ConstantUtil.SOMETHING_WENT_WRONG);
			bean.setData(ex.getMessage());
			ex.printStackTrace();
			return new ResponseEntity<>(bean, HttpStatus.EXPECTATION_FAILED);
		}
		return restData;

		// TODO Auto-generated method stub
	}

	public List<HprDoctorMappingDto> findByDoctorBySearchType(String apiSearchType, String Speciality,
			String doctorName, String hospitalName, String hprId, String fromDate, String toDate) {
		List<HprDoctorMappingDto> hprDetails = null;
		try {
			log.info("=============FINDING THE DOCTOR==============={}");
			log.info("TenantContext.getCurrentTenant()==============={}" , TenantContext.getCurrentTenant());
			if (TenantContext.getCurrentTenant() != null) {
				List<AbhaQueryTable> notifyTenantQueries = jdbcTemplateHelper.getResults(
						"select * from public.abha_query_table where  tenant_id = " + TenantContext.getCurrentTenant()
								+ " and query_type in ('GET-HPR-DOCTOR-DETAILS-BY-SPEACIALITY' ,"
								+ "'GET-HPR-DOCTOR-DETAILS-BY-SPEACIALITY-DOCTORNAME-FACILITY',"
								+ "'GET-HPR-DOCTOR-DETAILS-BY-SPEACIALITY-FACILITY',"
								+ "'GET-HPR-DOCTOR-DETAILS-BY-HPRID',"
								+ "'GET-HPR-DOCTOR-DETAILS-BY-DOCTORNAME-SPEACIALITY',"
								+ "'GET-HPR-DOCTOR-DETAILS-BY-DOCTORNAME','SELECT')",
						AbhaQueryTable.class);
				log.info("requestQueries============" + notifyTenantQueries);
				if (notifyTenantQueries != null && !notifyTenantQueries.isEmpty()) {
					hprDetails = extractResultSetService
							.excuteDynamicQueryForHprIdDetails(returnFinalQuery(
									bulidService.streamAndReturnQuery(apiSearchType, notifyTenantQueries), Speciality,
									apiSearchType, doctorName, hospitalName, hprId, fromDate, toDate));

				}
			}
			log.info("hprDetails========>" + hprDetails);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return hprDetails;
	}

	public String returnFinalQuery(String requestQueries, String speciality, String apiSearchType,
			String doctorName, String hospitalName, String hprId, String fromDate, String toDate)
			throws JsonProcessingException {
		String finalQuery = "";
		Map<String, String> replacementStrings = null;
			SearchTypeDto searchTypeDto=  this.getSearchTypes(speciality,doctorName,hospitalName,hprId,fromDate,toDate);
		
		switch (apiSearchType) {
		case "GET-HPR-DOCTOR-DETAILS-BY-SPEACIALITY":
			replacementStrings = Map.of("?1", " '" +searchTypeDto.getSpeciality()+"'", "?2"," '" +searchTypeDto.getFromDate()+"'", "?3",    " '" +searchTypeDto.getToDate()+"'");
			StrSubstitutor subSpeciality = new StrSubstitutor(replacementStrings, "{", "}");
			finalQuery = subSpeciality.replace(requestQueries);
			System.out.println("Final Query which is executing ==========>   " + finalQuery);

			break;
		case "GET-HPR-DOCTOR-DETAILS-BY-DOCTORNAME-SPEACIALITY":
			replacementStrings = Map.of("?1", " '" +searchTypeDto.getSpeciality()+"'", "?2"," '" +searchTypeDto.getFromDate()+"'" , "?3", " '" +searchTypeDto.getToDate()+"'", "?4", " '%" +searchTypeDto.getDoctorName()+"%'");
			StrSubstitutor subNameAndspeciality = new StrSubstitutor(replacementStrings, "{", "}");
			finalQuery = subNameAndspeciality.replace(requestQueries);
			log.info("Final Query which is executing ==========>   " + finalQuery);
			break;

		case "GET-HPR-DOCTOR-DETAILS-BY-SPEACIALITY-DOCTORNAME-FACILITY":
			replacementStrings = Map.of("?1", " '" +searchTypeDto.getSpeciality()+"'", "?2", " '" +searchTypeDto.getHospitalName()+"'", "?3",  " '" +searchTypeDto.getFromDate()+"'", "?4"," '" + searchTypeDto.getToDate()+"'", "?5",
					" '%" +searchTypeDto.getDoctorName()+"%'");
			StrSubstitutor subNameAndspecialityAndfacility = new StrSubstitutor(replacementStrings, "{", "}");
			finalQuery = subNameAndspecialityAndfacility.replace(requestQueries);
			log.info("Final Query which is executing ==========>   " + finalQuery);

			break;
		case "GET-HPR-DOCTOR-DETAILS-BY-SPEACIALITY-FACILITY":
			replacementStrings = Map.of("?1", " '" +searchTypeDto.getSpeciality()+"'", "?2", " '" +searchTypeDto.getHospitalName()+"'", "?3",   " '" +searchTypeDto.getFromDate()+"'", "?4", " '" + searchTypeDto.getToDate()+"'");
			StrSubstitutor subSpecialityAndfacility = new StrSubstitutor(replacementStrings, "{", "}");
			finalQuery = subSpecialityAndfacility.replace(requestQueries);
			log.info("Final Query which is executing ==========>   " + finalQuery);

			break;
		case "GET-HPR-DOCTOR-DETAILS-BY-HPRID":
			replacementStrings = Map.of("?1", " '" +searchTypeDto.getHprId()+"'", "?2"," '" +searchTypeDto.getFromDate()+"'" , "?3", " '" +searchTypeDto.getToDate()+"'");
			StrSubstitutor subHprId = new StrSubstitutor(replacementStrings, "{", "}");
			finalQuery = subHprId.replace(requestQueries);
			log.info("Final Query which is executing ==========>   " + finalQuery);

			break;
		case "GET-HPR-DOCTOR-DETAILS-BY-DOCTORNAME":
			replacementStrings = Map.of("?1", " '" +  searchTypeDto.getFromDate()+"'"    , "?2", " '" + searchTypeDto.getToDate()+"'", "?3", " '%" +searchTypeDto.getDoctorName()+"%'");
			StrSubstitutor subDoctorName = new StrSubstitutor(replacementStrings, "{", "}");
			finalQuery = subDoctorName.replace(requestQueries);
			log.info("Final Query which is executing ==========>   " + finalQuery);

			break;

		case "SELECT-DOCTOR":
			
			replacementStrings = Map.of("?1",  " '"+searchTypeDto.getHprId()+"'");
			StrSubstitutor subSecondHprId = new StrSubstitutor(replacementStrings, "{", "}");
			finalQuery = subSecondHprId.replace(requestQueries);
			log.info("Final Query which is executing ==========>   " + finalQuery);

			break;

		default:
			break;

		}
		return finalQuery;

	}

	private SearchTypeDto getSearchTypes(String speciality, String doctorName, String hospitalName, String hprId,
			String fromDate, String toDate) {
	   SearchTypeDto dto = new SearchTypeDto();
	   dto.setDoctorName(doctorName); 
	   dto.setHospitalName(hospitalName);
	   dto.setSpeciality(speciality);
	   dto.setHprId(hprId);
	   dto.setFromDate(fromDate);
	   dto.setToDate(toDate);
		return dto;
	}

	public List<Slots> BulidSelectCatalog(RequestDto requestDto, String apiType) {
		List<Slots> slots = new ArrayList<>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");

		try {

			List<DoctorDetailsSlotsDto> detailsSlotsDto = this.findBySecondSearchType(apiType,
					requestDto.getMessage().getIntent().getProvider().getFulfillments().get(0).getAgent().getId());
			if (detailsSlotsDto != null && !detailsSlotsDto.isEmpty()) {

				log.info("start=====================>" + detailsSlotsDto.get(0).getFromDate());

				requestDto.getMessage().getIntent().getProvider().getFulfillments().get(0).getStart().getTime()
						.setTimestamp(dateFormat.parse(dateFormat.format(detailsSlotsDto.get(0).getFromDate())));
				log.info("start=====================>" + detailsSlotsDto.get(0).getToDate());
				requestDto.getMessage().getIntent().getProvider().getFulfillments().get(0).getEnd().getTime()
						.setTimestamp(dateFormat.parse(dateFormat.format(detailsSlotsDto.get(0).getToDate())));
				log.info("request end date ==============>"
						+ requestDto.getMessage().getIntent().getProvider().getFulfillments().get(0).getEnd());

				Map<String, List<DoctorDetailsSlotsDto>> GroupByByslots = detailsSlotsDto.stream()
						.collect(Collectors.groupingBy(x -> x.getHprId().toString()));
				log.info("GroupByByslots==============>" + GroupByByslots);
				for (Entry<String, List<DoctorDetailsSlotsDto>> sp : GroupByByslots.entrySet()) {
					if (detailsSlotsDto.stream().filter(s -> s.getHprId().equals(sp.getKey())).findAny().isPresent()) {
						for (int j = 0; j < sp.getValue().size(); j++) {
							Slots detailsSlotDto = new Slots();

							detailsSlotDto.setSlots(sp.getValue().get(j).getTimeSlot());
							slots.add(detailsSlotDto);

						}
					}
				}
				System.out.println("listOfSlots========" + slots);
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return slots;
	}

	public List<DoctorDetailsSlotsDto> findBySecondSearchType(String apiSearchType, String hprId) {
		List<DoctorDetailsSlotsDto> hprDetails = null;
		try {
			List<AbhaQueryTable> notifyTenantQueries = jdbcTemplateHelper
					.getResults(
							"select * from public.abha_query_table where  tenant_id = "
									+ TenantContext.getCurrentTenant() + " and query_type in ('SELECT-DOCTOR')",
							AbhaQueryTable.class);
			log.info("requestQueries============" + notifyTenantQueries);
			if (notifyTenantQueries != null && !notifyTenantQueries.isEmpty()) {
				hprDetails = extractResultSetService
						.excuteDynamicQueryForDoctorDetailsSlots(returnFinalQuery(
								bulidService.streamAndReturnQuery(apiSearchType, notifyTenantQueries), null,
								apiSearchType, null, null, hprId, null, null));

			}
			System.out.println("hprDetails========>" + hprDetails);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return hprDetails;
	}

	public ResponseEntity<?> select(List<Slots> list, RequestDto requestDto) {
		ResponseBean bean = new ResponseBean();
		ResponseEntity<?> dto = null;

		try {

			requestDto.getMessage().getIntent().getProvider().getFulfillments().get(0).setSlots(list);
			requestDto.getContext().setAction("on_select");
			log.info("message=============" + requestDto);
			String Request = objectMapper.writeValueAsString(requestDto);
			dto = uhiService.checkRestCallSuccessOrNot(
					uhiService.restCallApi(null, Request, requestDto.getContext().getConsumer_uri() + "/on_select"));

		} catch (Exception ex) {
			log.error("Hspa select method error: " + ex);
			bean.setStatus(HttpStatus.EXPECTATION_FAILED);
			bean.setMessage(ConstantUtil.SOMETHING_WENT_WRONG);
			bean.setData(ex.getMessage());
			ex.printStackTrace();
			return new ResponseEntity<>(bean, HttpStatus.EXPECTATION_FAILED);
		}
		return dto;
		// TODO Auto-generated method stub

	}

}
