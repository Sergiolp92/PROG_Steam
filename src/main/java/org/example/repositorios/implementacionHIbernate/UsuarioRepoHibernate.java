package org.example.repositorios.implementacionHIbernate;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.modelo.entidad.UsuarioEntidad;
import org.example.modelo.form.UsuarioForm;
import org.example.repositorios.interfaz.IUsuarioRepo;
import org.example.transaction.ISesionManager;


import java.util.List;
import java.util.Optional;

public class UsuarioRepoHibernate implements IUsuarioRepo {

    private final ISesionManager sesionManager;;

    public UsuarioRepoHibernate(ISesionManager sesionManager) {
        this.sesionManager = sesionManager;
    }

    @Override
    public Optional<UsuarioEntidad> crear(UsuarioForm form) {
        var sesion = sesionManager.getSession();

        UsuarioEntidad usuario = new UsuarioEntidad(
                0L, form.getnombreusuario(), form.getEmail(), form.getNombre(), form.getPais()
                , form.getFechaN(),
                form.getFechaRegis(),
                 form.getAvatar(), form.getSaldo(),form.getEstadoCuenta());

        sesion.persist(usuario);
        return Optional.of(usuario);
    }

    @Override
    public Optional<UsuarioEntidad> leerPorId(Long id) {
        var sesion = sesionManager.getSession();
        UsuarioEntidad usuario = sesion.find(UsuarioEntidad.class, id);

        return Optional.ofNullable(usuario);
    }

    @Override
    public Optional<UsuarioEntidad> leerPorNombre(String nombre) {
        var sesion = sesionManager.getSession();

        CriteriaBuilder cb = sesion.getCriteriaBuilder();
        CriteriaQuery<UsuarioEntidad> cq = cb.createQuery(UsuarioEntidad.class);
        Root<UsuarioEntidad> root = cq.from(UsuarioEntidad.class);
        cq.select(root).where(cb.equal(root.get("nombre_usuario"), nombre));

        return sesion.createQuery(cq).getResultStream().findFirst();
    }

    @Override
    public Optional<UsuarioEntidad> leerPorEmail(String email) {
        var sesion = sesionManager.getSession();

        CriteriaBuilder cb = sesion.getCriteriaBuilder();
        CriteriaQuery<UsuarioEntidad> cq = cb.createQuery(UsuarioEntidad.class);
        Root<UsuarioEntidad> root = cq.from(UsuarioEntidad.class);

        cq.select(root).where(cb.equal(root.get("email"), email));
        return sesion.createQuery(cq).getResultStream().findFirst();
    }

    @Override
    public List<UsuarioEntidad> leerTodo() {
        var sesion = sesionManager.getSession();

        CriteriaBuilder cb = sesion.getCriteriaBuilder();
        CriteriaQuery<UsuarioEntidad> cq = cb.createQuery(UsuarioEntidad.class);
        Root<UsuarioEntidad> root = cq.from(UsuarioEntidad.class);

        cq.select(root).orderBy(cb.asc(root.get("fecha_registro")));
        return sesion.createQuery(cq).getResultList();
    }

    @Override
    public void sumarSaldo(Long idUsuario, Double precio) {
        var sesion = sesionManager.getSession();
        Optional<UsuarioEntidad> usuarioOpt = leerPorId(idUsuario);

        if (usuarioOpt.isPresent()) {
            UsuarioEntidad usuario = usuarioOpt.orElse(null);

            double nuevoSaldo = usuario.getSaldo() + precio;
            UsuarioEntidad actualizado = new UsuarioEntidad(idUsuario, usuario.getNombreUsuario(), usuario.getEmail(),  usuario.getNombreRealU(),
                    usuario.getPais(), usuario.getFechaN(), usuario.getFechaRegis(), usuario.getAvatar(), nuevoSaldo,usuario.getEstadoCuenta());

            sesion.merge(actualizado);
        }
    }

    @Override
    public void restarSaldo(Long idUsuario, Double precio) {
        var sesion = sesionManager.getSession();
        Optional<UsuarioEntidad> usuarioOpt = leerPorId(idUsuario);

        if (usuarioOpt.isPresent()) {
            UsuarioEntidad usuario = usuarioOpt.orElse(null);

            double nuevoSaldo = usuario.getSaldo() - precio;
            UsuarioEntidad actualizado = new UsuarioEntidad(idUsuario, usuario.getNombreUsuario(), usuario.getEmail(),  usuario.getNombreRealU(),
                    usuario.getPais(), usuario.getFechaN(), usuario.getFechaRegis(), usuario.getAvatar(), nuevoSaldo,usuario.getEstadoCuenta());

            sesion.merge(actualizado);
        }
    }

    @Override
    public Optional<UsuarioEntidad> actualizar(Long id, UsuarioForm form) {
        var sesion = sesionManager.getSession();
        Optional<UsuarioEntidad> usuarioOpt = this.leerPorId(id);

        if (usuarioOpt.isEmpty()) {
            return Optional.empty();
        } else {
            sesion.merge(new UsuarioEntidad(
                    id, form.getnombreusuario(), form.getEmail(), form.getNombre(), form.getPais(), form.getFechaN(),
                    usuarioOpt.get().getFechaRegis(), form.getAvatar(), form.getSaldo(),form.getEstadoCuenta()
            ));
            return leerPorId(id);
        }
    }

    @Override
    public boolean borrar(Long id) {
        var sesion = sesionManager.getSession();
        Optional<UsuarioEntidad> usuarioOpt = this.leerPorId(id);

        if (usuarioOpt.isEmpty()) {
            return false;
        } else {
            sesion.remove(usuarioOpt.get());
            return true;
        }
    }
}
