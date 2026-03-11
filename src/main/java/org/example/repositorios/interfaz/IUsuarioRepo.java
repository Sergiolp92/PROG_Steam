package org.example.repositorios.interfaz;

import org.example.modelo.entidad.UsuarioEntidad;
import org.example.modelo.form.UsuarioForm;

import java.util.List;
import java.util.Optional;

public interface IUsuarioRepo  {


    //crear
    Optional<UsuarioEntidad> crear(UsuarioForm form);

    //leer
    Optional<UsuarioEntidad> leerPorId(Long id);
    Optional<UsuarioEntidad> leerPorNombre(String nombre);
    List<UsuarioEntidad> leerTodo();


    //actualizar
    Optional<UsuarioEntidad> actualizar(Long id, UsuarioForm form);

    //borrar

    boolean borrar(Long id);
}


