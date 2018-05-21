package com.daisa;

import javax.swing.*;
import java.util.List;

/**
 * Ventana de selección y modificación de jugadores.
 *
 * @author David Roig
 * @author Isabel Montero
 */
public class ModificarJugador {
    private JPanel panelPrincipal;
    private JButton cancelarButton;
    private JButton cambiarNombreButton;
    private JButton cambiarNicknameButton;
    private JButton cambiarSueldoButton;
    private JButton cambiarTelefonoButton;
    private JScrollPane jugadoresScrollPane;
    private JFrame frame;
    private BaseDeDatos baseDeDatos;
    private VentanaPrincipalAdmin ventanaPrincipalAdmin;
    private ModificarJugador modificarJugador = this;
    private JTable tablaJugadores;

    /**
     * Constructor de la clase.
     * @param baseDeDatos
     * @param ventanaPrincipalAdmin
     */
    public ModificarJugador(final BaseDeDatos baseDeDatos, final VentanaPrincipalAdmin ventanaPrincipalAdmin) {
        this.baseDeDatos = baseDeDatos;
        this.ventanaPrincipalAdmin = ventanaPrincipalAdmin;

        cambiarNombreButton.addActionListener(e -> {
            Jugador jugador = baseDeDatos.getJugadores().get(tablaJugadores.getSelectedRow());
            CambiarNombreJugador cambiarNombreJugador = new CambiarNombreJugador(baseDeDatos, jugador, modificarJugador);
            cambiarNombreJugador.mostrar(frame);
            frame.setEnabled(false);
        });

        cambiarNicknameButton.addActionListener(e -> {
            Jugador jugador = baseDeDatos.getJugadores().get(tablaJugadores.getSelectedRow());
            CambiarNicknameJugador cambiarNicknameJugador = new CambiarNicknameJugador(baseDeDatos, jugador, modificarJugador);
            cambiarNicknameJugador.mostrar(frame);
            frame.setEnabled(false);
        });

        cambiarSueldoButton.addActionListener(e -> {
            Jugador jugador = baseDeDatos.getJugadores().get(tablaJugadores.getSelectedRow());
            CambiarSueldoJugador cambiarSueldoJugador = new CambiarSueldoJugador(baseDeDatos, jugador, modificarJugador);
            cambiarSueldoJugador.mostrar(frame);
            frame.setEnabled(false);
        });


        cambiarTelefonoButton.addActionListener(e -> {
            Jugador jugador = baseDeDatos.getJugadores().get(tablaJugadores.getSelectedRow());
            CambiarTelefonoJugador cambiarTelefonoJugador = new CambiarTelefonoJugador(baseDeDatos, jugador, modificarJugador);
            cambiarTelefonoJugador.mostrar(frame);
            frame.setEnabled(false);
        });

        cancelarButton.addActionListener(e -> {
            ventanaPrincipalAdmin.mostrar(frame);
        });
    }

    /**
     * Inicialización de la ventana.
     * @param frame
     */
    public void mostrar(final JFrame frame) {
        this.frame = frame;
        frame.setContentPane(panelPrincipal);

        tablaJugadores = crearTablaJugadores(baseDeDatos.getJugadores());
        jugadoresScrollPane.getViewport().add(tablaJugadores);

        tablaJugadores.getSelectionModel().addListSelectionListener(e -> {
            cambiarSueldoButton.setEnabled(true);
            cambiarNicknameButton.setEnabled(true);
            cambiarNombreButton.setEnabled(true);
            cambiarTelefonoButton.setEnabled(true);
        });

        cambiarSueldoButton.setEnabled(false);
        cambiarNicknameButton.setEnabled(false);
        cambiarNombreButton.setEnabled(false);
        cambiarTelefonoButton.setEnabled(false);

        frame.pack();
    }

    /**
     * Devuelve una tabla con todos los jugadores.
     * @param jugadores
     * @return JTable con todos los jugadores.
     */
    private JTable crearTablaJugadores(List<Jugador> jugadores) {
        String[] columnas = {"DNI", "Nombre", "Nickname", "Sueldo", "Teléfono", "Equipo"};
        String[][] jugadoresLista = new String[jugadores.size()][6];

        int count = 0;
        for (Jugador jugador : jugadores) {
            jugadoresLista[count][0] = jugador.getDni();
            jugadoresLista[count][1] = jugador.getNombre();
            jugadoresLista[count][2] = jugador.getNickname();
            jugadoresLista[count][3] = String.valueOf(jugador.getSueldo());
            jugadoresLista[count][4] = jugador.getTelefono();
            if (jugador.getEquipo() == null) jugadoresLista[count][5] = "-Sin Equipo-";
            else jugadoresLista[count][5] = jugador.getEquipo().getNombre();
            count++;
        }

        JTable table = new JTable(jugadoresLista, columnas);

        table.setCellSelectionEnabled(false);
        table.setRowSelectionAllowed(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setDefaultEditor(Object.class, null);
        return table;
    }

}
