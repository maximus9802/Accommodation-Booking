package com.quyvx.accommodationbooking.repository;

import com.quyvx.accommodationbooking.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    Optional<Booking> findById(Long id);

    List<Booking> findByAccountId(Long accountId);
}
