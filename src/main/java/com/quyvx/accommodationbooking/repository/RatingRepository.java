package com.quyvx.accommodationbooking.repository;

import com.quyvx.accommodationbooking.model.Rating;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends PagingAndSortingRepository<Rating, Long> {
    Rating save(Rating rating);
}
