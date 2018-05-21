package com.daisa;

import java.util.Objects;

/**
 * Clase Puntuacion, que liga Equipos y Ligas.
 * @author David Roig
 * @author Isabel Montero
 */
public class Puntuacion {
        private Equipo equipo;
        private Liga liga;
        private int puntos = 0;


    public Puntuacion(Equipo equipo, Liga liga, int puntos) {
        this.equipo = equipo;
        this.liga = liga;
        this.puntos = puntos;
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
        return Objects.equals(equipo, that.equipo) &&
                Objects.equals(liga, that.liga);
    }

    @Override
    public int hashCode() {

        return Objects.hash(equipo, liga);
    }
}
