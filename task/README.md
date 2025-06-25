# 🗂️ Task Manager - Sistema de Gestión de Tareas

**Task Manager** es una API REST desarrollada con **Spring Boot**, diseñada para gestionar tareas personales con soporte para creación, listado, filtrado dinámico, validaciones, y documentación Swagger. Incluye arquitectura en capas, manejo de errores y respuesta estructurada tipo `ApiResponse`.

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

4. **Acceder a la documentación Swagger**

http://localhost:8080/swagger-ui/index.html

---

## 📥 Endpoints principales

| Método | Ruta             | Descripción                           |
| ------ | ---------------- | ------------------------------------- |
| POST   | `/api/task/save` | Crear una nueva tarea                 |
| GET    | `/api/task/list` | Listar tareas (con filtros dinámicos) |
| PUT    | `/api/task/{id}` | Actualizar una tarea existente        |
| DELETE | `/api/task/{id}` | Eliminar una tarea                    |
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

## 🤝 Autor
Desarrollado por Gilberto J. Gutiérrez

Linkedin:  www.linkedin.com/in/gilbertojgutierrezm

---

## 📝 Licencia

Este proyecto es de uso libre para fines educativos y personales.