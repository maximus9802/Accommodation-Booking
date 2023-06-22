package com.quyvx.accommodationbooking.repository;

import com.quyvx.accommodationbooking.model.Rating;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends PagingAndSortingRepository<Rating, Long> {
    Rating save(Rating rating);
    void deleteById(Long id);
    Optional<Rating> findById(Long id);

    List<Rating> findByBookingId(Long bookingId);

    List<Rating> findByRoomId(Long roomId);
}
