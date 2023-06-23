package com.quyvx.accommodationbooking.repository;

import com.quyvx.accommodationbooking.model.DateRent;
import com.quyvx.accommodationbooking.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface DateRentRepository extends JpaRepository<DateRent, Long> {

    @Query("SELECT dr FROM DateRent dr WHERE dr.room.id = :roomId AND dr.date = :date")
    Optional<DateRent> findByDateAndRoomId(@Param("roomId") Long id, @Param("date") Date date);

    @Transactional
    void deleteByRoomIdAndDate(Long roomId, Date date);
//    @Modifying
//    @Query("DELETE FROM DateRent dr WHERE dr.room.id = :roomId AND dr.date = :date")
//    void deleteByRoomIdAndDate(@Param("roomId") Long roomId, @Param("date") Date date);
}
