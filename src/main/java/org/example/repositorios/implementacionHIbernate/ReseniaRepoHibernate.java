package org.example.repositorios.implementacionHIbernate;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.modelo.entidad.ReseniaEntidad;
import org.example.modelo.form.ReseniaForm;
import org.example.repositorios.interfaz.IReseniasRepo;
import org.example.transaction.ISesionManager;

import java.util.List;
import java.util.Optional;

public class ReseniaRepoHibernate implements IReseniasRepo {

    private final ISesionManager sesionManager;
    public ReseniaRepoHibernate(ISesionManager sesionManager) {
        this.sesionManager = sesionManager;
    }

    @Override
    public Optional<ReseniaEntidad> crear(ReseniaForm form) {
        var sesion = sesionManager.getSession();
        ReseniaEntidad resenia = new ReseniaEntidad(0L, form.getIdUsuario(), form.getIdJuego(), form.isRecomendado(),
                form.getTextoResenia(),
                form.getHorasJugadas(), form.getFechaPublicacionR(),
                form.getFechaUltimaEdicion(), form.getEstadoResenia());
        sesion.persist(resenia);
        return Optional.of(resenia);
    }

    @Override
    public Optional<ReseniaEntidad> leerPorId(Long id) {
        var sesion = sesionManager.getSession();

        ReseniaEntidad resenia = sesion.find(ReseniaEntidad.class, id);

        return Optional.ofNullable(resenia);    }

    @Override
    public Optional<ReseniaEntidad> leerPorNombre(String nombre) {
        var sesion = sesionManager.getSession();

        CriteriaBuilder cb = sesion.getCriteriaBuilder();
        CriteriaQuery<ReseniaEntidad> cq = cb.createQuery(ReseniaEntidad.class);
        Root<ReseniaEntidad> root = cq.from(ReseniaEntidad.class);
        cq.select(root).where(cb.equal(root.get("nombre_resenia"), nombre));

        return sesion.createQuery(cq).getResultStream().findFirst();
    }

    @Override
    public List<ReseniaEntidad> leerTodo() {
        var sesion = sesionManager.getSession();

        CriteriaBuilder cb = sesion.getCriteriaBuilder();
        CriteriaQuery<ReseniaEntidad> cq = cb.createQuery(ReseniaEntidad.class);
        Root<ReseniaEntidad> root = cq.from(ReseniaEntidad.class);
        cq.select(root).orderBy(cb.asc(root.get("fecha_publicacion_r")));

        return sesion.createQuery(cq).getResultList();
    }

    @Override
    public Optional<ReseniaEntidad> leerPorIdUsuarioYIdJuego(Long idUsuario, Long idJuego) {
        var sesion = sesionManager.getSession();

        CriteriaBuilder cb = sesion.getCriteriaBuilder();
        CriteriaQuery<ReseniaEntidad> cq = cb.createQuery(ReseniaEntidad.class);
        Root<ReseniaEntidad> root = cq.from(ReseniaEntidad.class);

        cq.select(root).where(cb.and(
                        cb.equal(root.get("usuario_id"), idUsuario),
                        cb.equal(root.get("juego_id"), idJuego)
                )
        );

        return sesion.createQuery(cq).getResultStream().findFirst();
    }

    @Override
    public Optional<ReseniaEntidad> actualizar(Long id, ReseniaForm form) {
        var sesion = sesionManager.getSession();
        ReseniaEntidad resenia = sesion.get(ReseniaEntidad.class, id);
        if (resenia == null) {
            return Optional.empty();
        }

        ReseniaEntidad reseniaN = new ReseniaEntidad(
                resenia.getIdResenia(),
                resenia.getUsuarioId(),
                resenia.getJuegoId(),
                form.isRecomendado(),
                form.getTextoResenia(),
                form.getHorasJugadas(),
                resenia.getFechaPublicacionR(),
                form.getFechaUltimaEdicion(),
                form.getEstadoResenia()
        );

        sesion.merge(reseniaN);
        return Optional.of(reseniaN);
    }

    @Override
    public boolean borrar(Long id) {
        var sesion = sesionManager.getSession();
        Optional<ReseniaEntidad> resenaEntidad = this.leerPorId(id);

        if (resenaEntidad.isEmpty()) {
            return false;
        } else {
            sesion.remove(resenaEntidad.get());
            return true;
        }
    }
}
