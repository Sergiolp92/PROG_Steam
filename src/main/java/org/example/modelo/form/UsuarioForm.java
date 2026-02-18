package org.example.modelo.form;

import org.example.enumerados.ErrorTipo;
import org.example.enumerados.EstadoCuenta;
import org.example.modelo.dto.ErrorDTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UsuarioForm {



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

    public UsuarioForm(String nombreusuario, String email, String contrasenia, String nombre, String pais, LocalDate fechaN ,LocalDate fechaRegis, String avatar) {
        this.nombreusuario = nombreusuario;
        this.email = email;
        this.contrasenia = contrasenia;
        this.nombre = nombre;
        this.pais = pais;
        this.fechaN = fechaN;
        this.avatar = avatar;
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
        } else if (!email.contains("@")){
            errores.add(new ErrorDTO("email", ErrorTipo.FORMATO_INVALIDO));
        }


        if (contrasenia == null) {
            errores.add(new ErrorDTO("contraseña", ErrorTipo.REQUERIDO));
        } else if (contrasenia.length() < 6 ) {
            errores.add(new ErrorDTO("contraseña", ErrorTipo.FORMATO_INVALIDO));
        }

        if (nombre == null || nombre.isBlank()){
            errores.add(new ErrorDTO("nombre", ErrorTipo.REQUERIDO));
        } else if (nombre.length() < 2 || nombre.length() > 50) {
            errores.add(new ErrorDTO("nombre", ErrorTipo.FORMATO_INVALIDO));
        }


        if (pais == null || pais.isBlank()) {
            errores.add(new ErrorDTO("pais", ErrorTipo.REQUERIDO));

        }

        if (fechaN == null ) {
            errores.add(new ErrorDTO("fecha nacimiento", ErrorTipo.REQUERIDO));

        }


        if (avatar != null && avatar.length() > 100) {
            errores.add(new ErrorDTO("avatar", ErrorTipo.VALOR_DEMASIADO_ALTO));
        }


        return errores;


    }
}
