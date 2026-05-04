package org.example.controlador;

import org.example.enumerados.ErrorTipo;
import org.example.enumerados.OrdenBusquedaJuego;
import org.example.excepciones.ValidationException;
import org.example.mapper.Mapper;
import org.example.modelo.dto.ErrorDTO;
import org.example.modelo.dto.JuegoDTO;
import org.example.modelo.form.BusquedaJuegoForm;
import org.example.modelo.form.JuegoForm;
import org.example.modelo.entidad.JuegoEntidad;
import org.example.repositorios.interfaz.IJuegosRepo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class JuegoControlador {
    private final IJuegosRepo juegosRepo;

    public JuegoControlador(IJuegosRepo juegosRepo) {
        this.juegosRepo = juegosRepo;
    }


   /* Añadir juego al catálogo
    Descripción: Registrar un nuevo videojuego en el catálogo de Steam
    Entrada: Datos del formulario del juego (título, descripción, desarrollador, fecha de lanzamiento,
                                             precio base, categorías, etc.)
    Salida: Juego creado exitosamente con su ID o lista de errores de validación
    Validaciones: Aplicar todas las restricciones definidas en la sección de validación de Juego
    */
    public Optional<JuegoDTO> aniadirJuego(JuegoForm juegoForm) throws ValidationException {
        var errores = juegoForm.validar();
        var juegoOpt = juegosRepo.leerPorNombre(juegoForm.getTitulo());
        if (juegoOpt.isPresent())
            errores.add(new ErrorDTO("titulo", ErrorTipo.DUPLICADO));
        if (!errores.isEmpty())
            throw new ValidationException(errores);

        juegosRepo.crear(juegoForm);

        // Después de crear, buscar el juego por nombre para devolverlo
        var juegoCreado = juegosRepo.leerPorNombre(juegoForm.getTitulo());

        return juegoCreado.map(Mapper::mapFromJuego);


    }
    /*
    Buscar juegos
    Descripción: Filtrar y buscar juegos en el catálogo según múltiples criterios
    Entrada: Criterios de búsqueda (texto, categoría, precio, clasificación, estado)
    Salida: Lista de juegos que cumplen los criterios con información resumida
    Datos mostrados: ID, título, desarrollador, precio base, descuento actual, clasificación, imagen
    */
    
    public List<JuegoDTO> buscarJuegos(BusquedaJuegoForm criterios) throws ValidationException {

        var errores = criterios.validar();
        
        if (!errores.isEmpty())
            throw new ValidationException(errores);

        var juegoFiltrado = juegosRepo.leerTodo().stream()
                .filter(j -> cumpleCriterios(j, criterios))
                .map(j -> Mapper.mapFromJuego(j))
                .toList();
        
        return juegoFiltrado;
    }

    private boolean cumpleCriterios(JuegoEntidad juego, BusquedaJuegoForm criterios) {
        // Filtrar por título
        if (criterios.getTitulo() != null && !criterios.getTitulo().isEmpty()) {

            return false;

        }

        // Filtrar por categoría
        if (criterios.getCategoria() != null && !criterios.getCategoria().isEmpty()) {

                return false;

        }

        // Filtrar por precio
        if (criterios.getPrecio() != null && juego.getPrecioB() <= criterios.getPrecio()) {
            return false;
        }

        // Filtrar por clasificación de edad
        if (criterios.getClasificacion() != null && !criterios.getClasificacion().equals(juego.getClasificacionEdad())) {
            return false;
        }

        // Filtrar por estado del juego
        if (criterios.getEstado() != null && !criterios.getEstado().equals(juego.getEstadoJuego())) {
            return false;
        }

        return true;
    }

    
    

    /*
    Consultar catálogo completo
    Descripción: Listar todos los juegos disponibles en la plataforma
    Entrada: orden (opcional: alfabético, precio, fecha)
    Salida: Lista paginada de juegos con información básica y metadatos de paginación
    Datos mostrados: Título, desarrollador, precio base, descuento actual, clasificación
    */
    public List<JuegoDTO> consultarTodoCatalogo(String filtro) {
        List<JuegoEntidad> juegos = juegosRepo.leerTodo();

        //ordenar
        if (filtro.equals(OrdenBusquedaJuego.ALFABETICO)){
        juegos.
                stream().sorted(Comparator.comparing(JuegoEntidad::getTitulo));
        return juegos.stream().map(Mapper::mapFromJuego).toList();
        }
        if (filtro.equals(OrdenBusquedaJuego.PRECIO)){
            juegos.
                    stream().sorted(Comparator.comparing(JuegoEntidad::getPrecioB));
            return juegos.stream().map(Mapper::mapFromJuego).toList();
        }
        if (filtro.equals(OrdenBusquedaJuego.FECHA)){
            juegos.stream().sorted(Comparator.comparing(JuegoEntidad::getFechaLanz));
            return juegos.stream().map(Mapper::mapFromJuego).toList();
        }

        return juegos.stream().map(Mapper::mapFromJuego).toList();
    }
    /*
    Consultar detalles de juego
    Descripción: Mostrar toda la información completa de un juego específico
    Entrada: ID del juego
    Salida: Información completa del juego o mensaje de error si no existe
    Datos mostrados: Todos los campos del juego más estadísticas y reseñas destacadas
    */
    public Optional<JuegoDTO> consultarJuego(long id) throws ValidationException {
        List<ErrorDTO> errores = new ArrayList<>();

        var juego = juegosRepo.leerPorId(id);
        if (juego.isEmpty()) {

            errores.add(new ErrorDTO("id", ErrorTipo.NO_ENCONTRADO));
        }

        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }

        return Optional.ofNullable(Mapper.mapFromJuego(juego.orElse(null)));
    }
    /*
    Aplicar descuento
    Descripción: Establecer un porcentaje de descuento temporal a un juego
    Entrada: ID del juego, porcentaje de descuento (0-100)
    Salida: Precio final actualizado o mensaje de error
    Validaciones: Juego existe, descuento en rango válido
    */

    /*
    Cambiar estado del juego
    Descripción: Modificar el estado de disponibilidad de un juego
    Entrada: ID del juego, nuevo estado
    Salida: Confirmación del cambio de estado o mensaje de error
    Validaciones: Juego existe, estado válido
    */

    
}
