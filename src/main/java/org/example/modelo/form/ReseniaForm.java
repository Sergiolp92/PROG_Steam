package org.example.modelo.form;

import org.example.enumerados.ErrorTipo;
import org.example.enumerados.EstadoResenia;
import org.example.modelo.dto.ErrorDTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReseniaForm {

    private Long idUsuario;
    private Long idJuego;
    private boolean recomendado;
    private String textoResenia;
    private LocalDate fechaUltimaEdicion;
    public EstadoResenia estadoResenia;
    public LocalDate fechaPublicacionR;
    public int horasJugadas;

    public ReseniaForm(Long idUsuario, Long idJuego, boolean recomendado, String textoResenia, LocalDate fechaUltimaEdicion, LocalDate fechaPublicacionR, int horasJugadas,EstadoResenia estadoResenia) {
        this.idUsuario = idUsuario;
        this.idJuego = idJuego;
        this.recomendado = recomendado;
        this.textoResenia = textoResenia;
        this.fechaUltimaEdicion = fechaUltimaEdicion;
        this.estadoResenia = estadoResenia;
        this.fechaPublicacionR = fechaPublicacionR;
        this.horasJugadas = horasJugadas;
    }

    public int getHorasJugadas() {
        return horasJugadas;
    }

    public EstadoResenia getEstadoResenia() {
        return estadoResenia;
    }

    public LocalDate getFechaPublicacionR() {
        return fechaPublicacionR;
    }

    public LocalDate getFechaUltimaEdicion() {
        return fechaUltimaEdicion;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public Long getIdJuego() {
        return idJuego;
    }

    public boolean isRecomendado() {
        return recomendado;
    }

    public String getTextoResenia() {
        return textoResenia;
    }

    public List<ErrorDTO> validar(){
        List<ErrorDTO> errores = new ArrayList<>();

        if(idUsuario == null){
            errores.add(new ErrorDTO("id Usuario", ErrorTipo.REQUERIDO));
        }

        if(idJuego == null){
            errores.add(new ErrorDTO("id Juego", ErrorTipo.REQUERIDO));
        }
         return errores;


    }
}
