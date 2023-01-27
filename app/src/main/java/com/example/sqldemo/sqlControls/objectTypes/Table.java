package com.example.sqldemo.sqlControls.objectTypes;

public enum Table {
    KLIENTAS("klientas"),
    MOKEJIMAS("mokejimas"),
    SASKAITA("saskaita"),
    SASKAITOS_VALDYTOJAS("saskaitos_valdytojas");
    private String value;
    Table(String value) {
        this.value = value;
    }
    public String value(){
        return value;
    }
}
