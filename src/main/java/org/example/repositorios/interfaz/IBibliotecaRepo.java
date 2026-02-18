package org.example.repositorios.interfaz;

import org.example.modelo.entidad.BibliotecaEntidad;
import org.example.modelo.entidad.UsuarioEntidad;
import org.example.modelo.form.BibliotecaForm;
import org.example.modelo.form.UsuarioForm;

import java.util.List;
import java.util.Optional;

public interface IBibliotecaRepo  {

    //crear
    void crear(BibliotecaForm form);

    //leer
    Optional<BibliotecaEntidad> leerPorId(Long id);
    Optional<BibliotecaEntidad> leerPorNombre(String nombre);
    List<BibliotecaEntidad> leerTodo();


    //actualizar
    Optional<BibliotecaEntidad> actualizar(Long id, BibliotecaForm form);

    //borrar

    boolean borrar(Long id);


}
