package org.example.repositorios.enMemoria;

import org.example.modelo.entidad.BibliotecaEntidad;
import org.example.modelo.entidad.UsuarioEntidad;
import org.example.modelo.form.BibliotecaForm;
import org.example.repositorios.interfaz.IBibliotecaRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BibliotecaRepo implements IBibliotecaRepo {
    private static List<BibliotecaEntidad> bibliotecas = new ArrayList<>();
    private static Long idContador = 1L;


    @Override
    public void crear(BibliotecaForm form) {
        Long id = idContador +1L;

        var biblioteca = new BibliotecaEntidad(id, form.getIdUsuario(), form.getIdJuego(), form.getFechaAdquisicion(),
                form.getTiempoDeJuego());
        bibliotecas.add(biblioteca);


    }

    @Override
    public Optional<BibliotecaEntidad> leerPorId(Long id) {
        return bibliotecas.stream()
                .filter(b -> b.getIdBiblio().equals(id))
                .findFirst();

    }

    @Override
    public Optional<BibliotecaEntidad> leerPorNombre(String nombre) {
        return Optional.empty();
    }


    @Override
    public List<BibliotecaEntidad> leerTodo() {
        return new ArrayList<>(bibliotecas);
    }

    @Override
    public Optional<BibliotecaEntidad> actualizar(Long id, BibliotecaForm form) {
        var bibliotecaOpt = leerPorId(id);
        if (bibliotecaOpt.isEmpty()) {
            throw new IllegalArgumentException("Biblioteca no encontrada");
        }

        var bibliotecaActualizada = new BibliotecaEntidad(id, form.getIdUsuario(), form.getIdJuego(), form.getFechaAdquisicion(),
                form.getTiempoDeJuego());
        bibliotecas.removeIf(b ->id.equals(b.getIdBiblio()));
        bibliotecas.add(bibliotecaActualizada);

        return Optional.of(bibliotecaActualizada);

    }

    @Override
    public boolean borrar(Long id) {
        return bibliotecas.removeIf(b ->id.equals(b.getIdBiblio()));

    }
}
