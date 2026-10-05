package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Repartidor;

/**
 * Gestiona las operaciones CRUD relacionadas con los repartidores.
 */
public class RepartidorDAO {

    /**
     * Guarda un nuevo repartidor en la base de datos.
     *
     * @param repartidor repartidor que será almacenado
     * @return true si el repartidor fue guardado correctamente
     */
    public boolean create(Repartidor repartidor) {

        String sql = "INSERT INTO repartidor (nombre) VALUES (?)";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            sentencia.setString(1, repartidor.getNombre());

            int filasInsertadas = sentencia.executeUpdate();

            if (filasInsertadas == 0) {
                return false;
            }

            try (ResultSet clavesGeneradas = sentencia.getGeneratedKeys()) {

                if (clavesGeneradas.next()) {
                    repartidor.setId(clavesGeneradas.getInt(1));
                    return true;
                }
            }

        } catch (SQLException ex) {
            System.out.println("Error al guardar repartidor.");
            System.out.println(ex.getMessage());
        }

        return false;
    }

    /**
     * Obtiene todos los repartidores registrados en la base de datos.
     *
     * @return lista de repartidores registrados
     */
    public List<Repartidor> readAll() {

        List<Repartidor> repartidores = new ArrayList<>();

        String sql = "SELECT id, nombre FROM repartidor ORDER BY id";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql);
             ResultSet resultado = sentencia.executeQuery()) {

            while (resultado.next()) {

                int id = resultado.getInt("id");
                String nombre = resultado.getString("nombre");

                repartidores.add(new Repartidor(id, nombre));
            }

        } catch (SQLException ex) {
            System.out.println("Error al listar repartidores.");
            System.out.println(ex.getMessage());
        }

        return repartidores;
    }

    /**
     * Actualiza los datos de un repartidor existente.
     *
     * @param repartidor repartidor que será actualizado
     * @return true si el repartidor fue actualizado correctamente
     */
    public boolean update(Repartidor repartidor) {

        String sql = "UPDATE repartidor SET nombre = ? WHERE id = ?";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setString(1, repartidor.getNombre());
            sentencia.setInt(2, repartidor.getId());

            int filasActualizadas = sentencia.executeUpdate();

            return filasActualizadas == 1;

        } catch (SQLException ex) {
            System.out.println("Error al actualizar repartidor.");
            System.out.println(ex.getMessage());

            return false;
        }
    }

    /**
     * Elimina un repartidor de la base de datos.
     *
     * @param id identificador del repartidor
     * @return true si el repartidor fue eliminado correctamente
     */
    public boolean delete(int id) {

        String sql = "DELETE FROM repartidor WHERE id = ?";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setInt(1, id);

            int filasEliminadas = sentencia.executeUpdate();

            return filasEliminadas == 1;

        } catch (SQLException ex) {
            System.out.println("Error al eliminar repartidor.");
            System.out.println(ex.getMessage());

            return false;
        }
    }
}