package com.daisa;

import org.junit.Test;

import static org.junit.Assert.*;

public class DirectorTest {

    @Test
    public void getDni() {
        Director director = new Director("dni", "nombre", "telefono", "email", "idUsuario");
        assertEquals("dni",director.getDni());
    }

    @Test
    public void getNombre() {
        Director director = new Director("dni", "nombre", "telefono", "email", "idUsuario");
        assertEquals("nombre",director.getNombre());
    }

    @Test
    public void getTelefono() {
        Director director = new Director("dni", "nombre", "telefono", "email", "idUsuario");
        assertEquals("telefono",director.getTelefono());
    }

    @Test
    public void getEmail() {
        Director director = new Director("dni", "nombre", "telefono", "email", "idUsuario");
        assertEquals("FALLO",director.getEmail());
    }
}