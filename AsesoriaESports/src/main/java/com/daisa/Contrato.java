package com.daisa;

import java.util.Date;
import java.util.Objects;

/**
 * Aquí va la documentación
 *
 * @author David Roig
 * @author Isabel Montero
 */
public class Contrato {
    private int id;
    private Jugador jugador;
    private Equipo equipo;
    private Date fechaAlta;
    private Date fechaBaja = null;

    // Sobre date: en el momento que date es creado (new date()) se le da la fecha actual
    public Contrato(Jugador jugador, Equipo equipo) {
        this.jugador = jugador;
        this.equipo = equipo;
        this.fechaAlta = new Date();
    }

    public void terminar() {
        this.fechaBaja = new Date();
    }

    /**
     * @return Devuelve true si el contrato no ha sido terminado
     */
    public boolean isActive() {
        return (fechaBaja == null);
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

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public Date getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(Date fechaBaja) {
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
