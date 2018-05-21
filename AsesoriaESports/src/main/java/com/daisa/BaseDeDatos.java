package com.daisa;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase que gestiona y contiene todos la recogida de datos desde la base de datos.
 * @author David Roig
 * @author Isabel Montero
 */
public class BaseDeDatos {

    private Connection conn = null;
    private String conexion;

    private List<Equipo> equipos;
    private List<Jugador> jugadores;
    private List<Director> directores;
    private List<Contrato> contratos;
    private List<Liga> ligas;
    private List<Jornada> jornadas;
    private List<Encuentro> encuentros;
    private List<Puntuacion> puntuaciones;
    private boolean cargaInicial;

    /**
     * Constructor de la clase
     * @param conexion String con cadena de conexión
     */
    public BaseDeDatos(String conexion) {
        this.conexion = conexion;
    }

    /**
     * Conexión al a base de datos.
     * @param userid
     * @param password
     */
    public void conectar(String userid, String password) {
        try {
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());

            conn = DriverManager.getConnection(conexion, userid, password);
            System.out.println("Conexión a base de datos abierta");

        } catch (SQLException ex) {
            Logger.getLogger(BaseDeDatos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Carga todos los datos de la base de datos a la clase a través de cada función especifica.
     */
    public void actualizar() {
        if (cargaInicial) System.out.println("Actualizando base de datos...");
        else System.out.println("Iniciando volcado de base de datos... ");
        if (!cargaInicial) System.out.print("--> Cargando directores... ");
        cargarDirectores();
        if (!cargaInicial) System.out.print("--> Cargando jugadores... ");
        cargarJugadores();
        if (!cargaInicial) System.out.print("--> Cargando equipos... ");
        cargarEquipos();
        if (!cargaInicial) System.out.print("--> Cargando contratos... ");
        cargarContratos();
        if (!cargaInicial) System.out.print("--> Vinculando contratos... ");
        vincularContratos();

        if (!cargaInicial) System.out.print("--> Cargando ligas... ");
        cargarLigas();
        if (!cargaInicial) System.out.print("--> Cargando jornadas... ");
        cargarJornadas();
        if (!cargaInicial) System.out.print("--> Cargando encuentros... ");
        cargarEncuentros();
        if (!cargaInicial) System.out.print("--> Organizando ligas... ");
        organizarLigas();

        if (!cargaInicial) System.out.print("--> Cargando puntuaciones... ");
        cargarPuntuaciones();
        if (!cargaInicial) System.out.print("--> Vinculando ligas y equipos... ");
        vincularPuntuaciones();

        System.out.println("¡Carga de datos ejecutada correctamente!");

        if (!cargaInicial) cargaInicial = true;
    }

    /**
     * Vincula a los equipos y las ligas a través de las puntuaciones.
     */
    private void vincularPuntuaciones() {
        for (Puntuacion puntuacion : puntuaciones) {
            puntuacion.getEquipo().getLigas().add(puntuacion.getLiga());
            puntuacion.getLiga().getEquipos().add(puntuacion.getEquipo());
            puntuacion.getEquipo().getPuntuaciones().add(puntuacion);
            puntuacion.getLiga().getPuntuaciones().add(puntuacion);
        }
        System.out.println(puntuaciones.size() + " vinculaciones de equipos y ligas creadas.");
    }

    /**
     * Carga las puntuaciones de la base de datos.
     */
    private void cargarPuntuaciones() {
        Statement stmt = null;
        puntuaciones = new ArrayList<>();
        String query = "select * from puntuacion";

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                int idEquipo = rs.getInt(1);
                int idLiga = rs.getInt(2);
                int puntos = rs.getInt(3);

                Liga liga = obtenerLiga(idLiga);
                Equipo equipo = obtenerEquipo(idEquipo);

                puntuaciones.add(new Puntuacion(equipo, liga, puntos));
            }
            System.out.println(puntuaciones.size() + " puntuaciones añadidas correctamente.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Vincula los encuentros y jornadas a sus respectivas ligas.
     */
    private void organizarLigas() {
        for (Encuentro encuentro : encuentros) {
            encuentro.getJornada().getEncuentros().add(encuentro);
        }
        for (Jornada jornada : jornadas) {
            jornada.getLiga().getJornadas().add(jornada);
        }
        System.out.println(encuentros.size() + " encuentros y " + jornadas.size() + " jornadas vinculadas.");
    }

    /**
     * Carga los encuentros de la base de datos.
     */
    private void cargarEncuentros() {
        Statement stmt = null;
        encuentros = new ArrayList<>();
        String query = "select * from encuentro";

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                int id = rs.getInt(1);
                int ganador = rs.getInt(2);
                int idEquipo1 = rs.getInt(3);
                int idEquipo2 = rs.getInt(4);
                int idLiga = rs.getInt(5);
                int idJornada = rs.getInt(6);

                Liga liga = obtenerLiga(idLiga);
                Jornada jornada = obtenerJornada(idJornada);
                Equipo[] equipos = {obtenerEquipo(idEquipo1), obtenerEquipo(idEquipo2)};

                encuentros.add(new Encuentro(id, jornada, ganador, equipos, liga));
            }
            System.out.println(encuentros.size() + " encuentros de liga añadidos.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Devuelve una jornada en base a una ID.
     * @param idJornada
     * @return La Jornada correspondiente a la ID.
     */
    private Jornada obtenerJornada(int idJornada) {
        Jornada jornada = null;
        if (jornadas == null) {
            cargarJornadas();
        } else {
            for (int i = 0; i < jornadas.size(); i++) {
                if (jornadas.get(i).getId() == (idJornada)) {
                    jornada = jornadas.get(i);
                    i = jornadas.size();
                }
            }
        }
        return jornada;
    }

    /**
     * Carga todas las jornadas.
     */
    private void cargarJornadas() {
        Statement stmt = null;
        jornadas = new ArrayList<>();
        String query = "select * from jornada";

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                int id = rs.getInt(1);
                int idLiga = rs.getInt(2);
                int dia = rs.getInt(3);

                Liga liga = obtenerLiga(idLiga);

                jornadas.add(new Jornada(liga, id, dia));
            }
            System.out.println(ligas.size() + " jornadas de liga añadidas.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Devuelve una liga en base a su ID.
     * @param idLiga
     * @return La Liga respectiva a su ID.
     */
    private Liga obtenerLiga(int idLiga) {
        Liga liga = null;
        if (ligas == null) {
            cargarLigas();
        } else {
            for (int i = 0; i < ligas.size(); i++) {
                if (ligas.get(i).getId() == (idLiga)) {
                    liga = ligas.get(i);
                    i = ligas.size();
                }
            }
        }
        return liga;
    }

    /**
     * Carga todas las ligas de la base de datos.
     */
    private void cargarLigas() {
        Statement stmt = null;
        ligas = new ArrayList<>();
        String query = "select * from liga";

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                int id = rs.getInt(1);
                String nombre = rs.getString(2);
                String fechaInicio = rs.getString(3);
                String terminadoChar = rs.getString(4);

                boolean terminado = (terminadoChar.equals("1"));

                ligas.add(new Liga(id, nombre, fechaInicio, terminado));
            }
            System.out.println(ligas.size() + " ligas añadidas.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Vincula a los jugadores y los equipos a través de los contratos.
     */
    private void vincularContratos() {
        int countVig = 0;
        for (Contrato contrato : contratos) {
            contrato.getJugador().getContratos().add(contrato);
            contrato.getEquipo().getContratos().add(contrato);
            if (contrato.getFechaBaja() == null) {
                contrato.getEquipo().getJugadores().add(contrato.getJugador());
                contrato.getJugador().setEquipo(contrato.getEquipo());
                countVig++;
            }
        }

        System.out.println(contratos.size() + " contratos creados, de los cuales " + countVig + " son vigentes.");
    }

    /**
     * Carga todos los directores de la base de datos.
     */
    public void cargarDirectores() {
        Statement stmt = null;
        directores = new ArrayList<>();
        String query = "select * from director";

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String dni = rs.getString(1);
                String nombre = rs.getString(2);
                String telefono = rs.getString(3);
                String email = rs.getString(4);
                String idUsuario = rs.getString(5);
                directores.add(new Director(dni, nombre, telefono, email, idUsuario));
            }
            System.out.println(directores.size() + " directores añadidos.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Carga todos los equipos de la base de datos.
     */
    public void cargarEquipos() {
        Statement stmt = null;
        equipos = new ArrayList<>();
        String query = "select * from equipo";

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                int idEquipo = rs.getInt(1);
                String nombre = rs.getString(2);
                String localidad = rs.getString(3);
                String dniDir = rs.getString(4);

                Director director = obtenerDirector(dniDir);

                equipos.add(new Equipo(idEquipo, nombre, localidad, director));
            }
            System.out.println(equipos.size() + " equipos añadidos.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Carga todos los jugadores de la base de datos.
     */
    public void cargarJugadores() {
        Statement stmt = null;
        jugadores = new ArrayList<>();
        String query = "select * from jugador";
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String dni = rs.getString(1);
                String nombre = rs.getString(2);
                String nickname = rs.getString(3);
                double sueldo = rs.getDouble(4);
                String telefono = rs.getString(5);
                jugadores.add(new Jugador(dni, nombre, nickname, sueldo, telefono));
            }
            System.out.println(jugadores.size() + " jugadores añadidos.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Carga todos los contratos de la base de datos.
     */
    public void cargarContratos() {
        Statement stmt = null;
        contratos = new ArrayList<>();
        String query = "select * from jugador_equipo";
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                int id = rs.getInt(1);
                String dniJugador = rs.getString(2);
                int idEquipo = rs.getInt(3);
                String fechaAlta = rs.getString(4);
                String fechaBaja = rs.getString(5);

                Jugador jugador = obtenerJugador(dniJugador);
                Equipo equipo = obtenerEquipo(idEquipo);

                contratos.add(new Contrato(id, jugador, equipo, fechaAlta, fechaBaja));
            }
            System.out.println(contratos.size() + " contratos añadidos.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Devuelve un jugador en base a su DNI.
     * @param dni
     * @return El Jugador correspondiente al DNI.
     */
    public Jugador obtenerJugador(String dni) {
        Jugador jugador = null;
        if (jugadores == null) {
            cargarJugadores();
        } else {
            for (int i = 0; i < jugadores.size(); i++) {
                if (jugadores.get(i).getDni().equals(dni)) {
                    jugador = jugadores.get(i);
                    i = jugadores.size();
                }
            }
        }
        return jugador;
    }

    /**
     * Devuelve un equipo en base a su ID.
     * @param id
     * @return El equipo correspondiente a su ID.
     */
    public Equipo obtenerEquipo(int id) {
        Equipo equipo = null;
        if (equipos == null) {
            cargarEquipos();
        } else {
            for (int i = 0; i < equipos.size(); i++) {
                if (equipos.get(i).getId() == id) {
                    equipo = equipos.get(i);
                    i = equipos.size();
                }
            }
        }
        return equipo;
    }

    /**
     * Devuelve un director en base a su DNI.
     * @param dni
     * @return El Director correspondiente a su DNI.
     */
    public Director obtenerDirector(String dni) {
        Director director = null;
        if (directores == null) {
            cargarDirectores();
        } else {
            for (int i = 0; i < directores.size(); i++) {
                if (directores.get(i).getDni().equals(dni)) {
                    director = directores.get(i);
                    i = directores.size();
                }
            }
        }
        return director;
    }

    /**
     * Intenta hacer login como usuario en la base de datos.
     * @param usuario
     * @param password
     * @return Devuelve un String tipologin, que determina que tipo de usuario ha hecho login.
     *         En caso de fallar, devuelve un String vacio.
     */
    public String login(String usuario, String password) {
        String query = "select tipologin from login " +
                "where id_usuario = '" + usuario + "' " +
                "and contrasenia = '" + password + "'";
        Statement stmt = null;

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                return rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Hace un update en la base de datos.
     * @param query String con la operación SQL a realizar.
     * @param id PK de la tabla.
     * @param nuevoValor Nuevo valor a introducir.
     */
    public void update(String query, Object id, Object nuevoValor) {
        CallableStatement stmt = null;

        try {
            stmt = conn.prepareCall(query);
            if (id instanceof Integer) {
                stmt.setInt(1, (Integer) id);
            } else stmt.setString(1, (String) id);
            if (nuevoValor instanceof Integer) {
                stmt.setInt(2, (Integer) nuevoValor);
            } else if (nuevoValor instanceof String) {
                stmt.setString(2, (String) nuevoValor);
            } else stmt.setDouble(2, (Double) nuevoValor);
            conn.setAutoCommit(false);
            stmt.execute();
            conn.commit();
            System.out.println("Update efectuado correctamente.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Equipo> getEquipos() {
        return equipos;
    }

    public List<Jugador> getJugadores() {
        return jugadores;
    }

    public List<Director> getDirectores() {
        return directores;
    }

    public List<Contrato> getContratos() {
        return contratos;
    }

    public List<Liga> getLigas() {
        return ligas;
    }

    public List<Jornada> getJornadas() {
        return jornadas;
    }

    public List<Encuentro> getEncuentros() {
        return encuentros;
    }

    public List<Puntuacion> getPuntuaciones() {
        return puntuaciones;
    }

    /**
     * Crea un contrato, vinculando así a un jugador con un equipo.
     * @param jugador
     * @param equipo
     */
    public void crearContrato(Jugador jugador, Equipo equipo) {
        PreparedStatement stmt = null;

        String dniJugador = jugador.getDni();
        int idEquipo = equipo.getId();
        String fechaAlta = new SimpleDateFormat("dd-MM-yyyy").format(new Date());

        int numContratos = selectCount("jugador_equipo");
        int idContrato = numContratos + 1;

        System.out.println("Creando contrato " + idContrato + "...");

        String query = "insert into jugador_equipo values (" +
                idContrato + "," +
                "'" + dniJugador + "'," +
                idEquipo + "," +
                "'" + fechaAlta + "'," +
                "null)";

        try {
            stmt = conn.prepareStatement(query);
            conn.setAutoCommit(false);
            stmt.execute();
            conn.commit();
            System.out.println("Contrato creado correctamente.");

            jugador.getContratos().add(new Contrato(idContrato, jugador, equipo, fechaAlta, null));


        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * Devuelve el número de tuplas en una tabla.
     * @param nombreTabla
     * @return Int con el número de tuplas de la tabla.
     */
    public int selectCount(String nombreTabla) {
        Statement stmt = null;

        String query = "select count(*) from " + nombreTabla;

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        return -1;
    }

    /**
     * Termina un contrato, desvinculando así al jugador y al equipo.
     * @param jugador
     */
    public void expulsarJugador(Jugador jugador) {
        PreparedStatement stmt = null;

        String fechaBaja = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        int idContrato = jugador.getContratoVigente().getId();
        jugador.getContratoVigente().setFechaBaja(fechaBaja);


        String query = "update jugador_equipo " +
                "set fecha_baja = '" + fechaBaja + "' " +
                "where id_jugador_equipo = " + idContrato;

        System.out.println("Dando de baja contrato " + idContrato + "...");

        try {
            stmt = conn.prepareStatement(query);
            conn.setAutoCommit(false);
            stmt.execute();
            conn.commit();
            System.out.println("Contrato actualizado correctamente.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Cambia el director al que está vinculado un equipo.
     * @param equipo
     * @param nuevoDirector
     */
    public void cambiarDirectorEquipo(Equipo equipo, Director nuevoDirector) {
        PreparedStatement stmt = null;

        int idEquipo = equipo.getId();
        String dniDirector = nuevoDirector.getDni();

        String query = "update equipo " +
                "set dni_director = '" + dniDirector + "' " +
                "where id_equipo = " + idEquipo;

        System.out.println("Cambiando director de equipo " + idEquipo + "...");

        try {
            stmt = conn.prepareStatement(query);
            conn.setAutoCommit(false);
            stmt.execute();
            conn.commit();
            System.out.println("Equipo cambiado de dueño correctamente.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Crea un equipo y lo vincula a su director.
     * @param nombre
     * @param localidad
     * @param director
     */
    public void crearEquipo(String nombre, String localidad, Director director) {
        PreparedStatement stmt = null;

        String dniDirector = director.getDni();

        int idEquipo = this.selectMax("id_equipo", "equipo") + 1;

        String query = "insert into equipo values (" +
                idEquipo + "," +
                "'" + nombre + "'," +
                "'" + localidad + "'," +
                "'" + dniDirector + "')";

        System.out.print("Creando equipo " + idEquipo + "... ");

        try {
            stmt = conn.prepareStatement(query);
            conn.setAutoCommit(false);
            stmt.execute();
            conn.commit();

            Equipo equipo = new Equipo(idEquipo, nombre, localidad, director);
            equipos.add(equipo);
            director.getEquipos().add(equipo);

            System.out.println("Equipo creado correctamente.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Selecciona el valor númerico existente más alto de un atributo.
     * @param nombreCampo
     * @param nombreTabla
     * @return Int con el valor existente más alto del atributo.
     */
    private int selectMax(String nombreCampo, String nombreTabla) {
        Statement stmt = null;

        String query = "select max(" + nombreCampo + ") from " + nombreTabla;

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        return -1;
    }

    /**
     * Crea un jugador.
     * @param dni
     * @param nombre
     * @param nickname
     * @param sueldo
     * @param telefono
     */
    public void crearJugador(String dni, String nombre, String nickname, double sueldo, String telefono) {
        PreparedStatement stmt = null;

        String query = "insert into jugador values (" +
                "'" + dni + "'," +
                "'" + nombre + "'," +
                "'" + nickname + "'," +
                sueldo + "," +
                "'" + telefono + "')";

        System.out.print("Creando jugador " + dni + "... ");

        try {
            stmt = conn.prepareStatement(query);
            conn.setAutoCommit(false);
            stmt.execute();
            conn.commit();

            Jugador jugador = new Jugador(dni, nombre, nickname, sueldo, telefono);
            jugadores.add(jugador);

            System.out.println("Jugador creado correctamente.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Devuelve una lista con los usuarios sin su contraseña.
     * @return Lista de Usuarios sin su contraseña.
     */
    public List<Usuario> cargarUsuarios() {
        Statement stmt = null;
        List<Usuario> usuarios = new ArrayList<>();
        String query = "select id_usuario, tipologin from login";
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String id = rs.getString(1);
                String tipoUsuario = rs.getString(2);
                usuarios.add(new Usuario(id, tipoUsuario));
            }
            System.out.println(jugadores.size() + " usuarios añadidos.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuarios;
    }

    /**
     * Crea una Liga, sus jornadas y sus encuentros, y hace todas las vinculaciones necesarias.
     * @param liga
     */
    public void crearLiga(Liga liga) {
        PreparedStatement stmt = null;

        int idLiga = selectMax("id_liga", "liga") + 1;
        String nombre = liga.getNombre();
        String fechaInicio = liga.getFechaInicio();
        int terminado;
        if (liga.isTerminado()) terminado = 1;
        else terminado = 0;


        String queryLiga = "insert into liga values (" +
                "" + idLiga + "," +
                "'" + nombre + "'," +
                "'" + fechaInicio + "'," +
                terminado + ")";

        System.out.print("Creando liga " + idLiga + "... ");

        try {
            stmt = conn.prepareStatement(queryLiga);
            conn.setAutoCommit(false);
            stmt.execute();

            liga.setId(idLiga);
            System.out.println("Introducciendo jornadas y encuentros...");
            List<Jornada> jornadas = liga.getJornadas();
            int idJornada = selectMax("id_jornada", "jornada") + 1;
            int dia = 1;

            for (Jornada jornada : jornadas) {
                String queryJornada = "insert into jornada values (" +
                        idJornada + "," +
                        idLiga + "," +
                        dia + ")";
                stmt = conn.prepareStatement(queryJornada);
                conn.setAutoCommit(false);
                stmt.execute();

                int idEncuentro = selectMax("id_encuentro", "encuentro") + 1;

                for (Encuentro encuentro : jornada.getEncuentros()) {
                    int idEquipo1 = encuentro.getEquipos()[0].getId();
                    int idEquipo2 = encuentro.getEquipos()[1].getId();

                    String queryEncuentro = "insert into encuentro values (" +
                            idEncuentro + "," +
                            "0" + "," +
                            idEquipo1 + "," +
                            idEquipo2 + "," +
                            idLiga + "," +
                            idJornada + ")";

                    stmt = conn.prepareStatement(queryEncuentro);
                    conn.setAutoCommit(false);
                    stmt.execute();

                    idEncuentro++;
                }
                idJornada++;
                dia++;
            }

            System.out.println("Introduciendo puntuaciones...");
            for (Equipo equipo : liga.getEquipos()) {
                int idEquipo = equipo.getId();
                String queryPuntuacion = "insert into puntuacion values (" +
                        idEquipo + "," +
                        idLiga + "," +
                        "0" + ")";

                stmt = conn.prepareStatement(queryPuntuacion);
                conn.setAutoCommit(false);
                stmt.execute();
            }
            System.out.println("Ejecutando inserciones...");
            conn.commit();
            System.out.println("¡Liga creada correctamente!");

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    /**
     * Asigna las puntuaciones en una liga en base a los encuentros de la jornada, y termina la liga de ser la última jornada.
     * @param jornada
     * @param puntuacionesArray Int Array que contiene el ganador de cada encuentro dentro de una jornada.
     * @param terminado Boolean que es verdadero si la liga termina con esta jornada.
     */
    public void asignarPuntuaciones(Jornada jornada, int[] puntuacionesArray, boolean terminado) {
        PreparedStatement stmt = null;
        String query;
        int ligaId = jornada.getLiga().getId();
        System.out.println("Asignando puntuaciones...");
        try {
            for (int i = 0; i < jornada.getEncuentros().size(); i++) {
                Encuentro encuentro = jornada.getEncuentros().get(i);
                int idEncuentro = encuentro.getId();
                int ganador = puntuacionesArray[i];
                query = "update encuentro set ganador = " + ganador + " " +
                        "where id_encuentro = " + idEncuentro;

                stmt = conn.prepareStatement(query);
                conn.setAutoCommit(false);
                stmt.execute();

                Equipo equipo = encuentro.getEquipos()[ganador - 1];
                int equipoId = equipo.getId();
                int puntos = 3;

                query = "update puntuacion set puntos = puntos + " + puntos + " " +
                        "where id_equipopts = " + equipoId + " " +
                        "and id_ligapts = " + ligaId;

                stmt = conn.prepareStatement(query);
                conn.setAutoCommit(false);
                stmt.execute();
            }
            if (terminado) {
                System.out.println("Terminando liga... ");

                query = "update liga set terminado = '1' " +
                        "where id_liga = " + ligaId;

                stmt = conn.prepareStatement(query);
                conn.setAutoCommit(false);
                stmt.execute();
            }
            conn.commit();

            System.out.println("Puntuaciones asignadas correctamente.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Devuelve las veces que aparece un valor específico en una tabla.
     * @param nombreTabla
     * @param nombreCampo
     * @param valor
     * @return Int con el número de coincidencias del valor.
     */
    public int selectCount(String nombreTabla, String nombreCampo, String valor) {
        Statement stmt = null;

        String query = "select count(*) from " + nombreTabla + " where " + nombreCampo + " = '" + valor + "'";

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        return -1;
    }

    /**
     * Crea un usuario.
     * @param id
     * @param password
     * @param tipoUsuario String. Puede ser o USER o ADMIN o DIR.
     */
    public void crearUsuario(String id, String password, String tipoUsuario) {
        PreparedStatement stmt = null;

        String query = "insert into login values (" +
                "'" + id + "'," +
                "'" + password + "'," +
                "'" + tipoUsuario + "')";

        System.out.print("Creando usuario " + id + "... ");

        try {
            stmt = conn.prepareStatement(query);
            conn.setAutoCommit(false);
            stmt.execute();
            conn.commit();

            System.out.println("Usuario creado correctamente.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Crea un director y su correspondiente usuario.
     * @param id
     * @param password
     * @param dni
     * @param nombre
     * @param telefono
     * @param email
     */
    public void crearDirector(String id, String password, String dni, String nombre, String telefono, String email) {
        PreparedStatement stmt = null;

        String query = "insert into login values (" +
                "'" + id + "'," +
                "'" + password + "'," +
                "'DIR')";

        System.out.print("Creando usuario " + id + "... ");

        try {
            stmt = conn.prepareStatement(query);
            conn.setAutoCommit(false);
            stmt.execute();

            System.out.println("Usuario creado correctamente. Creando director... ");

            query = "insert into director values (" +
                    "'" + dni + "'," +
                    "'" + nombre + "'," +
                    "'" + telefono + "'," +
                    "'" + email + "'," +
                    "'" + id + "')";

            stmt = conn.prepareStatement(query);
            stmt.execute();
            conn.commit();

            System.out.println("Director creado correctamente.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getConexion() {
        return conexion;
    }
}