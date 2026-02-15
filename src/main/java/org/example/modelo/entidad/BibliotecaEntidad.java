package org.example.modelo.entidad;

import org.example.enumerados.EstadoInstalacion;
import org.example.modelo.dto.JuegoDTO;
import org.example.modelo.dto.UsuarioDTO;

import java.time.LocalDate;
import java.util.Optional;

public class BibliotecaEntidad {

    private int idBiblio;
    private int idUsuario;

    private int idJuego;

    private LocalDate fechaAdquisicion;
    private int tiempoTotalJugado;
    private LocalDate ultimaFechaJuego;
    private EstadoInstalacion estadoInstalacion;

    public int getIdBiblio() {
        return idBiblio;
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



    public int getTiempoTotalJugado() {
        return tiempoTotalJugado;
    }



    public LocalDate getUltimaFechaJuego() {
        return ultimaFechaJuego;
    }



    public EstadoInstalacion getEstadoInstalacion() {
        return estadoInstalacion;
    }

}
