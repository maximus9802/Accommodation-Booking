package com.quyvx.accommodationbooking.repository;

import com.quyvx.accommodationbooking.model.Image;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends PagingAndSortingRepository<Image, Long> {
    Image save(Image image);
}
