package org.example.controlador;


import org.example.enumerados.ErrorTipo;
import org.example.enumerados.EstadoCuenta;
import org.example.excepciones.ValidationException;
import org.example.mapper.Mapper;
import org.example.modelo.dto.ErrorDTO;
import org.example.modelo.dto.UsuarioDTO;
import org.example.modelo.entidad.UsuarioEntidad;
import org.example.modelo.form.UsuarioForm;
import org.example.repositorios.interfaz.IUsuarioRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsuarioControlador  {

    private final IUsuarioRepo usuarioRepo;

    public UsuarioControlador(IUsuarioRepo usuarioRepo) {
        this.usuarioRepo = usuarioRepo;
    }

    /* Registrar nuevo usuario
     Descripción: Crear una nueva cuenta de usuario en la plataforma
     Entrada: Datos del formulario de registro (nombre de usuario, email, contraseña, nombre real,
                                                país, fecha de nacimiento)
     Salida: Usuario creado exitosamente o lista de errores de validación
     Validaciones: Aplicar todas las restricciones definidas en la sección de validación de Usuario
     */


    public Optional<UsuarioDTO> registrarUsuario(UsuarioForm usuarioForm) throws ValidationException {
        var errores = new ArrayList<ErrorDTO>();
        var usuarioOpt = usuarioRepo.leerPorNombre(usuarioForm.getnombreusuario());
        if (usuarioOpt.isPresent())
            errores.add(new ErrorDTO("Usuario", ErrorTipo.DUPLICADO));
        if (!errores.isEmpty())
            throw new ValidationException(errores);

        var usuarioC = usuarioRepo.crear(usuarioForm);

        var usuario = usuarioC.orElse(null);

        return Optional.ofNullable(Mapper.mapFromUsuario(usuario));




    }

    /*Consultar perfil
    Descripción: Mostrar la información de un usuario específico
    Entrada: ID o nombre del usuario a consultar
    Salida: Información del perfil del usuario o mensaje de acceso denegado
    Información mostrada: Nombre de usuario, avatar, país, fecha de registro, biblioteca y
    estadísticas de juego
    */

    public Optional<UsuarioDTO> consultarUsuario(Long id) throws ValidationException {
        var errores = new ArrayList<ErrorDTO>();
        var usuarioOpt = usuarioRepo.leerPorId(id);
        if (usuarioOpt.isEmpty()) {
            errores.add(new ErrorDTO("id", ErrorTipo.NO_ENCONTRADO));
        }

        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }

        var usuario = usuarioOpt.get();
        return Optional.of(Mapper.mapFromUsuario(usuario));
    }

    /*Añadir saldo a cartera
    Descripción: Recargar dinero en la cartera virtual de Steam del usuario
    Entrada: ID del usuario, cantidad a añadir
    Salida: Nuevo saldo de la cartera o mensaje de error
    Validaciones: Cantidad > 0, cuenta activa, rango entre 5.00 y 500.00
    */


    public void aniadirSaldo(long id, Double cantidad) throws ValidationException {
        List<ErrorDTO> errores = new ArrayList<>();

        var usuarioOpt = usuarioRepo.leerPorId(id);
        var usuario = usuarioOpt.orElse(null);

        if(usuario == null){
            errores.add(new ErrorDTO("Usuario", ErrorTipo.REQUERIDO));
            throw new ValidationException(errores);}


        if(usuario.getEstadoCuenta() != EstadoCuenta.ACTIVA){
            errores.add((new ErrorDTO("Cuenta", ErrorTipo.CUENTA)));
             }

        if(cantidad < 5.00 || cantidad > 500.00){
        errores.add(new ErrorDTO("Saldo", ErrorTipo.FUERA_DE_RANGO));
        }

        if (!errores.isEmpty()){
            throw new ValidationException(errores);
        }

        double saldoNuevo = usuario.getSaldo() + cantidad;

        usuarioRepo.actualizar(id,new UsuarioForm(usuario.getNombreUsuario(),usuario.getEmail(),usuario.getContrasenia(),usuario.getNombreRealU(),
                usuario.getPais(),saldoNuevo,usuario.getFechaN(),usuario.getFechaRegis(),usuario.getAvatar());))





    }
/*Consultar saldo

    Descripción: Mostrar el saldo disponible en la cartera Steam de un usuario
    Entrada: ID del usuario
    Salida: Saldo actual de la cartera (ejemplo: "45.67 €")
    Validaciones: Usuario debe existir en el sistema*/


    public Optional<UsuarioDTO> consultarSaldo(long id) throws ValidationException {
        return Optional.empty();
    }










}
