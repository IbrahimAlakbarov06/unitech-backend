package org.unitech.mstransfer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.unitech.mstransfer.domain.entity.Transfer;
import org.unitech.mstransfer.domain.repo.TransferDao;
import org.unitech.mstransfer.exception.InsufficientFundsException;
import org.unitech.mstransfer.exception.InvalidTransferException;
import org.unitech.mstransfer.exception.NotFoundException;
import org.unitech.mstransfer.mapper.TransferMapper;
import org.unitech.mstransfer.model.dto.request.TransferRequest;
import org.unitech.mstransfer.model.dto.response.AccountResponse;
import org.unitech.mstransfer.model.dto.response.CurrencyResponse;
import org.unitech.mstransfer.model.dto.response.TransferResponse;
import org.unitech.mstransfer.model.enums.TransferStatus;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransferService {
    private final AccountService accountService;
    private final TransferMapper transferMapper;
    private final TransferDao transferDao;
    private final CurrencyService currencyService;
    private final RabbitTemplate rabbitTemplate;

    public TransferResponse createTransfer(TransferRequest transferRequest) {
        AccountResponse fromAccount = getAccount(transferRequest.getFromAccountId());
        AccountResponse toAccount = getAccount(transferRequest.getToAccountId());

        if (fromAccount.getId().equals(toAccount.getId())) {
            throw new InvalidTransferException("Cannot transfer to the same account");
        }

        CurrencyResponse currencyResponse = currencyService.getExchangeRate(transferRequest.getCurrencyFrom(), transferRequest.getCurrencyTo());

        BigDecimal convertedAmount = transferRequest.getAmount().multiply(currencyResponse.getRate());

        if (fromAccount.getBalance().compareTo(transferRequest.getAmount()) < 0) {
            throw new InsufficientFundsException("Insufficient funds in account");
        }

        Transfer transfer = transferMapper.toEntity(transferRequest);
        transfer.setExchangeRate(currencyResponse.getRate());
        transfer.setConvertedAmount(convertedAmount);
        transfer.setStatus(TransferStatus.PENDING);

        Transfer savedTransfer = transferDao.save(transfer);

        sendEvent("TRANSFER_INITIATED", savedTransfer.getId());

        try {
            savedTransfer.setStatus(TransferStatus.COMPLETED);
            transferDao.save(savedTransfer);

            sendEvent("TRANSFER_SUCCESS", savedTransfer.getId());
        } catch (Exception e) {
            savedTransfer.setStatus(TransferStatus.FAILED);
            transferDao.save(savedTransfer);
            sendEvent("TRANSFER_FAILED", savedTransfer.getId());
            throw new InvalidTransferException("Transfer processing failed");
        }

        return transferMapper.toResponse(savedTransfer);
    }

    public List<TransferResponse> getTransfersByAccount(Long accountId) {
        List<Transfer> transfers = transferDao.getTransferByAccountId(accountId);
        return transferMapper.toResponseList(transfers);
    }

    public TransferResponse getTransferById(Long id) {
        Transfer transfer = transferDao.findById(id)
                .orElseThrow(() -> new NotFoundException("Transfer not found with ID: " + id));
        return transferMapper.toResponse(transfer);
    }

    private AccountResponse getAccount(Long accountId) {
        try {
            return accountService.getAccountById(accountId);
        } catch (Exception e) {
            throw new NotFoundException("Account not found with id " + accountId);
        }
    }
    private void sendEvent(String eventType, Long transferId) {
        try {
            String message = eventType + ": " + transferId + " at " + System.currentTimeMillis();
            rabbitTemplate.convertAndSend("transfer.events", message);
            log.info("Event sent: {}", message);
        } catch (Exception e) {
            log.warn("Failed to send event to RabbitMQ: {}", e.getMessage());
        }
    }
}
