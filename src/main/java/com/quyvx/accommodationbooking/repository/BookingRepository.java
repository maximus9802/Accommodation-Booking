package com.quyvx.accommodationbooking.repository;

import com.quyvx.accommodationbooking.model.Booking;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends PagingAndSortingRepository<Booking, Long>{
    Booking save(Booking booking);

    Optional<Booking> findById(Long id);


}
