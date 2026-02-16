package org.example.modelo.form;

import org.example.enumerados.ErrorTipo;
import org.example.modelo.dto.ErrorDTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReseñaForm {

    private Integer idUsuario;
    private Integer idJuego;
    private boolean recomendado;
    private String textoResenia;
    private LocalDate fechaUltimaEdicion;

    public ReseñaForm(Integer idUsuario, Integer idJuego, boolean recomendado, String textoResenia,LocalDate fechaUltimaEdicion) {
        this.idUsuario = idUsuario;
        this.idJuego = idJuego;
        this.recomendado = recomendado;
        this.textoResenia = textoResenia;
        this.fechaUltimaEdicion = fechaUltimaEdicion;
    }

    public LocalDate getFechaUltimaEdicion() {
        return fechaUltimaEdicion;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public Integer getIdJuego() {
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
