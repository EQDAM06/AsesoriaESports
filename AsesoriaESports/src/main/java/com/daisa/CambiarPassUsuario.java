package com.daisa;

import org.apache.commons.codec.digest.DigestUtils;

import javax.swing.*;

/**
 * Ventana para el cambio de contraseña de un usuario.
 * @author David Roig
 * @author Isabel Montero
 */
public class CambiarPassUsuario {
    private JButton cambiarPassButton;
    private JButton cancelarButton;
    private JPanel panelPrincipal;
    private JPasswordField passwordField1;
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
    public CambiarPassUsuario(final BaseDeDatos baseDeDatos, final Usuario usuario, final ModificarUsuario modificarUsuario) {
        this.baseDeDatos = baseDeDatos;
        this.modificarUsuario = modificarUsuario;
        this.usuario = usuario;

        cancelarButton.addActionListener(e -> {
            frameFondo.setEnabled(true);
            frame.dispose();
        });

        cambiarPassButton.addActionListener(e -> {
            if (DigestUtils.sha1Hex(new String(passwordField1.getPassword())).length() != 0) {
                if (DigestUtils.sha1Hex(new String(passwordField1.getPassword())).length() <= 50) {
                    int n = JOptionPane.showConfirmDialog(frame,
                            "Se cambiará la contraseña del usuario \n" +
                                    "\"" + usuario.getId() + "\"\n" +
                                    "¿Desea continuar?",
                            "Confirmar",
                            JOptionPane.YES_NO_OPTION); // 0 yes, 1 no
                    if (n == 0) {
                        cambiarPassword(DigestUtils.sha1Hex(new String(passwordField1.getPassword())), usuario);

                        JOptionPane.showMessageDialog(frame, "Contraseña cambiada correctamente", "Contraseña Cambiada", JOptionPane.PLAIN_MESSAGE);

                        baseDeDatos.actualizar();

                        modificarUsuario.mostrar(frameFondo);
                        frameFondo.setEnabled(true);
                        frame.dispose();
                    }
                } else {
                    JOptionPane.showMessageDialog(frame,
                            "La contraseña supera el límite de carácteres.",
                            "Contraseña Inválido",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    /**
     * Llamada a la base de datos para el cambio de contraseña.
     * @param password Contraseña ya encriptada.
     * @param usuario
     */
    private void cambiarPassword(String password, Usuario usuario) {
        String userID = usuario.getId();
        String query = "update login set contrasenia = ? where id_usuario = ?";
        baseDeDatos.update(query, password, userID);
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
