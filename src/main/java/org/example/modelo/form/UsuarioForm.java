package org.example.modelo.form;

import org.example.enumerados.ErrorTipo;
import org.example.enumerados.EstadoCuenta;

import org.example.modelo.dto.ErrorDTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class UsuarioForm {


    public static final int CONTRA_RANGO = 8;
    public static final int NOMBRE_MIN = 2;
    public static final int NOMBRE_MAX = 50;
    public static final int AVATAR_MAX = 100;
    public double saldo;
    public EstadoCuenta estadoCuenta;
    private String nombreusuario;
    private String email;
    private String contrasenia;
    private String nombre;
    private String pais;
    private LocalDate fechaN;
    public LocalDate fechaRegis;
    private String avatar;
    private static final Set<String> PAISES_PERMITIDOS = Set.of("españa", "portugal", "francia", "italia");

    public UsuarioForm(String nombreusuario, String email, String contrasenia, String nombre, String pais, double saldo, LocalDate fechaN , LocalDate fechaRegis, String avatar) {
        this.nombreusuario = nombreusuario;
        this.email = email;
        this.contrasenia = contrasenia;
        this.nombre = nombre;
        this.pais = pais;
        this.saldo = saldo;
        this.fechaN = fechaN;
        this.fechaRegis = fechaRegis;
        this.avatar = avatar;
        this.estadoCuenta = EstadoCuenta.ACTIVA;
    }

    public LocalDate getFechaRegis() {
        return fechaRegis;
    }

    public double getSaldo() {
        return saldo;
    }

    public EstadoCuenta getEstadoCuenta() {
        return estadoCuenta;
    }

    public String getnombreusuario() {
        return nombreusuario;
    }

    public String getEmail() {
        return email;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public String getNombre() {
        return nombre;
    }

    public String getPais() {
        return pais;
    }

    public LocalDate getFechaN() {
        return fechaN;
    }

    public String getAvatar() {
        return avatar;
    }

    public List<ErrorDTO> validar() {
        List<ErrorDTO> errores = new ArrayList<>();
        String regex = "^[a-zA-Z_][a-zA-Z0-9_-]{2,19}$";



        if (nombreusuario == null || nombreusuario.isBlank()){
            errores.add(new ErrorDTO("nombres usuario", ErrorTipo.REQUERIDO));
        }else if (!nombreusuario.matches(regex)){
            errores.add(new ErrorDTO("nombres usuario", ErrorTipo.FORMATO_INVALIDO));
        }


        if (email == null || email.isBlank()) {
            errores.add(new ErrorDTO("email", ErrorTipo.REQUERIDO));
        } else if (!email.matches("^[\\w.+-]+@[\\w-]+\\.[\\w.]+$")) {
            errores.add(new ErrorDTO("email", ErrorTipo.FORMATO_INVALIDO));
        }


        if (contrasenia == null) {
            errores.add(new ErrorDTO("contraseña", ErrorTipo.REQUERIDO));
        } else if (contrasenia.length() < CONTRA_RANGO) {
            errores.add(new ErrorDTO("contraseña", ErrorTipo.FORMATO_INVALIDO));
        } else if (!contrasenia.matches(".*[A-Z].*") || !contrasenia.matches(".*[a-z].*") || !contrasenia.matches(".*[0-9].*")) {
            errores.add(new ErrorDTO("contraseña", ErrorTipo.FORMATO_INVALIDO));
        }

        if (nombre == null || nombre.isBlank()){
            errores.add(new ErrorDTO("nombre", ErrorTipo.REQUERIDO));
        } else if (nombre.length() < NOMBRE_MIN || nombre.length() > NOMBRE_MAX) {
            errores.add(new ErrorDTO("nombre", ErrorTipo.FORMATO_INVALIDO));
        }


        if (pais == null || pais.isBlank()) {
            errores.add(new ErrorDTO("pais", ErrorTipo.REQUERIDO));
        }else if (!PAISES_PERMITIDOS.contains(pais.toLowerCase())) {
            errores.add(new ErrorDTO("pais", ErrorTipo.FORMATO_INVALIDO));
        }

        if (fechaN == null ) {
            errores.add(new ErrorDTO("fecha nacimiento", ErrorTipo.REQUERIDO));
        } else {
            if (fechaN.isAfter(LocalDate.now())) {
                errores.add(new ErrorDTO("fechaNacimiento", ErrorTipo.FORMATO_INVALIDO));
            } else if (fechaN.isAfter(LocalDate.now().minusYears(13))) {
                errores.add(new ErrorDTO("fechaNacimiento", ErrorTipo.VALOR_DEMASIADO_ALTO));
            }
        }


        if (avatar != null && avatar.length() > AVATAR_MAX) {
            errores.add(new ErrorDTO("avatar", ErrorTipo.VALOR_DEMASIADO_ALTO));
        }


        return errores;


    }
}
