package com.daisa;

import javax.swing.*;

/**
 * Ventana para el cambio de sueldo de un jugador.
 * @author David Roig
 * @author Isabel Montero
 */
public class CambiarSueldoJugador {
    private JTextField sueldoTextField;
    private JButton cambiarSueldoButton;
    private JButton cancelarButton;
    private JPanel panelPrincipal;
    private JFrame frame;
    private JFrame frameFondo;
    private BaseDeDatos baseDeDatos;
    private Jugador jugador;
    private ModificarJugador modificarJugador;

    /**
     * Constructor de la ventana.
     * @param baseDeDatos
     * @param jugador
     * @param modificarJugador
     */
    public CambiarSueldoJugador(final BaseDeDatos baseDeDatos, final Jugador jugador, final ModificarJugador modificarJugador) {
        this.baseDeDatos = baseDeDatos;
        this.modificarJugador = modificarJugador;
        this.jugador = jugador;

        cancelarButton.addActionListener(e -> {
            frameFondo.setEnabled(true);
            frame.dispose();
        });

        cambiarSueldoButton.addActionListener(e -> {
            if (!sueldoTextField.getText().equals("")) {
                if (sueldoTextField.getText().length() <= 20) {
                    try {
                        double sueldo = Double.parseDouble(sueldoTextField.getText());
                        if (sueldo > 8830) {
                            if (sueldo <= 100000) {
                                boolean sueldoAceptable = true;
                                if (jugador.getEquipo() != null) {
                                    if ((jugador.getEquipo().getSumaSueldos() - jugador.getSueldo() + sueldo) > 200000) {
                                        sueldoAceptable = false;
                                    }
                                }
                                if (sueldoAceptable) {
                                    int n = JOptionPane.showConfirmDialog(frame,
                                            "El jugador \n" +
                                                    "\"" + jugador.getNombre() + "\"\n" +
                                                    "pasará a tener de sueldo\n" +
                                                    "\"" + sueldoTextField.getText() + "\"\n" +
                                                    "¿Desea continuar?",
                                            "Confirmar",
                                            JOptionPane.YES_NO_OPTION); // 0 yes, 1 no
                                    if (n == 0) {
                                        cambiarSueldo(sueldoTextField.getText(), jugador);
                                        jugador.setSueldo(Double.parseDouble(sueldoTextField.getText()));
                                        JOptionPane.showMessageDialog(frame, "Sueldo cambiado correctamente", "Sueldo Cambiado", JOptionPane.PLAIN_MESSAGE);

                                        baseDeDatos.actualizar();

                                        modificarJugador.mostrar(frameFondo);
                                        frameFondo.setEnabled(true);
                                        frame.dispose();
                                    }
                                } else {
                                    JOptionPane.showMessageDialog(frame,
                                            "El nuevo sueldo superaría el límite del equipo (200000).",
                                            "Sueldo Inválido",
                                            JOptionPane.ERROR_MESSAGE);
                                }
                            } else {
                                JOptionPane.showMessageDialog(frame,
                                        "El sueldo supera el máximo permitido (99999).",
                                        "Sueldo Inválido",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(frame,
                                    "El sueldo no puede estar por debajo del salario mínimo interprofesional.",
                                    "Sueldo Inválido",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (NumberFormatException nfe) {
                        JOptionPane.showMessageDialog(frame,
                                "Sueldo inválido.",
                                "Sueldo Inválido",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(frame,
                            "El sueldo no puede superar los 20 dígitos.",
                            "Sueldo Inválido",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    /**
     * Llamada a la base de datos para el cambio de sueldo de un jugador.
     * @param sueldo
     * @param jugador
     */
    private void cambiarSueldo(String sueldo, Jugador jugador) {
        String dni = jugador.getDni();
        String query = "{ call datos_jugador.modificar_sueldo(?,?) }";
        baseDeDatos.update(query, dni, sueldo);
    }

    /**
     * Inicialización de la ventana.
     * @param frameFondo
     */
    public void mostrar(JFrame frameFondo) {
        frame = new JFrame("Cambiar Sueldo");
        this.frameFondo = frameFondo;
        frame.setResizable(false);
        frame.setContentPane(panelPrincipal);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(frameFondo);
        frame.setVisible(true);
    }
}
