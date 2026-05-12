# 🏪 KinalApp - Sistema de Gestión de Ventas

## 📋 Descripción

**KinalApp** es un sistema de gestión de ventas desarrollado como proyecto final del Colegio KINAL. Permite gestionar clientes, productos, ventas y usuarios de manera eficiente, con cálculo automático de totales, control de inventario y autenticación segura mediante **Spring Security**.

---

## 🚀 Tecnologías Utilizadas

| Tecnología        | Versión |
|-------------------|---------|
| Java              | 17      |
| Spring Boot       | 3.1.5   |
| Spring Security   | 6.1.5   |
| MySQL             | 8.0     |
| Thymeleaf         | 3.1     |
| Thymeleaf Security Extra | 3.1 |
| Bootstrap         | 5.3     |
| Maven             | -       |

---

## 🔐 Seguridad

La aplicación utiliza **Spring Security** para proteger todas las rutas. El sistema implementa:

- Autenticación mediante formulario de login nativo de Spring Security
- Protección de rutas: solo usuarios autenticados pueden acceder a los módulos
- Cierre de sesión seguro con invalidación de sesión HTTP
- Carga de usuarios desde la base de datos mediante `UserDetailsService`
- Roles de usuario: `ADMIN` y `USER`
- Rutas públicas: `/login`, `/css/**`, `/img/**`, `/api/auth/**`, `/api/usuarios`

---

## 🔑 Credenciales por Defecto

| Rol   | Usuario | Contraseña |
|-------|---------|------------|
| ADMIN | admin   | admin123   |
| USER  | user    | user123    |

> ⚠️ Si la base de datos está vacía, insertar manualmente con:
> ```sql
> USE dbclientes_in5am;
> INSERT INTO usuarios (username, password, email, rol, estado)
> VALUES ('admin', 'admin123', 'admin@kinal.com', 'ADMIN', 1);
> INSERT INTO usuarios (username, password, email, rol, estado)
> VALUES ('user', 'user123', 'user@kinal.com', 'USER', 1);
> ```

---

## 📥 Instalación

### 1. Clonar el repositorio
```bash
git clone https://github.com/jjax-2025012/Kinal-Proyecto.git
cd Kinal-Proyecto
```

### 2. Configurar la base de datos
Crear la base de datos en MySQL y configurar el archivo `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/dbclientes_in5am
spring.datasource.username=tu_usuario
spring.datasource.password=tu_password
spring.jpa.hibernate.ddl-auto=update
server.port=8081
spring.security.enabled=true
```

### 3. Ejecutar la aplicación

Desde IntelliJ IDEA o terminal:
```bash
mvn spring-boot:run
```

Luego abrir en el navegador: [http://localhost:8081/login](http://localhost:8081/login)

---

## 🧑‍💻 Manual de Usuario

### 🔐 Inicio de Sesión
1. Abrir: `http://localhost:8081/login`
2. Ingresar usuario y contraseña
3. Click en **"Iniciar Sesión"**
4. Spring Security valida las credenciales contra la base de datos
5. Si son correctas, redirige automáticamente al **Panel Principal** (`/index`)

### 🏠 Panel Principal (`/index`)
Después del login se muestra el panel principal con:
- Navbar con links a todos los módulos
- Nombre del usuario autenticado
- Cards de acceso rápido a: Clientes, Productos, Ventas y Usuarios
- Botón de **Cerrar Sesión**

### 📝 Registro de Usuario
1. En la pantalla de login, click en **"Registrarse"**
2. Completar: Usuario (mínimo 3 caracteres), Email, Contraseña, Confirmar contraseña, Rol
3. Click en **"Registrarse"**

---

### 👥 Gestión de Clientes (`/clientes`)

| Acción   | Descripción                                      |
|----------|--------------------------------------------------|
| Listar   | Ver todos los clientes registrados               |
| Crear    | Click "Nuevo Cliente" → Completar datos → Guardar |
| Editar   | Click "Editar" → Modificar datos → Guardar       |
| Eliminar | Click "Eliminar" → Confirmar                     |
| Estado   | Activo (1) / Inactivo (0)                        |

---

### 📦 Gestión de Productos (`/productos`)

| Acción   | Descripción                              |
|----------|------------------------------------------|
| Listar   | Ver productos con stock y precio         |
| Crear    | Click "Nuevo Producto" → Completar → Guardar |
| Editar   | Modificar datos → Guardar                |
| Eliminar | Confirmar eliminación                    |
| Stock    | Se descuenta automáticamente al vender   |

---

### 💰 Gestión de Ventas (`/ventas`)

| Acción          | Descripción                              |
|-----------------|------------------------------------------|
| Listar          | Ver todas las ventas registradas         |
| Crear Venta     | Seleccionar Cliente y Usuario            |
| Agregar Producto| Seleccionar producto, cantidad y precio  |
| Total           | Se calcula automáticamente con los detalles |
| Anular Venta    | Cambia estado a inactivo, no se elimina  |
| Ver Detalle     | Muestra todos los productos de la venta  |

---

### 👤 Gestión de Usuarios (`/usuarios`)

| Acción   | Descripción            |
|----------|------------------------|
| Listar   | Ver todos los usuarios |
| Crear    | Nuevo usuario          |
| Editar   | Modificar datos        |
| Eliminar | Confirmar eliminación  |
| Roles    | `ADMIN` / `USER`       |

---

### 🔓 Cerrar Sesión
- Click en **"Cerrar Sesión"** en el navbar o en el sidebar
- Spring Security invalida la sesión y elimina la cookie `JSESSIONID`
- Redirige a `/login?logout=true` con mensaje de confirmación

---

## 🔧 Endpoints API REST

| Método | Endpoint                        | Descripción          |
|--------|---------------------------------|----------------------|
| GET    | `/api/clientes`                 | Listar clientes      |
| POST   | `/api/clientes`                 | Crear cliente        |
| PUT    | `/api/clientes/{dpi}`           | Actualizar cliente   |
| DELETE | `/api/clientes/{dpi}`           | Eliminar cliente     |
| GET    | `/api/productos`                | Listar productos     |
| POST   | `/api/productos`                | Crear producto       |
| PUT    | `/api/productos/{codigo}`       | Actualizar producto  |
| DELETE | `/api/productos/{codigo}`       | Eliminar producto    |
| GET    | `/api/ventas`                   | Listar ventas        |
| POST   | `/api/ventas`                   | Crear venta          |
| PUT    | `/api/ventas/{codigo}/anular`   | Anular venta         |
| GET    | `/api/usuarios`                 | Listar usuarios      |
| POST   | `/api/usuarios`                 | Crear usuario        |
| PUT    | `/api/usuarios/{codigo}`        | Actualizar usuario   |
| DELETE | `/api/usuarios/{codigo}`        | Eliminar usuario     |
| POST   | `/api/auth/login`               | Autenticación REST   |

---

## 📁 Estructura del Proyecto

```bash
Kinal-Proyecto/
├── src/
│   ├── main/
│   │   ├── java/com/josethjax/kinalapp/
│   │   │   ├── config/
│   │   │   │   ├── DataInitializer.java
│   │   │   │   ├── SecurityConfig.java       ← Spring Security
│   │   │   │   └── WebConfig.java
│   │   │   ├── controller/
│   │   │   │   ├── AuthController.java
│   │   │   │   ├── ClienteViewController.java
│   │   │   │   ├── LoginViewController.java
│   │   │   │   ├── ProductoViewController.java
│   │   │   │   ├── UsuarioViewController.java
│   │   │   │   ├── VentaViewController.java
│   │   │   │   ├── DetalleVentaViewController.java
│   │   │   │   └── ViewController.java
│   │   │   ├── entity/
│   │   │   │   ├── Cliente.java
│   │   │   │   ├── DetalleVenta.java
│   │   │   │   ├── Producto.java
│   │   │   │   ├── Usuario.java
│   │   │   │   └── Venta.java
│   │   │   ├── repository/
│   │   │   ├── Service/
│   │   │   │   ├── UserDetailsServiceImpl.java  ← Spring Security
│   │   │   │   └── UsuarioService.java
│   │   └── resources/
│   │       ├── static/css/
│   │       ├── static/img/
│   │       └── templates/
│   │           ├── login.html
│   │           ├── index.html
│   │           ├── layout/base.html
│   │           ├── fragments/sidebar.html
│   │           ├── cliente/
│   │           ├── producto/
│   │           ├── venta/
│   │           ├── detalle-venta/
│   │           └── usuario/
│   └── test/
├── pom.xml
└── README.md
```

---

## ❓ Solución de Problemas

| Problema                  | Solución                                              |
|---------------------------|-------------------------------------------------------|
| Error de conexión MySQL   | Verificar credenciales en `application.properties`    |
| Puerto ocupado            | Cambiar `server.port` en `application.properties`     |
| No inicia sesión          | Verificar que existan usuarios en la tabla `usuarios` |
| Error 403 Forbidden       | Verificar que el usuario tenga el rol correcto        |
| Error 404                 | Verificar que la app esté corriendo en el puerto 8081 |
| No cargan estilos         | Limpiar caché del navegador (Ctrl+Shift+R)            |
| Sesión expira sola        | Normal — Spring Security cierra sesiones inactivas    |
| Total de venta en 0       | Agregar al menos un detalle de venta                  |

---

## 📝 Historial de Cambios

| Versión | Cambio                                                      |
|---------|-------------------------------------------------------------|
| 1.0     | Proyecto base con CRUD de clientes, productos, ventas y usuarios |
| 1.1     | Integración de **Spring Security** en el login             |
| 1.1     | Creación de `UserDetailsServiceImpl` para carga desde BD   |
| 1.1     | Creación de `SecurityConfig` con rutas protegidas          |
| 1.1     | Actualización de `login.html` para formulario nativo       |
| 1.1     | Actualización de `index.html` con navbar y cards de acceso |
| 1.1     | Actualización de `sidebar.html` y `base.html` con logout   |
| 1.1     | Eliminación del `AuthInterceptor` manual                   |

---

## 👨‍💻 Desarrollador

**Joseth Emanuel Jax Ramirez**  
Estudiante de Perito Técnico en Informática  
Colegio KINAL — 2026

---

## 📄 Licencia

Proyecto educativo — Colegio KINAL