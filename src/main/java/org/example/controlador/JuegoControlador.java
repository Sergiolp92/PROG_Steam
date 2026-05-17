package org.example.controlador;

import org.example.enumerados.ClasificacionEdad;
import org.example.enumerados.ErrorTipo;
import org.example.enumerados.EstadoJuego;
import org.example.enumerados.OrdenBusquedaJuego;
import org.example.excepciones.ValidationException;
import org.example.mapper.Mapper;
import org.example.modelo.dto.ErrorDTO;
import org.example.modelo.dto.JuegoDTO;
import org.example.modelo.form.BusquedaJuegoForm;
import org.example.modelo.form.JuegoForm;
import org.example.modelo.entidad.JuegoEntidad;
import org.example.repositorios.interfaz.IJuegosRepo;
import org.example.transaction.ITransactionManager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;


public class JuegoControlador {
    public static final int DESCUENTO_MIN = 0;
    public static final int DESCUENTO_MAX = 100;
    private final IJuegosRepo juegosRepo;

    public ITransactionManager tm;

    public JuegoControlador(IJuegosRepo juegosRepo, ITransactionManager tm) {
        this.tm = tm;
        this.juegosRepo = juegosRepo;
    }




    /**

     * @param juegoForm
     * @return Juego creado exitosamente con su ID o lista de errores de validación
     * @throws ValidationException
     */
    public Optional<JuegoDTO> aniadirJuego(JuegoForm juegoForm) throws ValidationException {
        var errores = juegoForm.validar();

        var juegoCreado =tm.inTransaction(()-> {
            var juegoOpt = juegosRepo.leerPorNombre(juegoForm.getTitulo());
            if (juegoOpt.isPresent()){
                errores.add(new ErrorDTO("titulo", ErrorTipo.DUPLICADO));
            }
            if (!errores.isEmpty()){
                throw new IllegalArgumentException();
            }

            return juegosRepo.crear(juegoForm);
        });
        if (!errores.isEmpty()){
            throw new ValidationException(errores);
        }


        return juegoCreado.map(Mapper::mapFromJuego);


    }


    /**
     * Buscar juegos en el catálogo según los criterios especificados en el formulario de búsqueda.
     * @param criterios
     * @return Lista de juegos que cumplen los criterios con información resumida
     * @throws ValidationException
     */
    
    public List<JuegoDTO> buscarJuegos(BusquedaJuegoForm criterios) throws ValidationException {

        var errores = criterios.validar();
        
        if (!errores.isEmpty())
            throw new ValidationException(errores);

        var juegoFiltrado = tm.inTransaction(()-> juegosRepo.leerTodo().stream()
                .filter(j -> cumpleCriterios(j, criterios))
                .map(j -> Mapper.mapFromJuego(j))
                .toList());
        
        return juegoFiltrado;
    }

    private boolean cumpleCriterios(JuegoEntidad juego, BusquedaJuegoForm criterios) {

        if (criterios.getTitulo() != null && !criterios.getTitulo().isEmpty()) {
            if (!juego.getTitulo().toLowerCase().contains(criterios.getTitulo().toLowerCase())) {
                return false;
            }
        }


        if (criterios.getCategoria() != null && !criterios.getCategoria().isEmpty()) {
            if (!juego.getCategoria().toLowerCase().contains(criterios.getCategoria().toLowerCase())) {
                return false;
            }
        }


        if (criterios.getPrecio() != null && juego.getPrecioB() > criterios.getPrecio()) {
            return false;
        }


        if (criterios.getClasificacion() != null && !criterios.getClasificacion().equals(juego.getClasificacionEdad())) {
            return false;
        }


        if (criterios.getEstado() != null && !criterios.getEstado().equals(juego.getEstadoJuego())) {
            return false;
        }

        return true;
    }

    
    



    /**
     * Consultar catálogo completo de juegos con opción de ordenamiento
     * @param filtro
     * @return Lista de juegos ordenada según el filtro especificado o sin ordenamiento si el filtro es inválido
     */
    public List<JuegoDTO> consultarTodoCatalogo(String filtro) {
        return tm.inTransaction(()-> {
            List<JuegoEntidad> juegos = juegosRepo.leerTodo();


            if (OrdenBusquedaJuego.ALFABETICO.equals(filtro)) {
                juegos.
                        stream().sorted(Comparator.comparing(JuegoEntidad::getTitulo));
                return juegos.stream().map(Mapper::mapFromJuego).toList();
            }
            if (OrdenBusquedaJuego.PRECIO.equals(filtro)) {
                juegos.
                        stream().sorted(Comparator.comparing(JuegoEntidad::getPrecioB));
                return juegos.stream().map(Mapper::mapFromJuego).toList();
            }
            if (OrdenBusquedaJuego.FECHA.equals(filtro)) {
                juegos.stream().sorted(Comparator.comparing(JuegoEntidad::getFechaLanz));
                return juegos.stream().map(Mapper::mapFromJuego).toList();
            }

            return juegos.stream().map(Mapper::mapFromJuego).toList();
        });
    }


    /**
     * Consultar detalles de un juego específico por su ID, mostrando toda la información completa del juego.
     * @param id
     * @return Información completa del juego o mensaje de error si no existe
     * @throws ValidationException
     */
    public Optional<JuegoDTO> consultarJuego(long id) throws ValidationException {
        List<ErrorDTO> errores = new ArrayList<>();

        var juego = tm.inTransaction(() -> juegosRepo.leerPorId(id));

        if (juego.isEmpty()) {
            errores.add(new ErrorDTO("id", ErrorTipo.NO_ENCONTRADO));
        }

        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }
        return Optional.ofNullable(Mapper.mapFromJuego(juego.orElse(null)));
    }


    /**
     * Aplicar un descuento a un juego específico, actualizando su precio final en función del porcentaje de descuento proporcionado.
     * @param id
     * @param descuento
     * @return Información del juego con el descuento aplicado o mensaje de error si el juego no existe o el descuento es inválido
     * @throws ValidationException
     */
    public Optional<JuegoDTO> aplicarDescuento(long id, double descuento) throws ValidationException {
        List<ErrorDTO> errores = new ArrayList<>();

        if (descuento < DESCUENTO_MIN || descuento > DESCUENTO_MAX) {
            errores.add(new ErrorDTO("descuento", ErrorTipo.FUERA_DE_RANGO));
        }

        var juegoActualizado = tm.inTransaction(() -> {
            var juegoOpt = juegosRepo.leerPorId(id);

            if (juegoOpt.isEmpty()) {
                errores.add(new ErrorDTO("id", ErrorTipo.NO_ENCONTRADO));
            }
            if (!errores.isEmpty()) {
                throw new IllegalArgumentException();
            }

            var j = juegoOpt.get();
            return juegosRepo.actualizar(id, new JuegoForm(
                    j.getTitulo(),
                    j.getDesarrollador(),
                    j.getFechaLanz(),
                    j.getPrecioB(),
                    j.getClasificacionEdad(),
                    j.getDescripcion(),
                    (int) descuento,
                    j.getCategoria(),
                    j.getIdioma(),
                    j.getEstadoJuego()
            ));
        });

        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }

        return Optional.ofNullable(Mapper.mapFromJuego(juegoActualizado.orElse(null)));
    }


    /**
     * Cambiar el estado de un juego específico, permitiendo modificar su disponibilidad en la plataforma (por ejemplo, activo, inactivo, en mantenimiento).
     * @param id
     * @param estado
     * @return Información del juego con el nuevo estado o mensaje de error si el juego no existe o el estado es inválido
     * @throws ValidationException
     */
    public Optional<JuegoDTO> cambiarEstado(long id , EstadoJuego estado) throws ValidationException{
        List<ErrorDTO> errores = new ArrayList<>();

        if (estado == null) {
            errores.add(new ErrorDTO("estado", ErrorTipo.VALOR_INVALIDO));
        }

        var juegoActualizado = tm.inTransaction(() -> {
            var juegoOpt = juegosRepo.leerPorId(id);

            if (juegoOpt.isEmpty()) {
                errores.add(new ErrorDTO("id", ErrorTipo.NO_ENCONTRADO));
            }
            if (!errores.isEmpty()) {
                throw new IllegalArgumentException();
            }

            var j = juegoOpt.get();
            return juegosRepo.actualizar(id, new JuegoForm(
                    j.getTitulo(),
                    j.getDesarrollador(),
                    j.getFechaLanz(),
                    j.getPrecioB(),
                    j.getClasificacionEdad(),
                    j.getDescripcion(),
                    j.getDescuento(),
                    j.getCategoria(),
                    j.getIdioma(),
                    estado
            ));
        });

        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }
        return Optional.ofNullable(Mapper.mapFromJuego(juegoActualizado.orElse(null)));
    }
}

