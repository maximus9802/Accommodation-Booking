package com.quyvx.accommodationbooking.service.notification;

import com.quyvx.accommodationbooking.dto.NotificationDto;

import java.util.List;

public interface NotificationService {
    NotificationDto sendNoti(Long idAccount, Long idUser, String message) throws  Exception;

    List<NotificationDto> findByAccountId(Long idAccount) throws Exception;
}
