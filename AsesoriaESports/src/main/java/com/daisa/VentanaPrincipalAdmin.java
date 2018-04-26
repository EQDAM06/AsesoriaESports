package com.daisa;

import javax.swing.*;

public class VentanaPrincipalAdmin {
    private String usuario;


    private JPanel panelPrincipal;

    public void mostrar() {
        final JFrame frame = new JFrame("Asesoría E-Sports - Admin");
        frame.setContentPane(panelPrincipal);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Constructor de la ventana
     * @param usuario   el nombre de usuario
     */
    public VentanaPrincipalAdmin(String usuario) {
        this.usuario = usuario;
    }
}
