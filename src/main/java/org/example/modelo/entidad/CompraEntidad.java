package org.example.modelo.entidad;

import org.example.enumerados.EstadoCompra;
import org.example.enumerados.MetodoPago;
import org.example.modelo.dto.JuegoDTO;
import org.example.modelo.dto.UsuarioDTO;

import java.time.LocalDate;
import java.util.Optional;

public class CompraEntidad {
    private int idCompra;
    private LocalDate fechaDeCompra;
    private MetodoPago metodoPago;
    private double precioSinDescuento;
    private double precioDescuentoAplicado;
    private EstadoCompra estadoCompra;

    public int getIdCompra() {
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
