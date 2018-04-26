package com.daisa;

import org.apache.commons.codec.digest.DigestUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Aquí va la documentación
 * @author David Roig
 * @author Isabel Montero
 */
public class LoginVentana {
    private JTextField usuarioField;
    private JPasswordField passwordField1;
    private JButton entrarButton;
    private JPanel panelPrincipal;

    public LoginVentana() {
        final JFrame frame = new JFrame("Asesoría E-Sports");
        frame.setContentPane(panelPrincipal);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.getRootPane().setDefaultButton(entrarButton);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        entrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String usuario = usuarioField.getText();
                String password = DigestUtils.sha1Hex(new String(passwordField1.getPassword())); //encriptado
                if (!usuario.equals("") || !usuario.equals("")) {
                    int tipoUsuario = login(usuario,password);
                    switch (tipoUsuario) {
                        case 0:
                            VentanaPrincipalUsuario ventanaPrincipalUsuario = new VentanaPrincipalUsuario(usuario, tipoUsuario);
                            ventanaPrincipalUsuario.mostrar();
                            frame.dispose();
                            break;
                        case 1:
                            ventanaPrincipalUsuario = new VentanaPrincipalUsuario(usuario, tipoUsuario);
                            ventanaPrincipalUsuario.mostrar();
                            frame.dispose();
                            break;
                        case 2:
                            VentanaPrincipalAdmin ventanaPrincipalAdmin = new VentanaPrincipalAdmin(usuario);
                            ventanaPrincipalAdmin.mostrar();
                            frame.dispose();
                            break;
                        case -1:
                            JOptionPane.showMessageDialog(frame,
                                    "Usuario o contraseña incorrectos.",
                                    "Error Autenticación",
                                    JOptionPane.ERROR_MESSAGE);
                            passwordField1.setText("");
                            break;
                    }
                }

            }
        });
    }

    /**
     * @param usuario   el nombre de usuario
     * @param password  la contraseña encriptada
     * @return -1 error; 0 usuario; 1 director; 2 administrador
     */
    private int login (String usuario, String password) {
        // TODO(Dave): lectura base de datos aquí
        return -1;
    }
}
