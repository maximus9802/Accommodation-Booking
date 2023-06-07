package com.quyvx.accommodationbooking.service.account;

import com.quyvx.accommodationbooking.dto.AccountDto;
import com.quyvx.accommodationbooking.dto.Message;
import com.quyvx.accommodationbooking.dto.PassUser;
import com.quyvx.accommodationbooking.exception.InvalidException;
import com.quyvx.accommodationbooking.model.Account;
import com.quyvx.accommodationbooking.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService{
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public AccountServiceImpl() {
    }

    @Override
    public String save(Account account) {
        Optional<Account> account1 = accountRepository.findByUsername(account.getUsername());
        if(account1.isEmpty()){
            accountRepository.save(account);
            return "Account successfully created.";
        }
        else {
            return "Username already exits.";
        }
    }

    @Override
    public Message updateAccount(Long id, AccountDto accountDto) throws InvalidException {
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if(optionalAccount.isPresent()){
            Account account = optionalAccount.get();
            account.setName(accountDto.getName());
            account.setAddress(accountDto.getAddress());
            account.setPhone(accountDto.getPhone());
            accountRepository.save(account);
            return new Message("Update successful!");
        } throw new InvalidException("Account is not found for the id " + id);
    }

    @Override
    public Message changeUsername(Long id, PassUser passUser) throws InvalidException {
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if(optionalAccount.isPresent()){
            Account account = optionalAccount.get();
            if(accountRepository.findByUsername(passUser.getNewUsername()).isPresent()){
                throw new InvalidException("Username already exits!");
            }
            var user = Account.builder()
                    .id(id)
                    .username(passUser.getNewUsername())
                    .password(account.getPassword())
                    .role(account.getRole())
                    .name(account.getName())
                    .address(account.getAddress())
                    .phone(account.getPhone())
                    .build();
            accountRepository.save(user);
            return new Message("Update username successfully!");
        } throw new InvalidException("Account is not found for the id " + id);
    }


    @Override
    public Message changePassword(Long id, PassUser passUser) throws InvalidException {
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if(optionalAccount.isPresent()){
            Account account = optionalAccount.get();
            if(!passwordEncoder.matches(passUser.getCurrentPassword(), account.getPassword())){
                throw new InvalidException("Invalid password!");
            }
            else {
                var user = Account.builder()
                        .id(account.getId())
                        .username(account.getUsername())
                        .password(passwordEncoder.encode(passUser.getNewPassword()))
                        .role(account.getRole())
                        .name(account.getName())
                        .address(account.getAddress())
                        .phone(account.getPhone())
                        .build();
                accountRepository.save(user);
                return new Message("Update password successfully!");
            }
        } throw new InvalidException("Account is not found for the id " + id);
    }


    @Override
    public Optional<Account> findById(Long id) {
        Optional<Account> account = accountRepository.findById(id);
        if(account.isPresent()){
            return account;
        }
        throw new RuntimeException("Account is not found for the id " + id);
    }

    @Override
    public Account findByPhone(String phone) {
        Optional<Account> account = accountRepository.findByPhone(phone);
        if(account.isPresent()){
            return account.get();
        }
        throw new RuntimeException("Account is not found for the phone " + phone);
    }

    @Override
    public Account findByUsername(String username) {
        Optional<Account> account = accountRepository.findByUsername(username);
        if(account.isPresent()){
            return account.get();
        }
        throw new RuntimeException("Account is not found for the username " + username);
    }

    @Override
    public boolean existsUsername(String username) {
        Optional<Account> account = accountRepository.findByUsername(username);
        return account.isPresent();
    }


}
