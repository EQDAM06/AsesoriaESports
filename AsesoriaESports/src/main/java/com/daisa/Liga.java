package com.daisa;

import java.util.*;

/**
 * Clase Liga, que contiene Jornadas.
 *
 * @author David Roig
 * @author Isabel Montero
 */
public class Liga {
    private int id;
    private String nombre;
    private String fechaInicio;
    private boolean terminado = false;
    private List<Equipo> equipos;
    private List<Puntuacion> puntuaciones;
    private List<Jornada> jornadas;

    public Liga(String nombre, String fechaInicio, List<Equipo> equipos, boolean terminado) {
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.equipos = equipos;
        this.terminado = terminado;
        this.jornadas = new ArrayList<>();
        this.puntuaciones = new ArrayList<>();
    }

    public Liga(int id, String nombre, String fechaInicio, boolean terminado) {
        this.id = id;
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.terminado = terminado;
        this.equipos = new ArrayList<>();
        this.jornadas = new ArrayList<>();
        this.puntuaciones = new ArrayList<>();
    }

    public Liga(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Crea las jornadas y los encuentros de la liga
     */
    public void crearJornadas() {
        ArrayList<Jornada> jornadas = new ArrayList<>();

        int numEquipos = equipos.size();

        for (int i = 0; i < numEquipos-1; i++) {

            ArrayList<Encuentro> encuentros = new ArrayList<>();
            Jornada jornada = new Jornada(this, i+1);

            for (int j = i+1; j < numEquipos; j++) {
                Encuentro encuentro = new Encuentro(jornada,this);
                encuentro.setEquipos(equipos.get(i), equipos.get(j));
                encuentros.add(encuentro);
            }
            jornada.setEncuentros(encuentros);
            jornadas.add(jornada);
        }

        this.jornadas = jornadas;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public void setFechaInicio(Date fechaInicio) {
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
