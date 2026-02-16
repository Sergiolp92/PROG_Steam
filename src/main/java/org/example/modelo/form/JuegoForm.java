package org.example.modelo.form;

import org.example.enumerados.ClasificacionEdad;
import org.example.enumerados.ErrorTipo;
import org.example.modelo.dto.ErrorDTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JuegoForm {
    
    private String titulo;
    private String desarrollador;
    private String fechaL;
    private String precioB;
    private String clasiEdad;
    private String descripcion;
    private Integer descuento;
    private String idioma;

    public JuegoForm(String titulo, String desarrollador, String fechaL, String precioB, String clasiEdad,String descripcion,Integer descuentoActual,String idioma) {
        this.titulo = titulo;
        this.desarrollador = desarrollador;
        this.fechaL = fechaL;
        this.precioB = precioB;
        this.clasiEdad = clasiEdad;
        this.descripcion = descripcion;
        this.descuento = descuento;
        this.idioma = idioma;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDesarrollador() {
        return desarrollador;
    }

    public String getFechaL() {
        return fechaL;
    }

    public String getPrecioB() {
        return precioB;
    }

    public String getClasiEdad() {
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
