package com.daisa;

import java.util.List;
import java.util.Objects;

/**
 * Aquí va la documentación
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

    public Equipo(String nombre, String localidad, Director director) {
        this.nombre = nombre;
        this.localidad = localidad;
        this.director = director;
    }

    /**
     * Añade jugadores al equipo, creando el Contrato en el momento
     *
     * @param jugador Jugador a añadir
     */
    public void addJugador(Jugador jugador) {
        jugadores.add(jugador);
        jugador.setEquipo(this);
        Contrato contrato = new Contrato(jugador, this);
        contratos.add(contrato);
        jugador.getContratos().add(contrato);
    }

    /**
     * Saca al jugador del equipo, terminando el contrato
     *
     * @param jugador Jugador a sacar
     */
    public void removeJugador(Jugador jugador) {
        jugadores.remove(jugador);
        jugador.setEquipo(null);
        Contrato contrato = jugador.getContratos().
                get(jugador.getContratos().size() - 1); // Esto obtiene el último contrato del jugador (o debería)
        contrato.terminar();
    }

    /**
     * Disuelve el equipo, echando a todos los jugadores y desligando al director.
     */
    public void disolverEquipo() {
        if (ligas.isEmpty()) { // Debería ser comprobado antes de llamar a la funcion, pero es importante asegurarse
            for (Jugador jugador : jugadores) {
                this.removeJugador(jugador);
            }
            director.getEquipos().remove(this);
        } else System.err.println("El equipo se encuentra actualmente en una liga y no puede ser disuelto");
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
