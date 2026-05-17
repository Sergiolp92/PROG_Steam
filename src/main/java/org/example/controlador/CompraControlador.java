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
import org.example.transaction.ITransactionManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CompraControlador {

    public static final float TIEMPO_DE_JUEGO = 0.0f;
    public static final int DIAS_MAX = 20;
    public static final float HORAS_MAX = 2.0f;
    private ICompraRepo compraRepo;
    private IJuegosRepo juegosRepo;
    private IUsuarioRepo usuarioRepo;
    private IBibliotecaRepo bibliotecaRepo;
    private ITransactionManager tm;

    public CompraControlador(ICompraRepo compraRepo, IJuegosRepo juegosRepo, IUsuarioRepo usuarioRepo, IBibliotecaRepo bibliotecaRepo, ITransactionManager tm) {
        this.compraRepo = compraRepo;
        this.juegosRepo = juegosRepo;
        this.usuarioRepo = usuarioRepo;
        this.bibliotecaRepo = bibliotecaRepo;
        this.tm = tm;
    }


    /**
     * Realizar compra
     * @param form
     * @return CompraDTO con detalles de la compra realizada
     * @throws ValidationException
     */
    public CompraDTO realizarCompra(CompraForm form) throws ValidationException {
        var errores = form.validar();

        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }

        var resultado = tm.inTransaction(() -> {
            var usuarioOpt = usuarioRepo.leerPorId(form.getIdUsuario());
            var juegoOpt = juegosRepo.leerPorId(form.getIdJuego());

            if (usuarioOpt.isEmpty()) {
                errores.add(new ErrorDTO("id", ErrorTipo.NO_ENCONTRADO));
            } else if (usuarioOpt.get().getEstadoCuenta() != EstadoCuenta.ACTIVA) {
                errores.add(new ErrorDTO("usuario", ErrorTipo.CUENTA));
            }

            if (juegoOpt.isEmpty()) {
                errores.add(new ErrorDTO("juego", ErrorTipo.NO_ENCONTRADO));
            } else if (juegoOpt.get().getEstadoJuego() != EstadoJuego.DISPONIBLE) {
                errores.add(new ErrorDTO("juego", ErrorTipo.JUEGO_NO_DISPONIBLE));
            }

            if (form.getIdUsuario() != null && form.getIdJuego() != null) {
                if (bibliotecaRepo.leerJuegoUsuario(form.getIdUsuario(), form.getIdJuego()).isPresent()) {
                    errores.add(new ErrorDTO("compra", ErrorTipo.DUPLICADO));
                }
            }

            if (MetodoPago.CARTERA_STEAM.equals(form.getMetodoPago()) && usuarioOpt.isPresent()) {
                if (form.getPrecioFinal() > usuarioOpt.get().getSaldo()) {
                    errores.add(new ErrorDTO("saldo", ErrorTipo.SALDO_INSUFICIENTE));
                }
            }

            if (!errores.isEmpty()) {
                throw new IllegalArgumentException();
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

            return Mapper.mapFromCompra(compra,
                    Mapper.mapFromUsuario(usuarioOpt.get()),
                    Mapper.mapFromJuego(juegoOpt.get()));
        });

        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }

        return resultado;
    }
    /*
    Procesar pago
    Descripción: Completar la transacción con el método de pago seleccionado
    Entrada: ID de compra, datos de pago según el método
    Salida: Confirmación de pago o mensaje de error
    Validaciones: Compra existe, estado válido para procesar, pago válido*/


    /**
     * Procesar pago
     * @param idCompra
     * @return CompraDTO con detalles de la compra procesada
     * @throws ValidationException
     */
    public CompraDTO procesarPago(long idCompra) throws ValidationException {
        List<ErrorDTO> errores = new ArrayList<>();

        var resultado = tm.inTransaction(() -> {
            var compraOpt = compraRepo.leerPorId(idCompra);

            if (compraOpt.isEmpty()) {
                errores.add(new ErrorDTO("compra", ErrorTipo.NO_ENCONTRADO));
                throw new IllegalArgumentException();
            }
            if (compraOpt.get().getEstadoCompra() != EstadoCompra.PENDIENTE) {
                errores.add(new ErrorDTO("compra", ErrorTipo.COMPRA_REALIZADA));
            }

            var usuarioOpt = usuarioRepo.leerPorId(compraOpt.get().getIdUsuario());
            var juegoOpt = juegosRepo.leerPorId(compraOpt.get().getIdJuego());

            if (compraOpt.get().getMetodoPago() == MetodoPago.CARTERA_STEAM) {
                if (compraOpt.get().getPrecioFinal() > usuarioOpt.get().getSaldo()) {
                    errores.add(new ErrorDTO("saldo", ErrorTipo.SALDO_INSUFICIENTE));
                }
            }

            if (!errores.isEmpty()) {
                throw new IllegalArgumentException();
            }

            if (compraOpt.get().getMetodoPago() == MetodoPago.CARTERA_STEAM) {
                usuarioRepo.restarSaldo(compraOpt.get().getIdUsuario(), compraOpt.get().getPrecioFinal());
            }

            var compraActualizada = compraRepo.actualizar(compraOpt.get().getIdCompra(), new CompraForm(
                    compraOpt.get().getIdUsuario(),
                    compraOpt.get().getIdJuego(),
                    compraOpt.get().getFechaC(),
                    compraOpt.get().getMetodoPago(),
                    compraOpt.get().getPrecioOriginal(),
                    compraOpt.get().getPrecioFinal(),
                    (int) ((compraOpt.get().getPrecioFinal()) - (compraOpt.get().getPrecioOriginal())),
                    EstadoCompra.COMPLETADA
            ));

            BibliotecaForm bibliotecaForm = new BibliotecaForm(
                    compraOpt.get().getIdUsuario(),
                    compraOpt.get().getIdJuego(),
                    LocalDate.now(), TIEMPO_DE_JUEGO, null);
            bibliotecaRepo.crear(bibliotecaForm);

            return Mapper.mapFromCompra(compraActualizada.get(),
                    Mapper.mapFromUsuario(usuarioOpt.get()),
                    Mapper.mapFromJuego(juegoOpt.get()));
        });

        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }

        return resultado;
    }



    /**
     * Consultar detalles de compra
     * @param idCompra
     * @param idUsuario
     * @return CompraDTO con detalles de la compra consultada
     * @throws ValidationException
     */

    public CompraDTO consultarDetallesCompra(long idCompra, long idUsuario) throws ValidationException {
        List<ErrorDTO> errores = new ArrayList<>();

        var resultado = tm.inTransaction(() -> {
            var compraOpt = compraRepo.leerPorId(idCompra);

            if (compraOpt.isEmpty()) {
                errores.add(new ErrorDTO("compra", ErrorTipo.NO_ENCONTRADO));
                throw new IllegalArgumentException();
            }
            if (compraOpt.get().getIdUsuario() != idUsuario) {
                errores.add(new ErrorDTO("usuario", ErrorTipo.NO_COINCIDE));
                throw new IllegalArgumentException();
            }

            var usuarioOpt = usuarioRepo.leerPorId(compraOpt.get().getIdUsuario());
            var juegoOpt = juegosRepo.leerPorId(compraOpt.get().getIdJuego());

            return Mapper.mapFromCompra(
                    compraOpt.orElse(null),
                    Mapper.mapFromUsuario(usuarioOpt.orElse(null)),
                    Mapper.mapFromJuego(juegoOpt.orElse(null)));
        });

        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }

        return resultado;
    }


    /**
     * Solicitar reembolso
     * @param idCompra
     * @return CompraDTO con detalles de la compra reembolsada
     * @throws ValidationException
     */

    public CompraDTO solicitarReembolso(long idCompra) throws ValidationException {
        List<ErrorDTO> errores = new ArrayList<>();

        var resultado = tm.inTransaction(() -> {
            var compraOpt = compraRepo.leerPorId(idCompra);

            if (compraOpt.isEmpty()) {
                errores.add(new ErrorDTO("compra", ErrorTipo.NO_ENCONTRADO));
                throw new IllegalArgumentException();
            }
            if (compraOpt.get().getEstadoCompra() != EstadoCompra.COMPLETADA) {
                errores.add(new ErrorDTO("compra", ErrorTipo.COMPRA_NO_REALIZADA));
            }
            if (LocalDate.now().isAfter(compraOpt.get().getFechaDeCompra().plusDays(DIAS_MAX))) {
                errores.add(new ErrorDTO("compra", ErrorTipo.PLAZO_VENCIDO));
            }

            var bibliotecaOpt = bibliotecaRepo.leerJuegoUsuario(compraOpt.get().getIdUsuario(), compraOpt.get().getIdJuego());

            if (bibliotecaOpt.isPresent() && bibliotecaOpt.get().getTiempoTotalJugado() > HORAS_MAX) {
                errores.add(new ErrorDTO("compra", ErrorTipo.PLAZO_VENCIDO));
            }

            if (!errores.isEmpty()) {
                throw new IllegalArgumentException();
            }

            usuarioRepo.sumarSaldo(compraOpt.get().getIdUsuario(), compraOpt.get().getPrecioFinal());

            compraRepo.actualizar(compraOpt.get().getIdCompra(), new CompraForm(
                    compraOpt.get().getIdUsuario(),
                    compraOpt.get().getIdJuego(),
                    compraOpt.get().getFechaC(),
                    compraOpt.get().getMetodoPago(),
                    compraOpt.get().getPrecioOriginal(),
                    compraOpt.get().getPrecioFinal(),
                    (int) ((compraOpt.get().getPrecioFinal()) - (compraOpt.get().getPrecioOriginal())),
                    EstadoCompra.REEMBOLSADA
            ));

            var usuarioOpt = usuarioRepo.leerPorId(compraOpt.get().getIdUsuario());
            var juegoOpt = juegosRepo.leerPorId(compraOpt.get().getIdJuego());

            return Mapper.mapFromCompra(
                    compraOpt.orElse(null),
                    Mapper.mapFromUsuario(usuarioOpt.orElse(null)),
                    Mapper.mapFromJuego(juegoOpt.orElse(null)));
        });

        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }

        return resultado;
    }
    }