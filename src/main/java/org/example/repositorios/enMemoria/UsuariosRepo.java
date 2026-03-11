package org.example.repositorios.enMemoria;

import org.example.modelo.entidad.UsuarioEntidad;
import org.example.modelo.form.UsuarioForm;
import org.example.repositorios.interfaz.IUsuarioRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsuariosRepo implements IUsuarioRepo {

    private static List<UsuarioEntidad> usuarios = new ArrayList<>();
    private static Long idContador = 1L;

    @Override
    public Optional<UsuarioEntidad> crear(UsuarioForm form) {
        Long id = idContador +1L;

            var usuario = new UsuarioEntidad(id, form.getnombreusuario(), form.getEmail(), form.getNombre(), form.getPais(), form.getFechaN(),form.getFechaRegis(),
                    form.getAvatar(), form.getSaldo(),form.getEstadoCuenta());
            usuarios.add(usuario);
        return Optional.of(usuario);
    }

    @Override
    public Optional<UsuarioEntidad> leerPorId(Long id) {
        return usuarios.stream()
                .filter(u -> id.equals((u.getId())))
                .findFirst();
    }

    @Override
    public Optional<UsuarioEntidad> leerPorNombre(String nombre) {
        return usuarios.stream()
                .filter(u -> nombre.equals(u.getNombreUsuario()))
                .findFirst();


    }


    @Override
    public List<UsuarioEntidad> leerTodo() {
        return new ArrayList<>(usuarios);
    }

    public Optional<UsuarioEntidad> actualizar(Long id, UsuarioForm form) {
        var usuarioOpt = leerPorId(id);
        if (usuarioOpt.isEmpty()) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }

        var usuarioActualizado = new UsuarioEntidad(id,form.getnombreusuario(), form.getEmail(), form.getNombre(), form.getPais(), form.getFechaN(), form.getFechaRegis(),
                form.getAvatar(),form.getSaldo(),form.getEstadoCuenta());
        usuarios.removeIf(u ->id.equals(u.getId()));
        usuarios.add(usuarioActualizado);

        return Optional.of(usuarioActualizado);

    }

    @Override
    public boolean borrar(Long id) {
        return usuarios.removeIf(u ->id.equals(u.getId()));


    }
}