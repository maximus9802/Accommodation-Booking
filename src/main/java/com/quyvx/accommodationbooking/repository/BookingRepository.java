package com.quyvx.accommodationbooking.repository;

import com.quyvx.accommodationbooking.model.Booking;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    Optional<Booking> findById(Long id);

    List<Booking> findByAccountId(Long accountId);
    @Query("SELECT b " +
            "FROM Booking b " +
            "WHERE b.account.phone = :phone")
    List<Booking> findByPhoneAccount(@Param("phone") String phone);

    @Query("SELECT br.id " +
            "FROM Booking b " +
            "JOIN b.rooms br " +
            "WHERE b.id = :bookingId")
    List<Long> getBookingRoomIds(@Param("bookingId") Long bookingId);

}
