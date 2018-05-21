package com.daisa;

import javax.swing.*;
/**
 * Ventana para el cambio de nickname de un jugador.
 * @author David Roig
 * @author Isabel Montero
 */
public class CambiarNicknameJugador {
    private JPanel panelPrincipal;
    private JTextField nicknameTextField;
    private JButton cambiarNicknameButton;
    private JButton cancelarButton;
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
    public CambiarNicknameJugador(final BaseDeDatos baseDeDatos, final Jugador jugador, final ModificarJugador modificarJugador) {
        this.baseDeDatos = baseDeDatos;
        this.modificarJugador = modificarJugador;
        this.jugador = jugador;

        cancelarButton.addActionListener(e -> {
            frameFondo.setEnabled(true);
            frame.dispose();
        });

        cambiarNicknameButton.addActionListener(e -> {
            if (!nicknameTextField.getText().equals("")) {
                if (nicknameTextField.getText().length() <= 50) {
                    int n = JOptionPane.showConfirmDialog(frame,
                            "El jugador \n" +
                                    "\"" + jugador.getNombre() + "\"\n" +
                                    "pasará a tener de nickname\n" +
                                    "\"" + nicknameTextField.getText() + "\"\n" +
                                    "¿Desea continuar?",
                            "Confirmar",
                            JOptionPane.YES_NO_OPTION); // 0 yes, 1 no
                    if (n == 0) {
                        cambiarNickname(nicknameTextField.getText(), jugador);
                        jugador.setNickname(nicknameTextField.getText());
                        JOptionPane.showMessageDialog(frame, "Nickname cambiado correctamente", "Nickname Cambiado", JOptionPane.PLAIN_MESSAGE);

                        baseDeDatos.actualizar();

                        modificarJugador.mostrar(frameFondo);
                        frameFondo.setEnabled(true);
                        frame.dispose();
                    }
                } else {
                    JOptionPane.showMessageDialog(frame,
                            "El nickname no puede superar los 50 carácteres.",
                            "Nickname Inválido",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    /**
     * Llamada a la base de datos para el cambio de nickname.
     * @param nickname
     * @param jugador
     */
    private void cambiarNickname(String nickname, Jugador jugador) {
        String dni = jugador.getDni();
        String query = "{ call datos_jugador.modificar_nickname(?,?) }";
        baseDeDatos.update(query, dni, nickname);
    }

    /**
     * Inicialización de la ventana.
     * @param frameFondo
     */
    public void mostrar(JFrame frameFondo) {
        frame = new JFrame("Cambiar Nickname");
        this.frameFondo = frameFondo;
        frame.setResizable(false);
        frame.setContentPane(panelPrincipal);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(frameFondo);
        frame.setVisible(true);
    }
}
