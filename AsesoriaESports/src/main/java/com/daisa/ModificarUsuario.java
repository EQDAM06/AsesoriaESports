package com.daisa;

import javax.swing.*;
import java.util.List;

/**
 * Ventana para seleccionar y modificar usuarios.
 *
 * @author David Roig
 * @author Isabel Montero
 */
public class ModificarUsuario {
    private JPanel panelPrincipal;
    private JButton cambiarIDUsuarioButton;
    private JButton cambiarContrasenaButton;
    private JScrollPane usuariosScrollPane;
    private JButton cancelarButton;
    private JFrame frame;
    private BaseDeDatos baseDeDatos;
    private VentanaPrincipalAdmin ventanaPrincipalAdmin;
    private List<Usuario> usuarios;
    private JTable usuariosTable;
    private ModificarUsuario modificarUsuario = this;

    /**
     * Constructor de la clase.
     * @param baseDeDatos
     * @param ventanaPrincipalAdmin
     */
    public ModificarUsuario(BaseDeDatos baseDeDatos, VentanaPrincipalAdmin ventanaPrincipalAdmin) {
        this.baseDeDatos = baseDeDatos;
        this.ventanaPrincipalAdmin = ventanaPrincipalAdmin;

        cambiarIDUsuarioButton.addActionListener(e -> {
            Usuario usuario = usuarios.get(usuariosTable.getSelectedRow());
            CambiarNombreUsuario cambiarNombreUsuario = new CambiarNombreUsuario(baseDeDatos,usuario,modificarUsuario);
            cambiarNombreUsuario.mostrar(frame);
            frame.setEnabled(false);
        });

        cambiarContrasenaButton.addActionListener(e -> {
            Usuario usuario = usuarios.get(usuariosTable.getSelectedRow());
            CambiarPassUsuario cambiarPassUsuario = new CambiarPassUsuario(baseDeDatos, usuario,modificarUsuario);
            cambiarPassUsuario.mostrar(frame);
            frame.setEnabled(false);
        });

        cancelarButton.addActionListener(e -> {
            ventanaPrincipalAdmin.mostrar(frame);
        });
    }

    /**
     * Inicialización de la ventana.
     * @param frame
     */
    public void mostrar(final JFrame frame) {
        this.frame = frame;
        usuarios = baseDeDatos.cargarUsuarios();
        frame.setContentPane(panelPrincipal);
        usuariosTable = crearTablaUsuario();
        usuariosScrollPane.getViewport().add(usuariosTable);

        usuariosTable.getSelectionModel().addListSelectionListener(e -> {
            cambiarIDUsuarioButton.setEnabled(true);
            cambiarContrasenaButton.setEnabled(true);

        });

        cambiarIDUsuarioButton.setEnabled(false);
        cambiarContrasenaButton.setEnabled(false);


        frame.pack();
    }

    /**
     * Devuelve una tabla con todos los usuarios.
     * @return JTable con todos los usuarios.
     */
    public JTable crearTablaUsuario() {
        String[] columnas = {"ID Usuario", "Tipo Usuario"};
        String[][] usuariosLista = new String[usuarios.size()][2];

        int count = 0;
        for (Usuario usuario : usuarios) {
            usuariosLista[count][0] = usuario.getId();
            usuariosLista[count][1] = usuario.getTipoUsuario();
            count++;
        }

        JTable table = new JTable(usuariosLista, columnas);
        table.setCellSelectionEnabled(false);
        table.setRowSelectionAllowed(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setDefaultEditor(Object.class, null);
        return table;
    }
}

