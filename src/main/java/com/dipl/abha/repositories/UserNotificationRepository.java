package com.dipl.abha.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dipl.abha.entities.UserNotificationEntity;

@Repository
public interface UserNotificationRepository extends JpaRepository<UserNotificationEntity, Long> {


}
