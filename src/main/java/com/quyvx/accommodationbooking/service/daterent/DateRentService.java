package com.quyvx.accommodationbooking.service.daterent;

import com.quyvx.accommodationbooking.model.DateRent;
import com.quyvx.accommodationbooking.repository.DateRentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class DateRentService {
    @Autowired
    private DateRentRepository dateRentRepository;

    public boolean checkFree(Long roomId, Date date){
        Optional<DateRent> dateRent = dateRentRepository.findByDateAndRoomId(roomId, date);
        return dateRent.isEmpty();
    }
}
