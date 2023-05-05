package com.quyvx.accommodationbooking.repository;

import com.quyvx.accommodationbooking.model.Hotel;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepository extends PagingAndSortingRepository<Hotel, Long>{
    Hotel save(Hotel hotel);


    List<Hotel> findByName(String name);
}
