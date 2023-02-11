package es.josetesan.springbatchxmlparser;

public record Laboratorio(
    Integer codigolaboratorio,
    String laboratorio,
    String direccion,
    String codigopostal,
    String localidad,
    String cif) {
}
