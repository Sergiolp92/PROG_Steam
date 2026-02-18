package org.example.modelo.entidad;

import org.example.enumerados.EstadoCuenta;

import java.time.LocalDate;

public class UsuarioEntidad {

    private Long id;
    private String nombreUsuario;
    private String email;
    private String nombreRealU;
    private String pais;
    private LocalDate fechaN;
    private LocalDate fechaRegis;
    private String avatar;
    private double saldo;
    private EstadoCuenta estadoCuenta;

    public UsuarioEntidad(Long id, String nombreUsuario, String email, String nombreRealU, String pais, LocalDate fechaN, LocalDate fechaRegis, String avatar, double saldo, EstadoCuenta estadoCuenta) {
        this.id = id;
        this.nombreUsuario = nombreUsuario;
        this.email = email;
        this.nombreRealU = nombreRealU;
        this.pais = pais;
        this.fechaN = fechaN;
        this.fechaRegis = fechaRegis;
        this.avatar = avatar;
        this.saldo = saldo;
        this.estadoCuenta = estadoCuenta;
    }

    public Long getId() {
        return id;
    }


    public String getNombreUsuario() {
        return nombreUsuario;
    }



    public String getEmail() {
        return email;
    }



    public String getNombreRealU() {
        return nombreRealU;
    }



    public String getPais() {
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
