package org.example.enumerados;

public enum ErrorTipo {
    REQUERIDO("El campo es obligatorio"),
    FORMATO_INVALIDO("El formato es inválido"),
    VALOR_DEMASIADO_ALTO("El valor es demasiado alto"),
    VALOR_DEMASIADO_BAJO("El valor es demasiado bajo"),
    NO_ENCONTRADO("No se encontró el elemento"),
    DUPLICADO("El elemento está duplicado"),
    PAIS_NO_VALIDO("Pais no encontrado");

    private final String mensaje;

    private ErrorTipo(String mensaje) {
        this.mensaje = mensaje;
    }
}
