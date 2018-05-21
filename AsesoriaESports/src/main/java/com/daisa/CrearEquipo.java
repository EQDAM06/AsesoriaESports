package com.daisa;

import javax.swing.*;

import java.util.List;

/**
 * Ventana para la creación de un equipo.
 * @author David Roig
 * @author Isabel Montero
 */
public class CrearEquipo {
    private JButton cancelarButton;
    private JButton crearEquipoButton;
    private JTextField nombreTextField;
    private JTextField localidadTextField;
    private JPanel panelPrincipal;
    private JComboBox directorComboBox;
    private JLabel directorLabel;
    private JFrame frame;
    private JFrame frameFondo;
    private BaseDeDatos baseDeDatos;
    private GestionEquipo gestionEquipo;
    private VentanaPrincipalAdmin ventanaPrincipalAdmin;

    /**
     * Constructor de la clase desde la ventana de director.
     * @param baseDeDatos
     * @param gestionEquipo
     */
    public CrearEquipo(final BaseDeDatos baseDeDatos, final GestionEquipo gestionEquipo) {
        this.baseDeDatos = baseDeDatos;
        this.gestionEquipo = gestionEquipo;

        cancelarButton.addActionListener(e -> {
            frameFondo.setEnabled(true);
            frame.dispose();
        });

        crearEquipoButton.addActionListener(e -> {
            if (!nombreTextField.getText().equals("")) {
                if (nombreTextField.getText().length() <= 50) {
                    if (nombreEstaDisponible(nombreTextField.getText())) {
                        if (!localidadTextField.getText().equals("") && localidadTextField.getText().length() <= 30) {
                            int n = JOptionPane.showConfirmDialog(frame,
                                    "El equipo se llamará " +
                                            "\"" + nombreTextField.getText() + "\"\n" +
                                            "¿Desea continuar?",
                                    "Confirmar",
                                    JOptionPane.YES_NO_OPTION); // 0 yes, 1 no
                            if (n == 0) {
                                baseDeDatos.crearEquipo(nombreTextField.getText(),localidadTextField.getText(),gestionEquipo.getDirector());

                                JOptionPane.showMessageDialog(frame, "Equipo creado correctamente", "Equipo Creado", JOptionPane.PLAIN_MESSAGE);

                                baseDeDatos.actualizar();

                                gestionEquipo.mostrar(frameFondo);
                                frameFondo.setEnabled(true);
                                frame.dispose();
                            }
                        } else {
                            JOptionPane.showMessageDialog(frame,
                                    "Localidad inválida.",
                                    "Localidad Inválida",
                                    JOptionPane.ERROR_MESSAGE);
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
     * Constructor de la clase desde la ventana de administrador.
     * @param baseDeDatos
     * @param ventanaPrincipalAdmin
     */
    public CrearEquipo(final BaseDeDatos baseDeDatos, final VentanaPrincipalAdmin ventanaPrincipalAdmin) {
        this.baseDeDatos = baseDeDatos;
        this.ventanaPrincipalAdmin = ventanaPrincipalAdmin;
        final List<Director> directores = baseDeDatos.getDirectores();

        directorLabel.setVisible(true);
        directorComboBox.setVisible(true);
        for (Director director:directores) {
            directorComboBox.addItem(director.getNombre());
        }

        cancelarButton.addActionListener(e -> {
            frameFondo.setEnabled(true);
            frame.dispose();
        });

        crearEquipoButton.addActionListener(e -> {
            if (!nombreTextField.getText().equals("")) {
                if (nombreTextField.getText().length() <= 50) {
                    if (nombreEstaDisponible(nombreTextField.getText())) {
                        if (!localidadTextField.getText().equals("") && localidadTextField.getText().length() <= 30) {
                            int n = JOptionPane.showConfirmDialog(frame,
                                    "El equipo se llamará " +
                                            "\"" + nombreTextField.getText() + "\"\n" +
                                            "¿Desea continuar?",
                                    "Confirmar",
                                    JOptionPane.YES_NO_OPTION); // 0 yes, 1 no
                            if (n == 0) {
                                Director director = directores.get(directorComboBox.getSelectedIndex());
                                baseDeDatos.crearEquipo(nombreTextField.getText(),localidadTextField.getText(),director);

                                JOptionPane.showMessageDialog(frame, "Equipo creado correctamente", "Equipo Creado", JOptionPane.PLAIN_MESSAGE);

                                baseDeDatos.actualizar();

                                ventanaPrincipalAdmin.mostrar(frameFondo);
                                frameFondo.setEnabled(true);
                                frame.dispose();
                            }
                        } else {
                            JOptionPane.showMessageDialog(frame,
                                    "Localidad inválida.",
                                    "Localidad Inválida",
                                    JOptionPane.ERROR_MESSAGE);
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
     * Devuelve la disponibilidad de un nombre de equipo.
     * @param nombre
     * @return Devuelve true si el nombre está disponible, false si ya existe.
     */
    private boolean nombreEstaDisponible(String nombre) {
        boolean disponible = true;
        for (Equipo equipo : baseDeDatos.getEquipos()) {
            if (equipo.getNombre().equals(nombre)) disponible = false;
        }
        return disponible;
    }

    /**
     * Inicialización de la ventana.
     * @param frameFondo
     */
    public void mostrar(JFrame frameFondo) {
        frame = new JFrame("Crear Equipo");
        this.frameFondo = frameFondo;
        frame.setResizable(false);
        frame.setContentPane(panelPrincipal);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(frameFondo);
        frame.setVisible(true);
    }
}
