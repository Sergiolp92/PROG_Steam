package org.example.controlador;

import org.example.enumerados.ErrorTipo;
import org.example.enumerados.EstadoInstalacion;
import org.example.enumerados.OrdenBusquedaBiblioteca;
import org.example.excepciones.ValidationException;
import org.example.mapper.Mapper;
import org.example.modelo.dto.BibliotecaDTO;
import org.example.modelo.dto.BibliotecaEstadisticasDTO;
import org.example.modelo.dto.ErrorDTO;
import org.example.modelo.dto.JuegoDTO;
import org.example.modelo.dto.UsuarioDTO;
import org.example.modelo.entidad.BibliotecaEntidad;
import org.example.modelo.entidad.JuegoEntidad;
import org.example.modelo.form.BibliotecaForm;
import org.example.repositorios.enMemoria.BibliotecaRepo;
import org.example.repositorios.enMemoria.CompraRepo;
import org.example.repositorios.enMemoria.JuegosRepo;
import org.example.repositorios.enMemoria.UsuariosRepo;
import org.example.repositorios.interfaz.IBibliotecaRepo;
import org.example.repositorios.interfaz.ICompraRepo;
import org.example.repositorios.interfaz.IJuegosRepo;
import org.example.repositorios.interfaz.IUsuarioRepo;
import org.example.transaction.ITransactionManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class BibliotecaControlador {
    public static final int HORAS_MIN = 0;
    public static final double vALOR_MIN = 0.0;
    private final IBibliotecaRepo bibliotecaRepo;
    private final IUsuarioRepo usuarioRepo;
    private final IJuegosRepo juegosRepo;
    private final ICompraRepo compraRepo;
    private final ITransactionManager tm;


    public BibliotecaControlador(IBibliotecaRepo bibliotecaRepo, IUsuarioRepo usuarioRepo, IJuegosRepo juegosRepo, ICompraRepo compraRepo, ITransactionManager tm) {
        this.bibliotecaRepo = bibliotecaRepo;
        this.usuarioRepo = usuarioRepo;
        this.juegosRepo = juegosRepo;
        this.compraRepo = compraRepo;
        this.tm = tm;
    }



    /**
     *  Ver biblioteca personal
     * @param id
     * @param filtro
     * @return Lista de juegos en la biblioteca con sus datos de uso
     * @throws ValidationException
     */
    public List<BibliotecaDTO> verBibliotecaPersonal(long id, OrdenBusquedaBiblioteca filtro) throws ValidationException {
        List<ErrorDTO> errores = new ArrayList<>();

        var resultado = tm.inTransaction(() -> {
            var usuarioOpt = usuarioRepo.leerPorId(id);

            if (usuarioOpt.isEmpty()) {
                errores.add(new ErrorDTO("usuario", ErrorTipo.NO_ENCONTRADO));
                throw new IllegalArgumentException();
            }

            List<BibliotecaEntidad> biblioteca = bibliotecaRepo.leerTodo().stream()
                    .filter(b -> b.getIdUsuario() == id)
                    .toList();

            if (OrdenBusquedaBiblioteca.ALFABETICO.equals(filtro)) {
                biblioteca = biblioteca.stream()
                        .sorted(Comparator.comparing(b -> juegosRepo.leerPorId(b.getIdJuego()).map(JuegoEntidad::getTitulo).orElse("")))
                        .toList();
            }
            if (OrdenBusquedaBiblioteca.TIEMPO_JUEGO.equals(filtro)) {
                biblioteca = biblioteca.stream()
                        .sorted(Comparator.comparing(BibliotecaEntidad::getTiempoTotalJugado))
                        .toList();
            }
            if (OrdenBusquedaBiblioteca.ULTIMA_SESION.equals(filtro)) {
                biblioteca = biblioteca.stream()
                        .sorted(Comparator.comparing(BibliotecaEntidad::getUltimaFechaJuego))
                        .toList();
            }
            if (OrdenBusquedaBiblioteca.FECHA_ADQUISICION.equals(filtro)) {
                biblioteca = biblioteca.stream()
                        .sorted(Comparator.comparing(BibliotecaEntidad::getFechaAdquisicion))
                        .toList();
            }

            UsuarioDTO u = usuarioOpt.map(Mapper::mapFromUsuario).orElse(null);

            return biblioteca.stream()
                    .map(b -> {
                        JuegoDTO j = juegosRepo.leerPorId(b.getIdJuego()).map(Mapper::mapFromJuego).orElse(null);
                        return Mapper.mapFromBiblioteca(b, u, j);
                    })
                    .toList();
        });

        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }

        return resultado;
    }



    /**
     * Añadir juego a biblioteca
     * @param bibliotecaForm
     * @return DTO del juego añadido a la biblioteca con detalles de adquisición
     * @throws ValidationException
     */
    public BibliotecaDTO aniadirJuegoBiblio(BibliotecaForm bibliotecaForm) throws ValidationException {
        List<ErrorDTO> errores = new ArrayList<>(bibliotecaForm.validar());

        var resultado = tm.inTransaction(() -> {
            var usuarioOpt = usuarioRepo.leerPorId(bibliotecaForm.getIdUsuario());
            var juegoOpt = juegosRepo.leerPorId(bibliotecaForm.getIdJuego());

            if (usuarioOpt.isEmpty()) {
                errores.add(new ErrorDTO("usuario", ErrorTipo.NO_ENCONTRADO));
            }
            if (juegoOpt.isEmpty()) {
                errores.add(new ErrorDTO("juego", ErrorTipo.NO_ENCONTRADO));
            }

            var juegoEnBiblioteca = bibliotecaRepo.leerTodo().stream()
                    .filter(b -> b.getIdUsuario().equals(bibliotecaForm.getIdUsuario())
                            && b.getIdJuego().equals(bibliotecaForm.getIdJuego()))
                    .findFirst();

            if (juegoEnBiblioteca.isPresent()) {
                errores.add(new ErrorDTO("juego", ErrorTipo.DUPLICADO));
            }
            if (!errores.isEmpty()) {
                throw new IllegalArgumentException();
            }

            bibliotecaRepo.crear(bibliotecaForm);

            var nuevaBiblioteca = bibliotecaRepo.leerTodo().stream()
                    .filter(b -> b.getIdUsuario().equals(bibliotecaForm.getIdUsuario())
                            && b.getIdJuego().equals(bibliotecaForm.getIdJuego()))
                    .findFirst()
                    .orElse(null);

            UsuarioDTO usuario = usuarioOpt.map(Mapper::mapFromUsuario).orElse(null);
            JuegoDTO juego = juegoOpt.map(Mapper::mapFromJuego).orElse(null);

            return Mapper.mapFromBiblioteca(nuevaBiblioteca, usuario, juego);
        });

        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }

        return resultado;
    }


   /**
     * Eliminar juego de biblioteca
     * @param idUsuario
     * @param idJuego
     * @throws ValidationException
        */
    public void eliminarJuegoBiblioteca(long idUsuario, long idJuego) throws ValidationException {
        List<ErrorDTO> errores = new ArrayList<>();

        tm.inTransaction(() -> {
            var usuarioOpt = usuarioRepo.leerPorId(idUsuario);
            if (usuarioOpt.isEmpty()) {
                errores.add(new ErrorDTO("usuario", ErrorTipo.NO_ENCONTRADO));
            }

            var juegoOpt = juegosRepo.leerPorId(idJuego);
            if (juegoOpt.isEmpty()) {
                errores.add(new ErrorDTO("juego", ErrorTipo.NO_ENCONTRADO));
            }

            var bibliotecaOpt = bibliotecaRepo.leerTodo().stream()
                    .filter(b -> b.getIdUsuario().equals(idUsuario) && b.getIdJuego().equals(idJuego))
                    .findFirst();

            if (bibliotecaOpt.isEmpty()) {
                errores.add(new ErrorDTO("biblioteca", ErrorTipo.NO_ENCONTRADO));
            }
            if (!errores.isEmpty()) {
                throw new IllegalArgumentException();
            }

            bibliotecaRepo.borrar(bibliotecaOpt.get().getIdBiblio());
            return null;
        });

        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }
    }


    /**
     * Actualizar tiempo de juego
     * @param bibliotecaForm
     * @param horas
     * @return DTO actualizado con nuevo tiempo total de juego
     * @throws ValidationException
     */
    public Optional<BibliotecaDTO> actualizarTiempoJuego(BibliotecaForm bibliotecaForm, double horas) throws ValidationException {
        List<ErrorDTO> errores = new ArrayList<>();

        if (horas <= HORAS_MIN) {
            errores.add(new ErrorDTO("horas", ErrorTipo.VALOR_INVALIDO));
        }

        var resultado = tm.inTransaction(() -> {
            var usuarioOpt = usuarioRepo.leerPorId(bibliotecaForm.getIdUsuario());
            var juegoOpt = juegosRepo.leerPorId(bibliotecaForm.getIdJuego());

            if (usuarioOpt.isEmpty()) {
                errores.add(new ErrorDTO("usuario", ErrorTipo.NO_ENCONTRADO));
            }
            if (juegoOpt.isEmpty()) {
                errores.add(new ErrorDTO("juego", ErrorTipo.NO_ENCONTRADO));
            }

            var juegoBiblioteca = bibliotecaRepo.leerTodo().stream()
                    .filter(b -> b.getIdUsuario().equals(bibliotecaForm.getIdUsuario())
                            && b.getIdJuego().equals(bibliotecaForm.getIdJuego()))
                    .findFirst();

            if (juegoBiblioteca.isEmpty()) {
                errores.add(new ErrorDTO("biblioteca", ErrorTipo.NO_ENCONTRADO));
            }
            if (!errores.isEmpty()) {
                throw new IllegalArgumentException();
            }

            float tiempoTotalJuego = (float) (juegoBiblioteca.get().getTiempoTotalJugado() + horas);

            var bibliotecaActualizada = bibliotecaRepo.actualizar(juegoBiblioteca.get().getIdBiblio(),
                    new BibliotecaForm(
                            bibliotecaForm.getIdUsuario(),
                            bibliotecaForm.getIdJuego(),
                            juegoBiblioteca.get().getFechaAdquisicion(),
                            tiempoTotalJuego,
                            LocalDate.now()
                    ));

            UsuarioDTO usuario = usuarioOpt.map(Mapper::mapFromUsuario).orElse(null);
            JuegoDTO juego = juegoOpt.map(Mapper::mapFromJuego).orElse(null);

            return bibliotecaActualizada.map(b -> Mapper.mapFromBiblioteca(b, usuario, juego));
        });

        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }

        return resultado;
    }


    /**
     * Consultar última sesión
     * @param idUsuario
     * @param idJuego
     * @return DTO con detalles de la última sesión o mensaje "Nunca jugado"
     * @throws ValidationException
     */
    public Optional<BibliotecaDTO> consultarUltimaSesion(Long idUsuario, Long idJuego) throws ValidationException {
        List<ErrorDTO> errores = new ArrayList<>();

        var resultado = tm.inTransaction(() -> {
            var usuarioOpt = usuarioRepo.leerPorId(idUsuario);
            var juegoOpt = juegosRepo.leerPorId(idJuego);

            if (usuarioOpt.isEmpty()) {
                errores.add(new ErrorDTO("usuario", ErrorTipo.NO_ENCONTRADO));
            }
            if (juegoOpt.isEmpty()) {
                errores.add(new ErrorDTO("juego", ErrorTipo.NO_ENCONTRADO));
            }
            if (!errores.isEmpty()) {
                throw new IllegalArgumentException();
            }

            var juegoBiblioteca = bibliotecaRepo.leerTodo().stream()
                    .filter(b -> b.getIdUsuario().equals(idUsuario) && b.getIdJuego().equals(idJuego))
                    .findFirst();

            var usuarioDto = Mapper.mapFromUsuario(usuarioOpt.orElse(null));
            var juegoDto = Mapper.mapFromJuego(juegoOpt.orElse(null));

            return Optional.ofNullable(Mapper.mapFromBiblioteca(juegoBiblioteca.orElse(null), usuarioDto, juegoDto));
        });

        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }

        return resultado;
    }


    /**
     * Ver estadísticas de biblioteca
     * @param idUsuario
     * @return DTO con estadísticas generales de la biblioteca del usuario
     * @throws ValidationException
     */
    public BibliotecaEstadisticasDTO verEstadisticasBiblioteca(long idUsuario) throws ValidationException {
        List<ErrorDTO> errores = new ArrayList<>();


        var usuarioOpt = usuarioRepo.leerPorId(idUsuario);
        if (usuarioOpt.isEmpty()) {
            errores.add(new ErrorDTO("usuario", ErrorTipo.NO_ENCONTRADO));
            throw new ValidationException(errores);
        }


        List<BibliotecaEntidad> bibliotecasUsuario = bibliotecaRepo.leerTodo().stream()
                .filter(b -> b.getIdUsuario().equals(idUsuario))
                .toList();


        int totalJuegos = bibliotecasUsuario.size();

        float horasTotales = bibliotecasUsuario.stream()
                .map(BibliotecaEntidad::getTiempoTotalJugado).reduce(0f,(acumulado,tiempo) -> acumulado + tiempo)
                ;

        int juegosInstalados = (int) bibliotecasUsuario.stream()
                .filter(b -> b.getEstadoInstalacion() == EstadoInstalacion.INSTALADO).toList().size();



        String juegoMasJugado = bibliotecasUsuario.stream()
                .max(Comparator.comparing(BibliotecaEntidad::getTiempoTotalJugado))
                        .map(b -> juegosRepo.leerPorId(b.getIdJuego()).map(JuegoEntidad::getTitulo).orElse("Desconocido"))
                .orElse("Ninguno");


        double valorTotal = bibliotecasUsuario.stream()
                .mapToDouble(b -> juegosRepo.leerPorId(b.getIdJuego())
                        .map(JuegoEntidad::getPrecioB)
                        .orElse(vALOR_MIN))
                .sum();


        int juegosNuncaJugados =bibliotecasUsuario.stream()
                .filter(b -> b.getTiempoTotalJugado() == HORAS_MIN).toList().size();


        return new BibliotecaEstadisticasDTO(idUsuario,totalJuegos, horasTotales, juegosInstalados,
                 juegoMasJugado, valorTotal, juegosNuncaJugados);
    }


}
