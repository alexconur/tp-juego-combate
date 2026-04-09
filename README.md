# Class Emblem — Motor de Combate Táctico en Java

<p align="left">
  <i>Juego de estrategia por turnos desarrollado en Java 21, con un fuerte enfoque en Arquitectura de Software, Patrones de Diseño y Código Limpio.</i>
</p>

## 🚀 Sobre el Proyecto

**Class Emblem** es un sistema de combate táctico por turnos (inspirado en juegos como Fire Emblem) ejecutado íntegramente por consola. El proyecto fue desarrollado como demostración práctica de principios avanzados de Programación Orientada a Objetos (POO), modelando un dominio complejo con múltiples tipos de unidades, terrenos con efectos dinámicos y resolución de combates.

El motor destaca por su diseño modular y extensible, permitiendo cargar mapas y ejércitos dinámicamente mediante el parseo de archivos CSV, garantizando que la lógica del negocio esté completamente desacoplada de la configuración de las partidas.

## 🛠️ Tecnologías y Herramientas

- **Lenguaje:** Java 21
- **Gestor de Dependencias:** Apache Maven
- **Arquitectura:** Modelo-Vista-Controlador (MVC)
- **Paradigma:** Programación Orientada a Objetos (SOLID)

## 📐 Arquitectura y Patrones de Diseño

El código fue estructurado para maximizar la cohesión y minimizar el acoplamiento, facilitando su escalabilidad:

- **Modelo-Vista-Controlador (MVC):** Separación estricta entre la lógica core del dominio (tablero, sistema de turnos, reglas de victoria), la representación visual mediante renderizado ANSI en consola, y la orquestación del flujo de usuario a través de controladores especializados.
- **Factory Pattern:** Implementado en la capa de persistencia (`UnidadFactory`, `EquipamientoFactory`, `FabricaCasillas`) para instanciar dinámicamente los terrenos y entidades correctas a partir de la lectura de archivos de configuración.
- **Strategy Pattern:** Utilizado en el diseño del equipamiento mágico (`EstrategiaBaculo`, `EstrategiaCuracion`, `EstrategiaFortaleza`). Esto permite alterar el comportamiento y los efectos de los ítems en tiempo de ejecución sin modificar las clases base ni saturar el código con condicionales.
- **Polimorfismo:** Jerarquías limpias para representar unidades (`Lord`, `Tropa`), armas y casillas del tablero (`Bosque`, `Pantano`, `Castillo`), cada una con comportamientos de movimiento y efectos únicos.

## ✨ Características Destacadas

- **Carga Dinámica de Escenarios:** Algoritmos de lectura de CSV para construir el estado inicial del juego, fomentando la reusabilidad del motor.
- **Pathfinding y Movimiento:** Cálculo en tiempo real de casillas alcanzables considerando los costos de movimiento de cada tipo de terreno y obstáculos.
- **Motor de Combate Determinista:** Resolución de daños, curaciones y mecánicas de ocultamiento (emboscadas) basadas en las estadísticas de las unidades y bonificadores temporales del terreno.
- **Interfaz Gráfica en Terminal:** Renderizado interactivo del tablero y los menús utilizando códigos de color ANSI, brindando una experiencia de usuario clara e intuitiva.

## ⚙️ Instalación y Ejecución

El proyecto está empaquetado con Maven para facilitar su compilación y ejecución en cualquier entorno (Linux, macOS, Windows) que cuente con Java 21.

### Compilar

```bash
mvn compile
```

### Ejecutar

```bash
mvn exec:java
```

Este comando ya ejecuta la main class porque definimos `<exec.mainClass>org.main.Main</exec.mainClass>` en el pom.xml

### Compilar y ejecutar

```bash
mvn compile exec:java -Dexec.mainClass="org.main.Main"
```

Este comando compila el código, ejecuta la clase principal y levanta automáticamente los recursos desde src/main/resources.

## 🎮 Flujo de Juego y Mecánicas

**1. Setup Inicial y Despliegue**
* **Carga Dinámica:** Selección de mapas y ejércitos parseados en tiempo de ejecución desde archivos `.csv` en `resources/`.
* **Posicionamiento:** Ubicación estratégica del `Lord` (unidad principal) y fase de despliegue de las tropas de reserva en el tablero.

**2. Ciclo de Combate (Sistema por turnos)**
A través de menús interactivos, el jugador administra su ejército y altera el estado del modelo ejecutando las siguientes acciones:
* **Movimiento Táctico:** Desplazamiento de unidades con validación de casillas alcanzables y restricciones de terreno.
* **Sistema de Acción:** Ataque a enemigos o curación de aliados, con resolución matemática basada en las estadísticas de la unidad y su equipamiento activo.
* **Emboscadas:** Activación de mecánicas de ocultamiento aprovechando las propiedades del entorno.
* **Gestión de Tropas:** Despliegue de unidades restantes desde la reserva hacia el campo de batalla.
* **Inspección:** Consulta en profundidad de los atributos internos de las unidades y los efectos de cada tipo de casilla.

**3. Resolución e Interfaz**
* **Condición de Victoria:** El bucle principal del juego evalúa constantemente el estado de la partida, finalizando automáticamente ante la caída de un `Lord` o por rendición.
* **Renderizado en Consola:** La representación visual se logra mediante secuencias de color ANSI, generando un tablero legible e interactivo directamente en la terminal.