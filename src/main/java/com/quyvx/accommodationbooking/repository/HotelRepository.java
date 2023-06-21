package com.quyvx.accommodationbooking.repository;

import com.quyvx.accommodationbooking.model.Hotel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HotelRepository extends PagingAndSortingRepository<Hotel, Long>{
    Hotel save(Hotel hotel);

    List<Hotel> findByAccountId(Long account_id, Pageable pageable);

    List<Hotel> findByName(String name);

    Optional<Hotel> findById(Long id);

    @Query("SELECT h FROM Hotel h WHERE h.location LIKE %:location%")
    Page<Hotel> findByLocation(@Param("location") String location, Pageable pageable);
}
