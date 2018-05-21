package com.daisa;


import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Ventana para la gestión de equipos del director.
 *
 * @author David Roig
 * @author Isabel Montero
 */
public class GestionEquipo {

    private JPanel panelPrincipal;
    private JPanel panelGestion;
    private JButton crearEquipoButton;
    private JButton gestionEquipoButton;
    private JScrollPane scroll;
    private BaseDeDatos baseDeDatos;
    private Director director;
    private JFrame frame;
    private List<Equipo> equiposDirector;
    private final GestionEquipo gestionEquipo = this;
    private JTable tablaEquipos;

    /**
     * Constructor de la clase.
     * @param usuario
     * @param baseDeDatos
     */
    public GestionEquipo(final String usuario, final BaseDeDatos baseDeDatos) {
        this.baseDeDatos = baseDeDatos;
        director = obtenerDirector(usuario);
        equiposDirector = filtrarEquipos(baseDeDatos.getEquipos());

        crearEquipoButton.addActionListener(e -> {
            CrearEquipo crearEquipo = new CrearEquipo(baseDeDatos,gestionEquipo);
            crearEquipo.mostrar(frame);
            frame.setEnabled(false);
        });
    }

    /**
     * Filtra los equipos, devolviendo una lista con sólo los que pertenecen al director.
     * @param equipos
     * @return Lista de Equipo ligados al director.
     */
    private List<Equipo> filtrarEquipos(List<Equipo> equipos) {
        List<Equipo> equipoFiltrado = new ArrayList<>();
        for (Equipo equipo : equipos) {
            if (equipo.getDirector().getDni().equals(director.getDni())) equipoFiltrado.add(equipo);
        }
        return equipoFiltrado;
    }

    /**
     * Devuelve la tabla con los equipos del director.
     * @param equipos
     * @return JTable con equipos del director.
     */
    private JTable configurarTabla(List<Equipo> equipos) {
        String[] columnas = {"ID Equipo", "Nombre", "Localidad", "Número Jugadores"};
        String[][] equiposLista = new String[equipos.size()][4];
        int count = 0;
        for (Equipo equipo : equipos) {
            equiposLista[count][0] = String.valueOf(equipo.getId());
            equiposLista[count][1] = equipo.getNombre();
            equiposLista[count][2] = equipo.getLocalidad();
            equiposLista[count][3] = String.valueOf(equipo.getJugadores().size());
            count++;
        }
        JTable table = new JTable(equiposLista, columnas);
        table.setCellSelectionEnabled(false);
        table.setRowSelectionAllowed(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setDefaultEditor(Object.class, null);
        return table;
    }

    /**
     * Devuelve el director en base a su ID de usuario.
     * @param idUsuario
     * @return El Director correspondiente a su ID de usuario.
     */
    private Director obtenerDirector(String idUsuario) {
        List<Director> directores = baseDeDatos.getDirectores();
        Director director = null;
        for (int i = 0; i < directores.size(); i++) {
            if (directores.get(i).getIdUsuario().equals(idUsuario)) {
                director = directores.get(i);
                i = directores.size();
            }
        }
        return director;
    }

    /**
     * Inicialización de la ventana.
     * @param frame
     */
    public void mostrar(final JFrame frame) {
        this.frame = frame;
        frame.setContentPane(panelPrincipal);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        equiposDirector = filtrarEquipos(baseDeDatos.getEquipos());
        gestionEquipoButton.setEnabled(false);
        tablaEquipos = configurarTabla(equiposDirector);
        scroll.getViewport().add(tablaEquipos);

        tablaEquipos.getSelectionModel().addListSelectionListener(event -> gestionEquipoButton.setEnabled(true));

        gestionEquipoButton.addActionListener(e -> {
            if (gestionEquipoButton.isEnabled()) {

                Equipo equipo = equiposDirector.get(tablaEquipos.getSelectedRow());
                ModificarEquipo modificarEquipo = new ModificarEquipo(baseDeDatos, equipo, gestionEquipo);
                modificarEquipo.mostrar(frame);
            }
        });

        frame.pack();

    }

    public Director getDirector() {
        return director;
    }
}
