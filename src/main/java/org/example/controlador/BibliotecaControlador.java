package org.example.controlador;

import org.example.enumerados.ErrorTipo;
import org.example.enumerados.OrdenBusquedaBiblioteca;
import org.example.excepciones.ValidationException;
import org.example.mapper.Mapper;
import org.example.modelo.dto.BibliotecaDTO;
import org.example.modelo.dto.ErrorDTO;
import org.example.modelo.entidad.BibliotecaEntidad;
import org.example.modelo.entidad.JuegoEntidad;
import org.example.modelo.entidad.UsuarioEntidad;
import org.example.repositorios.interfaz.IBibliotecaRepo;
import org.example.repositorios.interfaz.ICompraRepo;
import org.example.repositorios.interfaz.IJuegosRepo;
import org.example.repositorios.interfaz.IUsuarioRepo;

import java.util.ArrayList;
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

    public List<BibliotecaDTO> verBibliotecaPersonal(long id , OrdenBusquedaBiblioteca filtro ) throws ValidationException {

        List<ErrorDTO> errores = new ArrayList<>();
        var usuarioOpt = usuarioRepo.leerPorId(id);

        if (usuarioOpt.isEmpty()){
            errores.add(new ErrorDTO("usuario", ErrorTipo.NO_ENCONTRADO));
        }

        if (!errores.isEmpty()){
            throw new ValidationException(errores);
        }

        Optional<BibliotecaEntidad> bibliotecaUsuario = bibliotecaRepo.leerPorId(id).filter(b -> b.getIdUsuario() == id);



        if(filtro.equals(OrdenBusquedaBiblioteca.ALFABETICO)){


        }


    }

    /*
Añadir juego a biblioteca
Descripción: Agregar un juego adquirido a la biblioteca del usuario
Entrada: ID del usuario, ID del juego
Salida: Confirmación de adición a biblioteca o mensaje de error
Validaciones: Usuario existe, juego existe, no duplicado, compra verificada
*/
    /*
Eliminar juego de biblioteca
Descripción: Quitar un juego de la biblioteca del usuario
Entrada: ID del usuario, ID del juego
Salida: Confirmación de eliminación o cancelación
Validaciones: Entrada existe en la biblioteca
*/
    /*
Actualizar tiempo de juego
Descripción: Registrar y actualizar las horas jugadas de un juego
Entrada: ID del usuario, ID del juego, horas a añadir
Salida: Nuevo tiempo total de juego
Validaciones: Biblioteca existe, horas positivas
*/
    /*
Consultar última sesión
Descripción: Ver la última vez que se jugó a un juego específico
Entrada: ID del usuario, ID del juego
Salida: Fecha y hora de última sesión o mensaje "Nunca jugado"
Formato: "Última sesión: hace 2 días (18/01/2026 15:30)"

Filtrar biblioteca (Ficheros)
Descripción: Buscar juegos en la biblioteca personal según criterios
Entrada: ID del usuario, filtros (estado de instalación, texto de búsqueda)
Salida: Lista filtrada de juegos de la biblioteca
Datos mostrados: Título, tiempo jugado, estado
 */
    /*
Ver estadísticas de biblioteca
Descripción: Mostrar métricas generales de la biblioteca del usuario
Entrada: ID del usuario
Salida: Objeto con todas las estadísticas calculadas
Estadísticas: Total juegos, horas totales, juegos instalados, juego más jugado, valor total, juegos
nunca jugados
*/



}
