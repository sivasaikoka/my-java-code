package com.dipl.abha.repositories;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dipl.abha.entities.HIUDecryptedDocument;

@Repository
public interface HIUDecryptedDocumentRepository extends JpaRepository<HIUDecryptedDocument, Long>{

	@Query(value = "select hr2.txn_code as txn_code,hr.txn_code as receiver_private_Key,\r\n"
			+ "hr.request_json ->'hiRequest'->'keyMaterial'->>'nonce' as receiver_nonce,\r\n"
			+ "tmp.public_key_to_decrypt as sender_public_key,\r\n"
			+ "tmp.nonce_dectypt as sender_nonce from hiu_request hr \r\n"
			+ "inner join hiu_response hr2 on hr.request_code = hr2.request_code \r\n"
			+ "inner join (select response_json ->'keyMaterial'-> 'dhPublicKey'->>'keyValue' as public_key_to_decrypt ,\r\n"
			+ "response_json ->'keyMaterial'->> 'nonce' as nonce_dectypt, hr.txn_code \r\n"
			+ "from hiu_response hr where hr.hiu_request_type_id = 11)tmp on tmp.txn_code = hr2.txn_code \r\n"
			+ "where hr.hiu_request_type_id = 7 and hr2.hiu_request_type_id=8 \r\n"
			+ "and hr2.txn_code = ?1\r\n"
			+ "group by 1,2,3,4,5",nativeQuery = true)
	List<Map<String,String>> getKeysByTxnId(String transactoinId);

	@Query(value = " select hdd.* from hiu_decrypted_document hdd\n"
			+ " inner join hiu_decrypted_document  hrd\n"
			+ " on  hdd.txn_code = hrd.txn_code and hrd.is_deleted =false\n"
			+ "where hdd.consent_request_code = ?1 group by hdd.id, hdd.txn_code, hdd.consent_request_code, hdd.consent_artefact_code",nativeQuery = true)
	List<HIUDecryptedDocument> getDocumentByConsentRequestCode(String consentRequestId, Pageable pageable);
	
	@Query(value = "select hdd.* from hiu_response_document hrd \r\n" + "inner join hiu_decrypted_document hdd on \r\n"
			+ "hdd.txn_code = hrd.txn_code and hrd.is_deleted =false\r\n"
			+ "where hdd.consent_request_code = ?1 group by 1,2,3,4", nativeQuery = true)
	List<HIUDecryptedDocument> getDocumentByConsentRequestCode(String consentRequestId);
	
	@Modifying
	@Transactional
	@Query(value = "delete from hiu_decrypted_document where consent_artefact_code in (?1)",nativeQuery = true)
	void deleteExpireOrGrantedFromDecryptDocs(Set<String> consentIds);
	
	@Modifying
	@Transactional
	@Query(value = "delete from hiu_response_document where consent_artefact_code in (?1)",nativeQuery = true)
	void deleteExpireOrGrantedHiuRespDocs(Set<String> consentIds);
	
	@Query(value="select hr.response_json ->'consent'->'consentDetail'->'hip'->>'name' as \r\n"
			+ "hip_name,hr.response_json ->'consent'->'consentDetail'->'hip'->>'id' as hip_id,\r\n"
			+ "hr.consent_artefact_code from hiu_response hr where hr.hiu_request_type_id = 6 and\r\n"
			+ "consent_artefact_code in (?1) order by id desc",nativeQuery=true)
	List<Map<String,String>> getHipCodeAndHipNameByConsentArtifactIds(Set<String> consentRequestId);
	
	
	@Query(value = "select hr.request_json->'consent'->'hiu'->>'id' as hiu_id\r\n"
			+ "from hiu_request hr inner join hiu_response hr2 on hr2.request_code = hr.request_code \r\n"
			+ "where hr.hiu_request_type_id =1 and hr2.hiu_request_type_id =2 and\r\n"
			+ "hr2.consent_request_code = ?1  order by hr.id desc limit 1", nativeQuery = true)
	String getHIUIdByConsentRequestId(String consentRequestId);
	
	
}
