package com.daisa;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Ventana principal de administrador.
 *
 * @author David Roig
 * @author Isabel Montero
 */
public class VentanaPrincipalAdmin {
    private String usuario;
    private BaseDeDatos baseDeDatos;

    private JPanel panelPrincipal;
    private JButton ligaButton;
    private JButton equipoButton;
    private JButton jugadorButton;
    private JButton usuarioButton;
    private JButton addLigaButton;
    private JButton addEquipoButton;
    private JButton modLigaButton;
    private JButton modEquipoButton;
    private JButton addJugadorButton;
    private JButton modJugadorButton;
    private JButton addUsuarioButton;
    private JButton modUsuarioButton;
    private JPanel panelLiga;
    private JPanel panelEquipo;
    private JPanel panelJugador;
    private JPanel panelUsuario;
    private JPanel panelSuperior;
    private JPanel panelInferior;
    private JScrollPane infoScrollPane;
    private JTextPane infoTextPane;
    private JLabel escudoLabel;
    private JFrame frame;
    private VentanaPrincipalAdmin ventanaPrincipalAdmin = this;

    /**
     * Inicialización de la ventana
     *
     * @param frame
     */
    public void mostrar(JFrame frame) {
        this.frame = frame;
        frame.setTitle("Asesoría E-Sports - Admin // " + usuario);
        frame.setContentPane(panelPrincipal);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        cargarInfo();

        frame.pack();
    }

    //TODO: Ventana con información general de la aplicación y la base de datos
    private void cargarInfo() {
        infoTextPane.setText("Usuario: " + usuario + " --- Sistema de administración Agencía E-Sports ---"
                + "\nBase de Datos: " + baseDeDatos.getConexion()
                + "\n           Número de usuarios: " + baseDeDatos.cargarUsuarios().size()
                + "\n           Número de directores: " + baseDeDatos.getDirectores().size()
                + "\n           Número de jugadores: " + baseDeDatos.getJugadores().size()
                + "\n           Número de equipos: " + baseDeDatos.getEquipos().size()
                + "\n           Número de contratos: " + baseDeDatos.getContratos().size()
                + "\n           Número de ligas: " + baseDeDatos.getLigas().size()
                + "\n           Número de jornadas de liga: " + baseDeDatos.getJornadas().size()
                + "\n           Número de encuentros de liga: " + baseDeDatos.getEncuentros().size());

    }

    /**
     * Constructor de la clase.
     *
     * @param baseDeDatos
     * @param usuario
     */
    public VentanaPrincipalAdmin(final BaseDeDatos baseDeDatos, final String usuario) {
        this.baseDeDatos = baseDeDatos;
        baseDeDatos.actualizar();

        this.usuario = usuario;

        ligaButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                addEquipoButton.setVisible(false);
                modEquipoButton.setVisible(false);
                addJugadorButton.setVisible(false);
                modJugadorButton.setVisible(false);
                addUsuarioButton.setVisible(false);
                modUsuarioButton.setVisible(false);
                addLigaButton.setVisible(true);
                modLigaButton.setVisible(true);
            }
        });

        equipoButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                addLigaButton.setVisible(false);
                modLigaButton.setVisible(false);
                addJugadorButton.setVisible(false);
                modJugadorButton.setVisible(false);
                addUsuarioButton.setVisible(false);
                modUsuarioButton.setVisible(false);
                addEquipoButton.setVisible(true);
                modEquipoButton.setVisible(true);
            }
        });

        jugadorButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                addLigaButton.setVisible(false);
                modLigaButton.setVisible(false);
                addUsuarioButton.setVisible(false);
                modUsuarioButton.setVisible(false);
                addEquipoButton.setVisible(false);
                modEquipoButton.setVisible(false);
                addJugadorButton.setVisible(true);
                modJugadorButton.setVisible(true);
            }

        });
        usuarioButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                addLigaButton.setVisible(false);
                modLigaButton.setVisible(false);
                addJugadorButton.setVisible(false);
                modJugadorButton.setVisible(false);
                addEquipoButton.setVisible(false);
                modEquipoButton.setVisible(false);
                addUsuarioButton.setVisible(true);
                modUsuarioButton.setVisible(true);
            }

        });
        panelInferior.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                addLigaButton.setVisible(false);
                modLigaButton.setVisible(false);
                addEquipoButton.setVisible(false);
                modEquipoButton.setVisible(false);
                addJugadorButton.setVisible(false);
                modJugadorButton.setVisible(false);
                addUsuarioButton.setVisible(false);
                modUsuarioButton.setVisible(false);
            }
        });
        addLigaButton.addActionListener(e -> {
            if (addLigaButton.isVisible()) {
                CrearLiga crearLiga = new CrearLiga(baseDeDatos, ventanaPrincipalAdmin);
                crearLiga.mostrar(frame);
            }
        });
        modLigaButton.addActionListener(e -> {
            if (modLigaButton.isVisible()) {
                GestionLiga gestionLiga = new GestionLiga(baseDeDatos, ventanaPrincipalAdmin);
                gestionLiga.mostrar(frame);
            }
        });
        addEquipoButton.addActionListener(e -> {
            CrearEquipo crearEquipo = new CrearEquipo(baseDeDatos, ventanaPrincipalAdmin);
            crearEquipo.mostrar(frame);
            frame.setEnabled(false);
        });
        modEquipoButton.addActionListener(e -> {
            SeleccionarEquipo seleccionarEquipo = new SeleccionarEquipo(baseDeDatos, ventanaPrincipalAdmin);
            seleccionarEquipo.mostrar(frame);
        });
        addJugadorButton.addActionListener(e -> {
            CrearJugador crearJugador = new CrearJugador(baseDeDatos, ventanaPrincipalAdmin);
            crearJugador.mostrar(frame);
        });
        modJugadorButton.addActionListener(e -> {
            ModificarJugador modificarJugador = new ModificarJugador(baseDeDatos, ventanaPrincipalAdmin);
            modificarJugador.mostrar(frame);
        });
        addUsuarioButton.addActionListener(e -> {
            CrearUsuario crearUsuario = new CrearUsuario(baseDeDatos, ventanaPrincipalAdmin);
            crearUsuario.mostrar(frame);
        });
        modUsuarioButton.addActionListener(e -> {
            ModificarUsuario modificarUsuario = new ModificarUsuario(baseDeDatos, ventanaPrincipalAdmin);
            modificarUsuario.mostrar(frame);
        });
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        escudoLabel = new JLabel(new ImageIcon("escudoTxiki.gif"));

    }
}
