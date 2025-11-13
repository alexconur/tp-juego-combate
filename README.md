# Class Emblem — TP2 Paradigmas de Programación (FIUBA)

Sistema de combate táctico por turnos inspirado en Fire Emblem, desarrollado para el TP2 de Paradigmas de Programación (Brasburg – Raik) — 2º cuatrimestre 2025.

El proyecto está implementado en Java 21, sigue una arquitectura MVC y se ejecuta íntegramente por consola.

## Estructura del Proyecto

```
TP2/
 ├── src/
 │   ├── main/
 │   │   ├── java/org/
 │   │   │   ├── archivos
 │   │   │   ├── controlador
 │   │   │   ├── main      ← contiene Main.java (punto de entrada)
 │   │   │   ├── modelo
 │   │   │   └── vista
 │   │   └── resources     ← mapas, ejércitos, configuraciones
 │   └── test/
 ├── pom.xml
 ├── .gitignore
 ├── UML_ARCHIVOS.png
 ├── UML_CONTROLADOR.png
 ├── UML_MAIN.png
 ├── UML_MODELO.png
 ├── UML_VISTA.png
 ├── SECUENCIA_ATACAR_CURAR.png
 └── README.md
 └── Informe.pdf
```

## Requisitos

Para compilar y ejecutar el proyecto se necesita:

- Java 21 (cualquier distribución)
- Apache Maven 3.8+
- Consola / terminal

## Instalación

Linux (Ubuntu/Debian)

```bash
sudo apt update
sudo apt install -y openjdk-21-jdk maven
```

macOS (Homebrew)

```bash
brew install openjdk@21
brew install maven
```

Si es necesario, agregar Java al PATH:

```bash
export PATH="/usr/local/opt/openjdk@21/bin:$PATH"
```

Windows (winget)

```bash
winget install EclipseAdoptium.Temurin.21.JDK
winget install Apache.Maven
```

Verificación

```bash
java -version
mvn -version
```

Ambos deberían mostrar Java 21 y Maven instalado correctamente.

## Cómo compilar y ejecutar el juego

El TP debe correrse exclusivamente mediante Maven.

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

## Cómo jugar

Al iniciar el programa:

1. Seleccionar un mapa desde los archivos definidos en resources/.
2. Seleccionar ejércitos y equipamiento.
3. Colocar los lords de ambos bandos.
4. Colocar las unidades de cada bando.
5. Cada turno, el jugador puede:
   - Mover una unidad
   - Desplegar una unidad
   - Atacar o curar
   - Ver unidades
   - Ver información de casillas
   - Terminar turno
   - Rendirse
6. La partida termina cuando muere un lord o un jugador se rinde.
   Toda la interacción es por consola, con menús claros y colores ANSI para el tablero.
