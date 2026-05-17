package org.example.modelo.entidad;

import jakarta.persistence.*;
import org.example.enumerados.EstadoInstalacion;

import java.time.LocalDate;
@Table(name = "biblioteca")
@Entity
public class BibliotecaEntidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idBiblio;
    @Column(name = "id_usuario")
    private Long idUsuario;
    @Column(name = "id_juego")
    private Long idJuego;
    @Column (name = "fecha_adquisicion")
    private LocalDate fechaAdquisicion;
    @Column(name = "tiempo_total_jugado")
    private float tiempoTotalJugado;
    @Column(name = "ultima_fecha_juego")
    private LocalDate ultimaFechaJuego;
    @Column(name = "estado_instalacion")
    private EstadoInstalacion estadoInstalacion;

    public BibliotecaEntidad(Long idBiblio, Long idUsuario, Long idJuego, LocalDate fechaAdquisicion, float tiempoTotalJugado) {
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
