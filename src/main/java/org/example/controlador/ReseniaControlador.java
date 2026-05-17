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
import org.example.transaction.ITransactionManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class ReseniaControlador {
    public static final int TEXTO_MIN = 40;
    public static final int TEXTO_MAX = 2000;
    private IReseniasRepo repoResenia;
    private IBibliotecaRepo repoBiblioteca;
    private IJuegosRepo repoJuego;
    private IUsuarioRepo repoUsuario;
    private ITransactionManager tm;

     public ReseniaControlador(IReseniasRepo repoResenia, IBibliotecaRepo repoBiblioteca, IJuegosRepo repoJuego, IUsuarioRepo repoUsuario, ITransactionManager tm) {
        this.repoResenia = repoResenia;
        this.repoBiblioteca = repoBiblioteca;
        this.repoJuego = repoJuego;
        this.repoUsuario = repoUsuario;
        this.tm = tm;
    }



    /**
     * Crear una nueva reseña para un juego que el usuario posee
     * @param idUsuario
     * @param idJuego
     * @param recomendado
     * @param texto
     * @return Reseña creada exitosamente o lista de errores
     * @throws ValidationException
     */
    public ReseniaDTO escribirResenia(Long idUsuario, Long idJuego, boolean recomendado, String texto) throws ValidationException {
        List<ErrorDTO> errores = new ArrayList<>();

        if (texto == null || texto.length() < TEXTO_MIN || texto.length() > TEXTO_MAX) {
            errores.add(new ErrorDTO("Texto", ErrorTipo.VALOR_INVALIDO));
        }
        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }

        var resultado = tm.inTransaction(() -> {
            var reseniaOpt = repoResenia.leerPorIdUsuarioYIdJuego(idUsuario, idJuego);
            if (reseniaOpt.isPresent()) {
                errores.add(new ErrorDTO("Reseña", ErrorTipo.DUPLICADO));
            }

            var bibliotecaOpt = repoBiblioteca.leerJuegoUsuario(idUsuario, idJuego);
            if (bibliotecaOpt.isEmpty()) {
                errores.add(new ErrorDTO("Biblioteca", ErrorTipo.NO_ENCONTRADO));
            }

            if (!errores.isEmpty()) {
                throw new IllegalArgumentException();
            }

            var reseniaC = repoResenia.crear(new ReseniaForm(
                    idUsuario, idJuego, recomendado, texto,
                    LocalDate.now(), LocalDate.now(),
                    (int) bibliotecaOpt.get().getTiempoTotalJugado(),
                    EstadoResenia.PUBLICADA)).orElse(null);

            var usuarioOpt = repoUsuario.leerPorId(idUsuario);
            var juegoOpt = repoJuego.leerPorId(idJuego);

            return Mapper.mapFromResenia(reseniaC,
                    Mapper.mapFromUsuario(usuarioOpt.orElse(null)),
                    Mapper.mapFromJuego(juegoOpt.orElse(null)));
        });

        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }

        return resultado;
    }



    /**
     *  Cambiar el estado de una reseña a eliminada
     * @param idResenia
     * @param idUsuario
     * @return
     * @throws ValidationException
     */
    public Optional<ReseniaDTO> eliminarResenia(Long idResenia, Long idUsuario) throws ValidationException {
        List<ErrorDTO> errores = new ArrayList<>();

        var resultado = tm.inTransaction(() -> {
            var reseniaOpt = repoResenia.leerPorId(idResenia);

            if (reseniaOpt.isEmpty()) {
                errores.add(new ErrorDTO("Reseña", ErrorTipo.NO_ENCONTRADO));
                throw new IllegalArgumentException();
            }
            if (!idUsuario.equals(reseniaOpt.get().getUsuarioId())) {
                errores.add(new ErrorDTO("Reseña", ErrorTipo.NO_COINCIDE));
                throw new IllegalArgumentException();
            }

            var reseniaActualizada = repoResenia.actualizar(idResenia, new ReseniaForm(
                    reseniaOpt.get().getUsuarioId(),
                    reseniaOpt.get().getJuegoId(),
                    reseniaOpt.get().isRecomendado(),
                    reseniaOpt.get().gettResenia(),
                    reseniaOpt.get().getFechaEdicionR(),
                    reseniaOpt.get().getFechaPublicacionR(),
                    reseniaOpt.get().getHorasJugadas(),
                    EstadoResenia.ELIMINADA));

            var usuarioOpt = repoUsuario.leerPorId(idUsuario);
            var juegoOpt = repoJuego.leerPorId(reseniaOpt.get().getJuegoId());

            return Optional.ofNullable(Mapper.mapFromResenia(
                    reseniaActualizada.orElse(null),
                    Mapper.mapFromUsuario(usuarioOpt.orElse(null)),
                    Mapper.mapFromJuego(juegoOpt.orElse(null))));
        });

        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }

        return resultado;
    }


    /**
     * Listar todas las reseñas publicadas de un juego específico
     * @param id
     * @param valoracion
     * @param filtro
     * @return Lista de reseñas con estadísticas generales
     * @throws ValidationException
     */
    public List<ReseniaDTO> verReseniaJuego(long id, BusquedaReseniaPoN valoracion, OrdenBusquedaResenia filtro) throws ValidationException {
        List<ErrorDTO> errores = new ArrayList<>();

        var resultado = tm.inTransaction(() -> {
            var juegoOpt = repoJuego.leerPorId(id);

            if (juegoOpt.isEmpty()) {
                errores.add(new ErrorDTO("Juego", ErrorTipo.NO_ENCONTRADO));
                throw new IllegalArgumentException();
            }

            var resenias = repoResenia.leerTodo().stream()
                    .filter(r -> r.getJuegoId() == id)
                    .filter(r -> r.getEstado() != EstadoResenia.ELIMINADA && r.getEstado() != EstadoResenia.OCULTA);

            if (BusquedaReseniaPoN.POSITIVA.equals(valoracion)) {
                resenias = resenias.filter(r -> r.isRecomendado());
            }
            if (BusquedaReseniaPoN.NEGATIVA.equals(valoracion)) {
                resenias = resenias.filter(r -> !r.isRecomendado());
            }

            if (OrdenBusquedaResenia.RECIENTES.equals(filtro)) {
                resenias = resenias.sorted(Comparator.comparing(ReseniaEntidad::getFechaPublicacionR).reversed());
            }

            return resenias.map(r -> {
                var u = repoUsuario.leerPorId(r.getUsuarioId()).orElse(null);
                var j = repoJuego.leerPorId(r.getJuegoId()).orElse(null);
                return Mapper.mapFromResenia(r, Mapper.mapFromUsuario(u), Mapper.mapFromJuego(j));
            }).toList();
        });

        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }

        return resultado;
    }


    /**
     * Cambiar la visibilidad de una reseña a oculta
     * @param idResenia
     * @param idUsuario
     * @return Confirmación de ocultación
     * @throws ValidationException
     */
    public Optional<ReseniaDTO> ocultarResenia(Long idResenia, Long idUsuario) throws ValidationException {
        List<ErrorDTO> errores = new ArrayList<>();

        var resultado = tm.inTransaction(() -> {
            var reseniaOpt = repoResenia.leerPorId(idResenia);

            if (reseniaOpt.isEmpty()) {
                errores.add(new ErrorDTO("Reseña", ErrorTipo.NO_ENCONTRADO));
                throw new IllegalArgumentException();
            }
            if (!idUsuario.equals(reseniaOpt.get().getUsuarioId())) {
                errores.add(new ErrorDTO("Reseña", ErrorTipo.NO_COINCIDE));
                throw new IllegalArgumentException();
            }

            var reseniaActualizada = repoResenia.actualizar(idResenia, new ReseniaForm(
                    reseniaOpt.get().getUsuarioId(),
                    reseniaOpt.get().getJuegoId(),
                    reseniaOpt.get().isRecomendado(),
                    reseniaOpt.get().gettResenia(),
                    reseniaOpt.get().getFechaEdicionR(),
                    reseniaOpt.get().getFechaPublicacionR(),
                    reseniaOpt.get().getHorasJugadas(),
                    EstadoResenia.OCULTA));

            var usuarioOpt = repoUsuario.leerPorId(idUsuario);
            var juegoOpt = repoJuego.leerPorId(reseniaOpt.get().getJuegoId());

            return Optional.ofNullable(Mapper.mapFromResenia(
                    reseniaActualizada.orElse(null),
                    Mapper.mapFromUsuario(usuarioOpt.orElse(null)),
                    Mapper.mapFromJuego(juegoOpt.orElse(null))));
        });

        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }

        return resultado;
    }


    /**
     * Listar todas las reseñas escritas por un usuario específico
     * @param id
     * @param estado
     * @return Lista de reseñas del usuario con estadísticas
     * @throws ValidationException
     */
    public List<ReseniaDTO> verReseniaUsuario(long id, EstadoResenia estado) throws ValidationException {
        List<ErrorDTO> errores = new ArrayList<>();

        var resultado = tm.inTransaction(() -> {
            var usuarioOpt = repoUsuario.leerPorId(id);

            if (usuarioOpt.isEmpty()) {
                errores.add(new ErrorDTO("Usuario", ErrorTipo.NO_ENCONTRADO));
                throw new IllegalArgumentException();
            }

            var resenias = repoResenia.leerTodo().stream()
                    .filter(r -> r.getUsuarioId() == id);

            if (EstadoResenia.PUBLICADA.equals(estado)) {
                resenias = resenias.filter(r -> r.getEstado() == EstadoResenia.PUBLICADA);
            }
            if (EstadoResenia.ELIMINADA.equals(estado)) {
                resenias = resenias.filter(r -> r.getEstado() == EstadoResenia.ELIMINADA);
            }
            if (EstadoResenia.OCULTA.equals(estado)) {
                resenias = resenias.filter(r -> r.getEstado() == EstadoResenia.OCULTA);
            }

            return resenias.map(r -> Mapper.mapFromResenia(r,
                            Mapper.mapFromUsuario(usuarioOpt.orElse(null)), null))
                    .toList();
        });

        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }

        return resultado;
    }
}
