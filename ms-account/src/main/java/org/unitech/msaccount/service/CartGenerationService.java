package org.unitech.msaccount.service;

import org.springframework.stereotype.Service;
import org.unitech.msaccount.model.dto.AccountDto;
import org.unitech.msaccount.model.enums.AccountStatus;

import java.math.BigDecimal;
import java.util.Random;

@Service
public class CartGenerationService {
    private final Random random = new Random();

    public AccountDto createCart() {
        AccountDto accountDto = new AccountDto();
        accountDto.setCartNumber(cartGenerator());
        accountDto.setAccountStatus(AccountStatus.ACTIVE);
        accountDto.setBalance(BigDecimal.valueOf(0));
        accountDto.setCvv(cvvGenerator());
        accountDto.setPin(pinGenerator());
        return accountDto;
    }
    private String cartGenerator() {
        return "41697388" + String.format("%08d", random.nextInt(100000000));
    }

    private String pinGenerator() {
        return String.format("%04d", 1000 + random.nextInt(9000));
    }

    private String cvvGenerator() {
        return String.format("%03d", 100 + random.nextInt(900));
    }
}