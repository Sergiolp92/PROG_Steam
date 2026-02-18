package org.example.modelo.entidad;

import org.example.enumerados.ClasificacionEdad;
import org.example.enumerados.EstadoJuego;

import java.time.LocalDate;

public class JuegoEntidad {

    private Long idJuego;
    private String titulo;
    private String descripcion;
    private String desarrollador;
    private LocalDate fechaLanz;
    private double precioB;
    private int descuento;
    private String categoria;
    private ClasificacionEdad clasificacionEdad;
    private String idioma;
    private EstadoJuego estadoJuego;


    public JuegoEntidad(Long idJuego, String titulo, String descripcion, String desarrollador, LocalDate fechaLanz, double precioB, int descuento, String categoria, ClasificacionEdad clasificacionEdad, String idioma, EstadoJuego estadoJuego) {
        this.idJuego = idJuego;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.desarrollador = desarrollador;
        this.fechaLanz = fechaLanz;
        this.precioB = precioB;
        this.descuento = descuento;
        this.categoria = categoria;
        this.clasificacionEdad = clasificacionEdad;
        this.idioma = idioma;
        this.estadoJuego = estadoJuego;
    }

    public Long getIdJuego() {
        return idJuego;
    }


    public String getTitulo() {
        return titulo;
    }



    public String getDescripcion() {
        return descripcion;
    }



    public String getDesarrollador() {
        return desarrollador;
    }



    public LocalDate getFechaLanz() {
        return fechaLanz;
    }



    public double getPrecioB() {
        return precioB;
    }



    public int getDescuento() {
        return descuento;
    }



    public String getCategoria() {
        return categoria;
    }



    public ClasificacionEdad getClasificacionEdad() {
        return clasificacionEdad;
    }



    public String getIdioma() {
        return idioma;
    }



    public EstadoJuego getEstadoJuego() {
        return estadoJuego;
    }
}
