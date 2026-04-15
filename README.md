# 🏪 KinalApp - Sistema de Gestión de Ventas

## 📋 Descripción

**KinalApp** es un sistema de gestión de ventas desarrollado como proyecto final. Permite gestionar clientes, productos, ventas y usuarios de manera eficiente con cálculo automático de totales y control de inventario.

---

## 🚀 Tecnologías Utilizadas

| Tecnología | Versión |
|------------|---------|
| Java | 17 |
| Spring Boot | 3.1.5 |
| MySQL | 8.0 |
| Thymeleaf | 3.1 |
| Bootstrap | 5.3 |
| Maven | - |

---

## 🔐 Credenciales por Defecto

| Rol | Usuario | Contraseña |
|-----|---------|------------|
| ADMIN | Jax     | 1234       |
| USER | Jax     | 1234       |

---

## 📥 Instalación

### 1. Clonar el repositorio
```bash
git clone https://github.com/jjax-2025012/Kinal-Proyecto.git
cd Kinal-Proyecto

Configurar la base de datos
Crear una base de datos en MySQL (por ejemplo: kinalapp)
Configurar el archivo application.properties:
spring.datasource.url=jdbc:mysql://localhost:3306/kinalapp
spring.datasource.username=tu_usuario
spring.datasource.password=tu_password
spring.jpa.hibernate.ddl-auto=update
server.port=8081
3. Ejecutar la aplicación

Desde IntelliJ IDEA o terminal:

mvn spring-boot:run


🧑‍💻 Manual de Usuario


🔐 Inicio de Sesión
Abrir: http://localhost:8081/login
Ingresar usuario y contraseña
Click en "Iniciar Sesión"


📝 Registro de Usuario
En la pantalla de login, click en "Registrarse"
Completar:
Usuario (mínimo 3 caracteres)
Email
Contraseña (mínimo 4 caracteres)
Confirmar contraseña
Rol
Click en "Registrarse"


👥 Gestión de Clientes (/clientes)
Acción	Descripción
Listar	Ver todos los clientes registrados
Crear	Click "Nuevo Cliente" → Completar datos → Guardar
Editar	Click "Editar" → Modificar datos → Guardar
Eliminar	Click "Eliminar" → Confirmar
Estado	Activo (1) / Inactivo (0)


📦 Gestión de Productos (/productos)
Acción	Descripción
Listar	Ver productos con stock y precio
Crear	Click "Nuevo Producto" → Completar → Guardar
Editar	Modificar datos → Guardar
Eliminar	Confirmar eliminación
Stock	Se actualiza automáticamente


💰 Gestión de Ventas (/ventas)
Acción	Descripción
Listar	Ver todas las ventas
Crear Venta	Seleccionar Cliente y Usuario
Agregar Producto	Seleccionar producto y cantidad
Anular Venta	Confirmar (no se elimina)
Ver Detalle	Mostrar productos de la venta


👤 Gestión de Usuarios (/usuarios) - Solo ADMIN
Acción	Descripción
Listar	Ver usuarios
Crear	Nuevo usuario
Editar	Modificar datos
Eliminar	Confirmar
Roles	ADMIN / USER


🔓 Cerrar Sesión
Click en "Cerrar Sesión" en el menú lateral


🔧 Endpoints API REST
Método	Endpoint	Descripción
GET	/api/clientes	Listar clientes
POST	/api/clientes	Crear cliente
PUT	/api/clientes/{dpi}	Actualizar cliente
DELETE	/api/clientes/{dpi}	Eliminar cliente
GET	/api/productos	Listar productos
POST	/api/productos	Crear producto
PUT	/api/productos/{codigo}	Actualizar producto
DELETE	/api/productos/{codigo}	Eliminar producto
GET	/api/ventas	Listar ventas
POST	/api/ventas	Crear venta
PUT	/api/ventas/{codigo}/anular	Anular venta
GET	/api/usuarios	Listar usuarios
POST	/api/usuarios	Crear usuario
PUT	/api/usuarios/{codigo}	Actualizar usuario
DELETE	/api/usuarios/{codigo}	Eliminar usuario
POST	/api/auth/login	Autenticación


📁 Estructura del Proyecto
Kinal-Proyecto/
├── src/
│   ├── main/
│   │   ├── java/com/josethjax/kinalapp/
│   │   │   ├── config/
│   │   │   ├── controller/
│   │   │   ├── entity/
│   │   │   ├── interceptor/
│   │   │   ├── repository/
│   │   │   └── service/
│   │   └── resources/
│   │       ├── static/
│   │       └── templates/
│   └── test/
├── pom.xml
├── application.properties
└── README.md


❓ Solución de Problemas
Problema	Solución
Error MySQL	Verificar conexión y credenciales
Puerto ocupado	Cambiar server.port
No inicia sesión	Esperar carga de datos o registrarse
Total no se actualiza	Revisar subtotales
Error 404	Verificar rutas
No cargan estilos	Limpiar caché

👨‍💻 Desarrollador

Joseth Emanuel Jax Ramirez
Estudiante de Perito Técnico en Informática
Colegio KINAL

📄 Licencia

Proyecto educativo - Colegio KINAL