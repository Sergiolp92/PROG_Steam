package org.example.modelo.dto;

import org.example.enumerados.ErrorTipo;

public class ErrorDTO {

    private String campo;
    private ErrorTipo mensaje;



    public ErrorDTO(String campo, ErrorTipo mensaje) {
        this.campo = campo;
        this.mensaje = mensaje;
    }

    public String getCampo() {
        return campo;
    }

    public ErrorTipo getMensaje() {
        return mensaje;
    }




}
