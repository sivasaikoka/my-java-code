//package com.dipl.abha.uhi.service;
//
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.ArrayList;
//import java.util.Base64;
//import java.util.List;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//
//import com.dipl.abha.uhi.dto.Person;
//import com.dipl.abha.uhi.dto.RequestDto;
//import com.dipl.abha.uhi.entities.ChatUserModel;
//import com.dipl.abha.uhi.entities.MessagesEntity;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.itextpdf.text.pdf.codec.Base64.OutputStream;
//
//import lombok.extern.slf4j.Slf4j;
//
//@Service
//@Slf4j
//public class MessageService {
//	
//	
//	@Autowired
//	private ObjectMapper mapper;
//	
//	
//
//
//	public ResponseEntity<?> saveMessages(String request, HttpServletRequest headers) {
//		try {
//			RequestDto objReq = mapper.readValue(request,RequestDto.class);
//			String contentType = objReq.getMessage().getIntent().getChat().getContent().getContent_type();
//		    MessagesEntity messageSaved = new MessagesEntity();
//		    boolean checkIfContentTypeTextOrMedia = contentType.equalsIgnoreCase("text") || contentType.equalsIgnoreCase("media");
//		    if (checkIfContentTypeTextOrMedia) {
//		        messageSaved = this.saveChatDataInDb(objReq);
////		        callNotificationService(objReq);
//		        log.info("DB call done... Message Id is {}", getMessageId(objReq));
//		    } else {
//		        log.info("Message not saved to database. Content type is {}, Message Id is {}", contentType, getMessageId(objReq));
//		    }
//		    String action = objReq.getContext().getAction();
//		    actionsInCaseOf_OnMessageAction(objReq, messageSaved, action);
//		    return logResponse(request, objReq);
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//		// TODO Auto-generated method stub
//		return null;
//	}
//	
//	
//	
//	 public MessagesEntity saveChatDataInDb(RequestDto request) throws Exception {
//		 MessagesEntity messagesModelSaved = null;
//
//	        String content_type = request.getMessage().getIntent().getChat().getContent().getContent_type();
//
//	        if ("media".equalsIgnoreCase(content_type)) {
//	            configureFileToBeSaved_Details(request);
//	        }
//
//	        messagesModelSaved = saveMessage(request);
//	        saveSenderAndReceiver(request);
//	        log.info("Message is saved.. sending notification .. Message Id is {}", getMessageId(request));
//
//	        return messagesModelSaved;
//	    }
//	 
//	 
//	 
//	 public MessagesEntity saveChatDataInDb(Request request) throws Exception {
//		 MessagesEntity messagesModelSaved = null;
//
//	        String content_type = request.getMessage().getIntent().getChat().getContent().getContent_type();
//
//	        if ("media".equalsIgnoreCase(content_type)) {
//	            configureFileToBeSaved_Details(request);
//	        }
//
//	        messagesModelSaved = saveMessage(request);
//	        saveSenderAndReceiver(request);
//	        log.info("Message is saved.. sending notification .. Message Id is {}", getMessageId(request));
//
//	        return messagesModelSaved;
//	    }
//
//	    private void configureFileToBeSaved_Details(RequestDto request) throws IOException {
//
//	        String content_fileName = request.getContext().getMessage_id() + request.getMessage().getIntent().getChat().getContent().getContent_fileName();
//	        log.info("Media file type received.. File name is  ->> {} .. Message Id is {}", content_fileName, getMessageId(request));
//	        Files.createDirectories(Paths.get(uploadDir).toAbsolutePath().normalize());
//
//
//	        String content_url = hspaPublicBaseUrl + downloadPath + content_fileName;
//	        log.info("Setting content URL to ->> ->> {}", content_url);
//	        request.getMessage().getIntent().getChat().getContent().setContent_url(content_url);
//	    }
//
//	    
//
//	   
//
//	    void saveSenderAndReceiver(RequestDto request) throws Exception {
//	        ChatUserModel sender = getSenderOrReceiver(request.getMessage().getIntent().getChat().getSender().getPerson());
//
//	        ChatUserModel receiver = getSenderOrReceiver(request.getMessage().getIntent().getChat().getReceiver().getPerson());
//
//	        List<ChatUserModel> user = new ArrayList<>();
//	        user.add(receiver);
//	        user.add(sender);
//
//	        List<ChatUserModel> saveAll = chatUserRepository.saveAll(user);
//	        if (saveAll.isEmpty())
//	            throw new Exception("Error occurred while saving data");
//	    }
//
//	    private ChatUserModel getSenderOrReceiver(Person request) {
//	        ChatUserModel sender = new ChatUserModel();
//	        sender.setUserId(request.getId());
//	        sender.setUserName(request.getName());
//	        sender.setImage(request.getImage());
//	        return sender;
//	    }
//
//	    public MessagesEntity saveMessage(RequestDto request) {
//
//	        ObjectMapper om = new ObjectMapper();
//	        MessagesEntity m = new MessagesEntity();
//	        m.setContentId(request.getMessage().getIntent().getChat().getContent().getContent_id());
//	        m.setReceiver(request.getMessage().getIntent().getChat().getReceiver().getPerson().getId());
//	        m.setSender(request.getMessage().getIntent().getChat().getSender().getPerson().getId());
//	        m.setTime(getLocalDateTimeFromString(request));
//	        String content_type = request.getMessage().getIntent().getChat().getContent().getContent_type();
//	        if ("text".equalsIgnoreCase(content_type)) {
//	            m.setContentValue(request.getMessage().getIntent().getChat().getContent().getContent_value());
//	        }
//	        m.setContentUrl(request.getMessage().getIntent().getChat().getContent().getContent_url());
//	        m.setContentType(content_type);
//	        m.setConsumerUrl(request.getContext().getConsumerUri());
//	        m.setProviderUrl(request.getContext().getProviderUri());
//	        return messagesRepo.save(m);
//	    }
//}