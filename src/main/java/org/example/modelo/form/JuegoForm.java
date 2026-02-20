package org.example.modelo.form;

import org.example.enumerados.ClasificacionEdad;
import org.example.enumerados.ErrorTipo;
import org.example.enumerados.EstadoJuego;
import org.example.modelo.dto.ErrorDTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JuegoForm {

    public String categoria;
    private String titulo;
    private String desarrollador;
    private LocalDate fechaL;
    private Double precioB;
    private ClasificacionEdad clasiEdad;
    private String descripcion;
    private Integer descuento;


    private EstadoJuego estadoJuego;

    private String idioma;

    public JuegoForm(String titulo, String desarrollador, LocalDate fechaL, Double precioB, ClasificacionEdad clasiEdad,String descripcion,Integer descuentoActual, String categoria,String idioma,EstadoJuego estadoJuego) {
        this.titulo = titulo;
        this.desarrollador = desarrollador;
        this.fechaL = fechaL;
        this.precioB = precioB;
        this.clasiEdad = clasiEdad;
        this.descripcion = descripcion;
        this.descuento = descuento;
        this.categoria = categoria;
        this.idioma = idioma;
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

    public ClasificacionEdad getClasiEdad() {
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
        }else if ((titulo.length() < 1) || (titulo.length() > 100)){
            errores.add(new ErrorDTO("titulo", ErrorTipo.FORMATO_INVALIDO));
        }


        if (descripcion != null && descripcion.length() > 200) {
            errores.add(new ErrorDTO("descripción", ErrorTipo.VALOR_DEMASIADO_ALTO));
        }


        if (desarrollador == null || titulo.isBlank()){
            errores.add(new ErrorDTO("desarrollador", ErrorTipo.REQUERIDO));
        } else if (desarrollador.length() < 2 || desarrollador.length() > 100 ) {
            errores.add(new ErrorDTO("desarrollador", ErrorTipo.FORMATO_INVALIDO));
        }


        if (precioB == null || precioB.isBlank()) {
            errores.add(new ErrorDTO("precio", ErrorTipo.REQUERIDO));
        } else if (!precioB.matches(regex)) {
            errores.add(new ErrorDTO("precio", ErrorTipo.FORMATO_INVALIDO));
        } else {
            double precio = Double.parseDouble(precioB);
            if (precio < 0 || precio > 999.99) {
                errores.add(new ErrorDTO("precio", ErrorTipo.FORMATO_INVALIDO));
            }
        }

        if (descuento != null) {
            if (descuento < 0) {
                errores.add(new ErrorDTO("descuento", ErrorTipo.VALOR_DEMASIADO_BAJO));
            } else if (descuento > 100) {
                errores.add(new ErrorDTO("descuento", ErrorTipo.VALOR_DEMASIADO_ALTO));
            }
        }

        if (idioma !=null && idioma.length()>200){
            errores.add(new ErrorDTO("idioma", ErrorTipo.FORMATO_INVALIDO));

        }

        return errores;


    }
}
