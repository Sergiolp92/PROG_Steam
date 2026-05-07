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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class BibliotecaControlador {
    private final IBibliotecaRepo bibliotecaRepo;
    private final IUsuarioRepo usuarioRepo;
    private final IJuegosRepo juegosRepo;
    private final ICompraRepo compraRepo;

    public BibliotecaControlador(IBibliotecaRepo bibliotecaRepo, IUsuarioRepo usuarioRepo, IJuegosRepo juegosRepo, ICompraRepo compraRepo) {
        this.bibliotecaRepo = bibliotecaRepo;
        this.usuarioRepo = usuarioRepo;
        this.juegosRepo = juegosRepo;
        this.compraRepo = compraRepo;
    }

    /*
    * Ver biblioteca personal
Descripción: Listar todos los juegos que posee un usuario en su biblioteca
Entrada: ID del usuario, orden opcional (alfabético, tiempo de juego, última sesión, fecha de
adquisición)
Salida: Lista de juegos en la biblioteca con sus datos de uso
Datos mostrados: Título del juego, tiempo jugado, última sesión, estado de instalación
* */

    public List<BibliotecaDTO> verBibliotecaPersonal(long id, OrdenBusquedaBiblioteca filtro) throws ValidationException {

        List<ErrorDTO> errores = new ArrayList<>();
        var usuarioOpt = usuarioRepo.leerPorId(id);

        if (usuarioOpt.isEmpty()) {
            errores.add(new ErrorDTO("usuario", ErrorTipo.NO_ENCONTRADO));
        }

        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }


        List<BibliotecaEntidad> biblioteca = bibliotecaRepo.leerTodo().stream()
                .filter(b -> b.getIdUsuario() == id)
                .toList();


        if (filtro.equals(OrdenBusquedaBiblioteca.ALFABETICO)) {
            biblioteca = biblioteca.stream()
                    .sorted(Comparator.comparing(b -> juegosRepo.leerPorId(b.getIdJuego()).map(JuegoEntidad::getTitulo).orElse("")))
                    .toList();
        }
        if (filtro.equals(OrdenBusquedaBiblioteca.TIEMPO_JUEGO)) {
            biblioteca = biblioteca.stream()
                    .sorted(Comparator.comparing(BibliotecaEntidad::getTiempoTotalJugado))
                    .toList();
        }
        if (filtro.equals(OrdenBusquedaBiblioteca.ULTIMA_SESION)) {
            biblioteca = biblioteca.stream()
                    .sorted(Comparator.comparing(BibliotecaEntidad::getUltimaFechaJuego))
                    .toList();
        }
        if (filtro.equals(OrdenBusquedaBiblioteca.FECHA_ADQUISICION)) {
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


    }

    /*
Añadir juego a biblioteca
Descripción: Agregar un juego adquirido a la biblioteca del usuario
Entrada: ID del usuario, ID del juego
Salida: Confirmación de adición a biblioteca o mensaje de error
Validaciones: Usuario existe, juego existe, no duplicado, compra verificada
*/
    public BibliotecaDTO aniadirJuegoBiblio(BibliotecaForm bibliotecaForm) throws ValidationException {
        List<ErrorDTO> errores = new ArrayList<>(bibliotecaForm.validar());
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
            throw new ValidationException(errores);
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
    }

    /*
Eliminar juego de biblioteca
Descripción: Quitar un juego de la biblioteca del usuario
Entrada: ID del usuario, ID del juego
Salida: Confirmación de eliminación o cancelación
Validaciones: Entrada existe en la biblioteca
*/
    public void eliminarJuegoBiblioteca(long idUsuario, long idJuego) throws ValidationException {
        List<ErrorDTO> errores = new ArrayList<>();


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
            throw new ValidationException(errores);
        }

        BibliotecaEntidad biblioteca = bibliotecaOpt.get();
        bibliotecaRepo.borrar(biblioteca.getIdBiblio());
    }
    /*
Actualizar tiempo de juego
Descripción: Registrar y actualizar las horas jugadas de un juego
Entrada: ID del usuario, ID del juego, horas a añadir
Salida: Nuevo tiempo total de juego
Validaciones: Biblioteca existe, horas positivas
*/
    public Optional<BibliotecaDTO> actualizarTiempoJuego(BibliotecaForm bibliotecaForm ,double horas) throws ValidationException {
        List<ErrorDTO> errores = new ArrayList<>();
        var usuarioOpt = usuarioRepo.leerPorId(bibliotecaForm.getIdUsuario());
        var juegoOpt = juegosRepo.leerPorId(bibliotecaForm.getIdJuego());
        
        if (usuarioOpt.isEmpty()) {
            errores.add(new ErrorDTO("usuario", ErrorTipo.NO_ENCONTRADO));
        }
        if (juegoOpt.isEmpty()) {
            errores.add(new ErrorDTO("juego", ErrorTipo.NO_ENCONTRADO));
        }
        if (horas <= 0) {
            errores.add(new ErrorDTO("horas", ErrorTipo.VALOR_INVALIDO));
        }
        
        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }
        
        var juegoBiblioteca = bibliotecaRepo.leerTodo().stream()
                .filter(b -> b.getIdUsuario().equals(bibliotecaForm.getIdUsuario()) && b.getIdJuego().equals(bibliotecaForm.getIdJuego()))
                .findFirst();
        
        if (juegoBiblioteca.isEmpty()) {
            errores.add(new ErrorDTO("biblioteca", ErrorTipo.NO_ENCONTRADO));
            throw new ValidationException(errores);
        }
        
        float tiempoTotalJuego = (float) (juegoBiblioteca.get().getTiempoTotalJugado() +  horas);
        
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
    }
    /*
Consultar última sesión
Descripción: Ver la última vez que se jugó a un juego específico
Entrada: ID del usuario, ID del juego
Salida: Fecha y hora de última sesión o mensaje "Nunca jugado"
Formato: "Última sesión: hace 2 días (18/01/2026 15:30)"
*/
public Optional<BibliotecaDTO> consultarUltimaSesion (Long idUsuario, Long idJuego) throws ValidationException {
    List<ErrorDTO> errores = new ArrayList<>();


    var usuarioOpt = usuarioRepo.leerPorId(idUsuario);
    var juegoOpt = juegosRepo.leerPorId(idJuego);
    if (usuarioOpt.isEmpty()) {
        errores.add(new ErrorDTO("usuario", ErrorTipo.NO_ENCONTRADO));
    }

    
    if (juegoOpt.isEmpty()) {
        errores.add(new ErrorDTO("juego", ErrorTipo.NO_ENCONTRADO));
    }
    if (!errores.isEmpty()) {
        throw new ValidationException(errores);
    }

    var juegoBiblioteca = bibliotecaRepo.leerTodo().stream()
            .filter(b -> b.getIdUsuario().equals(idUsuario) && b.getIdJuego().equals(idJuego))
            .findFirst();


    var usuarioDto = Mapper.mapFromUsuario(usuarioOpt.orElse(null));
    var juegoDto = Mapper.mapFromJuego(juegoOpt.orElse(null));

    return Optional.ofNullable(Mapper.mapFromBiblioteca(juegoBiblioteca.orElse(null), usuarioDto, juegoDto));
    
}

    /*
Ver estadísticas de biblioteca
Descripción: Mostrar métricas generales de la biblioteca del usuario
Entrada: ID del usuario
Salida: Objeto con todas las estadísticas calculadas
Estadísticas: Total juegos, horas totales, juegos instalados, juego más jugado, valor total, juegos
nunca jugados
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
                        .orElse(0.0))
                .sum();


        int juegosNuncaJugados =bibliotecasUsuario.stream()
                .filter(b -> b.getTiempoTotalJugado() == 0).toList().size();


        return new BibliotecaEstadisticasDTO(idUsuario,totalJuegos, horasTotales, juegosInstalados,
                 juegoMasJugado, valorTotal, juegosNuncaJugados);
    }


}
