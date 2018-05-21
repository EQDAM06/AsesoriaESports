package com.daisa;

import javax.swing.*;

/**
 * Ventana para el cambio de nombre de un jugador.
 * @author David Roig
 * @author Isabel Montero
 */
public class CambiarNombreJugador {
    private JTextField nombreTextField;
    private JButton cambiarNombreButton;
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
    public CambiarNombreJugador(final BaseDeDatos baseDeDatos, final Jugador jugador, final ModificarJugador modificarJugador) {
        this.baseDeDatos = baseDeDatos;
        this.modificarJugador = modificarJugador;
        this.jugador = jugador;

        cancelarButton.addActionListener(e -> {
            frameFondo.setEnabled(true);
            frame.dispose();
        });

        cambiarNombreButton.addActionListener(e -> {
            if (!nombreTextField.getText().equals("")) {
                if (nombreTextField.getText().length() <= 50) {
                    int n = JOptionPane.showConfirmDialog(frame,
                            "El jugador \n" +
                                    "\"" + jugador.getNombre() + "\"\n" +
                                    "pasará a llamarse\n" +
                                    "\"" + nombreTextField.getText() + "\"\n" +
                                    "¿Desea continuar?",
                            "Confirmar",
                            JOptionPane.YES_NO_OPTION); // 0 yes, 1 no
                    if (n == 0) {
                        cambiarNombre(nombreTextField.getText(), jugador);
                        jugador.setNombre(nombreTextField.getText());
                        JOptionPane.showMessageDialog(frame, "Nombre cambiado correctamente", "Nombre Cambiado", JOptionPane.PLAIN_MESSAGE);

                        baseDeDatos.actualizar();

                        modificarJugador.mostrar(frameFondo);
                        frameFondo.setEnabled(true);
                        frame.dispose();
                    }
                } else {
                    JOptionPane.showMessageDialog(frame,
                            "El nombre no puede superar los 50 carácteres.",
                            "Nombre Inválido",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    /**
     * Llamada a la base de datos para el cambio de nombre del jugador.
     * @param nombre
     * @param jugador
     */
    private void cambiarNombre(String nombre, Jugador jugador) {
        String dni = jugador.getDni();
        String query = "update jugador set nombre = ? where dni = ?";
        baseDeDatos.update(query, nombre, dni);
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
