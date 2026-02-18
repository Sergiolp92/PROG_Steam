package org.example.repositorios.interfaz;

import org.example.modelo.entidad.ReseniaEntidad;
import org.example.modelo.entidad.UsuarioEntidad;
import org.example.modelo.form.ReseniaForm;
import org.example.modelo.form.UsuarioForm;

import java.util.List;
import java.util.Optional;

public interface IReseniasRepo  {

    //crear
    void crear(ReseniaForm form);

    //leer
    Optional<ReseniaEntidad> leerPorId(Long id);
    Optional<ReseniaEntidad> leerPorNombre(String nombre);
    List<ReseniaEntidad> leerTodo();


    //actualizar
    Optional<ReseniaEntidad> actualizar(Long id, ReseniaForm form);

    //borrar

    boolean borrar(Long id);

}
