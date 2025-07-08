package org.unitech.msaccount.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.unitech.msaccount.domain.entity.Account;
import org.unitech.msaccount.domain.repo.AccountRepository;
import org.unitech.msaccount.exception.InvalidPinException;
import org.unitech.msaccount.exception.NotFoundException;
import org.unitech.msaccount.mapper.AccountMapper;
import org.unitech.msaccount.model.dto.AccountDto;
import org.unitech.msaccount.model.dto.request.CreateAccountRequest;
import org.unitech.msaccount.model.dto.request.UpdatePinRequest;
import org.unitech.msaccount.model.dto.response.AccountResponse;
import org.unitech.msaccount.model.enums.AccountStatus;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final RabbitTemplate rabbitTemplate;
    private final UserService userService;
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final CartGenerationService cartGenerationService;

    public AccountResponse createAccount(CreateAccountRequest request) {
        try{
            userService.getUserById(request.getUserId());
        } catch (Exception e) {
            throw new NotFoundException(e.getMessage());
        }

        AccountDto accountDto = cartGenerationService.createCart();

        Account account = accountMapper.toEntity(accountDto);
        account.setUserId(request.getUserId());

        while (accountRepository.existsByCartNumber(account.getCartNumber())) {
            accountDto = cartGenerationService.createCart();
            account.setCartNumber(accountDto.getCartNumber());
        }

        Account savedAccount = accountRepository.save(account);

        try {
            rabbitTemplate.convertAndSend("account.created",
                    "Account created for user: " + request.getUserId() + " with card: " + savedAccount.getCartNumber());
            log.info("Account creation event sent to RabbitMQ for user: {}", request.getUserId());
        } catch (Exception e) {
            log.warn("Failed to send account creation event to RabbitMQ: {}", e.getMessage());
        }

        return accountMapper.toResponse(savedAccount);
    }

    public void blockAccount(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundException("Account not found with ID: " + accountId));

        account.setStatus(AccountStatus.BLOCKED);
        accountRepository.save(account);

        try {
            rabbitTemplate.convertAndSend("account.blocked",
                    "Account blocked: " + accountId);
            log.info("Account block event sent to RabbitMQ for account: {}", accountId);
        } catch (Exception e) {
            log.warn("Failed to send account block event to RabbitMQ: {}", e.getMessage());
        }
    }

    public AccountResponse updatePin(Long accountId, UpdatePinRequest request) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundException("Account not found with ID: " + accountId));

        if (!account.getPin().equals(request.getOldPin())) {
            throw new InvalidPinException("Old PIN is incorrect");
        }

        account.setPin(request.getNewPin());
        Account updatedAccount = accountRepository.save(account);

        try {
            rabbitTemplate.convertAndSend("account.pin.updated",
                    "PIN updated for account: " + accountId);
            log.info("PIN update event sent to RabbitMQ for account: {}", accountId);
        } catch (Exception e) {
            log.warn("Failed to send PIN update event to RabbitMQ: {}", e.getMessage());
        }

        return accountMapper.toResponse(updatedAccount);
    }

    public List<AccountResponse> getUserAccounts(Long userId) {
        List<Account> accounts = accountRepository.findByUserId(userId);
        return accountMapper.toResponseList(accounts);
    }

    public AccountResponse getUserById(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundException("Account not found with ID: " + accountId));

        return accountMapper.toResponse(account);
    }
}
