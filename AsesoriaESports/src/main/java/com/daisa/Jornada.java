package com.daisa;

import java.util.List;
import java.util.Objects;

/**
 * Aquí va la documentación
 * @author David Roig
 * @author Isabel Montero
 */
public class Jornada {
    private Liga liga;
    private List<Encuentro> encuentros;
    private int id;

    public Jornada(Liga liga, List<Equipo> equipos, int dia) {
        this.liga = liga;
        for (int i = 0; i < equipos.size(); i++) {

        }
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
