package org.example.repositorios.implementacionHIbernate;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.enumerados.EstadoCompra;
import org.example.modelo.entidad.CompraEntidad;
import org.example.modelo.form.CompraForm;
import org.example.repositorios.interfaz.ICompraRepo;
import org.example.transaction.ISesionManager;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class CompraRepoHibernate implements ICompraRepo {

    private ISesionManager sesionManager;
    public CompraRepoHibernate(ISesionManager sesionManager) {
        this.sesionManager = sesionManager;
    }
    @Override
    public Optional<CompraEntidad> crear(CompraForm form) {
        var sesion = sesionManager.getSession();
        CompraEntidad compra = new CompraEntidad(0L, form.getIdUsuario(), form.getIdJuego(), LocalDate.now(), form.getMetodoPago(), form.getPrecioOriginal(),
                form.getPrecioFinal(), EstadoCompra.PENDIENTE);
        sesion.persist(compra);

        return Optional.of(compra);
    }

    @Override
    public Optional<CompraEntidad> leerPorId(Long id) {
        var sesion = sesionManager.getSession();

        CompraEntidad compra = sesion.find(CompraEntidad.class, id);

        return Optional.ofNullable(compra);
    }

    @Override
    public Optional<CompraEntidad> leerPorNombre(String nombre) {
            var sesion = sesionManager.getSession();

            CriteriaBuilder cb = sesion.getCriteriaBuilder();
            CriteriaQuery<CompraEntidad> cq = cb.createQuery(CompraEntidad.class);
            Root<CompraEntidad> root = cq.from(CompraEntidad.class);


            cq.select(root).where(cb.equal(root.get("nombre"), nombre));

            return sesion.createQuery(cq).getResultStream().findFirst();
    }

    @Override
    public List<CompraEntidad> leerTodo() {
        var sesion = sesionManager.getSession();

        CriteriaBuilder cb = sesion.getCriteriaBuilder();;
        CriteriaQuery<CompraEntidad> cq = cb.createQuery(CompraEntidad.class);
        Root<CompraEntidad> root = cq.from(CompraEntidad.class);
        cq.select(root).orderBy(cb.asc(root.get("fecha_de_Compra")));

        return sesion.createQuery(cq).getResultList();
    }

    @Override
    public Optional<CompraEntidad> actualizar(Long id, CompraForm form) {
        var sesion = sesionManager.getSession();
        Optional<CompraEntidad> compraEntidad = this.leerPorId(id);

        if (compraEntidad.isEmpty()) {
            return Optional.empty();
        } else {
            sesion.merge(new CompraEntidad(id, form.getIdUsuario(), form.getIdJuego(), LocalDate.now(),form.getMetodoPago(), form.getPrecioOriginal(),
                    form.getPrecioFinal(),compraEntidad.get().getEstadoCompra()));
            return this.leerPorId(id);
        }
    }

    @Override
    public boolean borrar(Long id) {
        var sesion = sesionManager.getSession();
        Optional<CompraEntidad> compraEntidadOpt = this.leerPorId(id);

        if (compraEntidadOpt.isEmpty()) {
            return false;
        } else {
            CompraEntidad compraEntidad = compraEntidadOpt.get();
            sesion.remove(compraEntidad);
            return true;
        }
    }
}
