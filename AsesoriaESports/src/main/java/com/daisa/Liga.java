package com.daisa;

import java.util.*;

/**
 * Aquí va la documentación
 *
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

    public Liga(String nombre, Date fechaInicio, List<Equipo> equipos) {
        if (equipos.size() % 2 == 0) { // Nos aseguramos de nuevo de que el número de equipos sea par
            this.nombre = nombre;
            this.fechaInicio = fechaInicio;
            this.equipos = equipos;

            int sorteo[] = new int[equipos.size()];
            Arrays.fill(sorteo, -1);
            Random rand = new Random();
            for (int i = 0; i < equipos.size(); i++) {
                while (sorteo[i] == -1) {
                    int n = rand.nextInt(equipos.size() - 1);
                    int j = 0;
                    while ((j <= i) && sorteo[j] != n) {
                        if (j == i) {
                            sorteo[i] = n;
                        } else j++;
                    }
                }
            }
            List<Equipo> equiposSorteados = new ArrayList<>();
            for (int i = 0; i < equipos.size(); i++) {
                equiposSorteados.add(equipos.get(sorteo[i]));
            }

            for (int i = 0; i < equipos.size() - 1; i++) {
                jornadas.add(new Jornada(this, equiposSorteados,i+1));
            }
        }
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
