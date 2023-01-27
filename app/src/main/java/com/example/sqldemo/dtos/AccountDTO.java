package com.example.sqldemo.dtos;

public class AccountDTO {
    private final Long valdytojasId;
    private final String name;
    private final String lastName;
    private final String bankName;
    private final String bankAccountNumber;
    private final String smartIdHash;
    private final String balance;

    public AccountDTO(Long valdytojasId, String name, String lastName, String bankName, String bankAccountNumber, String smartIdHash, String balance) {
        this.valdytojasId = valdytojasId;
        this.name = name;
        this.lastName = lastName;
        this.bankName = bankName;
        this.bankAccountNumber = bankAccountNumber;
        this.smartIdHash = smartIdHash;
        this.balance = balance;
    }

    public Long getValdytojasId() {
        return valdytojasId;
    }

    public String getBankName() {
        return bankName;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public String getSmartIdHash() {
        return smartIdHash;
    }

    public String getBalance() {
        return balance;
    }
}
