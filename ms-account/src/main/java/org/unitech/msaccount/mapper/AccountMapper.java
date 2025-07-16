package org.unitech.msaccount.mapper;

import org.springframework.stereotype.Component;
import org.unitech.msaccount.domain.entity.Account;
import org.unitech.msaccount.model.dto.AccountDto;
import org.unitech.msaccount.model.dto.response.AccountResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AccountMapper {

    public Account toEntity(AccountDto dto) {
        if (dto == null) {
            return null;
        }

        Account account = new Account();
        account.setId(dto.getId());
        account.setUserId(dto.getUserId());
        account.setCartNumber(dto.getCartNumber());
        account.setBalance(dto.getBalance());
        account.setStatus(dto.getAccountStatus());
        account.setCurrency(dto.getCurrency());
        account.setPin(dto.getPin());
        account.setCvv(dto.getCvv());
        account.setCreatedAt(LocalDateTime.now());
        account.setUpdatedAt(LocalDateTime.now());

        return account;
    }

    public AccountDto toDto(Account entity) {
        if (entity == null) {
            return null;
        }

        AccountDto dto = new AccountDto();
        dto.setId(entity.getId());
        dto.setUserId(entity.getUserId());
        dto.setCartNumber(entity.getCartNumber());
        dto.setBalance(entity.getBalance());
        dto.setAccountStatus(entity.getStatus());
        dto.setCurrency(entity.getCurrency());
        dto.setPin(entity.getPin());
        dto.setCvv(entity.getCvv());

        return dto;
    }

    public AccountResponse toResponse(Account entity) {
        if (entity == null) {
            return null;
        }

        AccountResponse response = new AccountResponse();
        response.setId(entity.getId());
        response.setUserId(entity.getUserId());
        response.setCartNumber(maskCardNumber(entity.getCartNumber()));
        response.setBalance(entity.getBalance());
        response.setAccountStatus(entity.getStatus());
        response.setCurrency(entity.getCurrency());
        response.setCreatedAt(entity.getCreatedAt());

        return response;
    }

    public List<AccountResponse> toResponseList(List<Account> entities) {
        if (entities == null) {
            return null;
        }

        return entities.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 8) {
            return cardNumber;
        }
        String firstFour = cardNumber.substring(0, 4);
        String lastFour = cardNumber.substring(cardNumber.length() - 4);
        return firstFour + "****" + lastFour;
    }
}