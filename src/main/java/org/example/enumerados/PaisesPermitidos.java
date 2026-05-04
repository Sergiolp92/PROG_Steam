package org.example.enumerados;

public enum PaisesPermitidos {
    ESPAÑA, PORTUGAL, FRANCIA, ITALIA;


    public String toUpperCase() {
        return this.name().toUpperCase();

    }
}
