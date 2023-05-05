package com.quyvx.accommodationbooking.repository;

import com.quyvx.accommodationbooking.model.Account;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends PagingAndSortingRepository<Account, Long> {
    Account save(Account account);

    Optional<Account> findById(Long id);

    Optional<Account> findByUsername(String username);
}
