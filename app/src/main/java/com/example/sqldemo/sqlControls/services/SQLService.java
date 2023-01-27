package com.example.sqldemo.sqlControls.services;

import static com.example.sqldemo.session.LiveSessionObject.getUserEmail;
import static com.example.sqldemo.session.LiveSessionObject.getUserPassword;
import static com.example.sqldemo.sqlControls.objectTypes.Table.KLIENTAS;
import static com.example.sqldemo.sqlControls.objectTypes.Table.MOKEJIMAS;
import static com.example.sqldemo.sqlControls.objectTypes.Table.SASKAITA;
import static com.example.sqldemo.sqlControls.services.SQLConnectionService.autoWireObject;

import com.example.sqldemo.dtos.AccountDTO;
import com.example.sqldemo.dtos.UserDTO;
import com.example.sqldemo.sqlControls.objectTypes.Table;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SQLService {
    private SQLConnectionService sqlConnectionService;

    public SQLService() {
        this.sqlConnectionService = autoWireObject();
    }

    public ResultSet findByIdFrom(String id, Table table) {
        try {
            String query = "Select * from " + table + " where id = " + id;
            Statement statement = sqlConnectionService.getConnection().createStatement();
            return statement.executeQuery(query);
        } catch (Exception e) {
            System.out.println("[DEBEUG] FAILED TO FIND OBJECT BY ID");
            e.printStackTrace();
            return null;
        }
    }


    private ResultSet getUser(String email, String pass) {
        try {
            String query = "Select * from " + KLIENTAS.value() + " where el_pastas = '" + email + "' and slaptazodis = '" + pass + "'";
            Statement statement = sqlConnectionService.getConnection().createStatement();
            return statement.executeQuery(query);
        } catch (Exception e) {
            System.out.println("[DEBEUG] FAILED TO FIND user");
            e.printStackTrace();
            return null;
        }
    }

    private ResultSet getAccounts() {
        try {
            ResultSet user = getUser(getUserEmail(), getUserPassword());
            user.next();
            String query = "Select * from " + SASKAITA.value() + " where klientas_id = " + user.getInt("id");
            Statement statement = sqlConnectionService.getConnection().createStatement();
            return statement.executeQuery(query);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException();
        }
    }

    private ResultSet getForeignAccount(String accountNumber) {
        try {
            String query = "Select * from " + SASKAITA.value() + " where saskaitos_kodas = '" + accountNumber + "'";
            Statement statement = sqlConnectionService.getConnection().createStatement();
            return statement.executeQuery(query);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException();
        }
    }

    public boolean authenticateUser(String email, String pass) {
        if (email.isEmpty() || pass.isEmpty()) {
            return false;
        }
        try {
            ResultSet user = getUser(email, pass);
            return user != null && user.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean authenticateUserByPin(String pin) {
        try {
            ResultSet user = getUser(getUserEmail(), getUserPassword());
            if (user != null && user.next() && user.getString("pin").equals(pin)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<String> getAccountsList() {
        try {
            ResultSet accounts = getAccounts();
            List<String> accountsList = new ArrayList<>();
            while (accounts.next()) {
                accountsList.add(accounts.getString("banko_pavadinimas") + " "
                        + accounts.getString("saskaitos_kodas") + " "
                        + accounts.getBigDecimal("balansas"));
            }
            return accountsList;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public boolean transferMoneyFromLocal(int index, BigDecimal sum, String accountNumber, String fullNameForeign) {
        try {
            ResultSet accountsLocal = getAccounts();
            ResultSet userLocal = getUser(getUserEmail(), getUserPassword());
            userLocal.next();
            int i = 0;
            while (accountsLocal.next()) {
                if (i == index) {
                    BigDecimal newSumLocal = accountsLocal.getBigDecimal("balansas").subtract(sum);
                    ResultSet foreignAccount = getForeignAccount(accountNumber);
                    foreignAccount.next();
                    BigDecimal newSumForeign = foreignAccount.getBigDecimal("balansas").add(sum);

                    String queryLocal = "update " + SASKAITA.value() +
                            " set balansas = " + newSumLocal + "" +
                            " where saskaitos_kodas = '" + accountsLocal.getString("saskaitos_kodas") + "'";

                    String foreignQuery = "update " + SASKAITA.value() +
                            " set balansas = " + newSumForeign +
                            " where saskaitos_kodas = '" + accountNumber + "'";

                    String paymentLogQuery = "insert into " + MOKEJIMAS.value() + "(`siuntejo_pilnas_vardas`, `gavejo_pilnas_vardas`, `siuntejo_saskaitos_kodas`, `gavejo_saskaitos_kodas`, `kiekis`)" +
                            " values('" + userLocal.getString("vardas") + " " + userLocal.getString("pavarde") + "'" +
                            ", '" + fullNameForeign + "'" +
                            ", '" + accountsLocal.getString("saskaitos_kodas") + "'" +
                            ", '" + accountNumber + "'" +
                             ", " + sum + ")";

                    Statement statement = sqlConnectionService.getConnection().createStatement();

                    validateOperationIsPossible(newSumLocal);

                    statement.executeUpdate(queryLocal);
                    statement.executeUpdate(foreignQuery);
                    statement.executeUpdate(paymentLogQuery);

                    System.out.println("[DEBEUG] TRANSFER SUCCESSFUL" +
                            "\nqueryLocal = " + queryLocal +
                            "\nforeignQuery = " + foreignQuery +
                            "\nnewSumLocal = " + newSumLocal +
                            "\nnewSumForeign = " + newSumForeign);

                    return true;
                }
                i++;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void validateOperationIsPossible(BigDecimal newSumLocal) {
        if(0 > newSumLocal.compareTo(BigDecimal.ZERO)){
            throw new IllegalArgumentException("Trying to transfer more than possible");
        }
    }

    public boolean registerNewUser(UserDTO userDTO) {
        try {
            String query = "insert into " + KLIENTAS.value() + "(`vardas`, `pavarde`, `el_pastas`, `slaptazodis`, `gimimo_diena`, `asmens_kodas`, `pin`)" +
                    " values('" + userDTO.getVardas() + "'" +
                    ", '" + userDTO.getPavarde() + "'" +
                    ", '" + userDTO.getElPastas() + "'" +
                    ", '" + userDTO.getSlaptazodis() + "'" +
                    ", '" + userDTO.getGimimoDiena().toString() + "'" +
                    ", '" + userDTO.getAsmensKodas() + "'" +
                    ", '" + userDTO.getPin() + "') ";

            System.out.println("[DEBUG] register query = " + query);

            Statement statement = sqlConnectionService.getConnection().createStatement();
            return statement.executeUpdate(query) == 1;
        } catch (Exception e) {
            System.out.println("[DEBEUG] FAILED TO REGISTER USER");
            e.printStackTrace();
            return false;
        }
    }

    public boolean addNewAccount(AccountDTO accountDTO){
        try {
            ResultSet user = getUser(getUserEmail(), getUserPassword());
            user.next();
            String query = "insert into " + SASKAITA.value() + "(`klientas_id`, `banko_pavadinimas`, `saskaitos_kodas`, `smart_id_key`, `balansas`)" +
                    " values(" + user.getLong("id") +
                    ", '" + accountDTO.getBankName() + "'" +
                    ", '" + accountDTO.getBankAccountNumber() + "'" +
                    ", '" + accountDTO.getSmartIdHash() + "'" +
                     ", " + accountDTO.getBalance() + ") ";

            System.out.println("[DEBUG] add account query = " + query);

            Statement statement = sqlConnectionService.getConnection().createStatement();
            return statement.executeUpdate(query) == 1;
        } catch (Exception e) {
            System.out.println("[DEBEUG] FAILED TO REGISTER USER");
            e.printStackTrace();
            return false;
        }
    }

    public List<String> getPaymentHistory() {
        try {
            ResultSet user = getUser(getUserEmail(), getUserPassword());
            ResultSet accountsLocal = getAccounts();
            List<String> saskaitos = new ArrayList<>();

            while(accountsLocal.next()){
                saskaitos.add(accountsLocal.getString("saskaitos_kodas"));
            }

            user.next();
            String queryDynamicSaskaitos = saskaitos.stream().map(saskaita -> " siuntejo_saskaitos_kodas = '" + saskaita + "' OR ").collect(Collectors.joining());
            String query = "Select * from " + MOKEJIMAS.value() + " where" + queryDynamicSaskaitos.substring(0, queryDynamicSaskaitos.length()-4);
            System.out.println("[DEBUG] payment history query: " + query);
            Statement statement = sqlConnectionService.getConnection().createStatement();
            ResultSet mokejimai = statement.executeQuery(query);

            List<String> mokejimaiOut = new ArrayList<>();
            while (mokejimai.next()) {
                mokejimaiOut.add("Is: " + mokejimai.getString("siuntejo_saskaitos_kodas") + "; I: "
                        + mokejimai.getString("gavejo_saskaitos_kodas") + "; Suma: "
                        + mokejimai.getBigDecimal("kiekis"));
            }

            return mokejimaiOut;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}