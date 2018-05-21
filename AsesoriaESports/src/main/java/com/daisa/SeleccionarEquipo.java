package com.daisa;

import javax.swing.*;
import java.util.List;

/**
 * Ventana para seleccionar un equipo a modificar.
 *
 * @author David Roig
 * @author Isabel Montero
 */
public class SeleccionarEquipo {
    private JPanel panelPrincipal;
    private JButton cancelarbutton;
    private JButton modificarButton;
    private JScrollPane equipoScrollPane;
    private JFrame frame;
    private BaseDeDatos baseDeDatos;
    private VentanaPrincipalAdmin ventanaPrincipalAdmin;
    private JTable tablaEquipos;

    /**
     * Constructor de la clase.
     *
     * @param baseDeDatos
     * @param ventanaPrincipalAdmin
     */
    public SeleccionarEquipo(final BaseDeDatos baseDeDatos, final VentanaPrincipalAdmin ventanaPrincipalAdmin) {
        this.baseDeDatos = baseDeDatos;
        this.ventanaPrincipalAdmin = ventanaPrincipalAdmin;

        cancelarbutton.addActionListener(e -> ventanaPrincipalAdmin.mostrar(frame));
    }

    /**
     * Inicialización de la ventana.
     *
     * @param frame
     */
    public void mostrar(final JFrame frame) {
        this.frame = frame;
        frame.setContentPane(panelPrincipal);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final List<Equipo> equipos = baseDeDatos.getEquipos();

        modificarButton.setEnabled(false);
        tablaEquipos = configurarTablaEquipos(equipos);
        equipoScrollPane.getViewport().add(tablaEquipos);

        tablaEquipos.getSelectionModel().addListSelectionListener(event -> modificarButton.setEnabled(true));

        modificarButton.addActionListener(e -> {
            if (modificarButton.isEnabled()) {

                Equipo equipo = equipos.get(tablaEquipos.getSelectedRow());
                ModificarEquipo modificarEquipo = new ModificarEquipo(baseDeDatos, equipo, ventanaPrincipalAdmin);
                modificarEquipo.mostrar(frame);
            }
        });

        frame.pack();
    }

    /**
     * Devuelve una tabla con todos los equipos.
     *
     * @param equipos
     * @return JTable con todos los equipos.
     */
    private JTable configurarTablaEquipos(List<Equipo> equipos) {
        String[] columnas = {"ID Equipo", "Nombre", "Localidad", "Número Jugadores", "Número Ligas", "Suma Sueldo"};
        String[][] equiposLista = new String[equipos.size()][6];
        int count = 0;
        for (Equipo equipo : equipos) {
            equiposLista[count][0] = String.valueOf(equipo.getId());
            equiposLista[count][1] = equipo.getNombre();
            equiposLista[count][2] = equipo.getLocalidad();
            equiposLista[count][3] = String.valueOf(equipo.getJugadores().size());
            equiposLista[count][4] = String.valueOf(equipo.getLigas().size());
            equiposLista[count][5] = String.valueOf(equipo.getSumaSueldos());
            count++;
        }
        JTable table = new JTable(equiposLista, columnas);
        table.setCellSelectionEnabled(false);
        table.setRowSelectionAllowed(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setDefaultEditor(Object.class, null);
        return table;
    }


}
