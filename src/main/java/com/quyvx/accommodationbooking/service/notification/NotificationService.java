package com.quyvx.accommodationbooking.service.notification;

import com.quyvx.accommodationbooking.dto.NotificationDto;

public interface NotificationService {
    NotificationDto sendNoti(Long idAccount, Long idUser, String message) throws  Exception;
}
