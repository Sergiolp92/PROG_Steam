package org.example.modelo.dto;

public class BibliotecaEstadisticasDTO {
    private long idUsuario;
    private int totalJuegos;
    private float horasTotales;
    private int juegosInstalados;
    private String juegoMasJugado;
    private double valorTotal;
    private int juegosNuncaJugados;



    public BibliotecaEstadisticasDTO(long idUsuario, int totalJuegos, float horasTotales, int juegosInstalados,
                                     String juegoMasJugado, double valorTotal, int juegosNuncaJugados) {
        this.idUsuario = idUsuario;
        this.totalJuegos = totalJuegos;
        this.horasTotales = horasTotales;
        this.juegosInstalados = juegosInstalados;
        this.juegoMasJugado = juegoMasJugado;
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

    public String getJuegoMasJugado() {
        return juegoMasJugado;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public int getJuegosNuncaJugados() {
        return juegosNuncaJugados;
    }
}
