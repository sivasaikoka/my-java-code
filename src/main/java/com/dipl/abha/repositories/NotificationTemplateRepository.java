package com.dipl.abha.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dipl.abha.entities.NotificationTemplate;


@Repository
public interface NotificationTemplateRepository extends JpaRepository<NotificationTemplate, Long> {

	@Query(value = "select * from notification_template where notification_event_id =?1 and is_deleted=false", nativeQuery = true)
	public NotificationTemplate getByEventId(int i);

	public NotificationTemplate findByTemplateCode(String abhaTemplate);

}
