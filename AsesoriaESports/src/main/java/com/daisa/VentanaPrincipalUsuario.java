package com.daisa;

import javax.swing.*;

public class VentanaPrincipalUsuario {
    private String usuario;
    private int tipoUsuario;
    private JPanel panelPrincipal;

    public void mostrar() {
        final JFrame frame;
        if (tipoUsuario == 1) frame = new JFrame("Asesoría E-Sports - Director // " + usuario);
        else frame = new JFrame("Asesoría E-Sports - Usuario // " + usuario);
        frame.setContentPane(panelPrincipal);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * @param usuario El nombre de usuario
     * @param tipoUsuario El tipo de usuario: 0 usuario normal, 1 director
     */
    public VentanaPrincipalUsuario(String usuario, int tipoUsuario) {
        this.usuario = usuario;
        this.tipoUsuario = tipoUsuario;
    }
}
