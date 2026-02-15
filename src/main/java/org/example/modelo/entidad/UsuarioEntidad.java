package org.example.modelo.entidad;

import org.example.enumerados.EstadoCuenta;

import java.time.LocalDate;

public class UsuarioEntidad {

    private int idUsuario;
    private String nombreUsuario;
    private String email;
    private String nombreRealU;
    private String pais;
    private LocalDate fechaN;
    private LocalDate fechaRegis;
    private String avatar;
    private double saldo;
    private EstadoCuenta estadoCuenta;

    public int getIdUsuario() {
        return idUsuario;
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
