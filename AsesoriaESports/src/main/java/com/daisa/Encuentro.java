package com.daisa;

import java.util.Objects;

/**
 * Clase Encuentro, perteneciente a Jornada.
 * @author David Roig
 * @author Isabel Montero
 */
public class Encuentro {
    private int id;
    private Jornada jornada;
    private int ganador;
    private Equipo[] equipos;
    private Liga liga;


    public Encuentro(Jornada jornada, Liga liga) {
        this.jornada = jornada;
        this.liga = liga;
    }


    public Encuentro(int id, Jornada jornada, int ganador, Equipo[] equipos, Liga liga) {
        this.id = id;
        this.jornada = jornada;
        this.ganador = ganador;
        this.equipos = equipos;
        this.liga = liga;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Jornada getJornada() {
        return jornada;
    }

    public void setJornada(Jornada jornada) {
        this.jornada = jornada;
    }

    public int getGanador() {
        return ganador;
    }

    public void setGanador(int ganador) {
        this.ganador = ganador;
    }

    public Equipo[] getEquipos() {
        return equipos;
    }

    public void setEquipos(Equipo equipo1, Equipo equipo2) {
        this.equipos = new Equipo[] {equipo1,equipo2};
    }

    public Liga getLiga() {
        return liga;
    }

    public void setLiga(Liga liga) {
        this.liga = liga;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Encuentro encuentro = (Encuentro) o;
        return id == encuentro.id;
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}