package org.example.modelo.form;

import org.example.enumerados.ErrorTipo;
import org.example.enumerados.EstadoResenia;
import org.example.modelo.dto.ErrorDTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReseniaForm {

    public LocalDate fechaPublicacionR;
    public int horasJugadas;

    private Long idUsuario;
    private Long idJuego;
    private boolean recomendado;
    private String textoResenia;
    private LocalDate fechaUltimaEdicion;
    public EstadoResenia estadoResenia;

    public ReseniaForm(Long idUsuario, Long idJuego, boolean recomendado, String textoResenia, LocalDate fechaUltimaEdicion) {
        this.idUsuario = idUsuario;
        this.idJuego = idJuego;
        this.recomendado = recomendado;
        this.textoResenia = textoResenia;
        this.fechaUltimaEdicion = fechaUltimaEdicion;
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
