package org.example.modelo.form;

import org.example.enumerados.ClasificacionEdad;
import org.example.enumerados.ErrorTipo;
import org.example.enumerados.EstadoJuego;
import org.example.modelo.dto.ErrorDTO;

import java.util.ArrayList;
import java.util.List;

public class BusquedaJuegoForm {
    private String titulo;
    private String categoria;
    private Double precio;
    private ClasificacionEdad clasificacion;
    private EstadoJuego estado;
    

    public BusquedaJuegoForm(String titulo, String categoria, Double precio,
             ClasificacionEdad clasificacion, EstadoJuego estado) {
        this.titulo = titulo;
        this.categoria = categoria;
        this.precio = precio;
        this.clasificacion = clasificacion;
        this.estado = estado;
    }

    public String getTitulo() {
        return titulo;
    }
    
    public String getCategoria() {
        return categoria;
    }
    
    public Double getPrecio() {
        return precio;
    }
    
    public ClasificacionEdad getClasificacion() {
        return clasificacion;
    }
    
    public EstadoJuego getEstado() {
        return estado;
    }


    public List<ErrorDTO> validar() {

        List<ErrorDTO> errores = new ArrayList<>();

        if ((titulo == null || titulo.isEmpty()) && (categoria == null || categoria.isEmpty()) && precio == null && clasificacion == null && estado == null) {
            errores.add(new ErrorDTO("busqueda", ErrorTipo.BUSQUEDA_INVALIDA));

        }
        
        return errores;
    }
   
}
