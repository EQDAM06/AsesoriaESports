package com.daisa;

import javax.swing.*;

/**
 * Ventana para el cambio de nombre de una liga.
 * @author David Roig
 * @author Isabel Montero
 */
public class CambiarNombreLiga {
    private JTextField nombreTextField;
    private JLabel nombreLabel;
    private JPanel panelPrincipal;
    private JButton cancelarButton;
    private JButton cambiarNombreButton;
    private BaseDeDatos baseDeDatos;
    private JFrame frameFondo;
    private JFrame frame;
    private Liga liga;
    private GestionLiga gestionLiga;

    /**
     * Constructor de la clase.
     * @param baseDeDatos
     * @param liga
     * @param gestionLiga
     */
    public CambiarNombreLiga(final BaseDeDatos baseDeDatos, final Liga liga, final GestionLiga gestionLiga) {
        this.baseDeDatos = baseDeDatos;
        this.liga = liga;
        this.gestionLiga = gestionLiga;

        cancelarButton.addActionListener(e -> {
            frameFondo.setEnabled(true);
            frame.dispose();
        });

        cambiarNombreButton.addActionListener(e -> {
            if (!nombreTextField.getText().equals("")) {
                if (nombreTextField.getText().length() <= 50) {
                    if (nombreEstaDisponible(nombreTextField.getText())) {
                        int n = JOptionPane.showConfirmDialog(frame,
                                "La liga \n" +
                                        "\"" + liga.getNombre() + "\"\n" +
                                        "pasará a llamarse\n" +
                                        "\"" + nombreTextField.getText() + "\"\n" +
                                        "¿Desea continuar?",
                                "Confirmar",
                                JOptionPane.YES_NO_OPTION); // 0 yes, 1 no
                        if (n == 0) {
                            cambiarNombre(baseDeDatos, nombreTextField.getText(), liga);
                            liga.setNombre(nombreTextField.getText());
                            JOptionPane.showMessageDialog(frame, "Nombre cambiado correctamente", "Nombre Cambiado", JOptionPane.PLAIN_MESSAGE);

                            baseDeDatos.actualizar();

                            gestionLiga.mostrar(frameFondo);
                            frameFondo.setEnabled(true);
                            frame.dispose();
                        }

                    } else {
                        JOptionPane.showMessageDialog(frame,
                                "El nombre no está disponible.",
                                "Nombre Inválido",
                                JOptionPane.ERROR_MESSAGE);
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
     * Llamada a la base de datos para el cambio de nombre.
     * @param baseDeDatos
     * @param nombre
     * @param liga
     */
    private void cambiarNombre(BaseDeDatos baseDeDatos, String nombre, Liga liga) {
        int ligaID = liga.getId();
        String query = "update liga set nombre = ? where id_liga = ?";
        baseDeDatos.update(query, nombre, ligaID);
    }

    /**
     * Devuelve las coincidencias del nombre propuesto con los ya existentes.
     * @param nombre
     * @return Devuelve true si ya existe y false si está disponible.
     */
    private boolean nombreEstaDisponible(String nombre) {
        boolean disponible = true;
        for (Liga liga : baseDeDatos.getLigas()) {
            if (liga.getNombre().equals(nombre)) disponible = false;
        }
        return disponible;
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
