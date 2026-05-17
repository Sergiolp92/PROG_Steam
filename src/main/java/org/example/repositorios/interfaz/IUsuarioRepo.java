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
    Optional<UsuarioEntidad> leerPorEmail(String email);
    List<UsuarioEntidad> leerTodo();


    //actualizar
    void sumarSaldo(Long idUsuario, Double precioJuego) ;
    void restarSaldo(Long idUsuario, Double precioJuego);
    Optional<UsuarioEntidad> actualizar(Long id, UsuarioForm form);

    //borrar

    boolean borrar(Long id);
}


