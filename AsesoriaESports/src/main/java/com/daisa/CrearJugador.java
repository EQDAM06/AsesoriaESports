package com.daisa;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Ventana para la creación de un jugador.
 * @author David Roig
 * @author Isabel Montero
 */
public class CrearJugador {
    private JPanel panelPrincipal;
    private JButton cancelarButton;
    private JButton crearButton;
    private JTextField nombreTextField;
    private JTextField dniTextfield;
    private JTextField nicknameTextField;
    private JTextField sueldoTextField;
    private JTextField telefonoTextField;
    private JFrame frame;
    private BaseDeDatos baseDeDatos;
    private VentanaPrincipalAdmin ventanaPrincipalAdmin;

    /**
     * Constructor de la clase.
     * @param baseDeDatos
     * @param ventanaPrincipalAdmin
     */
    public CrearJugador(final BaseDeDatos baseDeDatos, final VentanaPrincipalAdmin ventanaPrincipalAdmin) {
        this.baseDeDatos = baseDeDatos;
        this.ventanaPrincipalAdmin = ventanaPrincipalAdmin;

        cancelarButton.addActionListener(e -> ventanaPrincipalAdmin.mostrar(frame));

        crearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String error = null;
                double sueldo = 0;
                if (nombreTextField.getText().isEmpty() || dniTextfield.getText().isEmpty() ||
                        nicknameTextField.getText().isEmpty() || sueldoTextField.getText().isEmpty() ||
                        telefonoTextField.getText().isEmpty()) error = "CAMPO_VACIO";
                try {
                    sueldo = Double.parseDouble(sueldoTextField.getText());
                } catch (NumberFormatException e1) {
                    if (error == null) error = "SUELDO_NUM";
                }
                if (nombreTextField.getText().length() >= 50 && error == null) error = "NOMBRE_LEN";
                if (dniTextfield.getText().length() > 9 && error == null) error = "DNI_LEN";
                if (nicknameTextField.getText().length() >= 50 && error == null) error = "NICK_LEN";
                if (sueldo < 8831 && error == null) error = "SUELDO_MIN";
                if (sueldo >= 100000 && error == null) error = "SUELDO_MAX";
                if (telefonoTextField.getText().length() > 15 && error == null) error = "TELE_LEN";
                if (!dniDisponible(dniTextfield.getText()) && error == null) error = "EXISTE";

                if (error != null) switch (error) {
                    case "CAMPO_VACIO":
                        JOptionPane.showMessageDialog(frame,
                                "Todos los campos son obligatorios.",
                                "Campo Vacio",
                                JOptionPane.ERROR_MESSAGE);
                        break;
                    case "SUELDO_NUM":
                        JOptionPane.showMessageDialog(frame,
                                "El sueldo es inválido.",
                                "Sueldo Inválido",
                                JOptionPane.ERROR_MESSAGE);
                        break;
                    case "NOMBRE_LEN":
                        JOptionPane.showMessageDialog(frame,
                                "Nombre inválido.",
                                "Nombre Inválido",
                                JOptionPane.ERROR_MESSAGE);
                        break;
                    case "DNI_LEN":
                        JOptionPane.showMessageDialog(frame,
                                "DNI inválido.",
                                "DNI Inválido",
                                JOptionPane.ERROR_MESSAGE);
                        break;
                    case "NICK_LEN":
                        JOptionPane.showMessageDialog(frame,
                                "Nickname inválido.",
                                "Nickname Inválido",
                                JOptionPane.ERROR_MESSAGE);
                        break;
                    case "SUELDO_MIN":
                        JOptionPane.showMessageDialog(frame,
                                "Sueldo por debajo del mínimo interprofesional (8831).",
                                "Sueldo Inválido",
                                JOptionPane.ERROR_MESSAGE);
                        break;
                    case "SUELDO_MAX":
                        JOptionPane.showMessageDialog(frame,
                                "Sueldo excede el máximo posible (99999).",
                                "Sueldo Inválido",
                                JOptionPane.ERROR_MESSAGE);
                        break;
                    case "TELE_LEN":
                        JOptionPane.showMessageDialog(frame,
                                "Teléfono Inválido.",
                                "Teléfono Inválido",
                                JOptionPane.ERROR_MESSAGE);
                        break;
                    case "EXISTE":
                        JOptionPane.showMessageDialog(frame,
                                "Jugador ya existente.",
                                "Jugador Existe",
                                JOptionPane.ERROR_MESSAGE);
                        break;
                }
                else {
                    int n = JOptionPane.showConfirmDialog(frame,
                            "Se va a crear el jugador " +
                                    "\"" + nombreTextField.getText() + "\"\n" +
                                    "¿Desea continuar?",
                            "Confirmar",
                            JOptionPane.YES_NO_OPTION); // 0 yes, 1 no
                    if (n == 0) {
                        baseDeDatos.crearJugador(dniTextfield.getText(), nombreTextField.getText(), nicknameTextField.getText(),
                                sueldo, telefonoTextField.getText());

                        JOptionPane.showMessageDialog(frame, "Jugador creado correctamente", "Jugador Creado", JOptionPane.PLAIN_MESSAGE);

                        baseDeDatos.actualizar();

                        ventanaPrincipalAdmin.mostrar(frame);
                    }
                }
            }

            /**
             * Devuelve la disponibilidad del DNI del jugador.
             * @param dni
             * @return True si está disponible, false si ya existe.
             */
            private boolean dniDisponible(String dni) {
                boolean disponible = true;
                for (Jugador jugador : baseDeDatos.getJugadores()) {
                    if (jugador.getDni().equals(dni)) disponible = false;
                }
                return disponible;
            }
        });
    }

    /**
     * Inicialización de la ventana.
     * @param frame
     */
    public void mostrar(JFrame frame) {
        this.frame = frame;
        frame.setContentPane(panelPrincipal);
        frame.pack();
    }
}
