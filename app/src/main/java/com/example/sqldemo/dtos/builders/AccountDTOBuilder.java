package com.example.sqldemo.dtos.builders;

import com.example.sqldemo.dtos.AccountDTO;

public final class AccountDTOBuilder {
    private Long valdytojasId;
    private String name;
    private String lastName;
    private String bankName;
    private String bankAccountNumber;
    private String smartIdHash;
    private String balance;

    private AccountDTOBuilder() {
    }

    public static AccountDTOBuilder anAccountDTO() {
        return new AccountDTOBuilder();
    }

    public AccountDTOBuilder withValdytojasId(Long valdytojasId) {
        this.valdytojasId = valdytojasId;
        return this;
    }

    public AccountDTOBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public AccountDTOBuilder withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public AccountDTOBuilder withBankName(String bankName) {
        this.bankName = bankName;
        return this;
    }

    public AccountDTOBuilder withBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
        return this;
    }

    public AccountDTOBuilder withSmartIdHash(String smartIdHash) {
        this.smartIdHash = smartIdHash;
        return this;
    }

    public AccountDTOBuilder withBalance(String balance) {
        this.balance = balance;
        return this;
    }

    public AccountDTO build() {
        return new AccountDTO(valdytojasId, name, lastName, bankName, bankAccountNumber, smartIdHash, balance);
    }
}
