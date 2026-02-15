package org.example.modelo.form;

import org.example.enumerados.ErrorTipo;
import org.example.modelo.dto.ErrorDTO;

import java.util.ArrayList;
import java.util.List;

public class UsuarioForm {

    private String nick;
    private String email;
    private String contrasenia;
    private String nombre;
    private String pais;
    private String fechaN;
    private String avatar;

    public UsuarioForm(String nick, String email, String contrasenia, String nombre, String pais, String fechaN , String avatar) {
        this.nick = nick;
        this.email = email;
        this.contrasenia = contrasenia;
        this.nombre = nombre;
        this.pais = pais;
        this.fechaN = fechaN;
        this.avatar = avatar;
    }

    public List<ErrorDTO> validar() {
        List<ErrorDTO> errores = new ArrayList<>();
        String regex = "^[a-zA-Z_][a-zA-Z0-9_-]{2,19}$";


        if (nick == null || nick.isBlank()){
            errores.add(new ErrorDTO("nick", ErrorTipo.REQUERIDO));
        }else if (!nick.matches(regex)){
            errores.add(new ErrorDTO("nick", ErrorTipo.FORMATO_INVALIDO));
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

        if (fechaN == null || fechaN.isBlank()) {
            errores.add(new ErrorDTO("fecha nacimiento", ErrorTipo.REQUERIDO));

        }


        if (avatar != null && avatar.length() > 100) {
            errores.add(new ErrorDTO("avatar", ErrorTipo.VALOR_DEMASIADO_ALTO));
        }


        return errores;


    }
}
