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

    public static ReseniaDTO mapFromResenia(ReseniaEntidad entidad,UsuarioEntidad usuario, JuegoEntidad juego){
        if (entidad == null)
            return null;

        return new ReseniaDTO(
                entidad.getIdResenia(),
                Mapper.mapFromUsuario(usuario),
                Mapper.mapFromJuego(juego),
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

    public static CompraDTO mapFromCompra(CompraEntidad entidad,UsuarioEntidad usuario, JuegoEntidad juego){
        if (entidad == null)
            return null;

        return new CompraDTO(
                entidad.getIdCompra(),
                Mapper.mapFromUsuario(usuario),
                Mapper.mapFromJuego(juego),
                entidad.getFechaDeCompra(),
                entidad.getMetodoPago(),
                entidad.getPrecioSinDescuento(),
                entidad.getPrecioDescuentoAplicado(),
                entidad.getEstadoCompra());

    }
    public static BibliotecaDTO mapFromBiblioteca (BibliotecaEntidad entidad,UsuarioEntidad usuario, JuegoEntidad juego) {

        if (entidad == null)
            return null;

        return new BibliotecaDTO(
                entidad.getIdBiblio(),
                entidad.getIdUsuario(),
                Mapper.mapFromUsuario(usuario),
                entidad.getIdJuego(),
                Mapper.mapFromJuego(juego),
                entidad.getFechaAdquisicion(),
                entidad.getTiempoTotalJugado(),
                entidad.getUltimaFechaJuego(),
                entidad.getEstadoInstalacion());
    }

}
