package JavaDBArchitects.modelo.dao;

import JavaDBArchitects.modelo.*;
import JavaDBArchitects.modelo.conexion.DatabaseConnection;
import JavaDBArchitects.controlador.excepciones.*;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SocioDAO {



    // Método registrarSocio mediante procedimiento almacenado
    public static void registrarSocioPA(String nombre, int tipoSocio, String nif, int idFederacion, Integer idSocioPadre, Object extra, String nombreFederacion) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/producto3", "root", "Againdifficult23!")) {
            conn.setAutoCommit(false);

            String sql = "{CALL registrarSocio(?, ?, ?, ?, ?, ?, ?)}";
            try (CallableStatement stmt = conn.prepareCall(sql)) {
                stmt.setString(1, nombre);
                stmt.setString(2, obtenerTipoSocio(tipoSocio));
                stmt.setString(3, nif);
                stmt.setInt(4, idFederacion);
                stmt.setObject(5, idSocioPadre);
                stmt.setString(6, obtenerTipoSeguro(extra));
                stmt.setString(7, nombreFederacion);  // Agregar el nombre de la federación

                stmt.executeUpdate();
                conn.commit();
                System.out.println("Socio registrado correctamente.");
            } catch (SQLException e) {
                if (conn != null) {
                    try {
                        conn.rollback();
                        System.err.println("Transacción revertida. Error al ejecutar el procedimiento: " + e.getMessage());
                    } catch (SQLException rollbackEx) {
                        System.err.println("Error al hacer rollback: " + rollbackEx.getMessage());
                    }
                }
            } finally {
                if (conn != null) {
                    conn.setAutoCommit(true);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error de conexión: " + e.getMessage());
        }
    }


    public List<Socio> listarSociosPorTipoPA(int tipoSocio) {
        List<Socio> socios = new ArrayList<>();
        String tipo = tipoSocio == 0 ? "ESTANDAR" : tipoSocio == 1 ? "FEDERADO" : "INFANTIL";

        String query = "{CALL listarSociosPorTipo(?)}";

        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement callableStatement = connection.prepareCall(query)) {

            connection.setAutoCommit(false);
            callableStatement.setString(1, tipo);

            ResultSet resultSet = callableStatement.executeQuery();

            while (resultSet.next()) {
                socios.add(mapResultSetToSocio(resultSet));
            }

            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return socios;
    }


    //Metodo para eliminar socio mediante procedimiento almacenado

    public static void eliminarSocioPA(int idSocio) {
        String url = "jdbc:mysql://127.0.0.1:3306/producto3";
        String usuario = "root";
        String contraseña = "Againdifficult23!";

        try (Connection conn = DriverManager.getConnection(url, usuario, contraseña)) {
            conn.setAutoCommit(false);  // Iniciar transacción

            String sql = "{CALL eliminarSocio(?)}";

            try (CallableStatement stmt = conn.prepareCall(sql)) {
                stmt.setInt(1, idSocio);  // Configurar el parámetro idSocio

                stmt.executeUpdate();  // Ejecutar el procedimiento
                conn.commit();  // Confirmar la transacción
                System.out.println("Socio eliminado correctamente.");
            } catch (SQLException e) {
                System.err.println("Error al eliminar el socio: " + e.getMessage());
                conn.rollback();  // Revertir la transacción en caso de error
            }
        } catch (SQLException e) {
            System.err.println("Error de conexión a la base de datos: " + e.getMessage());
        }
    }

//-----RESTO DE MÉTODOS

    private static String obtenerTipoSocio(int tipoSocio) {
        switch (tipoSocio) {
            case 0: return "ESTANDAR";
            case 1: return "FEDERADO";
            case 2: return "INFANTIL";
            default: throw new IllegalArgumentException("Tipo de socio inválido.");
        }
    }

    private static String obtenerTipoSeguro(Object extra) {
        if (extra instanceof Seguro) {
            Seguro seguro = (Seguro) extra;
            return seguro.getTipo().toString();
        }
        return null;
    }

    private void insertarFederacionConId(Integer idFederacion, String nombreFederacion, Connection connection) throws SQLException {
        String query = "INSERT INTO federaciones (id_federacion, nombre) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, idFederacion);
            preparedStatement.setString(2, nombreFederacion);
            preparedStatement.executeUpdate();
            System.out.println("Federación insertada con ID " + idFederacion + " y Nombre = " + nombreFederacion);
        }
    }

    private boolean federacionExiste(Integer idFederacion, Connection connection) throws SQLException {
        String query = "SELECT COUNT(*) FROM federaciones WHERE id_federacion = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, idFederacion);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        }
        return false;
    }

    // Método para obtener un socio por su número de socio
    public Socio getSocioByNumero(int numeroSocio) {
        String query = "SELECT * FROM Socios WHERE numeroSocio = ?";
        Socio socio = null;
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, numeroSocio);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                socio = mapResultSetToSocio(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return socio;
    }

    // Método auxiliar para mapear el ResultSet a un objeto Socio
    private Socio mapResultSetToSocio(ResultSet resultSet) throws SQLException {
        int numeroSocio = resultSet.getInt("numeroSocio");
        String nombre = resultSet.getString("nombre");
        String tipoSocio = resultSet.getString("tipo_socio");
        String nif = resultSet.getString("nif");
        BigDecimal cuotaMensual = resultSet.getBigDecimal("cuota_mensual");

        TipoSeguro tipoSeguro = resultSet.getString("tipo_seguro") != null ? TipoSeguro.valueOf(resultSet.getString("tipo_seguro")) : null;
        Integer idFederacion = resultSet.getObject("id_federacion", Integer.class);
        Integer idSocioPadre = resultSet.getObject("id_socio_padre", Integer.class);

        switch (tipoSocio) {
            case "ESTANDAR":
                Seguro seguro = tipoSeguro != null ? new Seguro(tipoSeguro, 0.0f) : null;
                return new Estandar(numeroSocio, nombre, nif, seguro, cuotaMensual);

            case "FEDERADO":
                Federacion federacion = new FederacionDAO().getFederacionById(idFederacion);
                return new Federado(numeroSocio, nombre, nif, federacion, cuotaMensual);

            case "INFANTIL":
                return new Infantil(numeroSocio, nombre, idSocioPadre, cuotaMensual);

            default:
                throw new SQLException("Tipo de socio desconocido: " + tipoSocio);
        }
    }
    // Método para actualizar los datos de un socio en la base de datos
    public void updateSocio(Socio socio) {
        String query = "UPDATE Socios SET nombre = ?, tipo_socio = ?, nif = ?, cuota_mensual = ? WHERE numeroSocio = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, socio.getNombre());
            preparedStatement.setString(2, socio.getTipoSocio());
            preparedStatement.setString(3, socio.getNif());
            preparedStatement.setBigDecimal(4, socio.getCuotaMensual());
            preparedStatement.setInt(5, socio.getNumeroSocio());

            preparedStatement.executeUpdate();
            System.out.println("Socio actualizado correctamente.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Métodos para verificar si un socio existe
    public boolean socioExiste(int numeroSocio) {
        String query = "SELECT COUNT(*) FROM Socios WHERE numeroSocio = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, numeroSocio);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

//-----MÉTODOS ANTERIORES SIN PROCEDIMIENTOS ALMACENADOS QUE YA NO SE USAN
    // Método para agregar un nuevo socio
    public void addSocio(Socio socio) throws SocioYaExisteException {
        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false);  // Iniciar transacción

            // Verificar si el socio ya existe
            if (socioExiste(socio.getNumeroSocio())) {
                throw new SocioYaExisteException("El socio con número " + socio.getNumeroSocio() + " ya existe.");
            }

            Integer numSocioPadre = null;

            // Verificar tipo de socio y realizar operaciones específicas
            if (socio instanceof Estandar) {
                TipoSeguro tipoSeguro = ((Estandar) socio).getTipoSeguro();
                if (tipoSeguro == null) {
                    throw new IllegalArgumentException("Error: El socio estándar debe tener un tipo de seguro definido como BASICO o COMPLETO.");
                }
            } else if (socio instanceof Federado) {
                Federacion federacion = ((Federado) socio).getFederacion();
                Integer idFederacion = federacion.getId_federacion();
                String nombreFederacion = federacion.getNombre();

                if (!federacionExiste(idFederacion, connection)) {
                    insertarFederacionConId(idFederacion, nombreFederacion, connection);
                }
                socio.setIdFederacion(idFederacion);
            } else if (socio instanceof Infantil) {
                numSocioPadre = socio.getIdSocioPadre();
                if (numSocioPadre != null && !socioExiste(numSocioPadre)) {
                    throw new IllegalArgumentException("Error: El socio padre o madre con ID " + numSocioPadre + " no existe.");
                }
            }

            // Proceder con la inserción del socio, incluyendo cuota_mensual
            String query = "INSERT INTO Socios (numeroSocio, nombre, tipo_socio, nif, tipo_seguro, id_federacion, id_socio_padre, cuota_mensual) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, socio.getNumeroSocio());
                preparedStatement.setString(2, socio.getNombre());
                preparedStatement.setString(3, socio.getTipoSocio());
                preparedStatement.setString(4, socio.getNif());

                if (socio instanceof Estandar) {
                    preparedStatement.setString(5, ((Estandar) socio).getTipoSeguro().name());
                } else {
                    preparedStatement.setNull(5, Types.VARCHAR);
                }

                if (socio instanceof Federado) {
                    preparedStatement.setObject(6, socio.getIdFederacion(), Types.INTEGER);
                } else {
                    preparedStatement.setNull(6, Types.INTEGER);
                }

                preparedStatement.setObject(7, numSocioPadre, Types.INTEGER);
                preparedStatement.setBigDecimal(8, socio.getCuotaMensual());

                preparedStatement.executeUpdate();
                connection.commit();
                System.out.println("Socio insertado correctamente.");
            } catch (SQLException e) {
                connection.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

