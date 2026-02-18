package org.example.repositorios.interfaz;

import org.example.modelo.entidad.JuegoEntidad;
import org.example.modelo.entidad.UsuarioEntidad;
import org.example.modelo.form.JuegoForm;
import org.example.modelo.form.UsuarioForm;

import java.util.List;
import java.util.Optional;

public interface IJuegosRepo {

   //crear
    void crear(JuegoForm form);

    //leer
    Optional<JuegoEntidad> leerPorId(Long id);
    Optional<JuegoEntidad> leerPorNombre(String nombre);
    List<JuegoEntidad> leerTodo();


    //actualizar
    Optional<JuegoEntidad> actualizar(Long id, JuegoForm form);

    //borrar

    boolean borrar(Long id);
}
