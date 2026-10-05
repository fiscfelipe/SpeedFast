package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Entrega;
import model.Pedido;

/**
 * Gestiona las operaciones CRUD relacionadas con las entregas.
 */
public class EntregaDAO {

    /**
    * Registra una nueva entrega y cambia automáticamente el estado del pedido asociado a ENTREGADO.
    *
    * Ambas operaciones se realizan dentro de una misma transacción. Si alguna falla, los cambios son revertidos.
    *
    * @param entrega entrega que será almacenada
    * @return true si la entrega y el cambio de estado se realizaron correctamente
    */
   public boolean create(Entrega entrega) {

       String sqlEntrega = "INSERT INTO entrega (id_pedido, id_repartidor, fecha, hora) VALUES (?, ?, ?, ?)";
       String sqlPedido = "UPDATE pedido SET estado = 'ENTREGADO' WHERE id = ?";

       Connection conexion = null;

       try {
           conexion = ConexionBD.conectar();
           conexion.setAutoCommit(false);

           try (PreparedStatement guardarEntrega = conexion.prepareStatement(sqlEntrega, Statement.RETURN_GENERATED_KEYS);
                PreparedStatement actualizarPedido = conexion.prepareStatement(sqlPedido)) {

               guardarEntrega.setInt(1, entrega.getIdPedido());
               guardarEntrega.setInt(2, entrega.getIdRepartidor());
               guardarEntrega.setDate(3, Date.valueOf(entrega.getFecha()));
               guardarEntrega.setTime(4, Time.valueOf(entrega.getHora()));

               int entregasInsertadas = guardarEntrega.executeUpdate();

               if (entregasInsertadas != 1) {
                   conexion.rollback();
                   return false;
               }

               try (ResultSet clavesGeneradas = guardarEntrega.getGeneratedKeys()) {

                   if (clavesGeneradas.next()) {
                       entrega.setId(clavesGeneradas.getInt(1));
                   } else {
                       conexion.rollback();
                       return false;
                   }
               }

               actualizarPedido.setInt(1, entrega.getIdPedido());

               int pedidosActualizados = actualizarPedido.executeUpdate();

               if (pedidosActualizados != 1) {
                   conexion.rollback();
                   return false;
               }

               conexion.commit();
               return true;
           }

       } catch (SQLException ex) {

           if (conexion != null) {
               try {
                   conexion.rollback();
               } catch (SQLException rollbackEx) {
                   System.out.println("Error al revertir la transacción.");
                   System.out.println(rollbackEx.getMessage());
               }
           }

           System.out.println("Error al registrar entrega.");
           System.out.println(ex.getMessage());

           return false;

       } finally {

           if (conexion != null) {
               try {
                   conexion.close();
               } catch (SQLException ex) {
                   System.out.println("Error al cerrar la conexión.");
                   System.out.println(ex.getMessage());
               }
           }
       }
   }

    /**
     * Obtiene todas las entregas registradas.
     *
     * @return lista de entregas
     */
    public List<Entrega> readAll() {

        List<Entrega> entregas = new ArrayList<>();

        String sql = "SELECT id, id_pedido, id_repartidor, fecha, hora FROM entrega ORDER BY id";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql);
             ResultSet resultado = sentencia.executeQuery()) {

            while (resultado.next()) {

                int id = resultado.getInt("id");
                int idPedido = resultado.getInt("id_pedido");
                int idRepartidor = resultado.getInt("id_repartidor");
                Date fecha = resultado.getDate("fecha");
                Time hora = resultado.getTime("hora");

                Entrega entrega = new Entrega(id, idPedido, idRepartidor, fecha.toLocalDate(), hora.toLocalTime());

                entregas.add(entrega);
            }

        } catch (SQLException ex) {
            System.out.println("Error al listar entregas.");
            System.out.println(ex.getMessage());
        }

        return entregas;
    }

    /**
     * Actualiza los datos editables de una entrega.
     *
     * El pedido asociado no se modifica mediante este método.
     *
     * @param entrega entrega con los nuevos datos
     * @return true si la entrega fue actualizada correctamente
     */
    public boolean update(Entrega entrega) {

        String sql = "UPDATE entrega SET id_repartidor = ?, fecha = ?, hora = ? WHERE id = ?";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setInt(1, entrega.getIdRepartidor());
            sentencia.setDate(2, Date.valueOf(entrega.getFecha()));
            sentencia.setTime(3, Time.valueOf(entrega.getHora()));
            sentencia.setInt(4, entrega.getId());

            int filasActualizadas = sentencia.executeUpdate();

            return filasActualizadas == 1;

        } catch (SQLException ex) {
            System.out.println("Error al actualizar entrega.");
            System.out.println(ex.getMessage());

            return false;
        }
    }

    /**
     * Elimina una entrega y devuelve el pedido asociado
     * al estado PENDIENTE.
     *
     * Ambas operaciones se realizan dentro de una misma transacción.
     *
     * @param id identificador de la entrega
     * @return true si la eliminación y actualización se realizaron correctamente
     */
    public boolean delete(int id) {

        String sqlBuscarPedido = "SELECT id_pedido FROM entrega WHERE id = ?";
        String sqlEliminarEntrega = "DELETE FROM entrega WHERE id = ?";
        String sqlActualizarPedido = "UPDATE pedido SET estado = 'PENDIENTE' WHERE id = ?";

        Connection conexion = null;

        try {
            conexion = ConexionBD.conectar();
            conexion.setAutoCommit(false);

            int idPedido;

            try (PreparedStatement buscarPedido = conexion.prepareStatement(sqlBuscarPedido)) {

                buscarPedido.setInt(1, id);

                try (ResultSet resultado = buscarPedido.executeQuery()) {

                    if (!resultado.next()) {
                        conexion.rollback();
                        return false;
                    }

                    idPedido = resultado.getInt("id_pedido");
                }
            }

            try (PreparedStatement eliminarEntrega = conexion.prepareStatement(sqlEliminarEntrega);
                PreparedStatement actualizarPedido = conexion.prepareStatement(sqlActualizarPedido)) {

                eliminarEntrega.setInt(1, id);

                int filasEliminadas = eliminarEntrega.executeUpdate();

                if (filasEliminadas != 1) {
                    conexion.rollback();
                    return false;
                }

                actualizarPedido.setInt(1, idPedido);

                int pedidosActualizados = actualizarPedido.executeUpdate();

                if (pedidosActualizados != 1) {
                    conexion.rollback();
                    return false;
                }

                conexion.commit();
                return true;
            }

        } catch (SQLException ex) {

            if (conexion != null) {
                try {
                    conexion.rollback();
                } catch (SQLException rollbackEx) {
                    System.out.println("Error al revertir la transacción.");
                    System.out.println(rollbackEx.getMessage());
                }
            }

            System.out.println("Error al eliminar entrega.");
            System.out.println(ex.getMessage());

            return false;

        } finally {

            if (conexion != null) {
                try {
                    conexion.close();
                } catch (SQLException ex) {
                    System.out.println("Error al cerrar la conexión.");
                    System.out.println(ex.getMessage());
                }
            }
        }
    }

    /**
     * Actualiza el estado del pedido y registra la entrega
     * dentro de una misma transacción.
     *
     * Si alguna de las dos operaciones falla, todos los cambios realizados durante la transacción se deshacen.
     *
     * @param pedido pedido que fue entregado
     * @param entrega entrega asociada al pedido y repartidor
     * @return true si ambas operaciones finalizaron correctamente
     */
    public boolean registrarEntregaCompleta(Pedido pedido, Entrega entrega) {

        String sqlActualizarPedido = "UPDATE pedido SET estado = ? WHERE id = ?";
        String sqlGuardarEntrega = "INSERT INTO entrega (id_pedido, id_repartidor, fecha, hora) VALUES (?, ?, ?, ?)";

        Connection conexion = null;

        try {
            conexion = ConexionBD.conectar();
            conexion.setAutoCommit(false);

            try (PreparedStatement actualizarPedido = conexion.prepareStatement(sqlActualizarPedido);
                 PreparedStatement guardarEntrega = conexion.prepareStatement(sqlGuardarEntrega, Statement.RETURN_GENERATED_KEYS)) {

                actualizarPedido.setString(1, pedido.getEstado().toString());
                actualizarPedido.setInt(2, pedido.getIdPedido());

                int pedidosActualizados = actualizarPedido.executeUpdate();

                if (pedidosActualizados != 1) {
                    conexion.rollback();
                    return false;
                }

                guardarEntrega.setInt(1, entrega.getIdPedido());
                guardarEntrega.setInt(2, entrega.getIdRepartidor());
                guardarEntrega.setDate(3, Date.valueOf(entrega.getFecha()));
                guardarEntrega.setTime(4, Time.valueOf(entrega.getHora()));

                int entregasInsertadas = guardarEntrega.executeUpdate();

                if (entregasInsertadas != 1) {
                    conexion.rollback();
                    return false;
                }

                try (ResultSet clavesGeneradas = guardarEntrega.getGeneratedKeys()) {
                    if (clavesGeneradas.next()) {
                        entrega.setId(clavesGeneradas.getInt(1));
                    } else {
                        conexion.rollback();
                        return false;
                    }
                }

                conexion.commit();

                return true;
            }

        } catch (SQLException ex) {

            if (conexion != null) {
                try {
                    conexion.rollback();
                } catch (SQLException rollbackEx) {
                    System.out.println("Error al revertir la transacción.");
                    System.out.println(rollbackEx.getMessage());
                }
            }

            System.out.println("Error al registrar la entrega completa.");
            System.out.println(ex.getMessage());

            return false;

        } finally {

            if (conexion != null) {
                try {
                    conexion.close();
                } catch (SQLException ex) {
                    System.out.println("Error al cerrar la conexión.");
                    System.out.println(ex.getMessage());
                }
            }
        }
    }
}