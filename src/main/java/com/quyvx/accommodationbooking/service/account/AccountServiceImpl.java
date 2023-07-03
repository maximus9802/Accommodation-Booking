package com.quyvx.accommodationbooking.service.account;

import com.quyvx.accommodationbooking.dto.AccountDto;
import com.quyvx.accommodationbooking.dto.Message;
import com.quyvx.accommodationbooking.dto.NotificationDto;
import com.quyvx.accommodationbooking.dto.PassUser;
import com.quyvx.accommodationbooking.exception.InvalidException;
import com.quyvx.accommodationbooking.model.Account;
import com.quyvx.accommodationbooking.model.Notification;
import com.quyvx.accommodationbooking.model.Role;
import com.quyvx.accommodationbooking.repository.AccountRepository;
import com.quyvx.accommodationbooking.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService{
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private NotificationRepository notificationRepository;

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
    public NotificationDto updateAccount(Long id, AccountDto accountDto) throws InvalidException {
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if(optionalAccount.isPresent()){
            Account account = optionalAccount.get();
            account.setName(accountDto.getName());
            account.setAddress(accountDto.getAddress());
            account.setPhone(accountDto.getPhone());

            Notification notification = new Notification();
            notification.setAccount(account);
            notification.setMessage("Update account successful!");

            accountRepository.save(account);
            notificationRepository.save(notification);

            return NotificationDto
                    .builder()
                    .accountId(id)
                    .message("Update account successful!")
                    .build();
        } throw new InvalidException("Account is not found for the id " + id);
    }

    @Override
    public NotificationDto changeUsername(Long id, PassUser passUser) throws InvalidException {
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
            var noti = Notification
                    .builder()
                    .account(account)
                    .message("Update username successfully!")
                    .build();
            accountRepository.save(user);
            notificationRepository.save(noti);

            return NotificationDto
                    .builder()
                    .accountId(id)
                    .message("Update username successfully!")
                    .build();
        } throw new InvalidException("Account is not found for the id " + id);
    }


    @Override
    public NotificationDto changePassword(Long id, PassUser passUser) throws InvalidException {
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
                var noti = Notification
                        .builder()
                        .account(account)
                        .message("Update password successfully!")
                        .build();
                accountRepository.save(user);
                notificationRepository.save(noti);

                return NotificationDto
                        .builder()
                        .accountId(id)
                        .message("Update password successfully!")
                        .build();
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
    public AccountDto getInformation(Long idAccount) throws InvalidException {
        Optional<Account> account = accountRepository.findById(idAccount);
        if(account.isPresent()){
            return AccountDto
                    .builder()
                    .name(account.get().getName())
                    .phone(account.get().getPhone())
                    .address(account.get().getAddress())
                    .score(account.get().getScore())
                    .build();
        } throw new InvalidException("Invalid Account.");
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

    @Override
    public List<AccountDto> getAllAccount(Long idAccount, Integer pageNumber, Integer pageSize, String sortBy) throws Exception {
        Optional<Account> optionalAccount = accountRepository.findById(idAccount);
        if(optionalAccount.isPresent()){
            if(optionalAccount.get().getRole() == Role.ADMIN){
                Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending());
                List<Account> accounts = accountRepository.findAll();
                List<AccountDto> accountDtos = new ArrayList<>();
                for(Account account : accounts){
                    var temp = AccountDto
                            .builder()
                            .name(account.getName())
                            .address(account.getAddress())
                            .phone(account.getPhone())
                            .id(account.getId())
                            .score(account.getScore())
                            .role(account.getRole())
                            .build();
                    accountDtos.add(temp);
                }
                return accountDtos;
            } throw new Exception("You are not granted this permission.");
        } throw new InvalidException("Invalid Account!");
    }


}
