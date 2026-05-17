package org.example.modelo.entidad;

import jakarta.persistence.*;
import org.example.enumerados.EstadoResenia;

import java.time.LocalDate;
@Table(name = "resenias")
@Entity
public class ReseniaEntidad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idResenia;
    @Column (name = "usuario_id")
    private long usuarioId;
    @Column (name = "juego_id")
    private long juegoId;
    @Column (name = "recomendado")
    private boolean recomendado;
    @Column (name = "t_resenia")
    private String tResenia;
    @Column (name = "horas_jugadas")
    private int horasJugadas;
    @Column (name = "fecha_publicacion_r")
    private LocalDate fechaPublicacionR;
    @Column (name = "fecha_edicion_r")
    private LocalDate fechaEdicionR;
    @Column (name = "estado")
    private EstadoResenia estado;


    public ReseniaEntidad(Long idResenia,long usuarioId, long juegoId, boolean recomendado, String tResenia, int horasJugadas, LocalDate fechaPublicacionR, LocalDate fechaEdicionR, EstadoResenia estado) {
        this.idResenia = idResenia;
        this.usuarioId = usuarioId;
        this.juegoId = juegoId;
        this.recomendado = recomendado;
        this.tResenia = tResenia;
        this.horasJugadas = horasJugadas;
        this.fechaPublicacionR = fechaPublicacionR;
        this.fechaEdicionR = fechaEdicionR;
        this.estado = estado;
    }

    public Long getIdResenia() {
        return idResenia;
    }

    public long getUsuarioId() {
        return usuarioId;
    }

    public long getJuegoId() {
        return juegoId;
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



    public EstadoResenia getEstado() {
        return estado;
    }

}
