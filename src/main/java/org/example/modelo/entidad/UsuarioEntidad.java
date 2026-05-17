package org.example.modelo.entidad;

import jakarta.persistence.*;
import org.example.enumerados.EstadoCuenta;


import java.time.LocalDate;
@Table(name = "usuarios")
@Entity
public class UsuarioEntidad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column (name = "nombre_usuario")
    private String nombreUsuario;
    @Column (name = "email")
    private String email;
    @Column (name = "nombre_real_u")
    private String nombreRealU;
    @Column (name = "pais")
    private String pais;
    @Column (name = "fecha_n")
    private LocalDate fechaN;
    @Column (name = "fecha_registro")
    private LocalDate fechaRegis;
    @Column (name = "avatar")
    private String avatar;
    @Column (name = "saldo")
    private Double saldo;
    @Column (name = "estado_cuenta")
    private EstadoCuenta estadoCuenta;

    public UsuarioEntidad(Long id, String nombreUsuario, String email, String nombreRealU, String pais, LocalDate fechaN, LocalDate fechaRegis, String avatar, Double saldo, EstadoCuenta estadoCuenta) {
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



    public Double getSaldo() {
        return saldo;
    }



    public EstadoCuenta getEstadoCuenta() {
        return estadoCuenta;
    }


    public String getContrasenia() {

        return "";
    }
}
