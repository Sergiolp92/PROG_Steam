package org.example.repositorios.enMemoria;

import org.example.enumerados.ClasificacionEdad;
import org.example.modelo.entidad.JuegoEntidad;
import org.example.modelo.entidad.UsuarioEntidad;
import org.example.modelo.form.JuegoForm;
import org.example.modelo.form.UsuarioForm;
import org.example.repositorios.interfaz.IJuegosRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JuegosRepo implements IJuegosRepo {

    private static List<JuegoEntidad> juegos = new ArrayList<>();
    private static long idContador = (Long) 1L;

    @Override
    public void crear(JuegoForm form) {
        idContador  = idContador +1L;

        var juego = new JuegoEntidad((Long)idContador, form.getTitulo(), form.getDescripcion(), form.getDesarrollador(),
                form.getFechaL(), form.getPrecioB(), form.getDescuento(), form.getCategoria(),form.getClasiEdad() ,
                form.getIdioma(), form.getEstadoJuego());
        juegos.add(juego);

    }

    @Override
    public Optional<JuegoEntidad> leerPorId(Long id) {
        return juegos.stream()
                .filter(j -> j.getIdJuego().equals(id))
                .findFirst();
    }

    @Override
    public Optional<JuegoEntidad> leerPorNombre(String nombre) {
        return juegos.stream()
                .filter(j -> nombre.equals(j.getTitulo()))
                .findFirst();


    }


    @Override
    public List<JuegoEntidad> leerTodo() {
        return new ArrayList<>(juegos);
    }

    public Optional<JuegoEntidad> actualizar(Long id, JuegoForm form) {
        var usuarioOpt = leerPorId(id);
        if (usuarioOpt.isEmpty()) {
            throw new IllegalArgumentException("Juego no encontrado");
        }

        var juegoActualizado = new JuegoEntidad((Long)idContador, form.getTitulo(), form.getDescripcion(), form.getDesarrollador(),
                form.getFechaL(), form.getPrecioB(), form.getDescuento(), form.getCategoria(),form.getClasiEdad() ,
                form.getIdioma(), form.getEstadoJuego());
        juegos.add(juegoActualizado);

        return Optional.of(juegoActualizado);

    }

    @Override
    public boolean borrar(Long id) {
        return juegos.removeIf(j ->id.equals(j.getIdJuego()));


    }
}
