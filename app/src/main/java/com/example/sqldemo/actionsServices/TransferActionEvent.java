package com.example.sqldemo.actionsServices;

import com.example.sqldemo.sqlControls.services.SQLService;

import java.math.BigDecimal;

public class TransferActionEvent {
    private final SQLService sqlService = new SQLService();

    public boolean transferMoney(String accountNumber, int accountIndexInList, BigDecimal sum, String fullNameForeign){
        return sqlService.transferMoneyFromLocal(accountIndexInList, sum, accountNumber, fullNameForeign);
    }
}
