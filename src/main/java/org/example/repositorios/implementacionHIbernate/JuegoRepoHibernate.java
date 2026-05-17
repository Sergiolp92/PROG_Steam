package org.example.repositorios.implementacionHIbernate;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.modelo.entidad.JuegoEntidad;
import org.example.modelo.form.JuegoForm;
import org.example.repositorios.interfaz.IJuegosRepo;
import org.example.transaction.ISesionManager;

import java.util.List;
import java.util.Optional;

public class JuegoRepoHibernate implements IJuegosRepo {

    private final ISesionManager sesionManager;

    public JuegoRepoHibernate(ISesionManager sesionManager) {
        this.sesionManager = sesionManager;
    }

    @Override
    public Optional<JuegoEntidad> crear(JuegoForm form) {
        var sesion = sesionManager.getSession();
        var juego = new JuegoEntidad(0L, form.getTitulo(), form.getDescripcion(), form.getDesarrollador(), form.getFechaL(),
                form.getPrecioB(), form.getDescuento(), form.getCategoria(), form.getClasiEdad(), form.getIdioma(), form.getEstadoJuego());
        sesion.persist(juego);
        return Optional.of(juego);
    }

    @Override
    public Optional<JuegoEntidad> leerPorId(Long id) {
        var sesion = sesionManager.getSession();
        var juego = sesion.find(JuegoEntidad.class, id);
        return Optional.ofNullable(juego);
    }

    @Override
    public Optional<JuegoEntidad> leerPorNombre(String nombre) {
        var sesion = sesionManager.getSession();
        CriteriaBuilder cb = sesion.getCriteriaBuilder();
        CriteriaQuery<JuegoEntidad> cq = cb.createQuery(JuegoEntidad.class);
        Root<JuegoEntidad> root = cq.from(JuegoEntidad.class);
        cq.select(root).where(cb.equal(root.get("titulo"), nombre));
        return sesion.createQuery(cq).getResultStream().findFirst();
    }

    @Override
    public List<JuegoEntidad> leerTodo() {
        var sesion = sesionManager.getSession();

        CriteriaBuilder cb = sesion.getCriteriaBuilder();
        CriteriaQuery<JuegoEntidad> cq = cb.createQuery(JuegoEntidad.class);
        Root<JuegoEntidad> root = cq.from(JuegoEntidad.class);
        cq.select(root).orderBy(cb.asc(root.get("titulo")));
        return sesion.createQuery(cq).getResultList();
    }

    @Override
    public Optional<JuegoEntidad> actualizar(Long id, JuegoForm form) {
        var sesion = sesionManager.getSession();
        var juegoOpt= this.leerPorId(id);
        if(juegoOpt.isEmpty()) {
            return Optional.empty();
        }else {
            sesion.merge(new JuegoEntidad(id, form.getTitulo(), form.getDescripcion(), form.getDesarrollador(), form.getFechaL(),
                    form.getPrecioB(), form.getDescuento(), form.getCategoria(), form.getClasiEdad(), form.getIdioma(), form.getEstadoJuego()));
            return this.leerPorId(id);
        }
    }

    @Override
    public boolean borrar(Long id) {
        var sesion = sesionManager.getSession();
        var juegoOpt = this.leerPorId(id);
        if (juegoOpt.isEmpty()) {
            return false;
        } else {
            sesion.remove(juegoOpt.get());
            return true;
        }

    }
}
