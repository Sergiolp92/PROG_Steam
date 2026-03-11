package org.example.repositorios.enMemoria;

import org.example.modelo.entidad.CompraEntidad;

import org.example.modelo.form.CompraForm;
import org.example.repositorios.interfaz.ICompraRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CompraRepo implements ICompraRepo {


    private static List<CompraEntidad> compras = new ArrayList<>();
    private static Long idContador = 1L;

    @Override
    public void crear(CompraForm form) {
        Long id = idContador +1L;

        var compra = new CompraEntidad(id,form.getFechaC(),form.getMetodoPago(),form.getPrecioOriginal(),
                form.getPrecioFinal(), form.getEstadoCompra());
        compras.add(compra);

    }

    @Override
    public Optional<CompraEntidad> leerPorId(Long id) {
        return compras.stream()
                .filter(c -> id.equals(c.getIdCompra()))
                .findFirst();
    }

    @Override
    public Optional<CompraEntidad> leerPorNombre(String nombre) {
        return Optional.empty();
    }



    @Override
    public List<CompraEntidad> leerTodo() {
        return new ArrayList<>(compras);
    }

    public Optional<CompraEntidad> actualizar(Long id, CompraForm form) {
        var usuarioOpt = leerPorId(id);
        if (usuarioOpt.isEmpty()) {
            throw new IllegalArgumentException("Compra no encontrada");
        }

        var compraActualizada = new CompraEntidad(id,form.getFechaC(),form.getMetodoPago(),form.getPrecioOriginal(),
                form.getPrecioFinal(), form.getEstadoCompra());
        compras.removeIf(c ->id.equals(c.getIdCompra()));
        compras.add(compraActualizada);

        return Optional.of(compraActualizada);

    }

    @Override
    public boolean borrar(Long id) {
        return compras.removeIf(c ->id.equals(c.getIdCompra()));


    }
}
