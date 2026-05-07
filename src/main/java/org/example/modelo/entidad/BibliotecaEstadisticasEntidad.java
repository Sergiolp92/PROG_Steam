package org.example.modelo.entidad;

import org.example.modelo.dto.JuegoDTO;

import java.util.Optional;

public class BibliotecaEstadisticasEntidad {
    private long idUsuario;
    private int totalJuegos;
    private float horasTotales;
    private int juegosInstalados;
    private Optional<BibliotecaEntidad> JuegoMasJugado;
    private double valorTotal;
    private int juegosNuncaJugados;

    public BibliotecaEstadisticasEntidad(long idUsuario, int totalJuegos, float horasTotales, int juegosInstalados,   Optional<BibliotecaEntidad> JuegoMasJugado, double valorTotal, int juegosNuncaJugados) {
        this.idUsuario = idUsuario;
        this.totalJuegos = totalJuegos;
        this.horasTotales = horasTotales;
        this.juegosInstalados = juegosInstalados;
        this.JuegoMasJugado = JuegoMasJugado;
        this.valorTotal = valorTotal;
        this.juegosNuncaJugados = juegosNuncaJugados;
    }

    public long getIdUsuario() {
        return idUsuario;
    }

    public int getTotalJuegos() {
        return totalJuegos;
    }

    public float getHorasTotales() {
        return horasTotales;
    }

    public int getJuegosInstalados() {
        return juegosInstalados;
    }

    public   Optional<BibliotecaEntidad> getJuegoMasJugado() {
        return JuegoMasJugado;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public int getJuegosNuncaJugados() {
        return juegosNuncaJugados;
    }
}

