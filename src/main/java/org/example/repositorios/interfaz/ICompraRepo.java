package org.example.repositorios.interfaz;

import org.example.modelo.entidad.CompraEntidad;
import org.example.modelo.entidad.UsuarioEntidad;
import org.example.modelo.form.CompraForm;
import org.example.modelo.form.UsuarioForm;

import java.util.List;
import java.util.Optional;

public interface ICompraRepo {


    //crear
    void crear(CompraForm form);

    //leer
    Optional<CompraEntidad> leerPorId(Long id);

    Optional<CompraEntidad> leerPorNombre(String nombre);

    List<CompraEntidad> leerTodo();


    //actualizar
    Optional<CompraEntidad> actualizar(Long id, CompraForm form);

    //borrar

    boolean borrar(Long id);
}

