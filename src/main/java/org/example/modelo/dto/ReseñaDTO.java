package org.example.modelo.dto;

import org.example.enumerados.EstadoReseña;

import java.time.LocalDate;
import java.util.Optional;

public class ReseñaDTO {

    private int idReseña;
    private UsuarioDTO usuario;
    private JuegoDTO juego;
    private boolean recomendado;
    private String tResenia;
    private int horasJugadas;
    private LocalDate fechaPublicacionR;
    private LocalDate fechaEdicionR;
    private EstadoReseña estado;

    public ReseñaDTO(int idReseña, UsuarioDTO usuario, JuegoDTO juego, boolean recomendado, String tResenia, int horasJugadas, LocalDate fechaPublicacionR, LocalDate fechaEdicionR, EstadoReseña estado) {
        this.idReseña = idReseña;
        this.usuario = usuario;
        this.juego = juego;
        this.recomendado = recomendado;
        this.tResenia = tResenia;
        this.horasJugadas = horasJugadas;
        this.fechaPublicacionR = fechaPublicacionR;
        this.fechaEdicionR = fechaEdicionR;
        this.estado = estado;
    }

    public int getIdReseña() {
        return idReseña;
    }



    public UsuarioDTO getUsuario() {
        return usuario;
    }



    public JuegoDTO getJuego() {
        return juego;
    }



    public boolean isRecomendado() {
        return recomendado;
    }



    public String gettResenia() {
        return tResenia;
    }



    public int getHorasJugadas() {
        return horasJugadas;
    }



    public LocalDate getFechaPublicacionR() {
        return fechaPublicacionR;
    }



    public LocalDate getFechaEdicionR() {
        return fechaEdicionR;
    }



    public EstadoReseña getEstado() {
        return estado;
    }


}
