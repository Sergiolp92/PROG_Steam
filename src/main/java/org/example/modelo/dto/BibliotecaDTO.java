package org.example.modelo.dto;

import org.example.enumerados.EstadoInstalacion;

import java.time.LocalDate;
import java.util.Optional;

public class BibliotecaDTO {

    private int idBiblio;
    private int idUsuario;
    private UsuarioDTO usuario;
    private int idJuego;
    private JuegoDTO juego;
    private LocalDate fechaAdquisicion;
    private int tiempoTotalJugado;
    private LocalDate ultimaFechaJuego;
    private EstadoInstalacion estadoInstalacion;

    public BibliotecaDTO(int idBiblio, int idUsuario, UsuarioDTO usuario, int idJuego, JuegoDTO juego, LocalDate fechaAdquisicion, int tiempoTotalJugado, LocalDate ultimaFechaJuego, EstadoInstalacion estadoInstalacion) {
        this.idBiblio = idBiblio;
        this.idUsuario = idUsuario;
        this.usuario = usuario;
        this.idJuego = idJuego;
        this.juego = juego;
        this.fechaAdquisicion = fechaAdquisicion;
        this.tiempoTotalJugado = tiempoTotalJugado;
        this.ultimaFechaJuego = ultimaFechaJuego;
        this.estadoInstalacion = estadoInstalacion.NO_INSTALADO;
    }

    public int getIdBiblio() {
        return idBiblio;
    }



    public int getIdUsuario() {
        return idUsuario;
    }



    public UsuarioDTO getUsuario() {
        return usuario;
    }



    public int getIdJuego() {
        return idJuego;
    }



    public JuegoDTO getJuego() {
        return juego;
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

