package com.daisa;


import javax.swing.*;
import java.util.List;

/**
 * Ventana para modificar un equipo.
 *
 * @author David Roig
 * @author Isabel Montero
 */
public class ModificarEquipo {
    private JPanel panelPrincipal;
    private JButton atrasButton;
    private JPanel panelEquipo;
    private JLabel nombreEquipo;
    private JButton addJugadorButton;
    private JButton expulsarJugadorButton;
    private JButton cambiarNombreButton;
    private JButton transferirEquipoButton;
    private JScrollPane jugadoresScrollPane;
    private BaseDeDatos baseDeDatos;
    private Equipo equipo;
    private JFrame frame;
    private List<Jugador> jugadoresEquipo;
    private GestionEquipo gestionEquipo;
    private final ModificarEquipo modificarEquipo = this;
    private VentanaPrincipalAdmin ventanaPrincipalAdmin;

    /**
     * Constructor de la clase desde la ventana de director.
     * @param baseDeDatos
     * @param equipo
     * @param gestionEquipo
     */
    public ModificarEquipo(final BaseDeDatos baseDeDatos, final Equipo equipo, final GestionEquipo gestionEquipo) {
        this.baseDeDatos = baseDeDatos;
        this.equipo = equipo;
        this.gestionEquipo = gestionEquipo;
        jugadoresEquipo = equipo.getJugadores();

        atrasButton.addActionListener(e -> gestionEquipo.mostrar(frame));

        cambiarNombreButton.addActionListener(e -> {
            CambiarNombreEquipo cambiarNombreEquipo = new CambiarNombreEquipo(baseDeDatos, equipo, modificarEquipo);
            cambiarNombreEquipo.mostrar(frame);
            frame.setEnabled(false);
        });

        addJugadorButton.addActionListener(e -> {
            AddJugadorVentana addJugadorVentana = new AddJugadorVentana(baseDeDatos, equipo, modificarEquipo);
            addJugadorVentana.mostrar(frame);
        });
        expulsarJugadorButton.addActionListener(e -> {
            RemoveJugador removeJugador = new RemoveJugador(baseDeDatos, equipo, modificarEquipo);
            removeJugador.mostrar(frame);
        });
        transferirEquipoButton.addActionListener(e -> {
            TransferirEquipo transferirEquipo = new TransferirEquipo(baseDeDatos, equipo, modificarEquipo);
            transferirEquipo.mostrar(frame);
            frame.setEnabled(false);
        });
    }

    /**
     * Constructor de la clase desde la ventana de administrador.
     * @param baseDeDatos
     * @param equipo
     * @param ventanaPrincipalAdmin
     */
    public ModificarEquipo(final BaseDeDatos baseDeDatos, final Equipo equipo, final VentanaPrincipalAdmin ventanaPrincipalAdmin) {
        this.baseDeDatos = baseDeDatos;
        this.equipo = equipo;
        this.ventanaPrincipalAdmin = ventanaPrincipalAdmin;
        jugadoresEquipo = equipo.getJugadores();

        atrasButton.addActionListener(e -> ventanaPrincipalAdmin.mostrar(frame));

        cambiarNombreButton.addActionListener(e -> {
            CambiarNombreEquipo cambiarNombreEquipo = new CambiarNombreEquipo(baseDeDatos, equipo, modificarEquipo);
            cambiarNombreEquipo.mostrar(frame);
            frame.setEnabled(false);
        });

        addJugadorButton.addActionListener(e -> {
            AddJugadorVentana addJugadorVentana = new AddJugadorVentana(baseDeDatos, equipo, modificarEquipo);
            addJugadorVentana.mostrar(frame);
        });
        expulsarJugadorButton.addActionListener(e -> {
            RemoveJugador removeJugador = new RemoveJugador(baseDeDatos, equipo, modificarEquipo);
            removeJugador.mostrar(frame);
        });
        transferirEquipoButton.addActionListener(e -> {
            TransferirEquipo transferirEquipo = new TransferirEquipo(baseDeDatos, equipo, modificarEquipo);
            transferirEquipo.mostrar(frame);
            frame.setEnabled(false);
        });
    }

    /**
     * Devuelve una tabla con los jugadores pertenecientes al equipo.
     * @param jugadores
     * @return JTable con los jugadores del equipo.
     */
    private JTable crearTablaJugadores(List<Jugador> jugadores) {
        String[] columnas = {"Nickname", "Nombre", "Teléfono", "Fecha Alta"};
        String[][] jugadoresLista = new String[jugadores.size()][4];
        int count = 0;
        for (Jugador jugador : jugadores) {
            jugadoresLista[count][0] = jugador.getNickname();
            jugadoresLista[count][1] = jugador.getNombre();
            jugadoresLista[count][2] = jugador.getTelefono();
            jugadoresLista[count][3] = jugador.getContratoVigente().getFechaAlta();
            count++;
        }

        JTable table = new JTable(jugadoresLista, columnas);
        table.setCellSelectionEnabled(false);
        table.setRowSelectionAllowed(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setDefaultEditor(Object.class, null);
        return table;
    }

    /**
     * Inicialización de la ventana.
     * @param frame
     */
    public void mostrar(JFrame frame) {
        this.frame = frame;
        nombreEquipo.setText(equipo.getNombre());
        final JTable tablaJugadores = crearTablaJugadores(equipo.getJugadores());
        jugadoresScrollPane.getViewport().add(tablaJugadores);
        addJugadorButton.setEnabled(jugadoresEquipo.size() < 6);
        expulsarJugadorButton.setEnabled(jugadoresEquipo.size() > 0);
        frame.setContentPane(panelPrincipal);
        frame.pack();
    }

    public GestionEquipo getGestionEquipo() {
        return gestionEquipo;
    }
}
