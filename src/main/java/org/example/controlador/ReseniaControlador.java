package org.example.controlador;

import org.example.enumerados.BusquedaReseniaPoN;
import org.example.enumerados.ErrorTipo;
import org.example.enumerados.EstadoResenia;
import org.example.enumerados.OrdenBusquedaResenia;
import org.example.excepciones.ValidationException;
import org.example.mapper.Mapper;
import org.example.modelo.dto.ErrorDTO;
import org.example.modelo.dto.ReseniaDTO;
import org.example.modelo.entidad.ReseniaEntidad;
import org.example.modelo.form.ReseniaForm;
import org.example.repositorios.enMemoria.ReseniasRepo;
import org.example.repositorios.interfaz.IBibliotecaRepo;
import org.example.repositorios.interfaz.IJuegosRepo;
import org.example.repositorios.interfaz.IReseniasRepo;
import org.example.repositorios.interfaz.IUsuarioRepo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReseniaControlador {
    private IReseniasRepo repoResenia;
    private IBibliotecaRepo repoBiblioteca;
    private IJuegosRepo repoJuego;
    private IUsuarioRepo repoUsuario;

    public ReseniaControlador(IReseniasRepo repoResenia, IBibliotecaRepo repoBiblioteca, IJuegosRepo repoJuego, IUsuarioRepo repoUsuario) {
        this.repoResenia = repoResenia;
        this.repoBiblioteca = repoBiblioteca;
        this.repoJuego = repoJuego;
        this.repoUsuario = repoUsuario;
    }

    /* Escribir reseña
     Descripción: Crear una nueva reseña para un juego que el usuario posee
     Entrada: ID del usuario, ID del juego, recomendado (boolean), texto de reseña
     Salida: Reseña creada exitosamente o lista de errores
     Validaciones: Usuario propietario del juego, no duplicada, texto válido*/
    public ReseniaDTO escribirResenia(Long idUsuario, Long idJuego, boolean recomendado, String texto) throws ValidationException {
        List<ErrorDTO> errores = new ArrayList<>();

        var reseniaOpt = repoResenia.leerPorIdUsuarioYIdJuego(idUsuario, idJuego);

        if (reseniaOpt.isPresent()) {
            errores.add(new ErrorDTO("Reseña", ErrorTipo.DUPLICADO));
        }

        if (texto == null || texto.length() < 40 || texto.length() > 2000) {
            errores.add(new ErrorDTO("Texto", ErrorTipo.VALOR_INVALIDO));
        }
        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }

        var bibliotecaOpt = repoBiblioteca.leerJuegoUsuario(idUsuario, idJuego);
        if (bibliotecaOpt.isEmpty()) {
            errores.add(new ErrorDTO("Biblioteca", ErrorTipo.NO_ENCONTRADO));
            throw new ValidationException(errores);
        }

        var reseniaC = repoResenia.crear(new ReseniaForm(idUsuario, idJuego, recomendado, texto, LocalDate.now(), LocalDate.now(), (int) bibliotecaOpt.get().getTiempoTotalJugado(), EstadoResenia.PUBLICADA)).orElse(null);

        var usuarioOpt = repoUsuario.leerPorId(idUsuario);
        var juegoOpt = repoJuego.leerPorId(idJuego);
        return Mapper.mapFromResenia(reseniaC, Mapper.mapFromUsuario(usuarioOpt.orElse(null)), Mapper.mapFromJuego(juegoOpt.orElse(null)));
    }

    /*
Eliminar reseña
Descripción: Cambiar el estado de una reseña a eliminada
Entrada: ID de reseña, ID del usuario (para verificar pertenencia)
Salida: Confirmación de eliminación
Validaciones: Reseña existe, pertenece al usuario*/
    public Optional<ReseniaDTO> eliminarResenia(Long idResenia, Long idUsuario) throws ValidationException {
        List<ErrorDTO> errores = new ArrayList<>();

        var reseniaOpt = repoResenia.leerPorId(idResenia);
        if (reseniaOpt.isEmpty()) {
            errores.add(new ErrorDTO("Reseña", ErrorTipo.NO_ENCONTRADO));
            throw new ValidationException(errores);
        }

        if (reseniaOpt.get().getUsuarioId() != idUsuario) {
            errores.add(new ErrorDTO("Reseña", ErrorTipo.NO_COINCIDE));
            throw new ValidationException(errores);
        }
        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }

        reseniaOpt = repoResenia.actualizar(idResenia, new ReseniaForm(reseniaOpt.get().getUsuarioId(), reseniaOpt.get().getJuegoId(), reseniaOpt.get().isRecomendado(), reseniaOpt.get().gettResenia(), reseniaOpt.get().getFechaEdicionR(),
                reseniaOpt.get().getFechaPublicacionR(), reseniaOpt.get().getHorasJugadas(), EstadoResenia.ELIMINADA));

        var usuarioOpt = repoUsuario.leerPorId(idUsuario);
        var juegoOpt = repoJuego.leerPorId(reseniaOpt.get().getJuegoId());
        return Optional.ofNullable(Mapper.mapFromResenia(reseniaOpt.orElse(null), Mapper.mapFromUsuario(usuarioOpt.orElse(null)), Mapper.mapFromJuego(juegoOpt.orElse(null))));
    }

    /*
    Ver reseñas de un juego
    Descripción: Listar todas las reseñas publicadas de un juego específico
    Entrada: ID del juego, filtro opcional (positivas/negativas), orden (recientes/útiles)
    Salida: Lista de reseñas con estadísticas generales
    Datos mostrados: Autor, recomendado, texto, horas jugadas, fecha*/
    public List<ReseniaDTO> verReseniaJuego(long id, BusquedaReseniaPoN valoracion, OrdenBusquedaResenia filtro) throws ValidationException {
        List<ErrorDTO> errores = new ArrayList<>();
        var juegoOpt = repoJuego.leerPorId(id);

        if (juegoOpt.isEmpty()) {
            errores.add(new ErrorDTO("Juego", ErrorTipo.NO_ENCONTRADO));
        }
        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }

        var resenias = repoResenia.leerTodo().stream().filter(r -> r.getJuegoId() == id);


        if (valoracion.equals(BusquedaReseniaPoN.POSITIVA)) {
            ;
            resenias = resenias.filter(r -> r.isRecomendado());
        }
        if (valoracion.equals(BusquedaReseniaPoN.NEGATIVA)) {
            resenias = resenias.filter(r -> !r.isRecomendado());
        }


        if (filtro.equals(OrdenBusquedaResenia.RECIENTES)) {
            resenias = resenias.filter(r -> r.isRecomendado());
        }
        if (filtro.equals(OrdenBusquedaResenia.UTILES)) {
            resenias = resenias.filter(r -> !r.isRecomendado());
        }


        var juego = repoJuego.leerPorId(id).orElse(null);
        var usuario = repoUsuario.leerPorId(id).orElse(null);

        return resenias.map(r -> Mapper.mapFromResenia(r, Mapper.mapFromUsuario(usuario), Mapper.mapFromJuego(juego))).toList();
    }
    /*
    Ocultar reseña
    Descripción: Cambiar la visibilidad de una reseña a oculta
    Entrada: ID de reseña, ID del usuario
    Salida: Confirmación de ocultación
    Validaciones: Reseña existe, pertenece al usuario, está publicada*/
    public Optional<ReseniaDTO> ocultarResenia(Long idResenia, Long idUsuario) throws ValidationException {
        List<ErrorDTO> errores = new ArrayList<>();

        var reseniaOpt = repoResenia.leerPorId(idResenia);
        if (reseniaOpt.isEmpty()) {
            errores.add(new ErrorDTO("Reseña", ErrorTipo.NO_ENCONTRADO));
            throw new ValidationException(errores);
        }

        if (reseniaOpt.get().getUsuarioId() != idUsuario) {
            errores.add(new ErrorDTO("Reseña", ErrorTipo.NO_COINCIDE));
            throw new ValidationException(errores);
        }

        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }

        reseniaOpt = repoResenia.actualizar(idResenia, new ReseniaForm(reseniaOpt.get().getUsuarioId(), reseniaOpt.get().getJuegoId(), reseniaOpt.get().isRecomendado(), reseniaOpt.get().gettResenia(), reseniaOpt.get().getFechaEdicionR(),
                reseniaOpt.get().getFechaPublicacionR(), reseniaOpt.get().getHorasJugadas(), EstadoResenia.OCULTA));

        var usuarioOpt = repoUsuario.leerPorId(idUsuario);
        var juegoOpt = repoJuego.leerPorId(reseniaOpt.get().getJuegoId());
        return Optional.ofNullable(Mapper.mapFromResenia(reseniaOpt.orElse(null), Mapper.mapFromUsuario(usuarioOpt.orElse(null)), Mapper.mapFromJuego(juegoOpt.orElse(null))));
    }
    /*
    Ver reseñas de un usuario
    Descripción: Listar todas las reseñas escritas por un usuario específico
    Entrada: ID del usuario,
    Salida: Lista de reseñas del usuario con estadísticas
    Datos mostrados: Juego, recomendado, texto (extracto), fecha, horas jugadas al momento*/
    public List<ReseniaDTO> verReseniaUsuario(long id, EstadoResenia estado) throws ValidationException {
        List<ErrorDTO> errores = new ArrayList<>();
        var usuarioOpt = repoUsuario.leerPorId(id);

        if (usuarioOpt.isEmpty()) {
            errores.add(new ErrorDTO("Usuario", ErrorTipo.NO_ENCONTRADO));
        }
        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }

        var resenias = repoResenia.leerTodo().stream().filter(r -> r.getUsuarioId() == id);

        if (estado.equals(EstadoResenia.PUBLICADA)) {
            resenias = resenias.filter(r -> r.getEstado() == EstadoResenia.PUBLICADA);
        }
        if (estado.equals(EstadoResenia.ELIMINADA)) {
            resenias = resenias.filter(r -> r.getEstado() == EstadoResenia.ELIMINADA);

        }
        if (estado.equals(EstadoResenia.OCULTA)) {
            resenias = resenias.filter(r -> r.getEstado() == EstadoResenia.OCULTA);
        }


        return resenias.map(r -> Mapper.mapFromResenia(r, Mapper.mapFromUsuario(usuarioOpt.orElse(null)), null)).toList();
    }
}
