package org.unitech.msaccount.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.unitech.msaccount.domain.entity.Account;
import org.unitech.msaccount.model.dto.AccountDto;
import org.unitech.msaccount.model.dto.response.AccountResponse;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    Account toEntity(AccountDto dto);

    AccountDto toDto(Account entity);

    @Mapping(target = "pin", ignore = true)
    @Mapping(target = "cvv", ignore = true)
    AccountResponse toResponse(Account entity);

    List<AccountResponse> toResponseList(List<Account> entities);

    void updateEntity(AccountDto dto, @MappingTarget Account entity);
}
