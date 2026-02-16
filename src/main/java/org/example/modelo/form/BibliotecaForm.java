package org.example.modelo.form;

import org.example.enumerados.ErrorTipo;
import org.example.modelo.dto.ErrorDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BibliotecaForm {

    private Integer idUsuario;
    private Integer idJuego;
    private LocalDate fechaAdquisicion;
    private Double tiempoDeJuego;
    private LocalDate ultimaVezJugada;

    public BibliotecaForm(int idUsuario, int idJuego, LocalDate fechaAdquisicion,Double tiempoDeJuego,LocalDate ultimaVezJugada) {
        this.idUsuario = idUsuario;
        this.idJuego = idJuego;
        this.fechaAdquisicion = fechaAdquisicion;
        this.tiempoDeJuego = tiempoDeJuego;
        this.ultimaVezJugada = ultimaVezJugada;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public int getIdJuego() {
        return idJuego;
    }

    public LocalDate getFechaAdquisicion() {
        return fechaAdquisicion;
    }

    public Double getTiempoDeJuego() {
        return tiempoDeJuego;
    }

    public List<ErrorDTO> validar(){
            List<ErrorDTO> errores = new ArrayList<>();

        if (idUsuario == null){
            errores.add(new ErrorDTO("id usuario",ErrorTipo.REQUERIDO));
        }

        if (idJuego == null){
            errores.add(new ErrorDTO("id juego",ErrorTipo.REQUERIDO));
        }

        if (fechaAdquisicion == null){
            errores.add(new ErrorDTO("id usuario",ErrorTipo.REQUERIDO));
        }

        if (tiempoDeJuego != null && tiempoDeJuego <0){
            errores.add(new ErrorDTO("Tiempo de juego",ErrorTipo.FORMATO_INVALIDO));
        } else if (Math.round(tiempoDeJuego * 10) != tiempoDeJuego * 10) {
            errores.add(new ErrorDTO("Tiempo de juego", ErrorTipo.FORMATO_INVALIDO));
        }

        return errores;


    }

}

