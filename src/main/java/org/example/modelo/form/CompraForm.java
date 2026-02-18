package org.example.modelo.form;

import org.example.enumerados.ErrorTipo;
import org.example.modelo.dto.ErrorDTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CompraForm {

    private Long idUsuario;
    private Long idJuego;
    private LocalDate fechaC;
    private String metodoPago;
    private Double precioOriginal;
    private Double precioFinal;
    private Integer descuento;

    public CompraForm(Long idUsuario, Long idJuego, LocalDate fechaC, String metodoPago, Double precioOriginal, Double precioFinal,Integer descuento) {
        this.idUsuario = idUsuario;
        this.idJuego = idJuego;
        this.fechaC = fechaC;
        this.metodoPago = metodoPago;
        this.precioOriginal = precioOriginal;
        this.precioFinal = precioFinal;
        this.descuento = descuento;
    }

    public Integer getDescuento() {
        return descuento;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public Long getIdJuego() {
        return idJuego;
    }

    public LocalDate getFechaC() {
        return fechaC;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public Double getPrecioOriginal() {
        return precioOriginal;
    }

    public Double getPrecioFinal() {
        return precioFinal;
    }

    public List<ErrorDTO> validar(){
        List<ErrorDTO> errores = new ArrayList<>();

        if (idUsuario == null){
            errores.add(new ErrorDTO("id Usuario", ErrorTipo.REQUERIDO));
        }

        if(idJuego == null){
            errores.add(new ErrorDTO("id Juego", ErrorTipo.REQUERIDO));
        }

        if(metodoPago == null || metodoPago.isBlank()){
            errores.add(new ErrorDTO("método pago", ErrorTipo.REQUERIDO));
        }

        if (precioOriginal == null || precioOriginal <0){
            errores.add(new ErrorDTO("Tiempo de juego",ErrorTipo.FORMATO_INVALIDO));
        } else if (Math.round(precioOriginal * 10) != precioOriginal * 10) {
            errores.add(new ErrorDTO("Tiempo de juego", ErrorTipo.FORMATO_INVALIDO));
        }

        if (descuento != null ){
            if(descuento <0 || descuento >100){
                errores.add(new ErrorDTO("descuento", ErrorTipo.FUERA_DE_RANGO));
            }
        }


        return errores;
    }
}
