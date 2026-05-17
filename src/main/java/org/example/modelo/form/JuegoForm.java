package org.example.modelo.form;

import org.example.enumerados.ClasificacionEdad;
import org.example.enumerados.ErrorTipo;
import org.example.enumerados.EstadoJuego;
import org.example.modelo.dto.ErrorDTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JuegoForm {

    public static final int TITULO_MIN = 1;
    public static final int TITULO_MAX = 100;
    public static final int DESCRIP_MAX = 200;
    public static final int DESA_MIN = 2;
    public static final int DESA_MAX = 100;
    public static final int PRECIO_MIN = 0;
    public static final double PRECIO_MAX = 999.99;
    public static final int IDIOMA_MAX = 200;
    private String titulo;
    private String desarrollador;
    private LocalDate fechaL;
    private Double precioB;
    private ClasificacionEdad clasiEdad;
    private String descripcion;
    private Integer descuento;
    private String categoria;
    private String idioma;
    private EstadoJuego estadoJuego;

    public JuegoForm(String titulo, String desarrollador, LocalDate fechaL, Double precioB, ClasificacionEdad clasiEdad,String descripcion,Integer descuento,String categoria, String idioma, EstadoJuego estadoJuego) {
        this.titulo = titulo;
        this.desarrollador = desarrollador;
        this.fechaL = fechaL;
        this.precioB = precioB;
        this.clasiEdad = clasiEdad;
        this.descripcion = descripcion;
        this.descuento = descuento;
        this.categoria = categoria;
        this.idioma = idioma;
        this.estadoJuego = estadoJuego;
    }

    public EstadoJuego getEstadoJuego() {
        return estadoJuego;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDesarrollador() {
        return desarrollador;
    }

    public LocalDate getFechaL() {
        return fechaL;
    }

    public Double getPrecioB() {
        return precioB;
    }

    public ClasificacionEdad  getClasiEdad() {
        return clasiEdad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Integer getDescuento() {
        return descuento;
    }

    public String getIdioma() {
        return idioma;
    }


    public List<ErrorDTO> validar() {

        String regex = "^(\\d+)(\\.\\d{1,2})?$";
        List<ErrorDTO> errores = new ArrayList<>();
        


        if (titulo == null || titulo.isBlank()){
            errores.add(new ErrorDTO("titulo", ErrorTipo.REQUERIDO));
        }else if ((titulo.length() < TITULO_MIN) || (titulo.length() > TITULO_MAX)){
            errores.add(new ErrorDTO("titulo", ErrorTipo.FORMATO_INVALIDO));
        }


        if (descripcion != null && descripcion.length() > DESCRIP_MAX) {
            errores.add(new ErrorDTO("descripción", ErrorTipo.VALOR_DEMASIADO_ALTO));
        }


        if (desarrollador == null || titulo.isBlank()){
            errores.add(new ErrorDTO("desarrollador", ErrorTipo.REQUERIDO));
        } else if (desarrollador.length() < DESA_MIN || desarrollador.length() > DESA_MAX) {
            errores.add(new ErrorDTO("desarrollador", ErrorTipo.FORMATO_INVALIDO));
        }


        if (precioB == null) {
            errores.add(new ErrorDTO("precio", ErrorTipo.REQUERIDO));
        } else if (precioB < PRECIO_MIN || precioB > PRECIO_MAX) {
            errores.add(new ErrorDTO("precio", ErrorTipo.FORMATO_INVALIDO));
        }

        if (fechaL == null){
            errores.add(new ErrorDTO("fecha lanzamiento", ErrorTipo.REQUERIDO));
        }
        if (descuento != null) {
            if (descuento < 0) {
                errores.add(new ErrorDTO("descuento", ErrorTipo.VALOR_DEMASIADO_BAJO));
            } else if (descuento > 100) {
                errores.add(new ErrorDTO("descuento", ErrorTipo.VALOR_DEMASIADO_ALTO));
            }
        }

        if (idioma == null || idioma.isBlank()) {
            errores.add(new ErrorDTO("idioma", ErrorTipo.REQUERIDO));
        } else if (idioma.length() > IDIOMA_MAX) {
            errores.add(new ErrorDTO("idioma", ErrorTipo.VALOR_DEMASIADO_ALTO));
        }

        if(clasiEdad == null){
            errores.add(new ErrorDTO("clasificacion edad", ErrorTipo.REQUERIDO));
        }


        return errores;


    }


}
