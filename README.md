# 🗂️ Task Manager - Sistema de Gestión de Tareas

**Task Manager** es una API REST desarrollada con **Spring Boot**, diseñada para gestionar tareas personales con soporte para creación, listado, filtrado dinámico, validaciones, y documentación Swagger. Incluye arquitectura en capas, manejo de errores y respuesta estructurada tipo `ApiResponse`.

---
# 🔍 Tabla de Contenidos
1. Tecnologías utilizadas

2. Estructura del Proyecto

3. Funcionalidades

4. Instalación y Ejecucion

5. Uso

6. API Endpoints

7. Validaciones incluidas

8. Pruebas Unitarias (en construcción)

9. Documentación Swagger

10. Contribuciones

11. Autor

12. Licencia

---
## 🚀 Tecnologías utilizadas

- Java 17
- Spring Boot 3.4.5
- Spring Data JPA (Hibernate)
- MySQL
- MapStruct
- Lombok
- Swagger (OpenAPI)
- JUnit + Mockito (en progreso)
- Maven

---

## 📦 Estructura del Proyecto

📁 src

└── main

        ├──  java
        │ └── com.proyecto.task
        │ ├── controller
        │ ├── service
        │ ├── repository
        │ ├── entities
        │ ├── dto
        │ ├── advice (manejo de errores)
        │ └── utils (utilidades generales)
        └── resources   
            ├── application.properties


---

## 🧩 Funcionalidades

- ✅ Crear tareas con validaciones (no se permiten fines de semana)
- ✅ Listar tareas con paginación
- ✅ Filtrar tareas por:
    - Estado (`Pendiente`, `Desarrollo`, etc.)
    - Palabra clave en título o descripción
    - Fecha de vencimiento (rango, vencidas, hoy)
- ✅ Manejo de errores con mensajes personalizados
- ✅ Documentación Swagger lista en `/swagger-ui/index.html`
- ✅ Respuesta estructurada con `statusCode`, `message` y `data`
- 🔄 Mapeo automático entre entidades y DTOs usando MapStruct
- ⚠️ Evita duplicar lógicas en el código gracias a capa DAO y utilidades comunes

---

## 🛠️ Instalación y Ejecución

1. **Clonar el repositorio**

```bash
git clone https://github.com/gilbertijgm/task.git
cd task
```
2. **Configurar base de datos en application.properties**

spring.datasource.url=jdbc:mysql://localhost:3306/task_db
spring.datasource.username=usuario
spring.datasource.password=contraseña
spring.jpa.hibernate.ddl-auto=update
⚠️ Asegurate de crear previamente la base de datos task_db en MySQL.

3. **Compilar y correr el proyecto**
```bash
mvn clean install
mvn spring-boot:run
```


---

## 🚀 Uso
Una vez en ejecución, puedes acceder a:

- http://localhost:8080/api/task/tasks

- http://localhost:8080/swagger-ui.html (documentación interactiva)
---

## 📥 API Endpoints
| Verbo  | Endpoint                       | Descripción                         |
| ------ | ------------------------------ | ----------------------------------- |
| POST   | `/api/task/save`               | Crea una nueva tarea                |
| PUT    | `/api/task/update?id={id}`     | Actualiza detalles de una tarea     |
| PATCH  | `/api/task/update-status`      | Actualiza solo el estado            |
| DELETE | `/api/task/deleteById?id={id}` | Elimina una tarea por ID            |
| GET    | `/api/task/tasks`              | Lista paginada y filtrada de tareas |
| GET    | `/api/task/findById/{id}`      | Obtiene tarea por ID                |



🔎 Filtros dinámicos:
El endpoint /tasks soporta filtros combinables a través de Query Params:
```bash
/api/task/tasks?page=0&size=5&estado=Pendiente&palabraClave=repo&fechaInicio=2025-06-01&fechaFin=2025-06-30&vencidas=false&vencenHoy=false
```
- estado: enum (Pendiente, Desarrollo, Completada, Cancelada)

- palabraClave: busca por título o descripción

- fechaInicio y fechaFin: filtra por rango de fechas de vencimiento

- vencidas, vencenHoy: flags booleanos

Estos filtros se aplican dinámicamente mediante Criteria API en el repositorio custom.

---

## 📚 Validaciones incluidas
- Título y descripción no pueden estar vacíos

- Fecha de vencimiento no puede ser pasada ni en fin de semana

- Estado obligatorio y debe ser uno de los definidos en el enum

---

## 🧪 Pruebas Unitarias (en construcción)
La estructura está preparada para incluir tests con JUnit 5 y Mockito.
Pronto se agregarán pruebas unitarias para el servicio y controlador.

---

## 📘 Documentación Swagger
Disponible en:

http://localhost:8080/swagger-ui.html

Expone todos los endpoints, parámetros, modelos y respuestas esperadas.

---

## 🤝 Contribuciones
¡Bienvenidas! Para contribuir:

- Haz fork del repositorio

- Crea una rama feature (git checkout -b feature/nueva-función)

- Haz commit de tus cambios (git commit -m "Añade algo")

- Haz push a la rama (git push origin feature/nueva-función)

- Abre un Pull Request

---

## 🤝 Autor
Desarrollado por Gilberto J. Gutiérrez

Linkedin:  www.linkedin.com/in/gilbertojgutierrezm

---

## 📝 Licencia

Este proyecto es de uso libre para fines educativos y personales.
