package com.daisa;

import org.apache.commons.codec.digest.DigestUtils;

import javax.swing.*;

/**
 * Ventana de inicio de sesión.
 *
 * @author David Roig
 * @author Isabel Montero
 */
public class LoginVentana {
    private JTextField usuarioField;
    private JPasswordField passwordField1;
    private JButton entrarButton;
    private JPanel panelPrincipal;
    private JLabel escudoLabel;
    private BaseDeDatos baseDeDatos;

    /**
     * Constructor de la clase.
     * @param baseDeDatos
     */
    public LoginVentana(final BaseDeDatos baseDeDatos) {
        this.baseDeDatos = baseDeDatos;
        final JFrame frame = new JFrame("Asesoría E-Sports");
        frame.setResizable(false);
        frame.setContentPane(panelPrincipal);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.getRootPane().setDefaultButton(entrarButton);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        entrarButton.addActionListener(e -> {
            String usuario = usuarioField.getText();
            String password = DigestUtils.sha1Hex(new String(passwordField1.getPassword())); //encriptado

            if (!usuario.equals("") || !usuario.equals("")) {
                int tipoUsuario = login(usuario, password);
                switch (tipoUsuario) {
                    case 0: // Usuario normal
                        VentanaPrincipalUsuario ventanaPrincipalUsuario = new VentanaPrincipalUsuario(baseDeDatos, usuario, tipoUsuario);
                        ventanaPrincipalUsuario.mostrar(frame);
                        break;

                    case 1: // Director
                        ventanaPrincipalUsuario = new VentanaPrincipalUsuario(baseDeDatos, usuario, tipoUsuario);
                        ventanaPrincipalUsuario.mostrar(frame);
                        break;

                    case 2: // Administrador
                        VentanaPrincipalAdmin ventanaPrincipalAdmin = new VentanaPrincipalAdmin(baseDeDatos, usuario);
                        ventanaPrincipalAdmin.mostrar(frame);
                        break;

                    case -1: // Error
                        JOptionPane.showMessageDialog(frame,
                                "Usuario o contraseña incorrectos.",
                                "Error Autenticación",
                                JOptionPane.ERROR_MESSAGE);
                        passwordField1.setText("");
                        break;
                }
            }

        });
    }

    /**
     * Intenta hacer login usando los datos que se encuentran en la base de datos
     *
     * @param usuario  el nombre de usuario
     * @param password la contraseña encriptada
     * @return Tipo de usuario: -1 error, 0 usuario normal, 1 director, 2 administrador
     */
    private int login(String usuario, String password) {

        switch ((baseDeDatos.login(usuario, password)).toUpperCase()) {
            case "USER":
                return 0;
            case "DIR":
                return 1;
            case "ADMIN":
                return 2;
            default:
                return -1;
        }
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        escudoLabel = new JLabel(new ImageIcon("escudo.gif"));
    }
}
