package com.daisa;

import javax.swing.*;

/**
 * Ventana para la asignación de puntuación de cada encuentro de una jornada por parte del Administrador.
 *
 * @author David Roig
 * @author Isabel Montero
 */
public class AsignarPuntuacion {
    private JButton cancelarButton;
    private JButton asignarButton;
    private JLabel equipo1Label;
    private JLabel equipo2Label;
    private JRadioButton equipo2Button;
    private JRadioButton equipo1Button;
    private JPanel panelPrincipal;
    private JPanel equiposPanel;
    private BaseDeDatos baseDeDatos;
    private JFrame frame;
    private JFrame frameFondo;
    private GestionLiga gestionLiga;
    private Liga liga;
    private Jornada jornada;
    private int encuentro;
    private int[] puntuacionesArray;
    private final AsignarPuntuacion asignarPuntuacion = this;

    /**
     * Constructor de la clase.
     *
     * @param baseDeDatos
     * @param gestionLiga
     * @param liga
     * @param ultimaJornada Int que establece el día más bajo que está sin modificar de la liga
     */
    public AsignarPuntuacion(BaseDeDatos baseDeDatos, GestionLiga gestionLiga, Liga liga, int ultimaJornada) {
        this.baseDeDatos = baseDeDatos;
        this.gestionLiga = gestionLiga;
        this.liga = liga;
        for (int i = 0; i < liga.getJornadas().size(); i++) {
            if (liga.getJornadas().get(i).getDia() == ultimaJornada) {
                this.jornada = liga.getJornadas().get(i);
                i = liga.getJornadas().size();
            }
        }
        puntuacionesArray = new int[jornada.getEncuentros().size()];
        encuentro = 0;
        equipo1Button.addActionListener(e -> equipo2Button.setSelected(false));
        equipo2Button.addActionListener(e -> equipo1Button.setSelected(false));
        cancelarButton.addActionListener(e -> {
            gestionLiga.mostrar(frameFondo);
            frameFondo.setEnabled(true);
            frame.dispose();
        });
        asignarButton.addActionListener(e -> {
            if (equipo1Button.isSelected() || equipo2Button.isSelected()) {
                if (equipo1Button.isSelected()) puntuacionesArray[encuentro] = 1;
                else puntuacionesArray[encuentro] = 2;
                encuentro++;
                if (puntuacionesArray.length == encuentro) { // no quedan más encuentros en la jornada
                    int n = JOptionPane.showConfirmDialog(frame,
                            "Se asignarán todas las puntuaciones marcadas de la jornada " +
                                    ultimaJornada + " de la liga " + liga.getNombre() + "\n" +
                                    "¿Desea continuar?",
                            "Confirmar",
                            JOptionPane.YES_NO_OPTION); // 0 yes, 1 no
                    if (n == 0) {
                        boolean terminado = ultimaJornada == liga.getJornadas().size();
                        baseDeDatos.asignarPuntuaciones(jornada, puntuacionesArray, terminado);

                        if (terminado) {
                            JOptionPane.showMessageDialog(frame, "Puntuaciones asignadas correctamente y liga finalizada.", "Puntuaciones Asignadas", JOptionPane.PLAIN_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(frame, "Puntuaciones asignadas correctamente.", "Puntuaciones Asignadas", JOptionPane.PLAIN_MESSAGE);
                        }

                        baseDeDatos.actualizar();
                        gestionLiga.mostrar(frameFondo);
                        frameFondo.setEnabled(true);
                        frame.dispose();

                    }
                } else {
                    asignarPuntuacion.mostrar(frameFondo);
                }
            }
        });
    }

    /**
     * Inicialización de la ventana
     *
     * @param frameFondo
     */
    public void mostrar(JFrame frameFondo) {
        if (this.frame == null) {
            this.frame = new JFrame("Asignar Puntuacion -- " + liga.getNombre());
            this.frameFondo = frameFondo;
            frame.setResizable(false);
            frame.setContentPane(panelPrincipal);
            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        }

        equipo1Label.setText(jornada.getEncuentros().get(encuentro).getEquipos()[0].getNombre());
        equipo2Label.setText(jornada.getEncuentros().get(encuentro).getEquipos()[1].getNombre());

        equipo1Button.setSelected(false);
        equipo2Button.setSelected(false);

        frame.pack();
        frame.setLocationRelativeTo(frameFondo);
        frame.setVisible(true);
    }
}
