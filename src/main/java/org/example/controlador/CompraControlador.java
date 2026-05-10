package org.example.controlador;

import org.example.enumerados.*;
import org.example.excepciones.ValidationException;
import org.example.mapper.Mapper;
import org.example.modelo.dto.CompraDTO;
import org.example.modelo.dto.ErrorDTO;
import org.example.modelo.entidad.CompraEntidad;
import org.example.modelo.form.BibliotecaForm;
import org.example.modelo.form.CompraForm;
import org.example.repositorios.interfaz.IBibliotecaRepo;
import org.example.repositorios.interfaz.ICompraRepo;
import org.example.repositorios.interfaz.IJuegosRepo;
import org.example.repositorios.interfaz.IUsuarioRepo;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CompraControlador {

    private ICompraRepo compraRepo;
    private IJuegosRepo juegosRepo;
    private IUsuarioRepo usuarioRepo;
    ;
    private IBibliotecaRepo bibliotecaRepo;

    public CompraControlador(ICompraRepo compraRepo, IJuegosRepo juegosRepo, IUsuarioRepo usuarioRepo, IBibliotecaRepo bibliotecaRepo) {
        this.compraRepo = compraRepo;
        this.juegosRepo = juegosRepo;
        this.usuarioRepo = usuarioRepo;
        this.bibliotecaRepo = bibliotecaRepo;
    }

    /* Realizar compra
        Descripción: Crear una nueva transacción para adquirir un juego
        Entrada: ID del usuario, ID del juego, método de pago
        Salida: ID de compra creada o mensaje de error
        Validaciones: Usuario activo, juego comprable, no duplicado, saldo suficiente si usa cartera*/
    public CompraDTO realizarCompra(CompraForm form) throws ValidationException {

        List<ErrorDTO> errores = new ArrayList<>();

        var usuarioOpt = usuarioRepo.leerPorId(form.getIdUsuario());
        var juegoOpt = juegosRepo.leerPorId(form.getIdJuego());


        if (usuarioOpt.isEmpty()) {
            errores.add(new ErrorDTO("id", ErrorTipo.NO_ENCONTRADO));
        }
        if (usuarioOpt.get().getEstadoCuenta() != EstadoCuenta.ACTIVA) {
            errores.add(new ErrorDTO("usuario", ErrorTipo.CUENTA));
        }
        if (juegoOpt.isEmpty()) {
            errores.add(new ErrorDTO("juego", ErrorTipo.NO_ENCONTRADO));
        }
        if (juegoOpt.get().getEstadoJuego() != EstadoJuego.DISPONIBLE) {
            errores.add(new ErrorDTO("juego", ErrorTipo.JUEGO_NO_DISPONIBLE));
        }
        if (bibliotecaRepo.leerJuegoUsuario(form.getIdUsuario(), form.getIdJuego()).isPresent()) {
            errores.add(new ErrorDTO("compra", ErrorTipo.DUPLICADO));
        }
        if (form.getMetodoPago().equals(MetodoPago.CARTERA_STEAM)) {
            if (form.getPrecioFinal() > usuarioOpt.get().getSaldo()) {
                errores.add(new ErrorDTO("saldo", ErrorTipo.SALDO_INSUFICIENTE));
            }
        }


        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }

        Optional<CompraEntidad> compraOpt = compraRepo.crear(new CompraForm(
                form.getIdUsuario(),
                form.getIdJuego(),
                form.getFechaC(),
                form.getMetodoPago(),
                form.getPrecioOriginal(),
                form.getPrecioFinal(),
                form.getDescuento(),
                EstadoCompra.PENDIENTE
        ));


        CompraEntidad compra = compraOpt.get();

        return Mapper.mapFromCompra(compra, Mapper.mapFromUsuario(usuarioOpt.get()), Mapper.mapFromJuego(juegoOpt.get()));
    }

    /*
    Procesar pago
    Descripción: Completar la transacción con el método de pago seleccionado
    Entrada: ID de compra, datos de pago según el método
    Salida: Confirmación de pago o mensaje de error
    Validaciones: Compra existe, estado válido para procesar, pago válido*/
    public CompraDTO procesarPago(long idCompra) throws ValidationException{
        List<ErrorDTO> errores = new ArrayList<>();
        var compraOpt = compraRepo.leerPorId(idCompra);


        if(compraOpt.isEmpty()){
            errores.add(new ErrorDTO("compra",ErrorTipo.NO_ENCONTRADO));
        }
        if(compraOpt.get().getEstadoCompra() != EstadoCompra.PENDIENTE || compraOpt.get().getEstadoCompra() == null){
            errores.add(new ErrorDTO("compra",ErrorTipo.COMPRA_REALIZADA));
        }

        var usuarioOpt = usuarioRepo.leerPorId(compraOpt.get().getIdUsuario());
        var juegoOpt = juegosRepo.leerPorId(compraOpt.get().getIdJuego());
        double nuevoSaldo = usuarioOpt.get().getSaldo() - compraOpt.get().getPrecioFinal();
        if(compraOpt.get().getMetodoPago() == MetodoPago.CARTERA_STEAM){
            if(compraOpt.get().getPrecioFinal() > usuarioOpt.get().getSaldo()) {
                errores.add(new ErrorDTO("saldo", ErrorTipo.SALDO_INSUFICIENTE));
                throw new ValidationException(errores);
            }
            usuarioRepo.restarSaldo(compraOpt.get().getIdUsuario(), compraOpt.get().getPrecioFinal());
        }
        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }


        var compraActualizada = compraRepo.actualizar(compraOpt.get().getIdCompra(), new CompraForm(
                compraOpt.get().getIdUsuario(),
                compraOpt.get().getIdJuego(),
                compraOpt.get().getFechaC(),
                compraOpt.get().getMetodoPago(),
                compraOpt.get().getPrecioOriginal(),
                compraOpt.get().getPrecioFinal(),
                (int)((compraOpt.get().getPrecioFinal()) -(compraOpt.get().getPrecioOriginal())),
                EstadoCompra.COMPLETADA

        ));

        BibliotecaForm bibliotecaForm = new BibliotecaForm(compraOpt.get().getIdUsuario(), compraOpt.get().getIdJuego(), LocalDate.now(),0.0f,null);
        return Mapper.mapFromCompra(compraActualizada.get(), Mapper.mapFromUsuario(usuarioOpt.get()), Mapper.mapFromJuego(juegoOpt.get()));


    }

    /*
    Consultar detalles de compra
    Descripción: Ver información completa de una transacción específica
    Entrada: ID de compra, ID del usuario (para verificar pertenencia)
    Salida: Información detallada de la compra o compra no encontrada
    Datos mostrados: Todos los campos de compra, información del juego, factura/recibo */
    public CompraDTO consultarDetallesCompra(long idCompra, long idUsuario) throws ValidationException {
        List<ErrorDTO> errores = new ArrayList<>();
        var compraOpt = compraRepo.leerPorId(idCompra);
        if(compraOpt.isEmpty()){
            errores.add(new ErrorDTO("compra",ErrorTipo.NO_ENCONTRADO));
        }
        if(compraOpt.get().getIdUsuario() != idUsuario){
            errores.add(new ErrorDTO("usuario",ErrorTipo.NO_COINCIDE));
        }
        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }
        var usuarioOpt = usuarioRepo.leerPorId(compraOpt.get().getIdUsuario());
        var juegoOpt = juegosRepo.leerPorId(compraOpt.get().getIdJuego());

        return Mapper.mapFromCompra(compraOpt.orElse(null),Mapper.mapFromUsuario(usuarioOpt.orElse(null)),Mapper.mapFromJuego(juegoOpt.orElse(null)));
    }
    /*
    Solicitar reembolso
    Descripción: Devolver una compra y reintegrar el dinero a la cartera
    Entrada: ID de compra, motivo del reembolso
    Salida: Confirmación de reembolso con nuevo saldo o mensaje de denegación
    Validaciones: Compra completada, dentro del plazo, pocas horas jugadas*/

    public CompraDTO solicitarReembolso(long idCompra) throws ValidationException{
        List<ErrorDTO> errores = new ArrayList<>();
        var compraOpt = compraRepo.leerPorId(idCompra);
        if(compraOpt.isEmpty()){
            errores.add(new ErrorDTO("compra",ErrorTipo.NO_ENCONTRADO));
        }
        if(compraOpt.get().getEstadoCompra() != EstadoCompra.COMPLETADA){
            errores.add(new ErrorDTO("compra",ErrorTipo.COMPRA_NO_REALIZADA));
        }
       if (LocalDate.now().isAfter(compraOpt.get().getFechaDeCompra().plusDays(20))) {
            errores.add(new ErrorDTO("compra", ErrorTipo.PLAZO_VENCIDO));
            throw new ValidationException(errores);
        }

       var bibliotecaOpt = bibliotecaRepo.leerJuegoUsuario(compraOpt.get().getIdUsuario(), compraOpt.get().getIdJuego());

       if(bibliotecaOpt.get().getTiempoTotalJugado() > 2.0f){
           errores.add(new ErrorDTO("compra", ErrorTipo.PLAZO_VENCIDO));
           throw new ValidationException(errores);
       }



        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }

        double reembolso = compraOpt.get().getPrecioFinal();
        usuarioRepo.sumarSaldo(compraOpt.get().getIdUsuario(), reembolso);
        compraRepo.actualizar( compraOpt.get().getIdCompra(), new CompraForm(
                compraOpt.get().getIdUsuario(),
                compraOpt.get().getIdJuego(),
                compraOpt.get().getFechaC(),
                compraOpt.get().getMetodoPago(),
                compraOpt.get().getPrecioOriginal(),
                compraOpt.get().getPrecioFinal(),
                (int)((compraOpt.get().getPrecioFinal()) -(compraOpt.get().getPrecioOriginal())),
                EstadoCompra.REEMBOLSADA)

        );
        var usuarioOpt = usuarioRepo.leerPorId(compraOpt.get().getIdUsuario());
        var juegoOpt = juegosRepo.leerPorId(compraOpt.get().getIdJuego());

        return Mapper.mapFromCompra(compraOpt.orElse(null),Mapper.mapFromUsuario(usuarioOpt.orElse(null)),Mapper.mapFromJuego(juegoOpt.orElse(null)));

    }
    }