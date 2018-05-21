package com.daisa;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Ventana para la expulsión de jugadores de un equipo.
 *
 * @author David Roig
 * @author Isabel Montero
 */
public class RemoveJugador {
    private JFrame frame;
    private BaseDeDatos baseDeDatos;
    private Equipo equipo;
    private JPanel panelPrincipal;
    private JButton cancelarButton;
    private JButton expulsarButton;
    private JLabel nombreEquipoLabel;
    private JScrollPane jugadoresScrollPane;
    private final List<Jugador> jugadoresEquipo = new ArrayList<>();
    private final ModificarEquipo modificarEquipo;
    private final JTable[] tablaJugadores;

    /**
     * Constructor de la clase.
     *
     * @param baseDeDatos
     * @param equipo
     * @param modificarEquipo
     */
    public RemoveJugador(final BaseDeDatos baseDeDatos, final Equipo equipo, final ModificarEquipo modificarEquipo) {
        this.baseDeDatos = baseDeDatos;
        this.equipo = equipo;
        this.modificarEquipo = modificarEquipo;

        jugadoresEquipo.addAll(equipo.getJugadores());
        tablaJugadores = new JTable[]{crearTablaJugadores(jugadoresEquipo)};

        jugadoresScrollPane.getViewport().add(tablaJugadores[0]);

        tablaJugadores[0].getSelectionModel().addListSelectionListener(event -> expulsarButton.setEnabled(true));

        cancelarButton.addActionListener(e -> modificarEquipo.mostrar(frame));

        expulsarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (expulsarButton.isEnabled()) {
                    String dniJugador = tablaJugadores[0].getValueAt(tablaJugadores[0].getSelectedRow(), 0).toString();
                    Jugador jugador = obtenerJugador(dniJugador, jugadoresEquipo);
                    int n = JOptionPane.showConfirmDialog(frame,
                            "¿Expulsar jugador \n" +
                                    jugador.getNombre() + " (" + jugador.getDni() + ")\n" +
                                    "de \"" + equipo.getNombre() + "\"?",
                            "Confirmar",
                            JOptionPane.YES_NO_OPTION); // 0 yes, 1 no
                    if (n == 0) {
                        equipo.getJugadores().remove(jugador);
                        jugadoresEquipo.remove(jugador);

                        baseDeDatos.expulsarJugador(jugador);

                        JOptionPane.showMessageDialog(frame, "Jugador expulsado correctamente", "Jugador Expulsado", JOptionPane.PLAIN_MESSAGE);

                        baseDeDatos.actualizar();

                        if (equipo.getJugadores().size() < 1) {
                            JOptionPane.showMessageDialog(frame, "No quedan jugadores en el equipo", "Equipo Vacio", JOptionPane.PLAIN_MESSAGE);
                            modificarEquipo.mostrar(frame);

                        } else {
                            tablaJugadores[0] = crearTablaJugadores(jugadoresEquipo);
                            jugadoresScrollPane.getViewport().remove(0);
                            jugadoresScrollPane.getViewport().add(tablaJugadores[0]);

                            tablaJugadores[0].getSelectionModel().addListSelectionListener(event -> expulsarButton.setEnabled(true));

                            expulsarButton.setEnabled(false);


                            mostrar(frame);
                        }
                    }

                }
            }

            /**
             * Devuelve un jugador en base a su DNI.
             * @param dniJugador
             * @param jugadores
             * @return Jugador correspondiente a su DNI.
             */
            private Jugador obtenerJugador(String dniJugador, List<Jugador> jugadores) {
                Jugador jugador = null;
                for (int i = 0; i < jugadores.size(); i++) {
                    if (jugadores.get(i).getDni().equals(dniJugador)) {
                        jugador = jugadores.get(i);
                        i = jugadores.size();
                    }
                }
                return jugador;
            }
        });
    }


    private JTable crearTablaJugadores(List<Jugador> jugadores) {
        String[] columnas = {"DNI", "Nickname", "Nombre", "Teléfono", "Sueldo", "Fecha Alta"};
        String[][] jugadoresLista = new String[jugadores.size()][6];
        int count = 0;
        for (Jugador jugador : jugadores) {
            jugadoresLista[count][0] = jugador.getDni();
            jugadoresLista[count][1] = jugador.getNickname();
            jugadoresLista[count][2] = jugador.getNombre();
            jugadoresLista[count][3] = jugador.getTelefono();
            jugadoresLista[count][4] = String.valueOf(jugador.getSueldo());
            jugadoresLista[count][5] = jugador.getContratoVigente().getFechaAlta();
            count++;
        }

        JTable table = new JTable(jugadoresLista, columnas);
        table.setCellSelectionEnabled(false);
        table.setRowSelectionAllowed(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setDefaultEditor(Object.class, null);
        return table;
    }

    public void mostrar(JFrame frame) {
        nombreEquipoLabel.setText(equipo.getNombre());
        this.frame = frame;
        expulsarButton.setEnabled(false);
        frame.setContentPane(panelPrincipal);
        frame.pack();
    }
}
