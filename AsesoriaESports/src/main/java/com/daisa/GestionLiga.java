package com.daisa;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Ventana de gestión de liga para el administrador.
 *
 * @author David Roig
 * @author Isabel Montero
 */
public class GestionLiga {
    private JButton atrasButton;
    private JButton cambiarNombreButton;
    private JButton asignarPuntosButton;
    private JPanel panelPrincipal;
    private JScrollPane ligasScrollPane;
    private JFrame frame;
    final private VentanaPrincipalAdmin ventanaPrincipalAdmin;
    final private BaseDeDatos baseDeDatos;
    final private GestionLiga gestionLiga = this;
    private JTable ligasTable;

    /**
     * Inicializa la ventana.
     * @param frame
     */
    public void mostrar(JFrame frame) {
        this.frame = frame;
        frame.setContentPane(panelPrincipal);
        cambiarNombreButton.setEnabled(false);
        asignarPuntosButton.setEnabled(false);

        ligasTable = cargarLigasTable();
        ligasScrollPane.getViewport().add(ligasTable);

        ligasTable.getSelectionModel().addListSelectionListener(event -> {
            cambiarNombreButton.setEnabled(true);
            asignarPuntosButton.setEnabled(true);
        });
        frame.pack();

    }

    /**
     * Constructor de la clase.
     * @param baseDeDatos
     * @param ventanaPrincipalAdmin
     */
    public GestionLiga(final BaseDeDatos baseDeDatos, final VentanaPrincipalAdmin ventanaPrincipalAdmin) {
        this.baseDeDatos = baseDeDatos;
        this.ventanaPrincipalAdmin = ventanaPrincipalAdmin;
        atrasButton.addActionListener(e -> ventanaPrincipalAdmin.mostrar(frame));
        cambiarNombreButton.addActionListener(e -> {
            if (cambiarNombreButton.isEnabled()) {
                Liga liga = baseDeDatos.getLigas().get(ligasTable.getSelectedRow());
                CambiarNombreLiga cambiarNombreLiga = new CambiarNombreLiga(baseDeDatos, liga, gestionLiga);
                cambiarNombreLiga.mostrar(frame);
                frame.setEnabled(false);
            }
        });
        asignarPuntosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (asignarPuntosButton.isEnabled()) {
                    Liga liga = baseDeDatos.getLigas().get(ligasTable.getSelectedRow());
                    if (liga.isTerminado()) JOptionPane.showMessageDialog(frame,
                            "La liga ya ha terminado y todos sus puntos han sido asignados.",
                            "Liga Cerrada",
                            JOptionPane.ERROR_MESSAGE);
                    else {
                        int ultimaJornada = obtenerUltimaJornada(liga);
                        int n = JOptionPane.showConfirmDialog(frame,
                                "Se asignarán todas las puntuaciones de la jornada " +
                                        ultimaJornada + " de la liga " + liga.getNombre() + "\n" +
                                        "¿Desea continuar?",
                                "Confirmar",
                                JOptionPane.YES_NO_OPTION); // 0 yes, 1 no
                        if (n == 0) {
                            AsignarPuntuacion asignarPuntuacion = new AsignarPuntuacion(baseDeDatos, gestionLiga, liga, ultimaJornada);
                            asignarPuntuacion.mostrar(frame);
                            frame.setEnabled(false);
                        }
                    }
                }
            }

            /**
             * Devuelve un índice con la primera de las jornadas que todavía no han sido dadas puntuaciones
             * @param liga
             * @return int con el primer día no puntuado de la jornada.
             */
            private int obtenerUltimaJornada(Liga liga) {
                int ultimaJornada = 0;
                for (int i = 0; i < liga.getJornadas().size(); i++) {
                    for (Encuentro encuentro : liga.getJornadas().get(i).getEncuentros()) {
                        if (encuentro.getGanador() == 0 &&
                                (ultimaJornada == 0 || liga.getJornadas().get(i).getDia() < ultimaJornada))
                            ultimaJornada = liga.getJornadas().get(i).getDia();
                    }
                }
                return ultimaJornada;
            }
        });
    }

    /**
     * Devuelve una tabla con todas las ligas.
     * @return JTable con todas las ligas.
     */
    private JTable cargarLigasTable() {
        List<Liga> ligas = baseDeDatos.getLigas();

        String[] columnas = {"ID Liga", "Nombre", "Fecha Inicio", "Equipos", "Terminado"};
        String[][] ligasLista = new String[ligas.size()][5];
        int count = 0;
        if (!ligas.isEmpty()) {
            for (Liga liga : ligas) {
                ligasLista[count][0] = String.valueOf(liga.getId());
                ligasLista[count][1] = liga.getNombre();
                ligasLista[count][2] = liga.getFechaInicio();
                ligasLista[count][3] = String.valueOf(liga.getEquipos().size());
                if (liga.isTerminado()) ligasLista[count][4] = "Finalizado";
                else ligasLista[count][4] = "En Proceso";
                count++;
            }
        }
        JTable table = new JTable(ligasLista, columnas);
        table.setCellSelectionEnabled(false);
        table.setRowSelectionAllowed(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setDefaultEditor(Object.class, null);
        return table;
    }

}
