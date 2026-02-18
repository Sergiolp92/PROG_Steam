package org.example.repositorios.enMemoria;

import org.example.modelo.entidad.ReseniaEntidad;
import org.example.modelo.form.ReseniaForm;
import org.example.repositorios.interfaz.IReseniasRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReseniasRepo implements IReseniasRepo {

    private static List<ReseniaEntidad> resenias = new ArrayList<>();
    private static Long idContador = 1L;

    @Override
    public void crear(ReseniaForm form) {
        Long id = idContador +1L;
        var resenia = new ReseniaEntidad(id, form.isRecomendado(), form.getTextoResenia(),form.getHorasJugadas(), form.getFechaPublicacionR(), form.getFechaUltimaEdicion(),form.getEstadoResenia());

        resenias.add(resenia);

    }

    @Override
    public Optional<ReseniaEntidad> leerPorId(Long id) {
        return resenias.stream()
                .filter(u -> u.getIdResenia().equals(id))
                .findFirst();
    }

    @Override
    public Optional<ReseniaEntidad> leerPorNombre(String nombre) {
        return Optional.empty();
    }


    @Override
    public List<ReseniaEntidad> leerTodo() {
        return new ArrayList<>(resenias);
    }

    public Optional<ReseniaEntidad> actualizar(Long id, ReseniaForm form) {
        var usuarioOpt = leerPorId(id);
        if (usuarioOpt.isEmpty()) {
            throw new IllegalArgumentException("Reseña no encontrada");
        }

        var reseniaActualizada = new ReseniaEntidad(id, form.isRecomendado(), form.getTextoResenia(),form.getHorasJugadas(), form.getFechaPublicacionR(),
                form.getFechaUltimaEdicion(),form.getEstadoResenia());
        resenias.removeIf(r ->id.equals(r.getIdResenia()));
        resenias.add(reseniaActualizada);

        return Optional.of(reseniaActualizada);

    }

    @Override
    public boolean borrar(Long id) {
        return resenias.removeIf(r ->id.equals(r.getIdResenia()));


    }
}
