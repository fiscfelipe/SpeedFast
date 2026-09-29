# SpeedFast

Proyecto desarrollado en Java para simular la gestión de pedidos y entregas de la empresa ficticia SpeedFast.

## Funcionalidades

La aplicación permite:

- Registrar pedidos de tipo COMIDA, ENCOMIENDA y EXPRESS.
- Validar los datos ingresados antes de registrar un pedido.
- Visualizar los pedidos almacenados mediante JTable.
- Consultar los pedidos registrados durante la ejecución de la aplicación.
- Seleccionar la cantidad de repartidores que participarán en una entrega.
- Ejecutar entregas concurrentes mediante múltiples hilos.
- Actualizar los estados de los pedidos:
  - PENDIENTE
  - EN_REPARTO
  - ENTREGADO
- Visualizar el repartidor responsable de cada pedido.
- Verificar el estado final de los pedidos al finalizar la simulación.
  

## Estructura del proyecto

```text
src/
├── UI/
│   ├── PanelEntrega.java
│   ├── PanelInicio.java
│   ├── PanelListaPedidos.java
│   ├── PanelRegistroPedido.java
│   └── VentanaPrincipal.java
│
├── app/
│   └── Main.java
│
└── model/
    ├── Cancelable.java
    ├── ControladorDeEnvios.java
    ├── Despachable.java
    ├── EstadoPedido.java
    ├── Pedido.java
    ├── PedidoComida.java
    ├── PedidoEncomienda.java
    ├── PedidoExpress.java
    ├── PrioridadPedido.java
    ├── Rastreable.java
    ├── Repartidor.java
    └── ZonaDeCarga.java
```

## Ejecución

1. Abrir el proyecto en NetBeans.
2. Ejecutar:

```text
app/Main.java
```

La aplicación abrirá la interfaz gráfica de SpeedFast.

## Uso de la aplicación

### Registrar pedido

Desde la opción **Registrar pedido** se solicitan:

- ID
- Dirección
- Distancia en kilómetros
- Tipo de pedido

El pedido se registra inicialmente con estado:

```text
PENDIENTE
```

Los datos ingresados son validados antes de incorporar el pedido al sistema.

### Listar pedidos

La opción **Listar pedidos** muestra los pedidos registrados mediante JTable.

La tabla permite consultar:

- ID
- Dirección
- Distancia
- Tipo
- Estado
- Repartidor asignado

La información se actualiza al volver a ingresar a esta sección.

### Iniciar entregas

La opción **Iniciar entregas** permite seleccionar la cantidad de repartidores que participarán en la simulación.

Los repartidores ejecutan las entregas concurrentemente.

Durante el proceso, un pedido puede pasar por:

```text
PENDIENTE → EN_REPARTO → ENTREGADO
```

Al finalizar la simulación se muestra el estado final de cada pedido, el repartidor responsable, la cantidad de pedidos entregados y la cantidad de pedidos pendientes.


## Manejo de concurrencia

La zona de carga es un recurso compartido utilizado por múltiples repartidores.

El proyecto utiliza mecanismos de concurrencia para impedir que un mismo pedido sea retirado simultáneamente por más de un repartidor.

Los repartidores se ejecutan mediante `ExecutorService`, permitiendo realizar varias entregas en paralelo.

También se contempla el caso de interrupción de un repartidor. Si una entrega se interrumpe antes de finalizar, el pedido vuelve al estado:

```text
PENDIENTE
```

y se reincorpora a la zona de carga para que pueda ser atendido nuevamente.


## Almacenamiento de datos

Durante la Semana 6 los pedidos se almacenan en memoria mediante las estructuras utilizadas por el sistema.

Los distintos paneles comparten las mismas instancias del controlador y de la zona de carga, permitiendo que los pedidos registrados desde la interfaz puedan ser consultados y utilizados posteriormente en la simulación de entregas.

Los datos se mantienen mientras la aplicación está en ejecución y se eliminan al cerrar el programa.
