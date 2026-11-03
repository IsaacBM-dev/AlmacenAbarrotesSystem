# 📦 AlmacenAbarrotesSystem
**Sistema de Control de Inventario, Monitoreo de Perecibles y Kardex Transaccional para Almacén Mayorista de Abarrotes**

![Java](https://img.shields.io/badge/Java-JDK%2021%20%2F%2026-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Maven](https://img.shields.io/badge/Apache%20Maven-3.9+-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)
![SQL Server](https://img.shields.io/badge/Microsoft%20SQL%20Server-T--SQL-CC2927?style=for-the-badge&logo=microsoftsqlserver&logoColor=white)
![FlatLaf](https://img.shields.io/badge/UI-Java%20Swing%20%2B%20FlatLaf%203.7.1-007ACC?style=for-the-badge)
![BCrypt](https://img.shields.io/badge/Security-jBCrypt%200.4-2EA44F?style=for-the-badge)

---

## 📋 Descripción del Proyecto

**AlmacenAbarrotesSystem** es una aplicación de escritorio desarrollada en **Java (Swing + JDBC + Maven)** en **Apache NetBeans** y conectada a **Microsoft SQL Server**, diseñada para resolver cuatro problemáticas críticas en los almacenes mayoristas de abarrotes:

1. **Pérdidas financieras por vencimiento de perecibles:** Lácteos, embutidos y conservas caducan sin alertas preventivas.
2. **Quiebres de stock en alta rotación:** Artículos esenciales (arroz, azúcar, aceite) se agotan sin previo aviso por falta de monitoreo del punto de reorden (`stock_minimo`).
3. **Descuadre físico-contable por unidades de empaque:** Confusión entre unidades mayoristas (`SACO 50KG`, `CAJA X 24`, `PAQUETE X 12`) y unidades sueltas.
4. **Falta de trazabilidad en el Kardex:** Ausencia de un registro auditable que vincule cada ingreso, despacho o baja por merma con el usuario responsable, fecha, hora y motivo.

---

## ✨ Características Principales

- 🔐 **Autenticación Segura con BCrypt:** Contraseñas encriptadas mediante el algoritmo Blowfish (`jbcrypt-0.4`) con *salt* aleatorio y botón integrado para mostrar/ocultar clave (`showRevealButton`).
- 👥 **Control de Acceso Basado en Roles (RBAC):**
  - **`ADMIN`:** Acceso total a Gestión de Usuarios, Catálogo de Productos, Kardex Transaccional y Monitor de Alertas.
  - **`ALMACENERO`:** Acceso exclusivo al registro de Movimientos (Kardex), visualización de Alertas y consulta de Catálogo en modo solo lectura.
- 🚨 **Dashboard de Alertas en Tiempo Real:** Identifica automáticamente productos con `stock_actual <= stock_minimo` o próximos a vencer en `<= 30 días`, resaltando las filas con código de colores según su nivel de criticidad.
- 📦 **Regla de "Unidad de Almacén Única":** Estandariza las entradas y salidas por empaque mayorista (`SACO 50KG`, `CAJA X 24`, `PAQUETE X 12`, `CAJA X 12`, `SACO 15KG`, `UNIDAD`).
- 🔄 **Kardex Transaccional ACID y Validación de Stock No Negativo:**
  - Bloquea en tiempo real cualquier intento de registrar una `SALIDA` superior al `stock_actual` disponible.
  - Actualiza automáticamente el stock del producto en SQL Server mediante Procedimiento Almacenado transaccional (`BEGIN TRAN` / `UPDLOCK` / `COMMIT` / `ROLLBACK`).
- 🗑️ **Borrado Lógico (`BIT`):** Ningún usuario o producto con historial en el Kardex se elimina físicamente (`1 = Activo`, `0 = Inactivo`).
- 🌗 **Modo Claro (Light Owl) y Modo Oscuro (Night Owl):** Alternancia dinámica de temas visuales en vivo mediante **FlatLaf IntelliJ Themes** (`FlatMTLightOwlIJTheme` y `FlatMTNightOwlIJTheme`) e indicador de pestaña activa en el menú lateral.

---

## 🏗️ Arquitectura del Software (Patrón MVC + DAO)

El proyecto está organizado bajo el principio de **Separación de Responsabilidades (SoC)** en 6 paquetes claramente definidos:

```text
AlmacenAbarrotesSystem/
├── pom.xml                                        # Configuración Maven, recursos de íconos y dependencias
├── BD_AlmacenAbarrotes.sql                        # Script T-SQL completo (Tablas, Checks, SPs y Datos Semilla)
└── src/main/java/
    ├── com/senati/almacenabarrotessystem/         # Paquete principal de arranque y pruebas por consola
    │   ├── AlmacenSystem.java                     # Clase Main: inicializa tema FlatLaf y lanza JFrame_Login
    │   ├── Test_Conexion.java                     # Prueba unitaria de conexión JDBC a SQL Server
    │   ├── Test_Hash.java                         # Generador y verificador de hashes BCrypt
    │   ├── Test_Producto.java                     # Prueba de CRUD de catálogo y alertas por consola
    │   └── Test_Movimiento.java                   # Prueba transaccional del Kardex y bloqueo de stock negativo
    │
    ├── controller/                                # Capa de Controlador (Reglas de Negocio y Sesión)
    │   ├── Sesion.java                            # Estado estático del usuario autenticado en memoria
    │   ├── UsuarioController.java                 # Validaciones de autenticación, registro y estado
    │   ├── ProductoController.java                # Validaciones de precios (> 0), stocks (>= 0) y fechas
    │   └── MovimientoController.java              # Validación crítica de stock disponible antes de salidas
    │
    ├── dao/                                       # Capa de Acceso a Datos (Persistencia JDBC)
    │   ├── conexion_class.java                    # Conexión JDBC a localhost:1433 (BD_AlmacenAbarrotes)
    │   ├── UsuarioDAO.java                        # Consumo de SPs de Usuario
    │   ├── ProductoDAO.java                       # Consumo de SPs de Producto y mapeo de AlertaResumen
    │   └── MovimientoDAO.java                     # Ejecución transaccional de sp_Movimiento_Registrar
    │
    ├── model/                                     # Capa de Modelo (Entidades POJO y DTO)
    │   ├── Usuario.java                           # Entidad Usuario
    │   ├── Producto.java                          # Entidad Producto
    │   ├── Movimiento.java                        # Entidad Movimiento (Kardex)
    │   └── AlertaResumen.java                     # DTO para el Monitor de Alertas del Dashboard
    │
    ├── util/                                      # Capa de Utilidades y Seguridad
    │   ├── protegerClave.java                     # Métodos estáticos BCrypt.hashpw y BCrypt.checkpw
    │   └── GestorTema.java                        # Motor de cambio entre Light Owl / Night Owl y estilos UI
    │
    └── view/                                      # Capa de Presentación (Java Swing GUI Forms)
        ├── JFrame_Login.java / .form              # Ventana de autenticación
        ├── JFrame_Principal.java / .form          # Dashboard principal con Sidebar y contenedor dinámico
        ├── JPanel_Producto.java / .form           # Módulo de Catálogo y Ficha Técnica de Abarrotes
        ├── JPanel_Movimiento.java / .form         # Módulo de Operaciones y Trazabilidad del Kardex
        └── JPanel_Usuario.java / .form            # Módulo de Administración de Usuarios (Exclusivo ADMIN)
```

---

## 🗄️ Modelo de Base de Datos (`BD_AlmacenAbarrotes`)

El esquema relacional en **Microsoft SQL Server** cumple hasta la **Tercera Forma Normal (3FN)**:

| Tabla | Clave Primaria | Descripción | Restricciones Clave (`CHECK` / `UNIQUE`) |
|---|---|---|---|
| **`dbo.Usuario`** | `id_usuario` (`INT IDENTITY`) | Operadores del sistema y sus credenciales encriptadas. | `UQ_Usuario_Username`, `rol IN ('ADMIN', 'ALMACENERO')` |
| **`dbo.Producto`** | `id_producto` (`INT IDENTITY`) | Catálogo maestro de abarrotes con su unidad de almacén y vencimiento. | `UQ_Producto_Codigo`, `stock_actual >= 0`, `stock_minimo >= 0`, `precio_referencial > 0` |
| **`dbo.Movimiento`** | `id_movimiento` (`INT IDENTITY`) | Historial inmutable del Kardex vinculado a `Producto` y `Usuario`. | `tipo_movimiento IN ('ENTRADA', 'SALIDA')`, `cantidad > 0` |

---

## ⚙️ Requisitos Previos y Dependencias Maven

### Entorno Requerido
- **Java Development Kit (JDK):** versión 21 o 26.
- **IDE:** Apache NetBeans IDE (con soporte Maven integrado).
- **Motor de Base de Datos:** Microsoft SQL Server (2019 / 2022) con TCP/IP habilitado en el puerto `1433`.

### Dependencias Incluidas en `pom.xml`
| Librería | Coordenada Maven (`groupId:artifactId:version`) | Propósito |
|---|---|---|
| **Driver JDBC SQL Server** | `com.microsoft.sqlserver:mssql-jdbc:13.6.0.jre11` | Conectividad oficial con SQL Server vía TCP/IP y soporte TLS. |
| **FlatLaf Core** | `com.formdev:flatlaf:3.7.1` | Look and Feel moderno para componentes Java Swing. |
| **FlatLaf IntelliJ Themes** | `com.formdev:flatlaf-intellij-themes:3.7.1` | Temas visuales **Light Owl** y **Night Owl**. |
| **jBCrypt** | `org.mindrot:jbcrypt:0.4` | Hashing criptográfico Blowfish para contraseñas. |

---

## 🚀 Guía de Instalación y Ejecución

### 1. Clonar el Repositorio
```bash
git clone https://github.com/tu-usuario/AlmacenAbarrotesSystem.git
```

### 2. Crear la Base de Datos en SQL Server
1. Abre **SQL Server Management Studio (SSMS)**.
2. Abre y ejecuta completo el archivo **`BD_AlmacenAbarrotes.sql`**.
   - Creará la base de datos `BD_AlmacenAbarrotes`, las 3 tablas, los 12 procedimientos almacenados y los datos semilla de prueba.

### 3. Configurar las Credenciales JDBC
1. Abre el proyecto en **Apache NetBeans**.
2. Edita el archivo `src/main/java/dao/conexion_class.java` y coloca el usuario y contraseña de tu instancia local de SQL Server:
   ```java
   private static final String USER = "sa";
   private static final String PASSWORD = "TuPasswordDeSQLServer";
   ```
3. Ejecuta `Test_Conexion.java` (`Shift + F6`) para verificar que la conexión sea exitosa.
4. *(Opcional)* Ejecuta `Test_Hash.java` (`Shift + F6`) para sincronizar el hash BCrypt inicial en tu base de datos.

### 4. Compilar y Ejecutar la Aplicación
1. Haz clic derecho sobre el proyecto en NetBeans y selecciona **`Clean and Build`** (esto descargará las dependencias Maven y empaquetará los íconos).
2. Ejecuta el proyecto presionando **`F6`** (clase principal `AlmacenSystem.java`).

---

## 🔑 Credenciales de Prueba por Defecto

| Rol | Usuario (`username`) | Contraseña | Permisos Asignados |
|---|---|---|---|
| **Administrador** | `admin` | `admin123` | Acceso total (Usuarios, Catálogo CRUD, Kardex y Alertas). |
| **Almacenero** | `almacenero1` | `admin123` | Registro de Kardex, Alertas y consulta de Catálogo en modo lectura. |

---

## 📈 Roadmap de Mejoras Futuras

- [ ] Migración de la capa `dao` hacia **JPA / Hibernate**.
- [ ] Exposición de servicios mediante **API REST con Spring Boot**.
- [ ] Exportación directa de reportes de Kardex y Mermas a **PDF (OpenPDF/JasperReports)** y **Excel (Apache POI)**.
- [ ] Integración con lector óptico de códigos de barras (EAN-13 / QR) en el buscador y registro de movimientos.

---

## 👨‍💻 Autores

Proyecto desarrollado para el curso de Desarrollo de Software / Ingeniería de Sistemas — **SENATI**.
