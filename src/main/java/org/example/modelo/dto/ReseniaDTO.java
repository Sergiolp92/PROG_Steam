package org.example.modelo.dto;

import org.example.enumerados.EstadoResenia;

import java.time.LocalDate;

public class ReseniaDTO {

    private Long idResenia;
    private UsuarioDTO usuario;
    private JuegoDTO juego;
    private boolean recomendado;
    private String tResenia;
    private int horasJugadas;
    private LocalDate fechaPublicacionR;
    private LocalDate fechaEdicionR;
    private EstadoResenia estado;

    public ReseniaDTO(Long idResenia, UsuarioDTO usuario, JuegoDTO juego, boolean recomendado, String tResenia, int horasJugadas, LocalDate fechaPublicacionR, LocalDate fechaEdicionR, EstadoResenia estado) {
        this.idResenia = idResenia;
        this.usuario = usuario;
        this.juego = juego;
        this.recomendado = recomendado;
        this.tResenia = tResenia;
        this.horasJugadas = horasJugadas;
        this.fechaPublicacionR = fechaPublicacionR;
        this.fechaEdicionR = fechaEdicionR;
        this.estado = estado.PUBLICADA;
    }

    public Long getIdResenia() {
        return idResenia;
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



    public EstadoResenia getEstado() {
        return estado;
    }


}
