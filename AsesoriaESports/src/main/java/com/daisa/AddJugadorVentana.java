package com.daisa;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Ventana para la insercción de jugadores dentro de equipos.
 * @author David Roig
 * @author Isabel Montero
 */
public class AddJugadorVentana {
    private final BaseDeDatos baseDeDatos;
    private final Equipo equipo;
    private JPanel panelPrincipal;
    private JLabel nombreEquipoLabel;
    private JButton volverButton;
    private JButton addButton;
    private JScrollPane scrollPaneJugadores;
    private JFrame frame;
    private ModificarEquipo modificarEquipo;
    final private JTable[] tablaJugadores = new JTable[1];

    /**
     * Constructor de la clase
     * @param baseDeDatos
     * @param equipo
     * @param modificarEquipo
     */
    public AddJugadorVentana(final BaseDeDatos baseDeDatos, final Equipo equipo, final ModificarEquipo modificarEquipo) {
        this.baseDeDatos = baseDeDatos;
        this.equipo = equipo;
        this.modificarEquipo = modificarEquipo;
        nombreEquipoLabel.setText(equipo.getNombre());

        tablaJugadores[0] = crearTablaJugadores(baseDeDatos.getJugadores());
        scrollPaneJugadores.getViewport().add(tablaJugadores[0]);

        volverButton.addActionListener(e -> modificarEquipo.mostrar(frame));

        tablaJugadores[0].getSelectionModel().addListSelectionListener(event -> addButton.setEnabled(true));

        addButton.addActionListener(e -> {
            if (addButton.isEnabled()) {
                Jugador jugador = null;
                String dniJugador = tablaJugadores[0].getValueAt(tablaJugadores[0].getSelectedRow(), 0).toString();
                List<Jugador> jugadores = baseDeDatos.getJugadores();
                for (int i = 0; i < jugadores.size(); i++) {
                    if (jugadores.get(i).getDni().equals(dniJugador)) {
                        jugador = jugadores.get(i);
                        i = jugadores.size();
                    }
                }

                assert jugador != null;
                int n = JOptionPane.showConfirmDialog(frame,
                        "¿Añadir jugador \n" +
                                jugador.getNombre() + " (" + jugador.getDni() + ")\n" +
                                "a \"" + equipo.getNombre() + "\"?",
                        "Confirmar",
                        JOptionPane.YES_NO_OPTION); // 0 yes, 1 no
                if (n == 0) {
                    if (equipo.getSumaSueldos() + jugador.getSueldo() > 200000) {
                        JOptionPane.showMessageDialog(frame,
                                "No se puede añadir al jugador. La suma de su sueldo al del resto del equipo" +
                                        " supera el límite preestablecido de 200000.",
                                "Sueldo Excede Límite",
                                JOptionPane.ERROR_MESSAGE);
                    } else {
                        equipo.getJugadores().add(jugador);
                        baseDeDatos.crearContrato(jugador, equipo);

                        baseDeDatos.actualizar();

                        JOptionPane.showMessageDialog(frame, "Jugador añadido correctamente", "Jugador Añadido", JOptionPane.PLAIN_MESSAGE);

                        if (equipo.getJugadores().size() > 5) {
                            JOptionPane.showMessageDialog(frame, "Límite de 6 jugadores alcanzado", "Límite Jugadores", JOptionPane.PLAIN_MESSAGE);
                            modificarEquipo.mostrar(frame);

                        } else {
                            tablaJugadores[0] = crearTablaJugadores(baseDeDatos.getJugadores());
                            scrollPaneJugadores.getViewport().remove(0);
                            scrollPaneJugadores.getViewport().add(tablaJugadores[0]);

                            tablaJugadores[0].getSelectionModel().addListSelectionListener(event -> addButton.setEnabled(true));
                            mostrar(frame);
                        }

                    }
                }
            }
        });
    }

    /**
     * Crea la tabla de jugadores disponibles.
     * @param jugadores
     * @return
     */
    private JTable crearTablaJugadores(List<Jugador> jugadores) {
        List<Jugador> jugadoresLibres = new ArrayList<>();
        for (Jugador jugador : jugadores) {
            if (jugador.getEquipo() == null) jugadoresLibres.add(jugador);
        }

        String[] columnas = {"DNI","Nickname", "Nombre", "Teléfono", "Sueldo"};
        String[][] jugadoresLista = new String[jugadoresLibres.size()][5];
        int count = 0;
        for (Jugador jugador : jugadoresLibres) {
            jugadoresLista[count][0] = jugador.getDni();
            jugadoresLista[count][1] = jugador.getNickname();
            jugadoresLista[count][2] = jugador.getNombre();
            jugadoresLista[count][3] = jugador.getTelefono();
            jugadoresLista[count][4] = String.valueOf(jugador.getSueldo());
            count++;
        }

        JTable table = new JTable(jugadoresLista, columnas);
        table.setCellSelectionEnabled(false);
        table.setRowSelectionAllowed(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setDefaultEditor(Object.class, null);
        return table;

    }

    /**
     * Inicialización de ventana.
     * @param frame
     */
    public void mostrar (final JFrame frame) {
        this.frame = frame;
        frame.setContentPane(panelPrincipal);
        addButton.setEnabled(false);
        frame.pack();
    }
}
