package com.daisa;

import javax.swing.*;

/**
 * Ventana para el cambio de teléfono de un jugador.
 * @author David Roig
 * @author Isabel Montero
 */
public class CambiarTelefonoJugador {
    private JTextField telefonoTextField;
    private JButton cambiarTelefonoButton;
    private JButton cancelarButton;
    private JPanel panelPrincipal;
    private JFrame frame;
    private JFrame frameFondo;
    private BaseDeDatos baseDeDatos;
    private Jugador jugador;
    private ModificarJugador modificarJugador;

    /**
     * Constructor de la clase.
     * @param baseDeDatos
     * @param jugador
     * @param modificarJugador
     */
    public CambiarTelefonoJugador(final BaseDeDatos baseDeDatos, final Jugador jugador, final ModificarJugador modificarJugador) {
        this.baseDeDatos = baseDeDatos;
        this.modificarJugador = modificarJugador;
        this.jugador = jugador;

        cancelarButton.addActionListener(e -> {
            frameFondo.setEnabled(true);
            frame.dispose();
        });

        cambiarTelefonoButton.addActionListener(e -> {
            if (!telefonoTextField.getText().equals("")) {
                if (telefonoTextField.getText().length() <= 15) {
                    int n = JOptionPane.showConfirmDialog(frame,
                            "El jugador \n" +
                                    "\"" + jugador.getNombre() + "\"\n" +
                                    "pasará a tener de telefono\n" +
                                    "\"" + telefonoTextField.getText() + "\"\n" +
                                    "¿Desea continuar?",
                            "Confirmar",
                            JOptionPane.YES_NO_OPTION); // 0 yes, 1 no
                    if (n == 0) {
                        cambiarTelefono(telefonoTextField.getText(), jugador);
                        jugador.setTelefono(telefonoTextField.getText());
                        JOptionPane.showMessageDialog(frame, "Telefono cambiado correctamente", "Telefono Cambiado", JOptionPane.PLAIN_MESSAGE);

                        baseDeDatos.actualizar();

                        modificarJugador.mostrar(frameFondo);
                        frameFondo.setEnabled(true);
                        frame.dispose();
                    }
                } else {
                    JOptionPane.showMessageDialog(frame,
                            "El telefono no puede superar los 15 carácteres.",
                            "Telefono Inválido",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    /**
     * Llamada a la base de datos para el cambio de teléfono.
     * @param telefono
     * @param jugador
     */
    private void cambiarTelefono(String telefono, Jugador jugador) {
        String dni = jugador.getDni();
        String query = "{ call datos_jugador.modificar_telefono(?,?) }";
        baseDeDatos.update(query, dni, telefono);
    }

    /**
     * Inicialización de la ventana.
     * @param frameFondo
     */
    public void mostrar(JFrame frameFondo) {
        frame = new JFrame("Cambiar Telefono");
        this.frameFondo = frameFondo;
        frame.setResizable(false);
        frame.setContentPane(panelPrincipal);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(frameFondo);
        frame.setVisible(true);
    }
}
