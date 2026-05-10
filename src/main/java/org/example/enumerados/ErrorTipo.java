package org.example.enumerados;

public enum ErrorTipo {
    REQUERIDO("El campo es obligatorio"),
    FORMATO_INVALIDO("El formato es inválido"),
    VALOR_DEMASIADO_ALTO("El valor es demasiado alto"),
    VALOR_DEMASIADO_BAJO("El valor es demasiado bajo"),
    NO_ENCONTRADO("No se encontró el elemento"),
    DUPLICADO("El elemento está duplicado"),
    PAIS_NO_VALIDO("Pais no encontrado"),
    FUERA_DE_RANGO("Valor fuera de rango"),
    CUENTA("La cuenta está bloqueada o desactivada"),
    BUSQUEDA_INVALIDA("La búsqueda es inválida"),
    VALOR_INVALIDO("El valor es inválido"),
    JUEGO_NO_DISPONIBLE("El juego no está disponible para compra"),
    SALDO_INSUFICIENTE("El saldo es insuficiente para realizar la compra"),
    COMPRA_REALIZADA("La compra ya ha sido realizada"),
    NO_COINCIDE("El valor no coincide con el esperado"),
    COMPRA_NO_REALIZADA("La compra no ha sido realizada"),
    PLAZO_VENCIDO("El plazo para reembolsar la compra ha vencido");


    private final String mensaje;

    private ErrorTipo(String mensaje) {
        this.mensaje = mensaje;
    }
}
