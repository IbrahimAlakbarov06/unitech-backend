package org.unitech.mstransfer.mapper;

import org.springframework.stereotype.Component;
import org.unitech.mstransfer.domain.entity.Transfer;
import org.unitech.mstransfer.model.dto.request.TransferRequest;
import org.unitech.mstransfer.model.dto.response.TransferResponse;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TransferMapper {

    public Transfer toEntity(TransferRequest request) {
        Transfer transfer = new Transfer();
        transfer.setFromAccountId(request.getFromAccountId());
        transfer.setToAccountId(request.getToAccountId());
        transfer.setAmount(request.getAmount());
        transfer.setCurrencyFrom(request.getCurrencyFrom());
        transfer.setCurrencyTo(request.getCurrencyTo());
        transfer.setDescription(request.getDescription());
        return transfer;
    }

    public TransferResponse toResponse(Transfer transfer) {
        TransferResponse response = new TransferResponse();
        response.setId(transfer.getId());
        response.setFromAccountId(transfer.getFromAccountId());
        response.setToAccountId(transfer.getToAccountId());
        response.setAmount(transfer.getAmount());
        response.setCurrencyFrom(transfer.getCurrencyFrom());
        response.setCurrencyTo(transfer.getCurrencyTo());
        response.setExchangeRate(transfer.getExchangeRate());
        response.setConvertedAmount(transfer.getConvertedAmount());
        response.setStatus(transfer.getStatus());
        response.setDescription(transfer.getDescription());
        response.setCreatedAt(transfer.getCreatedAt());
        return response;
    }

    public List<TransferResponse> toResponseList(List<Transfer> transfers) {
        return transfers.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}