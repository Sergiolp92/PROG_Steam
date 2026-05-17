package org.example.repositorios.implementacionHIbernate;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.modelo.entidad.BibliotecaEntidad;
import org.example.modelo.form.BibliotecaForm;
import org.example.repositorios.interfaz.IBibliotecaRepo;
import org.example.transaction.ISesionManager;

import java.util.List;
import java.util.Optional;

public class BibliotecaRepoHibernate implements IBibliotecaRepo {

    private ISesionManager sesionManager;
    public BibliotecaRepoHibernate(ISesionManager sesionManager) {
        this.sesionManager = sesionManager;
    }

    @Override
    public Optional<BibliotecaEntidad> crear(BibliotecaForm form) {
        var sesion = sesionManager.getSession();
        BibliotecaEntidad biblioteca = new BibliotecaEntidad(0L, form.getIdUsuario(), form.getIdJuego(), form.getFechaAdquisicion(),
                form.getTiempoDeJuego());
        sesion.persist(biblioteca);
        return Optional.of(biblioteca);

    }

    @Override
    public Optional<BibliotecaEntidad> leerPorId(Long id) {
        var sesion = sesionManager.getSession();

        BibliotecaEntidad biblioteca = sesion.find(BibliotecaEntidad.class, id);

        return Optional.ofNullable(biblioteca);
    }

    @Override
    public Optional<BibliotecaEntidad> leerPorNombre(String nombre) {
        var sesion = sesionManager.getSession();

        CriteriaBuilder cb = sesion.getCriteriaBuilder();
        CriteriaQuery<BibliotecaEntidad> cq = cb.createQuery(BibliotecaEntidad.class);
        Root<BibliotecaEntidad> root = cq.from(BibliotecaEntidad.class);
        cq.select(root).where(cb.equal(root.get("nombre"), nombre));

        return sesion.createQuery(cq).getResultStream().findFirst();


    }

    @Override
    public List<BibliotecaEntidad> leerTodo() {
        var sesion = sesionManager.getSession();

        CriteriaBuilder cb = sesion.getCriteriaBuilder();

        CriteriaQuery<BibliotecaEntidad> cq = cb.createQuery(BibliotecaEntidad.class);
        Root<BibliotecaEntidad> root = cq.from(BibliotecaEntidad.class);

        cq.select(root).orderBy(cb.asc(root.get("fecha_adquisicion")));

        return sesion.createQuery(cq).getResultList();
    }

    @Override
    public Optional<BibliotecaEntidad> leerJuegoUsuario(Long idJuego, Long idUsuario) {
        var sesion = sesionManager.getSession();

        CriteriaBuilder cb = sesion.getCriteriaBuilder();
        CriteriaQuery<BibliotecaEntidad> cq = cb.createQuery(BibliotecaEntidad.class);
        Root<BibliotecaEntidad> root = cq.from(BibliotecaEntidad.class);

        cq.select(root).where(cb.and(
                        cb.equal(root.get("id_usuario"), idUsuario),
                        cb.equal(root.get("id_juego"), idJuego)
                )
        );

        return sesion.createQuery(cq).getResultStream().findFirst();
    }

    @Override
    public Optional<BibliotecaEntidad> actualizar(Long id, BibliotecaForm form) {
        var sesion = sesionManager.getSession();
        Optional<BibliotecaEntidad> bibliotecaOpt = this.leerPorId(id);

        if (bibliotecaOpt.isEmpty()) {
            return Optional.empty();
        } else {
            sesion.merge(new BibliotecaEntidad(id, form.getIdUsuario(), form.getIdJuego(), form.getFechaAdquisicion(), form.getTiempoDeJuego()));
            return this.leerPorId(id);
        }
    }

    @Override
    public boolean borrar(Long id) {
        var sesion = sesionManager.getSession();
        Optional<BibliotecaEntidad> bibliotecaOpt = this.leerPorId(id);

        if (bibliotecaOpt.isEmpty()) {
            return false;
        } else {
            BibliotecaEntidad biblioteca = bibliotecaOpt.get();
            sesion.remove(biblioteca);
            return true;
        }
    }
}
