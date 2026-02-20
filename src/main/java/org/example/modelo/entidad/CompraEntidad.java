package org.example.modelo.entidad;

import org.example.enumerados.EstadoCompra;
import org.example.enumerados.MetodoPago;
import org.example.modelo.dto.JuegoDTO;
import org.example.modelo.dto.UsuarioDTO;

import java.time.LocalDate;
import java.util.Optional;

public class CompraEntidad {
    private Long idCompra;
    private LocalDate fechaDeCompra;
    private MetodoPago metodoPago;
    private double precioOriginal;
    private double precioFinal;
    private EstadoCompra estadoCompra;

    public CompraEntidad(Long idCompra, LocalDate fechaDeCompra, MetodoPago metodoPago, double precioOriginal, double precioFinal, EstadoCompra estadoCompra) {
        this.idCompra = idCompra;
        this.fechaDeCompra = fechaDeCompra;
        this.metodoPago = metodoPago;
        this.precioOriginal = precioOriginal;
        this.precioFinal = precioFinal;
        this.estadoCompra = estadoCompra;
    }

    public Long getIdCompra() {
        return idCompra;
    }



    public LocalDate getFechaDeCompra() {
        return fechaDeCompra;
    }



    public MetodoPago getMetodoPago() {
        return metodoPago;
    }



    public double precioOriginal() {
        return precioOriginal;
    }



    public double precioFinal() {
        return precioFinal;
    }



    public EstadoCompra getEstadoCompra() {
        return estadoCompra;
    }

}
