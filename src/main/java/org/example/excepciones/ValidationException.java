package org.example.excepciones;

import org.example.modelo.dto.ErrorDTO;

import java.util.List;


public class ValidationException extends Exception {


    private List<ErrorDTO> errores;

    public ValidationException(List<ErrorDTO> errores) {
        super("Errores de validaci�n");
        this.errores = errores;
    }
    public List<ErrorDTO> getErrores() {
        return errores;
    }

}