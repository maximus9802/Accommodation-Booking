package com.quyvx.accommodationbooking.repository;

import com.quyvx.accommodationbooking.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByAccountId(Long accountId);

}
