package org.example.modelo.entidad;

import jakarta.persistence.*;
import org.example.HibernateUtil;
import org.example.enumerados.ClasificacionEdad;
import org.example.enumerados.EstadoJuego;

import javax.annotation.processing.Generated;
import java.time.LocalDate;


@Table(name = "juegos")
@Entity
public class JuegoEntidad {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idJuego;
   @Column(name = "ttulo")
    private String titulo;
   @Column(name = "descripción")
    private String descripcion;
   @Column(name = "desarrollador")
    private String desarrollador;
   @Column(name = "fecha_lanzamiento")
    private LocalDate fechaLanz;
   @Column(name = "precio_base")
    private double precioB;
   @Column(name = "descuento")
    private int descuento;
   @Column(name = "categoría")
    private String categoria;
   @Column(name = "clasificación_edad")
   private ClasificacionEdad clasificacionEdad;
    @Column(name = "idioma")
    private String idioma;
    @Column(name = "estado_juego")
    private EstadoJuego estadoJuego;


    public JuegoEntidad(Long idJuego, String titulo,
                        String descripcion, String desarrollador, LocalDate fechaLanz,
                        double precioB, int descuento, String categoria, ClasificacionEdad clasificacionEdad,
                        String idioma, EstadoJuego estadoJuego) {
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


    public static void main(String[] args) {
        var session= HibernateUtil.getSessionFactory().openSession();
        session.close();
    }
}
