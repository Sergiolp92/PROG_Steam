package org.example.modelo.entidad;

import org.example.enumerados.EstadoInstalacion;

import java.time.LocalDate;

public class BibliotecaEntidad {

    private Long idBiblio;
    private Long idUsuario;
    private Long idJuego;
    private LocalDate fechaAdquisicion;
    private Double tiempoTotalJugado;
    private LocalDate ultimaFechaJuego;
    private EstadoInstalacion estadoInstalacion;

    public BibliotecaEntidad(Long idBiblio, Long idUsuario, Long idJuego, LocalDate fechaAdquisicion, Double tiempoTotalJugado) {
        this.idBiblio = idBiblio;
        this.idUsuario = idUsuario;
        this.idJuego = idJuego;
        this.fechaAdquisicion = fechaAdquisicion;
        this.tiempoTotalJugado = tiempoTotalJugado;
        this.ultimaFechaJuego = ultimaFechaJuego;
        this.estadoInstalacion = estadoInstalacion;
    }

    public Long getIdBiblio() {
        return idBiblio;
    }



    public Long getIdUsuario() {
        return idUsuario;
    }






    public Long getIdJuego() {
        return idJuego;
    }







    public LocalDate getFechaAdquisicion() {
        return fechaAdquisicion;
    }



    public Double getTiempoTotalJugado() {
        return tiempoTotalJugado;
    }



    public LocalDate getUltimaFechaJuego() {
        return ultimaFechaJuego;
    }



    public EstadoInstalacion getEstadoInstalacion() {
        return estadoInstalacion;
    }

}
