package com.daisa;

import javax.swing.*;

/**
 * Ventana para el cambio de nombre de un usuario.
 * @author David Roig
 * @author Isabel Montero
 */
public class CambiarNombreUsuario {
    private JTextField nombreTextField;
    private JButton cambiarNombreButton;
    private JButton cancelarButton;
    private JPanel panelPrincipal;
    private JFrame frame;
    private JFrame frameFondo;
    private BaseDeDatos baseDeDatos;
    private ModificarUsuario modificarUsuario;
    private Usuario usuario;

    /**
     * Constructor de la ventana.
     * @param baseDeDatos
     * @param usuario
     * @param modificarUsuario
     */
    public CambiarNombreUsuario(final BaseDeDatos baseDeDatos, final Usuario usuario, final ModificarUsuario modificarUsuario) {
        this.baseDeDatos = baseDeDatos;

        cancelarButton.addActionListener(e -> {
            frameFondo.setEnabled(true);
            frame.dispose();
        });

        cambiarNombreButton.addActionListener(e -> {
            if (!nombreTextField.getText().equals("")) {
                if (nombreTextField.getText().length() <= 20) {
                    int n = JOptionPane.showConfirmDialog(frame,
                            "El usuario \n" +
                                    "\"" + usuario.getId() + "\"\n" +
                                    "pasará a llamarse\n" +
                                    "\"" + nombreTextField.getText() + "\"\n" +
                                    "¿Desea continuar?",
                            "Confirmar",
                            JOptionPane.YES_NO_OPTION); // 0 yes, 1 no
                    if (n == 0) {
                        cambiarNombre(nombreTextField.getText(), usuario);

                        JOptionPane.showMessageDialog(frame, "Nombre cambiado correctamente", "Nombre Cambiado", JOptionPane.PLAIN_MESSAGE);

                        baseDeDatos.actualizar();

                        modificarUsuario.mostrar(frameFondo);
                        frameFondo.setEnabled(true);
                        frame.dispose();
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
     * Llamada a la base de datos para el cambio de nombre de usuario.
     * @param nombre
     * @param usuario
     */
    private void cambiarNombre(String nombre, Usuario usuario) {
        String userID = usuario.getId();
        String query = "update login set id_usuario = ? where id_usuario = ?";
        baseDeDatos.update(query, nombre, userID);
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
