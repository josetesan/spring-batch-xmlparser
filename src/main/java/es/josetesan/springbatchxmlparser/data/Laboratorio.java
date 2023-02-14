package es.josetesan.springbatchxmlparser.data;

import java.util.StringJoiner;

public class Laboratorio {
  Integer codigolaboratorio;
  String laboratorio;
  String direccion;
  String codigopostal;
  String localidad;
  String cif;

  public Laboratorio() {}

  public Laboratorio(
      Integer codigolaboratorio,
      String laboratorio,
      String direccion,
      String codigopostal,
      String localidad,
      String cif) {
    this.codigolaboratorio = codigolaboratorio;
    this.laboratorio = laboratorio;
    this.direccion = direccion;
    this.codigopostal = codigopostal;
    this.localidad = localidad;
    this.cif = cif;
  }

  public Integer getCodigolaboratorio() {
    return codigolaboratorio;
  }

  public String getLaboratorio() {
    return laboratorio;
  }

  public String getDireccion() {
    return direccion;
  }

  public String getCodigopostal() {
    return codigopostal;
  }

  public String getLocalidad() {
    return localidad;
  }

  public String getCif() {
    return cif;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", Laboratorio.class.getSimpleName() + "[", "]")
        .add("codigolaboratorio=" + codigolaboratorio)
        .add("laboratorio='" + laboratorio + "'")
        .add("direccion='" + direccion + "'")
        .add("codigopostal='" + codigopostal + "'")
        .add("localidad='" + localidad + "'")
        .add("cif='" + cif + "'")
        .toString();
  }
}
