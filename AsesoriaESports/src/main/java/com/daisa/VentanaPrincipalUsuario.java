package com.daisa;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Ventana principal para usuario y director
 *
 * @author David Roig
 * @author Isabel Montero
 */
public class VentanaPrincipalUsuario {
    private String usuario;
    private int tipoUsuario;
    private JPanel panelPrincipal;
    private JComboBox comboLigas;
    private JButton gestionButton;
    private JButton jorAntButton;
    private JScrollPane ligasScrollPanel;
    private JLabel ligaLabel;
    private BaseDeDatos baseDeDatos;
    private JFrame frame;
    private boolean[] iniciado = {false};

    /**
     * Inicialización de la ventana.
     * @param frame
     */
    public void mostrar(JFrame frame) {
        baseDeDatos.actualizar();
        this.frame = frame;
        if (tipoUsuario == 1) frame.setTitle("Asesoría E-Sports - Director // " + usuario);
        else frame.setTitle("Asesoría E-Sports - Usuario // " + usuario);
        frame.setContentPane(panelPrincipal);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        if (tipoUsuario == 0) gestionButton.setVisible(false);  // Esconde el botón de gestión para usuarios base

        JTable ultimaJornadaTable = crearUltimaJornadaTable();
        ligasScrollPanel.getViewport().add(ultimaJornadaTable);

        for (Liga liga : baseDeDatos.getLigas()) {
            comboLigas.addItem(liga.getNombre());
        }
        frame.pack();

    }

    /**
     * Devuelve una tabla con la última jornada puntuada de cada liga.
     * @return JTable con los encuentros de la última jornada actualizada de cada liga.
     */
    private JTable crearUltimaJornadaTable() {
        String[] columnas = {"Liga", "Jornada", "Equipo 1", "Equipo 2", "Ganador"};

        List<Encuentro> encuentros = new ArrayList<>();
        List<Jornada> jornadas = new ArrayList<>();
        for (Liga liga : baseDeDatos.getLigas()) {
            int ultimaJornada = 0;
            for (Jornada jornada : liga.getJornadas()) {
                for (Encuentro encuentro : jornada.getEncuentros()) {
                    if (encuentro.getGanador() != 0 && jornada.getDia() > ultimaJornada) ultimaJornada = jornada.getDia();
                }
            }
            if (ultimaJornada != 0) {
                for (int i = 0; i < liga.getJornadas().size(); i++) {
                    if (liga.getJornadas().get(i).getDia() == ultimaJornada) {
                        jornadas.add(liga.getJornadas().get(i));
                        i = liga.getJornadas().size();
                    }
                }
            }
        }
        for (Jornada jornada : jornadas) {
            encuentros.addAll(jornada.getEncuentros());
        }


        String[][] encuentrosLista = new String[encuentros.size()][5];

        int count = 0;
        for (Encuentro encuentro : encuentros) {
            encuentrosLista[count][0] = encuentro.getLiga().getNombre();
            encuentrosLista[count][1] = String.valueOf(encuentro.getJornada().getDia());
            encuentrosLista[count][2] = encuentro.getEquipos()[0].getNombre();
            encuentrosLista[count][3] = encuentro.getEquipos()[1].getNombre();
            encuentrosLista[count][4] = encuentro.getEquipos()[encuentro.getGanador() - 1].getNombre();
            count++;
        }

        JTable table = new JTable(encuentrosLista, columnas);
        table.setCellSelectionEnabled(false);
        table.setRowSelectionAllowed(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setDefaultEditor(Object.class, null);
        return table;
    }

    /**
     * Constructor de la clase.
     * @param baseDeDatos
     * @param usuario
     * @param tipoUsuario
     */
    public VentanaPrincipalUsuario(final BaseDeDatos baseDeDatos, final String usuario, int tipoUsuario) {
        this.baseDeDatos = baseDeDatos;
        this.usuario = usuario;
        this.tipoUsuario = tipoUsuario;
        gestionButton.addActionListener(e -> {
            GestionEquipo gestionEquipo = new GestionEquipo(usuario, baseDeDatos);
            gestionEquipo.mostrar(frame);
        });
        comboLigas.addActionListener(e -> {
            if (!iniciado[0]) {
                iniciado[0] = true;
            } else {
                JTable tablaLiga = crearTablaLiga(comboLigas.getSelectedIndex());
                ligasScrollPanel.getViewport().add(tablaLiga);
                ligaLabel.setText(baseDeDatos.getLigas().get(comboLigas.getSelectedIndex()).getNombre());
            }
        });
        jorAntButton.addActionListener(e -> {
            JTable tablaTodasJornadas = crearTablaTodasJornadas();
            ligasScrollPanel.getViewport().add(tablaTodasJornadas);
            ligaLabel.setText("Todas las jornadas");
        });
    }

    /**
     * Devuelve una tabla con todas las jornadas de cada liga.
     * @return JTable de todas las jornadas.
     */
    private JTable crearTablaTodasJornadas() {
        String[] columnas = {"Liga", "Jornada", "Equipo 1", "Equipo 2", "Ganador"};
        List<Encuentro> encuentros = new ArrayList<>();
        List<Jornada> jornadas = new ArrayList<>(baseDeDatos.getJornadas());
        jornadas.sort(Comparator.comparing(Jornada::getDia));
        for (Jornada jornada:jornadas) {
            encuentros.addAll(jornada.getEncuentros());
        }

        String[][] encuentrosLista = new String[encuentros.size()][5];

        int count = 0;
        for (Encuentro encuentro : encuentros) {
            encuentrosLista[count][0] = encuentro.getLiga().getNombre();
            encuentrosLista[count][1] = String.valueOf(encuentro.getJornada().getDia());
            encuentrosLista[count][2] = encuentro.getEquipos()[0].getNombre();
            encuentrosLista[count][3] = encuentro.getEquipos()[1].getNombre();
            if (encuentro.getGanador() == 0) encuentrosLista[count][4] = "";
            else encuentrosLista[count][4] = encuentro.getEquipos()[encuentro.getGanador() - 1].getNombre();
            count++;
        }

        JTable table = new JTable(encuentrosLista, columnas);
        table.setCellSelectionEnabled(false);
        table.setRowSelectionAllowed(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setDefaultEditor(Object.class, null);
        return table;
    }

    /**
     * Devuelve una tabla con la clasifiación de una liga.
     * @param indexLiga int de índice para la liga.
     * @return JTable con todos los equipos de una liga y sus puntuaciones.
     */
    private JTable crearTablaLiga(int indexLiga) {
        String[] columnas = {"Equipo", "Puntuación"};
        Liga liga = baseDeDatos.getLigas().get(indexLiga);
        List<Puntuacion> puntuaciones = liga.getPuntuaciones();
        puntuaciones.sort(Comparator.comparing(Puntuacion::getPuntos));

        String[][] puntuacionesLista = new String[puntuaciones.size()][2];

        int count = 0;
        for (Puntuacion puntuacion : puntuaciones) {
            puntuacionesLista[count][0] = puntuacion.getEquipo().getNombre();
            puntuacionesLista[count][1] = String.valueOf(puntuacion.getPuntos());

            count++;
        }

        JTable table = new JTable(puntuacionesLista, columnas);
        table.setCellSelectionEnabled(false);
        table.setRowSelectionAllowed(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setDefaultEditor(Object.class, null);
        return table;
    }
}
