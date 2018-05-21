package com.daisa;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Ventana para transferir un equipo de un director a otro.
 *
 * @author David Roig
 * @author Isabel Montero
 */
public class TransferirEquipo {
    private JPanel panelPrincipal;
    private JComboBox comboBox1;
    private JButton transferirButton;
    private JButton cancelarButton;
    private JFrame frame;
    private JFrame frameFondo;
    final private BaseDeDatos baseDeDatos;
    final private Equipo equipo;
    final private ModificarEquipo modificarEquipo;
    private List<Director> directoresDisp;

    /**
     * Constructor de la clase.
     * @param baseDeDatos
     * @param equipo
     * @param modificarEquipo
     */
    public TransferirEquipo(final BaseDeDatos baseDeDatos, final Equipo equipo, final ModificarEquipo modificarEquipo) {
        this.baseDeDatos = baseDeDatos;
        this.equipo = equipo;
        this.modificarEquipo = modificarEquipo;
        final List<Director> directoresDisp = new ArrayList<>(baseDeDatos.getDirectores());
        directoresDisp.remove(equipo.getDirector());
        for (Director director:directoresDisp) {
            comboBox1.addItem(director.getNombre());
        }

        cancelarButton.addActionListener(e -> {
            frameFondo.setEnabled(true);
            frame.dispose();
        });
        transferirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Director nuevoDirector = obtenerDirector(comboBox1.getSelectedIndex());
                int n = JOptionPane.showConfirmDialog(frame,
                        "El equipo \n" +
                                "\"" + equipo.getNombre() + "\"\n" +
                                "pasará a ser propiedad de\n" +
                                "\"" + nuevoDirector.getNombre() + "\"\n" +
                                "¿Desea continuar?",
                        "Confirmar",
                        JOptionPane.YES_NO_OPTION); // 0 yes, 1 no
                if (n == 0) {
                    baseDeDatos.cambiarDirectorEquipo(equipo,nuevoDirector);
                    equipo.setDirector(nuevoDirector);
                    equipo.getDirector().getEquipos().remove(equipo);
                    JOptionPane.showMessageDialog(frame, "Director cambiado correctamente", "Director Cambiado", JOptionPane.PLAIN_MESSAGE);

                    baseDeDatos.actualizar();

                    modificarEquipo.getGestionEquipo().mostrar(frameFondo);
                    frameFondo.setEnabled(true);
                    frame.dispose();
                }
            }

            /**
             * Devuelve un director en base a un índice de combobox.
             * @param index
             * @return Director correspondiente al índice de combobox.
             */
            private Director obtenerDirector(int index) {
                return directoresDisp.get(index);
            }
        });
    }

    /**
     * Inicialización de la ventana.
     * @param frameFondo
     */
    public void mostrar(JFrame frameFondo) {
        frame = new JFrame("Cambiar Nombre");
        this.frameFondo = frameFondo;

        frame.setResizable(false);
        frame.setContentPane(panelPrincipal);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(frameFondo);
        frame.setVisible(true);
    }
}
