package org.example.mapper;

import org.example.modelo.dto.*;
import org.example.modelo.entidad.*;

public class Mapper {

    public static UsuarioDTO mapFromUsuario(UsuarioEntidad entidad){
        if (entidad == null)
            return null;
        return  new UsuarioDTO(
            entidad.getId(),
            entidad.getNombreUsuario(),
            entidad.getEmail(),
            entidad.getNombreRealU(),
            entidad.getPais(),
            entidad.getFechaN(),
            entidad.getFechaRegis(),
            entidad.getAvatar(),
            entidad.getSaldo(),
            entidad.getEstadoCuenta() );

    }

    public static ReseniaDTO mapFromResenia(ReseniaEntidad entidad,UsuarioDTO usuario, JuegoDTO juego){
        if (entidad == null)
            return null;

        return new ReseniaDTO(
                entidad.getIdResenia(),
                usuario,
                juego,
                entidad.isRecomendado(),
                entidad.gettResenia(),
                entidad.getHorasJugadas(),
                entidad.getFechaPublicacionR(),
                entidad.getFechaEdicionR(),
                entidad.getEstado());

    }
    public static JuegoDTO mapFromJuego(JuegoEntidad entidad){
        if (entidad == null)
            return null;

        return  new JuegoDTO(
                entidad.getIdJuego(),
                entidad.getTitulo(),
                entidad.getDescripcion(),
                entidad.getDesarrollador(),
                entidad.getFechaLanz(),
                entidad.getPrecioB(),
                entidad.getDescuento(),
                entidad.getCategoria(),
                entidad.getClasificacionEdad(),
                entidad.getIdioma(),
                entidad.getEstadoJuego());

    }

    public static CompraDTO mapFromCompra(CompraEntidad entidad,UsuarioDTO usuario, JuegoDTO juego){
        if (entidad == null)
            return null;

        return new CompraDTO(
                entidad.getIdCompra(),
                (usuario),
                (juego),
                entidad.getFechaDeCompra(),
                entidad.getMetodoPago(),
                entidad.getPrecioOriginal(),
                entidad.getPrecioFinal(),
                entidad.getEstadoCompra());

    }
    public static BibliotecaDTO mapFromBiblioteca (BibliotecaEntidad entidad,UsuarioDTO usuario, JuegoDTO juego) {

        if (entidad == null)
            return null;

        return new BibliotecaDTO(
                entidad.getIdBiblio(),
                entidad.getIdUsuario(),
                (usuario),
                entidad.getIdJuego(),
                (juego),
                entidad.getFechaAdquisicion(),
                entidad.getTiempoTotalJugado(),
                entidad.getUltimaFechaJuego(),
                entidad.getEstadoInstalacion());
    }



}
