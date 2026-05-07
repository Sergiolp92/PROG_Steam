package org.example.modelo.dto;

import org.example.enumerados.EstadoInstalacion;

import java.time.LocalDate;

public class BibliotecaDTO {

    private Long idBiblio;
    private Long idUsuario;
    private UsuarioDTO usuario;
    private Long idJuego;
    private JuegoDTO juego;
    private LocalDate fechaAdquisicion;
    private float tiempoTotalJugado;
    private LocalDate ultimaFechaJuego;
    private EstadoInstalacion estadoInstalacion;

    public BibliotecaDTO(Long idBiblio, Long idUsuario, UsuarioDTO usuario, Long idJuego, JuegoDTO juego, LocalDate fechaAdquisicion, float tiempoTotalJugado, LocalDate ultimaFechaJuego, EstadoInstalacion estadoInstalacion) {
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

    public Long getIdBiblio() {
        return idBiblio;
    }



    public Long getIdUsuario() {
        return idUsuario;
    }



    public UsuarioDTO getUsuario() {
        return usuario;
    }



    public Long getIdJuego() {
        return idJuego;
    }



    public JuegoDTO getJuego() {
        return juego;
    }



    public LocalDate getFechaAdquisicion() {
        return fechaAdquisicion;
    }



    public float getTiempoTotalJugado() {
        return tiempoTotalJugado;
    }



    public LocalDate getUltimaFechaJuego() {
        return ultimaFechaJuego;
    }



    public EstadoInstalacion getEstadoInstalacion() {
        return estadoInstalacion;
    }


}

