package org.example.modelo.entidad;

import org.example.enumerados.EstadoReseña;
import org.example.modelo.dto.JuegoDTO;
import org.example.modelo.dto.UsuarioDTO;

import java.time.LocalDate;
import java.util.Optional;

public class ReseñaEntidad {

    private int idReseña;
    private boolean recomendado;
    private String tResenia;
    private int horasJugadas;
    private LocalDate fechaPublicacionR;
    private LocalDate fechaEdicionR;
    private EstadoReseña estado;




    public int getIdReseña() {
        return idReseña;
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
