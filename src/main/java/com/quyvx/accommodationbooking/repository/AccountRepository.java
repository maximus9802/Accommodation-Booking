package com.quyvx.accommodationbooking.repository;

import com.quyvx.accommodationbooking.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findById(Long id);

    Optional<Account> findByUsername(String username);

    Optional<Account> findByPhone(String phone);
}
