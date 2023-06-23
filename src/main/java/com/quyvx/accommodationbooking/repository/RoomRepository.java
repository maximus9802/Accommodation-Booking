package com.quyvx.accommodationbooking.repository;

import com.quyvx.accommodationbooking.model.Room;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {


    @NotNull Optional<Room> findById(@NotNull Long id);

    List<Room> findByRoomType(String roomType);

    List<Room> findByHotelId(Long hotelId);

    @Query("SELECT DISTINCT r.roomType FROM Room r WHERE r.hotel.id = :hotelId")
    List<String> findDistinctRoomTypeByHotelId(@Param("hotelId") Long hotelId);

    @Query("SELECT r FROM Room r WHERE r.hotel.id = :hotelId AND r.roomType = :roomType")
    List<Room> findByHotelIdAndRoomType(@Param("hotelId") Long hotelId, @Param("roomType") String roomType);

}
