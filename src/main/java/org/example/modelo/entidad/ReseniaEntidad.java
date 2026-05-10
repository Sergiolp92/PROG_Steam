package org.example.modelo.entidad;

import org.example.enumerados.EstadoResenia;

import java.time.LocalDate;

public class ReseniaEntidad {

    private Long idResenia;
    private long usuarioId;
    private long juegoId;
    private boolean recomendado;
    private String tResenia;
    private int horasJugadas;
    private LocalDate fechaPublicacionR;
    private LocalDate fechaEdicionR;
    private EstadoResenia estado;


    public ReseniaEntidad(Long idResenia,long usuarioId, long juegoId, boolean recomendado, String tResenia, int horasJugadas, LocalDate fechaPublicacionR, LocalDate fechaEdicionR, EstadoResenia estado) {
        this.idResenia = idResenia;
        this.recomendado = recomendado;
        this.tResenia = tResenia;
        this.horasJugadas = horasJugadas;
        this.fechaPublicacionR = fechaPublicacionR;
        this.fechaEdicionR = fechaEdicionR;
        this.estado = estado;
    }

    public Long getIdResenia() {
        return idResenia;
    }

    public long getUsuarioId() {
        return usuarioId;
    }

    public long getJuegoId() {
        return juegoId;
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
