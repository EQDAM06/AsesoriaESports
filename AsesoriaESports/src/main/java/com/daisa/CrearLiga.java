package com.daisa;

import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Ventana para la creación de una liga.
 * @author David Roig
 * @author Isabel Montero
 */
public class CrearLiga {
    private JPanel panelPrincipal;
    private JTextField nombreTextField;
    private JButton addEquipoButton;
    private JButton quitarEquipoButton;
    private JButton cancelarButton;
    private JButton crearLigaButton;
    private JList equiposList;
    private JScrollPane equiposLibresScrollPane;
    private JScrollPane equiposLigaScrollPane;
    private JDatePickerImpl datePicker;
    private JPanel panelCalendario;
    private JFrame frame;
    final BaseDeDatos baseDeDatos;
    private List<Equipo> equiposDisponibles;
    private List<Equipo> equiposEnLiga;
    final private VentanaPrincipalAdmin ventanaPrincipalAdmin;

    /**
     * Inicialización de la ventana.
     * @param frame
     */
    public void mostrar(JFrame frame) {
        this.frame = frame;
        frame.setContentPane(panelPrincipal);
        frame.pack();
    }

    /**
     * Constructor de la clase.
     * @param baseDeDatos
     * @param ventanaPrincipalAdmin
     */
    public CrearLiga(final BaseDeDatos baseDeDatos, final VentanaPrincipalAdmin ventanaPrincipalAdmin) {
        this.baseDeDatos = baseDeDatos;
        equiposDisponibles = new ArrayList<>(baseDeDatos.getEquipos());
        this.ventanaPrincipalAdmin = ventanaPrincipalAdmin;

        crearDatePicker();

        for (int i = 0; i < equiposDisponibles.size(); i++) {
            if (equiposDisponibles.get(i).getJugadores().isEmpty())
                equiposDisponibles.remove(equiposDisponibles.get(i));
        }

        equiposEnLiga = new ArrayList<>();


        JTable[] equiposEnLigaTabla = {configurarTablaPeque(equiposEnLiga)};

        JTable[] equiposLibresTabla = {configurarTabla(equiposDisponibles)};
        equiposLibresScrollPane.getViewport().add(equiposLibresTabla[0]);

        equiposLibresTabla[0].getSelectionModel().addListSelectionListener(event -> addEquipoButton.setEnabled(true));

        equiposEnLigaTabla[0].getSelectionModel().addListSelectionListener(event -> quitarEquipoButton.setEnabled(true));

        cancelarButton.addActionListener(e -> ventanaPrincipalAdmin.mostrar(frame));


        addEquipoButton.addActionListener(e -> {
            if (addEquipoButton.isEnabled()) {
                Equipo equipo = equiposDisponibles.get(equiposLibresTabla[0].getSelectedRow());

                equiposDisponibles.remove(equipo);
                equiposEnLiga.add(equipo);


                equiposLibresTabla[0] = configurarTabla(equiposDisponibles);
                equiposLibresScrollPane.getViewport().remove(0);
                equiposLibresScrollPane.getViewport().add(equiposLibresTabla[0]);
                equiposEnLigaTabla[0] = configurarTablaPeque(equiposEnLiga);
                equiposLigaScrollPane.getViewport().add(equiposEnLigaTabla[0]);

                equiposLibresTabla[0].getSelectionModel().addListSelectionListener(event -> addEquipoButton.setEnabled(true));

                equiposEnLigaTabla[0].getSelectionModel().addListSelectionListener(event -> quitarEquipoButton.setEnabled(true));

                if (equiposEnLiga.size() > 1) crearLigaButton.setEnabled(true);
                addEquipoButton.setEnabled(false);
            }
        });

        quitarEquipoButton.addActionListener(e -> {
            if (quitarEquipoButton.isEnabled()) {
                Equipo equipo = equiposEnLiga.get(equiposEnLigaTabla[0].getSelectedRow());

                equiposDisponibles.add(equipo);
                equiposEnLiga.remove(equipo);

                equiposLibresTabla[0] = configurarTabla(equiposDisponibles);
                equiposLibresScrollPane.getViewport().remove(0);
                equiposLibresScrollPane.getViewport().add(equiposLibresTabla[0]);
                equiposEnLigaTabla[0] = configurarTablaPeque(equiposEnLiga);
                equiposLigaScrollPane.getViewport().remove(0);
                equiposLigaScrollPane.getViewport().add(equiposEnLigaTabla[0]);

                equiposLibresTabla[0].getSelectionModel().addListSelectionListener(event -> addEquipoButton.setEnabled(true));

                equiposEnLigaTabla[0].getSelectionModel().addListSelectionListener(event -> quitarEquipoButton.setEnabled(true));

                if (equiposEnLiga.size() < 2) crearLigaButton.setEnabled(false);
                quitarEquipoButton.setEnabled(false);
            }
        });
        crearLigaButton.addActionListener(e -> {
            if (crearLigaButton.isEnabled() && equiposEnLiga.size() > 1) {
                if (nombreTextField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(frame,
                            "El nombre de liga es un campo necesario.",
                            "Nombre Inválido",
                            JOptionPane.ERROR_MESSAGE);
                } else if (!nombreEstaDisponible(nombreTextField.getText())) {
                    JOptionPane.showMessageDialog(frame,
                            "El nombre de liga no está disponible.",
                            "Nombre Inválido",
                            JOptionPane.ERROR_MESSAGE);
                } else if (datePicker.getModel().getValue() == null) {
                    JOptionPane.showMessageDialog(frame,
                            "La fecha de inicio es un campo requerido.",
                            "Fecha Inválida",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    int n = JOptionPane.showConfirmDialog(frame,
                            "Se va a crear la liga " +
                                    "\"" + nombreTextField.getText() + "\"\n" +
                                    "¿Desea continuar?",
                            "Confirmar",
                            JOptionPane.YES_NO_OPTION); // 0 yes, 1 no
                    if (n == 0) {
                        // aceptado
                        String nombre = nombreTextField.getText();

                        DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                        String fechaInicio = format.format((Date) datePicker.getModel().getValue());

                        System.out.println("Generando liga...");
                        Liga liga = new Liga(nombre, fechaInicio, equiposEnLiga, false);
                        liga.crearJornadas();
                        baseDeDatos.crearLiga(liga);

                        JOptionPane.showMessageDialog(frame, "Liga creada correctamente", "Liga Creada", JOptionPane.PLAIN_MESSAGE);

                        baseDeDatos.actualizar();
                        ventanaPrincipalAdmin.mostrar(frame);

                    }
                }
            }
        });
    }

    /**
     * Genera el calendario para escoger fecha.
     */
    private void crearDatePicker() {
        UtilDateModel model = new UtilDateModel();
        JDatePanelImpl datePanel = new JDatePanelImpl(model);

        datePicker = new JDatePickerImpl(datePanel);
        panelCalendario.add(datePicker);
        datePicker.addActionListener(e -> {

            Date datePickerDate = (Date) datePicker.getModel().getValue();
            DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            System.out.println(format.format(datePickerDate));
        });
    }

    /**
     * Genera la tabla de equipos propuestos para la liga.
     * @param equipos
     * @return La JTable de equipos propuestos para la liga.
     */
    private JTable configurarTablaPeque(List<Equipo> equipos) {
        String[] columnas = {"ID Equipo", "Nombre"};
        String[][] equiposLista = new String[equipos.size()][2];
        int count = 0;
        if (!equipos.isEmpty()) {
            for (Equipo equipo : equipos) {
                equiposLista[count][0] = String.valueOf(equipo.getId());
                equiposLista[count][1] = equipo.getNombre();
                count++;
            }
        }
        JTable table = new JTable(equiposLista, columnas);
        table.setCellSelectionEnabled(false);
        table.setRowSelectionAllowed(true);
        //TODO: Selección multiple
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setDefaultEditor(Object.class, null);
        return table;
    }

    /**
     * Genera la tabla de equipos disponibles para la liga.
     * @param equipos
     * @return La JTable de equipos disponibles para la liga.
     */
    private JTable configurarTabla(List<Equipo> equipos) {
        String[] columnas = {"ID Equipo", "Nombre", "Localidad", "Jugadores"};
        String[][] equiposLista = new String[equipos.size()][4];

        int count = 0;
        for (Equipo equipo : equipos) {
            equiposLista[count][0] = String.valueOf(equipo.getId());
            equiposLista[count][1] = equipo.getNombre();
            equiposLista[count][2] = equipo.getLocalidad();
            if (equipo.getJugadores() != null) {
                equiposLista[count][3] = String.valueOf(equipo.getJugadores().size());
            } else equiposLista[count][3] = "0";
            count++;
        }
        JTable table = new JTable(equiposLista, columnas);
        table.setCellSelectionEnabled(false);
        table.setRowSelectionAllowed(true);
        //TODO: Selección multiple
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setDefaultEditor(Object.class, null);
        return table;
    }

    /**
     * Devuelve la disponibilidad del nombre de la liga.
     * @param nombre
     * @return True si el nombre está ocupado, false si el nombre está disponible.
     */
    private boolean nombreEstaDisponible(String nombre) {
        boolean coincidencia = false;
        for (Liga liga:baseDeDatos.getLigas()) {
            if (liga.getNombre().toUpperCase().equals(nombre.toUpperCase())) coincidencia = true;
        }
        return !coincidencia;
    }
}
