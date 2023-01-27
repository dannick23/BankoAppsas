package com.example.sqldemo.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

public class UserDTO {
    private final Long saskaitosValdytojasId;
    private final String vardas;
    private final String pavarde;
    private final String elPastas;
    private final String slaptazodis;
    private final String smartIdKey;
    private final LocalDate gimimoDiena;
    private final String asmensKodas;
    private final String fingerprintHash;
    private final String faceIdHash;
    private final String pin;

    public UserDTO(Long saskaitosValdytojasId,
                   String vardas,
                   String pavarde,
                   String elPastas,
                   String slaptazodis,
                   String smartIdKey,
                   LocalDate gimimoDiena,
                   String asmensKodas,
                   String fingerprintHash,
                   String faceIdHash,
                   String pin) {
        this.saskaitosValdytojasId = saskaitosValdytojasId;
        this.vardas = vardas;
        this.pavarde = pavarde;
        this.elPastas = elPastas;
        this.slaptazodis = slaptazodis;
        this.smartIdKey = smartIdKey;
        this.gimimoDiena = gimimoDiena;
        this.asmensKodas = asmensKodas;
        this.fingerprintHash = fingerprintHash;
        this.faceIdHash = faceIdHash;
        this.pin = pin;
    }

    public Long getSaskaitosValdytojasId() {
        return saskaitosValdytojasId;
    }

    public String getVardas() {
        return vardas;
    }

    public String getPavarde() {
        return pavarde;
    }

    public String getElPastas() {
        return elPastas;
    }

    public String getSlaptazodis() {
        return slaptazodis;
    }

    public String getSmartIdKey() {
        return smartIdKey;
    }

    public LocalDate getGimimoDiena() {
        return gimimoDiena;
    }

    public String getAsmensKodas() {
        return asmensKodas;
    }

    public String getFingerprintHash() {
        return fingerprintHash;
    }

    public String getFaceIdHash() {
        return faceIdHash;
    }

    public String getPin() {
        return pin;
    }
}
