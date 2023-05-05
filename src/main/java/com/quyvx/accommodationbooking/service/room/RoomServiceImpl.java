package com.quyvx.accommodationbooking.service.room;

import com.quyvx.accommodationbooking.model.Room;
import com.quyvx.accommodationbooking.repository.RoomRepository;
import com.quyvx.accommodationbooking.service.room.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomRepository roomRepository;


}
