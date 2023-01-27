package com.example.sqldemo.dtos.builders;

import com.example.sqldemo.dtos.UserDTO;

import java.math.BigDecimal;
import java.time.LocalDate;

public class UserDTOBuilder {
    private Long saskaitosValdytojasId;
    private String vardas;
    private String pavarde;
    private String elPastas;
    private String slaptazodis;
    private String smartIdKey;
    private LocalDate gimimoDiena;
    private String asmensKodas;
    private String fingerprintHash;
    private String faceIdHash;
    private String pin;

    public UserDTOBuilder withSaskaitosValdytojasId(Long saskaitosValdytojasId) {
        this.saskaitosValdytojasId = saskaitosValdytojasId;
        return this;
    }

    public UserDTOBuilder withVardas(String vardas) {
        this.vardas = vardas;
        return this;
    }

    public UserDTOBuilder withPavarde(String pavarde) {
        this.pavarde = pavarde;
        return this;
    }

    public UserDTOBuilder withElPastas(String elPastas) {
        this.elPastas = elPastas;
        return this;
    }

    public UserDTOBuilder withSlaptazodis(String slaptazodis) {
        this.slaptazodis = slaptazodis;
        return this;
    }

    public UserDTOBuilder withSmartIdKey(String smartIdKey) {
        this.smartIdKey = smartIdKey;
        return this;
    }

    public UserDTOBuilder withGimimoDiena(LocalDate gimimoDiena) {
        this.gimimoDiena = gimimoDiena;
        return this;
    }

    public UserDTOBuilder withAsmensKodas(String asmensKodas) {
        this.asmensKodas = asmensKodas;
        return this;
    }

    public UserDTOBuilder withFingerprintHash(String fingerprintHash) {
        this.fingerprintHash = fingerprintHash;
        return this;
    }

    public UserDTOBuilder withFaceIdHash(String faceIdHash) {
        this.faceIdHash = faceIdHash;
        return this;
    }

    public UserDTOBuilder withPin(String pin) {
        this.pin = pin;
        return this;
    }

    public UserDTO build() {
        return new UserDTO(saskaitosValdytojasId,
                vardas,
                pavarde,
                elPastas,
                slaptazodis,
                smartIdKey,
                gimimoDiena,
                asmensKodas,
                fingerprintHash,
                faceIdHash,
                pin);
    }
}
