package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.EstadoPedido;
import model.Pedido;
import model.PedidoComida;
import model.PedidoEncomienda;
import model.PedidoExpress;

/**
 * Gestiona las operaciones CRUD relacionadas con los pedidos.
 */
public class PedidoDAO {

    /**
     * Guarda un nuevo pedido en la base de datos.
     * El identificador es generado automáticamente por MySQL.
     *
     * @param pedido pedido que será almacenado
     * @return true si el pedido fue guardado correctamente
     */
    public boolean create(Pedido pedido) {

        String sql = "INSERT INTO pedido (direccion, distancia_km, tipo, estado) VALUES (?, ?, ?, ?)";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            sentencia.setString(1, pedido.getDireccionEntrega());
            sentencia.setDouble(2, pedido.getDistanciaKm());
            sentencia.setString(3, pedido.getTipoPedido());
            sentencia.setString(4, pedido.getEstado().toString());

            int filasInsertadas = sentencia.executeUpdate();

            if (filasInsertadas == 0) {
                return false;
            }

            try (ResultSet clavesGeneradas = sentencia.getGeneratedKeys()) {

                if (clavesGeneradas.next()) {
                    pedido.setIdPedido(clavesGeneradas.getInt(1));
                    return true;
                }
            }

        } catch (SQLException ex) {
            System.out.println("Error al guardar pedido.");
            System.out.println(ex.getMessage());
        }

        return false;
    }

    /**
     * Obtiene todos los pedidos registrados.
     *
     * @return lista de pedidos
     */
    public List<Pedido> readAll() {

        List<Pedido> pedidos = new ArrayList<>();

        String sql = "SELECT id, direccion, distancia_km, tipo, estado FROM pedido ORDER BY id";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql);
             ResultSet resultado = sentencia.executeQuery()) {

            while (resultado.next()) {

                int id = resultado.getInt("id");
                String direccion = resultado.getString("direccion");
                double distanciaKm = resultado.getDouble("distancia_km");
                String tipo = resultado.getString("tipo");
                String estado = resultado.getString("estado");

                Pedido pedido;

                switch (tipo) {
                    case "COMIDA":
                        pedido = new PedidoComida(id, direccion, distanciaKm);
                        break;

                    case "ENCOMIENDA":
                        pedido = new PedidoEncomienda(id, direccion, distanciaKm);
                        break;

                    case "EXPRESS":
                        pedido = new PedidoExpress(id, direccion, distanciaKm);
                        break;

                    default:
                        System.out.println("Tipo de pedido desconocido: " + tipo);
                        continue;
                }

                pedido.setEstado(EstadoPedido.valueOf(estado));
                pedidos.add(pedido);
            }

        } catch (SQLException ex) {
            System.out.println("Error al listar pedidos.");
            System.out.println(ex.getMessage());
        }

        return pedidos;
    }

    /**
     * Actualiza los datos editables de un pedido.
     *
     * El estado no se modifica mediante este método, ya que solo puede cambiar mediante la lógica de entrega.
     *
     * @param pedido pedido con los nuevos datos
     * @return true si el pedido fue actualizado correctamente
     */
    public boolean update(Pedido pedido) {

        String sql = "UPDATE pedido SET direccion = ?, distancia_km = ?, tipo = ? WHERE id = ?";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setString(1, pedido.getDireccionEntrega());
            sentencia.setDouble(2, pedido.getDistanciaKm());
            sentencia.setString(3, pedido.getTipoPedido());
            sentencia.setInt(4, pedido.getIdPedido());

            int filasActualizadas = sentencia.executeUpdate();

            return filasActualizadas == 1;

        } catch (SQLException ex) {
            System.out.println("Error al actualizar pedido.");
            System.out.println(ex.getMessage());

            return false;
        }
    }

    /**
     * Elimina un pedido de la base de datos.
     *
     * @param id identificador del pedido
     * @return true si el pedido fue eliminado correctamente
     */
    public boolean delete(int id) {

        String sql = "DELETE FROM pedido WHERE id = ?";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setInt(1, id);

            int filasEliminadas = sentencia.executeUpdate();

            return filasEliminadas == 1;

        } catch (SQLException ex) {
            System.out.println("Error al eliminar pedido.");
            System.out.println(ex.getMessage());

            return false;
        }
    }

    /**
     * Actualiza solamente el estado de un pedido.
     *
     * Este método se utiliza durante la simulación de entregas.
     *
     * @param pedido pedido cuyo estado será actualizado
     * @return true si el estado fue actualizado correctamente
     */
    public boolean actualizarEstado(Pedido pedido) {

        String sql = "UPDATE pedido SET estado = ? WHERE id = ?";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setString(1, pedido.getEstado().toString());
            sentencia.setInt(2, pedido.getIdPedido());

            int filasActualizadas = sentencia.executeUpdate();

            return filasActualizadas == 1;

        } catch (SQLException ex) {
            System.out.println("Error al actualizar estado del pedido.");
            System.out.println(ex.getMessage());

            return false;
        }
    }
}