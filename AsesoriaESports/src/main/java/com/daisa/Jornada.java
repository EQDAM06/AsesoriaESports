package com.daisa;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Clase Jornada, perteneciente a una Liga y que contiene Encuentros.
 *
 * @author David Roig
 * @author Isabel Montero
 */
public class Jornada {
    private Liga liga;
    private List<Encuentro> encuentros;
    private int dia;
    private int id;

    public Jornada(Liga liga, int dia) {
        this.liga = liga;
        this.dia = dia;
    }

    public Jornada(Liga liga, int id, int dia) {
        this.liga = liga;
        this.dia = dia;
        this.id = id;
        encuentros = new ArrayList<>();
    }

    public Liga getLiga() {
        return liga;
    }

    public void setLiga(Liga liga) {
        this.liga = liga;
    }

    public List<Encuentro> getEncuentros() {
        return encuentros;
    }

    public void setEncuentros(List<Encuentro> encuentros) {
        this.encuentros = encuentros;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Jornada jornada = (Jornada) o;
        return id == jornada.id;
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}
