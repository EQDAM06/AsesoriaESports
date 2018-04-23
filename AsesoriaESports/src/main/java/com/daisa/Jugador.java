package com.daisa;

import java.util.Objects;

/**
 * Aquí va la documentación
 * @author David Roig
 * @author Isabel Montero
 */
public class Jugador {
    private String dni;
    private String nombre;
    private String nickname;
    private double sueldo;
    private String telefono;
    private Equipo equipo;
    private Contrato contrato;

    public Jugador(String dni, String nombre, String nickname, double sueldo, String telefono) {
        this.dni = dni;
        this.nombre = nombre;
        this.nickname = nickname;
        this.sueldo = sueldo;
        this.telefono = telefono;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public double getSueldo() {
        return sueldo;
    }

    public void setSueldo(double sueldo) {
        this.sueldo = sueldo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    public Contrato getContrato() {
        return contrato;
    }

    public void setContrato(Contrato contrato) {
        this.contrato = contrato;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Jugador jugador = (Jugador) o;
        return Objects.equals(dni, jugador.dni);
    }

    @Override
    public int hashCode() {

        return Objects.hash(dni);
    }
}
