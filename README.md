# SpeedFast

Proyecto desarrollado en Java para simular la gestión de pedidos y entregas de la empresa ficticia SpeedFast.

La aplicación integra programación orientada a objetos, concurrencia, una interfaz gráfica desarrollada con Java Swing y persistencia de datos mediante MySQL y JDBC.

## Funcionalidades

La aplicación permite:

- Registrar pedidos de tipo COMIDA, ENCOMIENDA y EXPRESS.
- Validar los datos ingresados antes de registrar un pedido.
- Generar automáticamente el identificador de cada pedido mediante MySQL.
- Visualizar los pedidos almacenados mediante JTable.
- Consultar, actualizar y eliminar pedidos registrados.
- Registrar, consultar, actualizar y eliminar repartidores.
- Registrar, consultar, actualizar y eliminar entregas.
- Seleccionar Pedido y Repartidor mediante JComboBox cargados desde la base de datos.
- Seleccionar la cantidad de repartidores que participarán en una entrega.
- Ejecutar entregas concurrentes mediante múltiples hilos.
- Actualizar los estados de los pedidos:
  - PENDIENTE
  - EN_REPARTO
  - ENTREGADO
- Registrar automáticamente una entrega al finalizar correctamente una simulación.
- Cambiar automáticamente un pedido a ENTREGADO al registrar una entrega.
- Devolver un pedido a PENDIENTE al eliminar su entrega.
- Visualizar el repartidor responsable de cada pedido durante la simulación.
- Verificar el estado final de los pedidos al finalizar la simulación.
- Mantener los datos almacenados después de cerrar y volver a abrir la aplicación.

## Estructura del proyecto

```text
src/
├── UI/
│   ├── PanelEntrega.java
│   ├── PanelGestionEntregas.java
│   ├── PanelGestionRepartidores.java
│   ├── PanelInicio.java
│   ├── PanelListaPedidos.java
│   ├── PanelRegistroPedido.java
│   └── VentanaPrincipal.java
│
├── app/
│   └── Main.java
│
├── dao/
│   ├── ConexionBD.java
│   ├── EntregaDAO.java
│   ├── PedidoDAO.java
│   └── RepartidorDAO.java
│
└── model/
    ├── Cancelable.java
    ├── ControladorDeEnvios.java
    ├── Despachable.java
    ├── Entrega.java
    ├── EstadoPedido.java
    ├── Pedido.java
    ├── PedidoComida.java
    ├── PedidoEncomienda.java
    ├── PedidoExpress.java
    ├── PrioridadPedido.java
    ├── Rastreable.java
    ├── Repartidor.java
    └── ZonaDeCarga.java

lib/
└── mysql-connector-j-26.7.0.jar

speedfast_db.sql
```

## Base de datos

El proyecto utiliza la base de datos MySQL:

```text
speedfast_db
```

El archivo `speedfast_db.sql` incluido en el proyecto contiene la creación de las tablas utilizadas por la aplicación:

- `repartidor`
- `pedido`
- `entrega`

También incorpora los repartidores iniciales utilizados por la simulación.

Antes de ejecutar la aplicación:

1. Abrir MySQL Workbench.
2. Ejecutar el archivo:

```text
speedfast_db.sql
```

3. Confirmar que la base de datos `speedfast_db` fue creada correctamente.

## Configuración de la conexión

La conexión se encuentra configurada en:

```text
src/dao/ConexionBD.java
```

Antes de ejecutar el proyecto, reemplazar:

```java
private static final String PASSWORD = "CAMBIAR_AQUÍ";
```

por la contraseña local del usuario `root` de MySQL.

La conexión utilizada es:

```text
jdbc:mysql://localhost:3306/speedfast_db
```

El proyecto incluye el conector JDBC dentro de:

```text
lib/mysql-connector-j-26.7.0.jar
```

## Ejecución

1. Crear la base de datos ejecutando `speedfast_db.sql`.
2. Configurar la contraseña de MySQL en `ConexionBD.java`.
3. Abrir el proyecto en NetBeans.
4. Ejecutar:

```text
app/Main.java
```

La aplicación abrirá la interfaz gráfica de SpeedFast.

## Uso de la aplicación

### Registrar pedido

Desde la opción **Registrar pedido** se solicitan:

- Dirección
- Distancia en kilómetros
- Tipo de pedido

El identificador es generado automáticamente por MySQL.

El pedido se registra inicialmente con estado:

```text
PENDIENTE
```

Los datos ingresados son validados antes de incorporar el pedido al sistema y almacenarlo en la base de datos.

### Gestionar pedidos

La opción **Gestionar pedidos** muestra los pedidos registrados mediante JTable.

La tabla permite consultar:

- ID
- Dirección
- Distancia
- Tipo
- Estado

Es posible actualizar:

- Dirección
- Distancia
- Tipo

El estado no puede modificarse manualmente desde este panel, ya que sus cambios dependen de la lógica de entrega.

También es posible eliminar pedidos que no tengan una entrega asociada.

### Gestionar repartidores

La opción **Gestionar repartidores** permite:

- Registrar repartidores.
- Consultar repartidores.
- Actualizar el nombre de un repartidor.
- Eliminar repartidores.

Si un repartidor tiene entregas asociadas, la integridad referencial de la base de datos impide su eliminación.

### Gestionar entregas

La opción **Gestionar entregas** permite:

- Registrar una entrega.
- Consultar las entregas existentes.
- Actualizar el repartidor, la fecha y la hora.
- Eliminar una entrega.

Para registrar una entrega se seleccionan Pedido y Repartidor mediante JComboBox cargados directamente desde la base de datos.

Al registrar una entrega, el pedido asociado cambia automáticamente a:

```text
ENTREGADO
```

Al eliminar una entrega, el pedido asociado vuelve automáticamente a:

```text
PENDIENTE
```

Una vez registrada una entrega, el pedido asociado no puede modificarse desde la edición de esa entrega.

### Iniciar entregas

La opción **Iniciar entregas** permite seleccionar la cantidad de repartidores que participarán en la simulación.

Los repartidores ejecutan las entregas concurrentemente.

Durante el proceso, un pedido puede pasar por:

```text
PENDIENTE → EN_REPARTO → ENTREGADO
```

Al finalizar correctamente una entrega, el cambio de estado del pedido y el registro de la entrega se realizan dentro de una misma transacción.

Si alguna operación falla, los cambios son revertidos y el pedido vuelve a estado PENDIENTE.

Al finalizar la simulación se muestra el estado final de cada pedido, el repartidor responsable, la cantidad de pedidos entregados y la cantidad de pedidos pendientes.

## Operaciones CRUD

Las entidades principales utilizan clases DAO con operaciones CRUD:

```text
RepartidorDAO
PedidoDAO
EntregaDAO
```

Los métodos principales son:

```text
create()
readAll()
update()
delete()
```

El acceso a la base de datos utiliza `PreparedStatement`, `ResultSet` y cierre automático de recursos mediante `try-with-resources`.

## Manejo de transacciones

Las operaciones que requieren mantener consistencia entre `pedido` y `entrega` utilizan transacciones JDBC.

Al registrar una entrega:

```text
Registrar entrega
        +
Cambiar pedido a ENTREGADO
        ↓
      COMMIT
```

Si alguna operación falla:

```text
ROLLBACK
```

Al eliminar una entrega:

```text
Eliminar entrega
        +
Cambiar pedido a PENDIENTE
        ↓
      COMMIT
```

## Manejo de concurrencia

La zona de carga es un recurso compartido utilizado por múltiples repartidores.

El proyecto utiliza mecanismos de concurrencia para impedir que un mismo pedido sea retirado simultáneamente por más de un repartidor.

Los repartidores se ejecutan mediante `ExecutorService`, permitiendo realizar varias entregas en paralelo.

También se contempla el caso de interrupción de un repartidor. Si una entrega se interrumpe antes de finalizar, el pedido vuelve al estado:

```text
PENDIENTE
```

y se reincorpora a la zona de carga para que pueda ser atendido nuevamente.

## Persistencia de datos

Los pedidos, repartidores y entregas se almacenan de forma persistente en MySQL.

Los paneles consultan la base de datos para actualizar sus tablas y controles de selección.

Los pedidos pendientes también pueden recuperarse desde la base de datos después de reiniciar la aplicación y reincorporarse a la simulación de entregas.

## Validaciones y manejo de errores

La interfaz valida los datos antes de ejecutar operaciones sobre la base de datos.

Entre las validaciones implementadas se encuentran:

- Campos obligatorios.
- Distancias numéricas mayores que cero.
- Fechas válidas.
- Horas válidas.
- Selección de Pedido y Repartidor.
- Confirmación antes de eliminar registros.

Los resultados de las operaciones se informan mediante `JOptionPane` y los errores SQL son manejados en las clases DAO.
