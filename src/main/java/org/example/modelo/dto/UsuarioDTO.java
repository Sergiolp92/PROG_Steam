package org.example.modelo.dto;

import org.example.enumerados.EstadoCuenta;
import org.example.enumerados.PaisesPermitidos;

import java.time.LocalDate;

public class UsuarioDTO {

    private int id;
    private String nick;
    private String email;
    private String nombre;
    private PaisesPermitidos pais;
    private LocalDate fechaN;
    private LocalDate fechaRegis;
    private String avatar;
    private double saldo;
    private EstadoCuenta estadoCuenta;

    public UsuarioDTO(int id, String nick, String email, String nombre, PaisesPermitidos pais, LocalDate fechaN, LocalDate fechaRegis, String avatar, double saldo, EstadoCuenta estadoCuenta) {
        this.id = id;
        this.nick = nick;
        this.email = email;
        this.nombre = nombre;
        this.pais = pais;
        this.fechaN = fechaN;
        this.fechaRegis = fechaRegis;
        this.avatar = avatar;
        this.saldo = saldo;
        this.estadoCuenta = EstadoCuenta.ACTIVA;
    }


    public int getId() {
        return id;
    }

    public String getNick() {
        return nick;
    }

    public String getEmail() {
        return email;
    }

    public String getNombre() {
        return nombre;
    }

    public PaisesPermitidos getPais() {
        return pais;
    }

    public LocalDate getFechaN() {
        return fechaN;
    }

    public LocalDate getFechaRegis() {
        return fechaRegis;
    }

    public String getAvatar() {
        return avatar;
    }

    public double getSaldo() {
        return saldo;
    }

    public EstadoCuenta getEstadoCuenta() {
        return estadoCuenta;
    }


}



