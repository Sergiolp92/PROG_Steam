package org.example.controlador;


import org.example.enumerados.ErrorTipo;
import org.example.enumerados.EstadoCuenta;

import org.example.excepciones.ValidationException;
import org.example.mapper.Mapper;
import org.example.modelo.dto.ErrorDTO;
import org.example.modelo.dto.UsuarioDTO;
import org.example.modelo.form.UsuarioForm;
import org.example.repositorios.enMemoria.UsuariosRepo;
import org.example.repositorios.interfaz.IUsuarioRepo;
import org.example.transaction.ITransactionManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsuarioControlador {

    public static final double CARTERA_0 = 0.00;
    public static final double CANTIDAD_MIN = 5.00;
    public static final double CANTIDAD_MAX = 500.00;
    private final IUsuarioRepo usuarioRepo;
    private ITransactionManager tm;

    public UsuarioControlador(IUsuarioRepo usuarioRepo, ITransactionManager tm) {
        this.usuarioRepo = usuarioRepo;
        this.tm= tm;
    }


    /**
     * Registrar un nuevo usuario en la plataforma
     * @param usuarioForm
     * @return Optional con el UsuarioDTO que contiene la información del usuario registrado
     * @throws ValidationException
     */
    public Optional<UsuarioDTO> registrarUsuario(UsuarioForm usuarioForm) throws ValidationException {
        var errores = usuarioForm.validar();
        if (!errores.isEmpty())
            throw new ValidationException(errores);

        var usuarioC = tm.inTransaction(() -> {
            var usuarioOpt = usuarioRepo.leerPorNombre(usuarioForm.getnombreusuario());
            var emailOpt = usuarioRepo.leerPorEmail(usuarioForm.getEmail());

            if (usuarioOpt.isPresent())
                errores.add(new ErrorDTO("Usuario", ErrorTipo.DUPLICADO));
            if (emailOpt.isPresent())
                errores.add(new ErrorDTO("Email", ErrorTipo.DUPLICADO));
            if (!errores.isEmpty())
                throw new IllegalArgumentException();

            return usuarioRepo.crear(usuarioForm);
        });

        if (!errores.isEmpty())
            throw new ValidationException(errores);

        return Optional.ofNullable(Mapper.mapFromUsuario(usuarioC.orElse(null)));
    }


    /**
     * Consultar el perfil de un usuario por su ID
     * @param id
     * @return Optional con el UsuarioDTO que contiene la información del perfil del usuario
     * @throws ValidationException
     */

    public Optional<UsuarioDTO> consultarUsuario(Long id) throws ValidationException {
        var errores = new ArrayList<ErrorDTO>();

        var usuarioOpt = tm.inTransaction(() -> usuarioRepo.leerPorId(id));

        if (usuarioOpt.isEmpty()) {
            errores.add(new ErrorDTO("id", ErrorTipo.NO_ENCONTRADO));
        }
        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }

        return usuarioOpt.map(Mapper::mapFromUsuario);
    }




    /**
     * Añadir saldo a la cartera de un usuario
     * @param id
     * @param cantidad
     * @return Optional con el UsuarioDTO que contiene el saldo actualizado
     * @throws ValidationException
     */


    public Optional<UsuarioDTO> aniadirSaldo(long id, float cantidad) throws ValidationException {
        List<ErrorDTO> errores = new ArrayList<>();

        if (cantidad < CARTERA_0) {
            errores.add(new ErrorDTO("Saldo", ErrorTipo.FORMATO_INVALIDO));
        }
        if (cantidad < CANTIDAD_MIN || cantidad > CANTIDAD_MAX) {
            errores.add(new ErrorDTO("Saldo", ErrorTipo.FUERA_DE_RANGO));
        }

        var usuarioActualizado = tm.inTransaction(() -> {
            var usuarioOpt = usuarioRepo.leerPorId(id);

            if (usuarioOpt.isEmpty()) {
                errores.add(new ErrorDTO("Usuario", ErrorTipo.REQUERIDO));
            }
            if (usuarioOpt.isPresent() && usuarioOpt.get().getEstadoCuenta() != EstadoCuenta.ACTIVA) {
                errores.add(new ErrorDTO("Cuenta", ErrorTipo.CUENTA));
            }
            if (!errores.isEmpty()) {
                throw new IllegalArgumentException();
            }

            var u = usuarioOpt.get();
            double saldoNuevo = u.getSaldo() + cantidad;

            return usuarioRepo.actualizar(id, new UsuarioForm(
                    u.getNombreUsuario(),
                    u.getEmail(),
                    u.getContrasenia(),
                    u.getNombreRealU(),
                    u.getPais(),
                    saldoNuevo,
                    u.getFechaN(),
                    u.getFechaRegis(),
                    u.getAvatar()
            ));
        });

        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }

        return Optional.ofNullable(Mapper.mapFromUsuario(usuarioActualizado.orElse(null)));
    }


    /**
     * Consultar el saldo de un usuario por su ID
     * @param id
     * @return Optional con el UsuarioDTO que contiene el saldo actualizado
     * @throws ValidationException
     */


    public Optional<UsuarioDTO> consultarSaldo(long id) throws ValidationException {
        List<ErrorDTO> errores = new ArrayList<>();

        var usuarioOpt = tm.inTransaction(() -> usuarioRepo.leerPorId(id));

        if (usuarioOpt.isEmpty()) {
            errores.add(new ErrorDTO("id", ErrorTipo.NO_ENCONTRADO));
            throw new ValidationException(errores);
        }

        return Optional.of(Mapper.mapFromUsuario(usuarioOpt.get()));
    }


}
