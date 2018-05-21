package com.daisa;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Clase de Equipo
 *
 * @author David Roig
 * @author Isabel Montero
 */
public class Equipo {
    private int id;
    private String nombre;
    private String localidad;
    private Director director;
    private List<Jugador> jugadores;
    private List<Liga> ligas;
    private List<Contrato> contratos;
    private List<Puntuacion> puntuaciones;

    public Equipo(int id ,String nombre, String localidad, Director director) {
        this.id = id;
        this.nombre = nombre;
        this.localidad = localidad;
        this.director = director;
        jugadores = new ArrayList<>();
        contratos = new ArrayList<>();
        ligas = new ArrayList<>();
        puntuaciones = new ArrayList<>();
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

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public Director getDirector() {
        return director;
    }

    public void setDirector(Director director) {
        this.director = director;
    }

    public List<Jugador> getJugadores() {
        return jugadores;
    }

    public void setJugadores(List<Jugador> jugadores) {
        this.jugadores = jugadores;
    }

    public List<Liga> getLigas() {
        return ligas;
    }

    public void setLigas(List<Liga> ligas) {
        this.ligas = ligas;
    }

    public List<Contrato> getContratos() {
        return contratos;
    }

    public void setContratos(List<Contrato> contratos) {
        this.contratos = contratos;
    }

    public List<Puntuacion> getPuntuaciones() {
        return puntuaciones;
    }

    public void setPuntuaciones(List<Puntuacion> puntuaciones) {
        this.puntuaciones = puntuaciones;
    }

    /**
     * Devuelve la suma de los sueldos de cada jugador.
     * @return Double de la suma de los sueldos de cada jugador.
     */
    public double getSumaSueldos() {
        double sumaSueldos = 0;
        for (Jugador jugador:jugadores) {
            sumaSueldos += jugador.getSueldo();
        }
        return sumaSueldos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Equipo equipo = (Equipo) o;
        return Objects.equals(nombre, equipo.nombre);
    }

    @Override
    public int hashCode() {

        return Objects.hash(nombre);
    }
}
