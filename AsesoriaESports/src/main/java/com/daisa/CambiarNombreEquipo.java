package com.daisa;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 * Ventana para el cambio de nombre de un equipo.
 * @author David Roig
 * @author Isabel Montero
 */
public class CambiarNombreEquipo {
    private JPanel panelPrincipal;
    private JTextField nombreTextField;
    private JButton cambiarNombreButton;
    private JButton cancelarButton;
    private BaseDeDatos baseDeDatos;
    private Equipo equipo;
    private JFrame frameFondo;
    private JFrame frame;
    private ModificarEquipo modificarEquipo;

    /**
     * Constructor de la clase.
     * @param baseDeDatos
     * @param equipo
     * @param modificarEquipo
     */
    public CambiarNombreEquipo(final BaseDeDatos baseDeDatos, final Equipo equipo, final ModificarEquipo modificarEquipo) {
        this.baseDeDatos = baseDeDatos;
        this.equipo = equipo;
        this.modificarEquipo = modificarEquipo;

        cancelarButton.addActionListener(e -> {
            frameFondo.setEnabled(true);
            frame.dispose();
        });

        cambiarNombreButton.addActionListener(e -> {
            if (!nombreTextField.getText().equals("")) {
                if (nombreTextField.getText().length() <= 20) {
                    if (!nombreEstaDisponible(nombreTextField.getText())) {
                        int n = JOptionPane.showConfirmDialog(frame,
                                "El equipo \n" +
                                        "\"" + equipo.getNombre() + "\"\n" +
                                        "pasará a llamarse\n" +
                                        "\"" + nombreTextField.getText() + "\"\n" +
                                        "¿Desea continuar?",
                                "Confirmar",
                                JOptionPane.YES_NO_OPTION); // 0 yes, 1 no
                        if (n == 0) {
                            cambiarNombre(baseDeDatos, nombreTextField.getText(), equipo);
                            equipo.setNombre(nombreTextField.getText());
                            JOptionPane.showMessageDialog(frame, "Nombre cambiado correctamente", "Nombre Cambiado", JOptionPane.PLAIN_MESSAGE);

                            baseDeDatos.actualizar();

                            modificarEquipo.mostrar(frameFondo);
                            frameFondo.setEnabled(true);
                            frame.dispose();
                        }

                    } else {
                        JOptionPane.showMessageDialog(frame,
                                "El nombre no está disponible.",
                                "Nombre Inválido",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(frame,
                            "El nombre no puede superar los 20 carácteres.",
                            "Nombre Inválido",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    /**
     * Llamada a la base de datos para el cambio de nombre de equipo.
     * @param baseDeDatos
     * @param nombre
     * @param equipo
     */
    private void cambiarNombre(BaseDeDatos baseDeDatos, String nombre, Equipo equipo) {
        int equipoID = equipo.getId();
        String query = "{ call datos_equipo.modificar_nombre(?,?) }";
        baseDeDatos.update(query, equipoID, nombre);
    }

    /**
     * Devuelve las coincidencias del nombre propuesto con los ya existentes.
     * @param nombre
     * @return Devuelve true si ya existe y false si está disponible.
     */
    private boolean nombreEstaDisponible(String nombre) {
        boolean disponible = false;
        for (Equipo equipo : baseDeDatos.getEquipos()) {
            if (equipo.getNombre().toUpperCase().equals(nombre.toUpperCase())) disponible = true;
        }
        return disponible;
    }

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
