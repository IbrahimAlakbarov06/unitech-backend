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
import org.unitech.msaccount.model.dto.request.DepositRequest;
import org.unitech.msaccount.model.dto.request.UpdatePinRequest;
import org.unitech.msaccount.model.dto.response.AccountResponse;
import org.unitech.msaccount.model.enums.AccountStatus;

import java.math.BigDecimal;
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

        sendEvent("ACCOUNT_CREATED", account.getId());

        return accountMapper.toResponse(savedAccount);
    }

    public void blockAccount(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundException("Account not found with ID: " + accountId));

        account.setStatus(AccountStatus.BLOCKED);
        accountRepository.save(account);

        sendEvent("ACCOUNT_BLOCKED", account.getId());
    }

    public AccountResponse updatePin(Long accountId, UpdatePinRequest request) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundException("Account not found with ID: " + accountId));

        if (!account.getPin().equals(request.getOldPin())) {
            throw new InvalidPinException("Old PIN is incorrect");
        }

        account.setPin(request.getNewPin());
        Account updatedAccount = accountRepository.save(account);

        sendEvent("ACCOUNT_UPDATED_PIN", updatedAccount.getId());

        return accountMapper.toResponse(updatedAccount);
    }

    public List<AccountResponse> getUserAccounts(Long userId) {
        List<Account> accounts = accountRepository.findByUserId(userId);
        return accountMapper.toResponseList(accounts);
    }

    public AccountResponse getAccountById(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundException("Account not found with ID: " + accountId));

        return accountMapper.toResponse(account);
    }

    public AccountResponse deposit(Long accountId, DepositRequest request) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundException("Account not found with ID: " + accountId));

        if (request.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        account.setBalance(account.getBalance().add(request.getAmount()));
        Account updatedAccount = accountRepository.save(account);

        sendEvent("ACCOUNT_DEPOSITED", updatedAccount.getId());

        return accountMapper.toResponse(updatedAccount);
    }

    private void sendEvent(String eventType, Long accountId) {
        try {
            String message = eventType + ": " + accountId + "at " + System.currentTimeMillis();
            rabbitTemplate.convertAndSend("account.events", message);
            log.info("Event sent to RabbitMQ for account: " + accountId);
        } catch (Exception e) {
            log.warn("Failed to send event to RabbitMQ: {}", e.getMessage());
        }
    }
}
