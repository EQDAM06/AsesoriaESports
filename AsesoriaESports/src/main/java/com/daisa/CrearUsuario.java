package com.daisa;

import org.apache.commons.codec.digest.DigestUtils;

import javax.swing.*;

/**
 * Ventana para la creación de un usuario.
 * @author David Roig
 * @author Isabel Montero
 */
public class CrearUsuario {
    private JPanel panelPrincipal;
    private JButton cancelarButton;
    private JButton crearButton;
    private JTextField idTextField;
    private JPasswordField passwordField1;
    private JComboBox tipoUsuarioComboBox;
    private JTextField nombreTextField;
    private JTextField dniTextField;
    private JTextField telefonoTextField;
    private JTextField emailTextField;
    private JPanel panelDirector;
    private JFrame frame;
    private BaseDeDatos baseDeDatos;
    private VentanaPrincipalAdmin ventanaPrincipalAdmin;

    /**
     * Constructor de la clase.
     * @param baseDeDatos
     * @param ventanaPrincipalAdmin
     */
    public CrearUsuario(BaseDeDatos baseDeDatos, VentanaPrincipalAdmin ventanaPrincipalAdmin) {
        this.baseDeDatos = baseDeDatos;
        this.ventanaPrincipalAdmin = ventanaPrincipalAdmin;
        tipoUsuarioComboBox.addActionListener(e -> {
            if (tipoUsuarioComboBox.getSelectedIndex() == 1) panelDirector.setVisible(true);
            else panelDirector.setVisible(false);
        });

        cancelarButton.addActionListener(e -> {
            ventanaPrincipalAdmin.mostrar(frame);
        });

        crearButton.addActionListener(e -> {
            String error = null;
            boolean isDirector = tipoUsuarioComboBox.getSelectedIndex() == 1;
            if ((idTextField.getText().isEmpty() || passwordField1.getPassword().length == 0) ||
                    (isDirector &&
                            (nombreTextField.getText().isEmpty() || dniTextField.getText().isEmpty() ||
                                    telefonoTextField.getText().isEmpty() || emailTextField.getText().isEmpty()))) {
                error = "CAMPO_VACIO";
            }
            if (error == null && nombreUsuarioExiste(idTextField.getText())) {
                error = "ID_EXIST";
            }
            if (error == null && idTextField.getText().length() > 20) {
                error = "ID_LEN";
            }
            if (error == null && DigestUtils.sha1Hex(new String(passwordField1.getPassword())).length() > 50) {
                error = "PASS_LEN";
            }
            if (error == null && isDirector) {
                if (dniExiste(dniTextField.getText())) error = "DNI_EXIST";
            }
            if (error == null && isDirector && dniTextField.getText().length() > 9) {
                error = "DNI_LEN";
            }
            if (error == null && isDirector && nombreTextField.getText().length() > 50) {
                error = "NOM_LEN";
            }
            if (error == null && isDirector && telefonoTextField.getText().length() > 15) {
                error = "TEL_LEN";
            }
            if (error == null && isDirector && emailTextField.getText().length() > 30) {
                error = "EMAIL_LEN";
            }

            if (error == null) {
                int n = JOptionPane.showConfirmDialog(frame,
                        "Se va a crear al usuario " +
                                idTextField.getText() + "\n" +
                                "¿Desea continuar?",
                        "Confirmar",
                        JOptionPane.YES_NO_OPTION); // 0 yes, 1 no
                if (n == 0) {
                    String id = idTextField.getText();
                    String password = DigestUtils.sha1Hex(new String(passwordField1.getPassword()));
                    String tipoUsuario;

                    if (!isDirector) {
                        if (tipoUsuarioComboBox.getSelectedIndex() == 2) tipoUsuario = "ADMIN";
                        else tipoUsuario = "USER";

                        baseDeDatos.crearUsuario(id, password, tipoUsuario);
                        JOptionPane.showMessageDialog(frame, "Usuario creado correctamente", "Usuario Creado", JOptionPane.PLAIN_MESSAGE);

                        baseDeDatos.actualizar();

                        ventanaPrincipalAdmin.mostrar(frame);

                    } else {
                        String nombre = nombreTextField.getText();
                        String dni = dniTextField.getText();
                        String telefono = telefonoTextField.getText();
                        String email = emailTextField.getText();

                        baseDeDatos.crearDirector(id, password, dni, nombre, telefono, email);
                        JOptionPane.showMessageDialog(frame, "Director creado correctamente", "Director Creado", JOptionPane.PLAIN_MESSAGE);

                        baseDeDatos.actualizar();

                        ventanaPrincipalAdmin.mostrar(frame);
                    }
                }
            } else switch (error) {
                case "CAMPO_VACIO":
                    JOptionPane.showMessageDialog(frame,
                            "Ninguno de los campos puede estar vacio.",
                            "Campos Vacios",
                            JOptionPane.ERROR_MESSAGE);
                    break;
                case "ID_EXIST":
                    JOptionPane.showMessageDialog(frame,
                            "El ID de usuario ya existe.",
                            "ID Inválida",
                            JOptionPane.ERROR_MESSAGE);
                    break;
                case "ID_LEN":
                    JOptionPane.showMessageDialog(frame,
                            "El ID de usuario no puede superar los 20 carácteres.",
                            "ID Inválido",
                            JOptionPane.ERROR_MESSAGE);
                    break;
                case "PASS_LEN":
                    JOptionPane.showMessageDialog(frame,
                            "La contraseña no puede superar los 50 carácteres.",
                            "Contraseña Inválida",
                            JOptionPane.ERROR_MESSAGE);
                    break;
                case "DNI_EXIST":
                    JOptionPane.showMessageDialog(frame,
                            "El director ya existe.",
                            "DNI Inválido",
                            JOptionPane.ERROR_MESSAGE);
                    break;
                case "DNI_LEN":
                    JOptionPane.showMessageDialog(frame,
                            "El DNI no puede superar los 9 carácteres.",
                            "DNI Inválido",
                            JOptionPane.ERROR_MESSAGE);
                    break;
                case "NOM_LEN":
                    JOptionPane.showMessageDialog(frame,
                            "El nombre no puede superar los 50 carácteres.",
                            "Nombre Inválido",
                            JOptionPane.ERROR_MESSAGE);
                    break;
                case "TEL_LEN":
                    JOptionPane.showMessageDialog(frame,
                            "El teléfono no puede superar los 15 carácteres.",
                            "Teléfono Inválido",
                            JOptionPane.ERROR_MESSAGE);
                    break;
                case "EMAIL_LEN":
                    JOptionPane.showMessageDialog(frame,
                            "El email no puede superar los 30 carácteres.",
                            "Email Inválido",
                            JOptionPane.ERROR_MESSAGE);
                    break;
            }
        });
    }

    /**
     * Devuelve la disponibilidad del DNI para el nuevo director.
     * @param dni
     * @return True si el DNI ya existe, false si está disponible.
     */
    private boolean dniExiste(String dni) {
        for (Director director : baseDeDatos.getDirectores()) {
            if (director.getDni().toUpperCase().equals(dni.toUpperCase())) return true;
        }
        return false;
    }

    /**
     * Devuelve la disponibilidad del nombre para el nuevo usuario.
     * @param nombre
     * @return True si el usuario ya existe, false si está disponible.
     */
    private boolean nombreUsuarioExiste(String nombre) {
        int count = baseDeDatos.selectCount("login", "id_usuario", nombre);
        return (count != 0);
    }

    /**
     * Inicializa la ventana.
     * @param frame
     */
    public void mostrar(final JFrame frame) {
        this.frame = frame;
        frame.setContentPane(panelPrincipal);
        frame.pack();
    }
}
