package com.daisa;

/**
 * Aplicación Asesoria E-Sports
 * @author David Roig
 * @author Isabel Montero
 */
public class Main {
    public static void main(String[] args) {

        final BaseDeDatos baseDeDatos = new BaseDeDatos("jdbc:oracle:thin:@SrvOracle:1521:orcl");
        baseDeDatos.conectar("eqdam06", "eqdam06");
        new LoginVentana(baseDeDatos);
    }
}
