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
    private double precioSinDescuento;
    private double precioDescuentoAplicado;
    private EstadoCompra estadoCompra;

    public CompraEntidad(Long idCompra, LocalDate fechaDeCompra, MetodoPago metodoPago, double precioSinDescuento, double precioDescuentoAplicado, EstadoCompra estadoCompra) {
        this.idCompra = idCompra;
        this.fechaDeCompra = fechaDeCompra;
        this.metodoPago = metodoPago;
        this.precioSinDescuento = precioSinDescuento;
        this.precioDescuentoAplicado = precioDescuentoAplicado;
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



    public double getPrecioSinDescuento() {
        return precioSinDescuento;
    }



    public double getPrecioDescuentoAplicado() {
        return precioDescuentoAplicado;
    }



    public EstadoCompra getEstadoCompra() {
        return estadoCompra;
    }

}
