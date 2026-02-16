package org.example.modelo.dto;

import org.example.enumerados.EstadoCompra;
import org.example.enumerados.MetodoPago;

import java.time.LocalDate;
import java.util.Optional;

public class CompraDTO {

    private int idCompra;
    private UsuarioDTO usuario;
    private JuegoDTO juego;
    private LocalDate fechaDeCompra;
    private MetodoPago metodoPago;
    private double precioSinDescuento;
    private double precioDescuentoAplicado;
    private EstadoCompra estadoCompra;

    public CompraDTO(int idCompra, UsuarioDTO usuario, JuegoDTO juego, LocalDate fechaDeCompra, MetodoPago metodoPago, double precioSinDescuento, double precioDescuentoAplicado, EstadoCompra estadoCompra) {
        this.idCompra = idCompra;
        this.usuario = usuario;
        this.juego = juego;
        this.fechaDeCompra = fechaDeCompra;
        this.metodoPago = metodoPago;
        this.precioSinDescuento = precioSinDescuento;
        this.precioDescuentoAplicado = precioDescuentoAplicado;
        this.estadoCompra = estadoCompra.PENDIENTE;
    }

    public int getIdCompra() {
        return idCompra;
    }



    public UsuarioDTO getUsuario() {
        return usuario;
    }



    public JuegoDTO getJuego() {
        return juego;
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
