package com.quyvx.accommodationbooking.service.account;

import com.quyvx.accommodationbooking.dto.AccountDto;
import com.quyvx.accommodationbooking.dto.Message;
import com.quyvx.accommodationbooking.dto.NotificationDto;
import com.quyvx.accommodationbooking.dto.PassUser;
import com.quyvx.accommodationbooking.exception.InvalidException;
import com.quyvx.accommodationbooking.model.Account;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    String save(Account account);

    NotificationDto updateAccount(Long id, AccountDto accountDto) throws InvalidException;

    NotificationDto changeUsername(Long id, PassUser passUser) throws InvalidException;

    NotificationDto changePassword(Long id, PassUser passUser) throws InvalidException;

    Optional<Account> findById(Long id);

    AccountDto getInformation(Long idAccount) throws InvalidException;

    Account findByPhone(String phone);

    Account findByUsername(String username);

    boolean existsUsername(String username);

    List<AccountDto> getAllAccount(Long idAccount,Integer pageNumber, Integer pageSize, String sortBy) throws Exception;

}
