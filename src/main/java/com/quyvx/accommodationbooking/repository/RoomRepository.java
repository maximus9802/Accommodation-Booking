package com.quyvx.accommodationbooking.repository;

import com.quyvx.accommodationbooking.model.Room;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends PagingAndSortingRepository<Room, Long> {

    Room save(Room room);
}
