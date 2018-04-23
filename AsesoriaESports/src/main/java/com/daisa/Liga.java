package com.daisa;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Aquí va la documentación
 * @author David Roig
 * @author Isabel Montero
 */
public class Liga {
    private int id;
    private String nombre;
    private Date fechaInicio;
    private boolean terminado = false;
    private List<Equipo> equipos;
    private List<Puntuacion> puntuaciones;
    private List<Jornada> jornadas;

    public Liga(String nombre, Date fechaInicio, boolean terminado, List<Equipo> equipos) {
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.terminado = terminado;
        this.equipos = equipos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public boolean isTerminado() {
        return terminado;
    }

    public void setTerminado(boolean terminado) {
        this.terminado = terminado;
    }

    public List<Equipo> getEquipos() {
        return equipos;
    }

    public void setEquipos(List<Equipo> equipos) {
        this.equipos = equipos;
    }

    public List<Puntuacion> getPuntuaciones() {
        return puntuaciones;
    }

    public void setPuntuaciones(List<Puntuacion> puntuaciones) {
        this.puntuaciones = puntuaciones;
    }

    public List<Jornada> getJornadas() {
        return jornadas;
    }

    public void setJornadas(List<Jornada> jornadas) {
        this.jornadas = jornadas;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Liga liga = (Liga) o;
        return id == liga.id;
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}
