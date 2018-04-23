package com.daisa;

import java.util.Objects;

/**
 * Aquí va la documentación
 * @author David Roig
 * @author Isabel Montero
 */
public class Puntuacion {
        private int id;
        private Equipo equipo;
        private Liga liga;
        private int puntos = 0;

    public Puntuacion(Equipo equipo, Liga liga) {
        this.equipo = equipo;
        this.liga = liga;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    public Liga getLiga() {
        return liga;
    }

    public void setLiga(Liga liga) {
        this.liga = liga;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Puntuacion that = (Puntuacion) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}
