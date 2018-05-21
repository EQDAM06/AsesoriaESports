package com.daisa;

import java.util.Objects;

/**
 * Clase para los contratos que vinculan a un jugador y su equipo.
 *
 * @author David Roig
 * @author Isabel Montero
 */
public class Contrato {
    private int id;
    private Jugador jugador;
    private Equipo equipo;
    private String fechaAlta;
    private String fechaBaja;

    public Contrato(int idContrato, Jugador jugador, Equipo equipo, String fechaAlta, String fechaBaja) {
        id = idContrato;
        this.jugador = jugador;
        this.equipo = equipo;
        this.fechaAlta = fechaAlta;
        this.fechaBaja = fechaBaja;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Jugador getJugador() {
        return jugador;
    }

    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    public String getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(String fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public String getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(String fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contrato contrato1 = (Contrato) o;
        return id == contrato1.id;
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}
