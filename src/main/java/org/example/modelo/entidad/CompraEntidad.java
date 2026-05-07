package org.example.modelo.entidad;

import org.example.enumerados.EstadoCompra;
import org.example.enumerados.MetodoPago;
import org.example.modelo.dto.JuegoDTO;
import org.example.modelo.dto.UsuarioDTO;

import java.time.LocalDate;
import java.util.Optional;

public class CompraEntidad {
    private Long idCompra;
    private long idJuego;
    private long idUsuario;
    private LocalDate fechaDeCompra;
    private MetodoPago metodoPago;
    private double precioOriginal;
    private double precioFinal;
    private EstadoCompra estadoCompra;

    public CompraEntidad(Long idCompra,long idUsuario,long idJuego ,LocalDate fechaDeCompra, MetodoPago metodoPago,
                         double precioOriginal, double precioFinal, EstadoCompra estadoCompra) {
        this.idCompra = idCompra;
        this.idUsuario = idUsuario;
        this.idJuego = idJuego;
        this.fechaDeCompra = fechaDeCompra;
        this.metodoPago = metodoPago;
        this.precioOriginal = precioOriginal;
        this.precioFinal = precioFinal;
        this.estadoCompra = estadoCompra;

    }

    public Long getIdCompra() {
        return idCompra;
    }

    public long getIdUsuario() {
        return idUsuario;
    }

    public long getIdJuego() {
        return idJuego;
    }

    public LocalDate getFechaDeCompra() {
        return fechaDeCompra;
    }



    public MetodoPago getMetodoPago() {
        return metodoPago;
    }



    public double getPrecioOriginal() {
        return precioOriginal;
    }



    public double getPrecioFinal() {
        return precioFinal;
    }



    public EstadoCompra getEstadoCompra() {
        return estadoCompra;
    }

    public LocalDate getFechaC() {
        return fechaDeCompra;
    }


}
