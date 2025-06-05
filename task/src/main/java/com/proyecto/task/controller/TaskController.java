package com.proyecto.task.controller;

import com.proyecto.task.controller.dtos.StatusUpdateDTO;
import com.proyecto.task.controller.dtos.TaskDTO;
import com.proyecto.task.entities.Task;
import com.proyecto.task.response.ApiResponse;
import com.proyecto.task.response.PagedResponse;
import com.proyecto.task.response.Pagination;
import com.proyecto.task.service.ITaskService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/task")
@RequiredArgsConstructor
public class TaskController {

//    @Autowired
//    private ITaskService taskService;

    private final ITaskService taskService;

//    public TaskController(ITaskService taskService){
//        this.taskService = taskService;
//    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody @Validated TaskDTO taskDTO ) throws URISyntaxException {

        TaskDTO task = taskService.saveTask(taskDTO);

        ApiResponse<TaskDTO> response = new ApiResponse<>(201, "Tarea Creada con Exito" , task);
        return ResponseEntity.created(new URI("/api/task/save/")).body(response);
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestParam(required = false) Long id, @RequestBody TaskDTO taskDTO ) throws URISyntaxException {

        TaskDTO task = taskService.updateTask(taskDTO, id);

        ApiResponse<TaskDTO> response = new ApiResponse<>(200, "Tarea Actualizada con Exito" , task);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/update-status")
    public ResponseEntity<?> actualizarEstado(@RequestParam Long id, @RequestBody StatusUpdateDTO dto) {
        try {
            TaskDTO estadoActualizado = taskService.updateStatus(dto.getStatus(), id );

            ApiResponse<TaskDTO> response = new ApiResponse<>(201, "Estado Actualizado con Extio" , estadoActualizado);
            return ResponseEntity.ok(response);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }

    @PostMapping("/bulk")
    public ResponseEntity<?> saveListTask(@RequestBody List<TaskDTO> listTaks ) throws URISyntaxException {

        taskService.saveListTask(listTaks);
        return ResponseEntity.created(new URI("/api/task/save/")).body("Tareas guardadas correctamente");
    }

    @DeleteMapping("/deleteById")
    public ResponseEntity<?> deleteById(@RequestParam(required = false) Long id){
        taskService.deleteTaskById(id);

        return ResponseEntity.ok("Registro Eliminado");
    }

    @GetMapping("/tasks")
    public ResponseEntity<PagedResponse<TaskDTO>> tasks(@RequestParam(defaultValue = "0") int page, // Número de página (por defecto 0)
                                                        @RequestParam(defaultValue = "5")int size,  // Tamaño de página (por defecto 5 elementos por página)
                                               HttpServletRequest request // Necesario para armar las URLs base en los enlaces
    ){
        // Creamos un objeto Pageable con la página y el tamaño
        Pageable pageable = PageRequest.of(page, size);
        // Obtenemos la página de tareas desde el servicio
        Page<TaskDTO> pageResult = taskService.tasks(pageable);

        // Creamos el objeto con los datos de paginación
        Pagination pagination = new Pagination(
                pageResult.getNumber(),         // Página actual
                pageResult.getSize(),           // Tamaño de la página
                pageResult.getTotalPages(),     // Total de páginas
                pageResult.getTotalElements(),  // Total de elementos
                pageResult.hasNext(),           // ¿Hay una página siguiente?
                pageResult.hasPrevious()        // ¿Hay una página anterior?
        );

        // Obtenemos la URL base del request para construir los enlaces de navegación
        String baseUrl = request.getRequestURL().toString();

        // Creamos un mapa con los enlaces tipo HATEOAS manuales (next, previous, first, last)
        Map<String, String> links = new LinkedHashMap<>();
        links.put("firstPage", baseUrl + "?page=0&size=" + size);
        links.put("lastPage", baseUrl + "?page=" + (pageResult.getTotalPages() - 1) + "&size=" + size);
        links.put("nextPage", pageResult.hasNext() ? baseUrl + "?page=" + (page + 1) + "&size=" + size : null);
        links.put("previousPage", pageResult.hasPrevious() ? baseUrl + "?page=" + (page - 1) + "&size=" + size : null);

        // Creamos la respuesta final con todos los datos
        PagedResponse<TaskDTO> response = new PagedResponse<>(
                200,                // Código HTTP
                "Listado de Tareas",           // Mensaje
                pageResult.getContent(),       // Con .getContent() obtenés solo la lista de elementos sin los metadatos que trae Page.
                pagination,                    // Info de paginación
                links                          // Enlaces de navegación
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/findById")
    public ResponseEntity<?> findById(@RequestParam(required = false) Long id){
        //verificamos si el id no es nulo o vacio
        if (id == null ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El ID proporcionado no puede ser nulo o vacio");
        }

        try {
            Optional<TaskDTO> task = taskService.findTaskById(id);

            ApiResponse<Optional<TaskDTO>> response = new ApiResponse<>(200, "Tarea Encontrada" , task);
            return ResponseEntity.ok(response);
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El ID proporcionado no es válido.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }

    }

    @GetMapping("/findAll")
    public ResponseEntity<?> findAll(){

        return ResponseEntity.ok(taskService.findAllTask());
    }

    @GetMapping("/findByStatus")
    public ResponseEntity<?> findByStatus(@RequestParam(defaultValue = "0") int page, // Número de página (por defecto 0)
                                          @RequestParam(defaultValue = "5") int size,  // Tamaño de página (por defecto 5 elementos por página)
                                          HttpServletRequest request, // Necesario para armar las URLs base en los enlaces
                                          @RequestParam(required = false) Task.Status status
    ){
        //verificamoso que el estado proporcionado no sea nulo o vacio
        if (status == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El Status proporcionado no puede ser nulo o vacio");
        }

        // Creamos un objeto Pageable con la página y el tamaño
        Pageable pageable = PageRequest.of(page, size);

        // Obtenemos la página de tareas desde el servicio
        Page<TaskDTO> pageResult = taskService.findByStatus(pageable, status);

        // Creamos el objeto con los datos de paginación
        Pagination pagination = new Pagination(
                pageResult.getNumber(),         // Página actual
                pageResult.getSize(),           // Tamaño de la página
                pageResult.getTotalPages(),     // Total de páginas
                pageResult.getTotalElements(),  // Total de elementos
                pageResult.hasNext(),           // ¿Hay una página siguiente?
                pageResult.hasPrevious()        // ¿Hay una página anterior?
        );

        // Obtenemos la URL base del request para construir los enlaces de navegación
        String baseUrl = request.getRequestURL().toString();

        // Creamos un mapa con los enlaces tipo HATEOAS manuales (next, previous, first, last)
        Map<String, String> links = new LinkedHashMap<>();
        links.put("firstPage", baseUrl + "?page=0&size=" + size + "&status=" + status);
        links.put("lastPage", baseUrl + "?page=" + (pageResult.getTotalPages() - 1) + "&size=" + size + "&status=" + status);
        links.put("nextPage", pageResult.hasNext() ? baseUrl + "?page=" + (page + 1) + "&size=" + size + "&status=" + status : null);
        links.put("previousPage", pageResult.hasPrevious() ? baseUrl + "?page=" + (page - 1) + "&size=" + size + "&status=" + status : null);

        // Creamos la respuesta final con todos los datos
        PagedResponse<TaskDTO> response = new PagedResponse<>(
                200,                // Código HTTP
                "Tareas encontradas con el estado: " + status,           // Mensaje
                pageResult.getContent(),       // Con .getContent() obtenés solo la lista de elementos sin los metadatos que trae Page.
                pagination,                    // Info de paginación
                links                          // Enlaces de navegación
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/vencidas")
    public ResponseEntity<?> findByExpirationDateBefore(@RequestParam(defaultValue = "0") int page, // Número de página (por defecto 0)
                                                        @RequestParam(defaultValue = "5") int size, // Tamaño de página (por defecto 5 elementos por página)
                                                        HttpServletRequest request
    ){

        // Creamos un objeto Pageable con la página y el tamaño
        Pageable pageable = PageRequest.of(page, size);

        // Obtenemos la página de tareas desde el servicio
        Page<TaskDTO> pageResult = taskService.findByExpirationDateBefore(pageable);

        // Creamos el objeto con los datos de paginación
        Pagination pagination = new Pagination(
                pageResult.getNumber(),         // Página actual
                pageResult.getSize(),           // Tamaño de la página
                pageResult.getTotalPages(),     // Total de páginas
                pageResult.getTotalElements(),  // Total de elementos
                pageResult.hasNext(),           // ¿Hay una página siguiente?
                pageResult.hasPrevious()        // ¿Hay una página anterior?
        );

        // Obtenemos la URL base del request para construir los enlaces de navegación
        String baseUrl = request.getRequestURL().toString();

        // Creamos un mapa con los enlaces tipo HATEOAS manuales (next, previous, first, last)
        Map<String, String> links = new LinkedHashMap<>();
        links.put("firstPage", baseUrl + "?page=0&size=" + size );
        links.put("lastPage", baseUrl + "?page=" + (pageResult.getTotalPages() - 1) + "&size=" + size );
        links.put("nextPage", pageResult.hasNext() ? baseUrl + "?page=" + (page + 1) + "&size=" + size : null);
        links.put("previousPage", pageResult.hasPrevious() ? baseUrl + "?page=" + (page - 1) + "&size=" + size  : null);

        // Creamos la respuesta final con todos los datos
        PagedResponse<TaskDTO> response = new PagedResponse<>(
                200,                // Código HTTP
                "Listado de Tareas Vencidas.",           // Mensaje
                pageResult.getContent(),       // Con .getContent() obtenés solo la lista de elementos sin los metadatos que trae Page.
                pagination,                    // Info de paginación
                links                          // Enlaces de navegación
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/vencen-hoy")
    public ResponseEntity<?> findByExpirationDate(@RequestParam(defaultValue = "0") int page, // Número de página (por defecto 0)
                                                  @RequestParam(defaultValue = "5") int size, // Tamaño de página (por defecto 5 elementos por página)
                                                  HttpServletRequest request
    ){

        // Creamos un objeto Pageable con la página y el tamaño
        Pageable pageable = PageRequest.of(page, size);

        // Obtenemos la página de tareas desde el servicio
        Page<TaskDTO> pageResult = taskService.findByExpirationDate(pageable);

        // Creamos el objeto con los datos de paginación
        Pagination pagination = new Pagination(
                pageResult.getNumber(),         // Página actual
                pageResult.getSize(),           // Tamaño de la página
                pageResult.getTotalPages(),     // Total de páginas
                pageResult.getTotalElements(),  // Total de elementos
                pageResult.hasNext(),           // ¿Hay una página siguiente?
                pageResult.hasPrevious()        // ¿Hay una página anterior?
        );

        // Obtenemos la URL base del request para construir los enlaces de navegación
        String baseUrl = request.getRequestURL().toString();

        // Creamos un mapa con los enlaces tipo HATEOAS manuales (next, previous, first, last)
        Map<String, String> links = new LinkedHashMap<>();
        links.put("firstPage", baseUrl + "?page=0&size=" + size );
        links.put("lastPage", baseUrl + "?page=" + (pageResult.getTotalPages() - 1) + "&size=" + size );
        links.put("nextPage", pageResult.hasNext() ? baseUrl + "?page=" + (page + 1) + "&size=" + size : null);
        links.put("previousPage", pageResult.hasPrevious() ? baseUrl + "?page=" + (page - 1) + "&size=" + size  : null);

        // Creamos la respuesta final con todos los datos
        PagedResponse<TaskDTO> response = new PagedResponse<>(
                200,                // Código HTTP
                "Listado de Tareas que Vencen Hoy.",           // Mensaje
                pageResult.getContent(),       // Con .getContent() obtenés solo la lista de elementos sin los metadatos que trae Page.
                pagination,                    // Info de paginación
                links                          // Enlaces de navegación
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/por-fecha")
    public ResponseEntity<?>  findByExpirationDateBetween(@RequestParam(defaultValue = "0") int page, // Número de página (por defecto 0)
                                                          @RequestParam(defaultValue = "5") int size, // Tamaño de página (por defecto 5 elementos por página)
                                                          @RequestParam(required = false)LocalDate desde,
                                                          @RequestParam(required = false)LocalDate hasta,
                                                          HttpServletRequest request){

        // Creamos un objeto Pageable con la página y el tamaño
        Pageable pageable = PageRequest.of(page, size);

        // Obtenemos la página de tareas desde el servicio
        Page<TaskDTO> pageResult = taskService.findByExpirationDateBetween(pageable, desde, hasta);

        // Creamos el objeto con los datos de paginación
        Pagination pagination = new Pagination(
                pageResult.getNumber(),         // Página actual
                pageResult.getSize(),           // Tamaño de la página
                pageResult.getTotalPages(),     // Total de páginas
                pageResult.getTotalElements(),  // Total de elementos
                pageResult.hasNext(),           // ¿Hay una página siguiente?
                pageResult.hasPrevious()        // ¿Hay una página anterior?
        );

        // Obtenemos la URL base del request para construir los enlaces de navegación
        String baseUrl = request.getRequestURL().toString();

        // Creamos un mapa con los enlaces tipo HATEOAS manuales (next, previous, first, last)
        Map<String, String> links = new LinkedHashMap<>();
        links.put("firstPage", baseUrl + "?page=0&size=" + size + "&desde=" + desde + "&hasta=" + hasta);
        links.put("lastPage", baseUrl + "?page=" + (pageResult.getTotalPages() - 1) + "&size=" + size + "&desde=" + desde + "&hasta=" + hasta );
        links.put("nextPage", pageResult.hasNext() ? baseUrl + "?page=" + (page + 1) + "&size=" + size + "&desde=" + desde + "&hasta=" + hasta : null);
        links.put("previousPage", pageResult.hasPrevious() ? baseUrl + "?page=" + (page - 1) + "&size=" + size + "&desde=" + desde + "&hasta=" + hasta  : null);

        // Creamos la respuesta final con todos los datos
        PagedResponse<TaskDTO> response = new PagedResponse<>(
                200,                // Código HTTP
                "Listado de Tareas desde " + desde + " hasta " + hasta,           // Mensaje
                pageResult.getContent(),       // Con .getContent() obtenés solo la lista de elementos sin los metadatos que trae Page.
                pagination,                    // Info de paginación
                links                          // Enlaces de navegación
        );

        return ResponseEntity.ok(response);
    }
    @GetMapping("/buscar")
    public ResponseEntity<?> findByTitleContaining(@RequestParam(defaultValue = "0") int page, // Número de página (por defecto 0)
                                                   @RequestParam(defaultValue = "5") int size, // Tamaño de página (por defecto 5 elementos por página)
                                                   @RequestParam(required = false) String texto,
                                                   HttpServletRequest request
    ){
        // Creamos un objeto Pageable con la página y el tamaño
        Pageable pageable = PageRequest.of(page, size);

        // Obtenemos la página de tareas desde el servicio
        Page<TaskDTO> pageResult = taskService.findByTitleContaining(pageable, texto);

        // Creamos el objeto con los datos de paginación
        Pagination pagination = new Pagination(
                pageResult.getNumber(),         // Página actual
                pageResult.getSize(),           // Tamaño de la página
                pageResult.getTotalPages(),     // Total de páginas
                pageResult.getTotalElements(),  // Total de elementos
                pageResult.hasNext(),           // ¿Hay una página siguiente?
                pageResult.hasPrevious()        // ¿Hay una página anterior?
        );

        // Obtenemos la URL base del request para construir los enlaces de navegación
        String baseUrl = request.getRequestURL().toString();

        // Creamos un mapa con los enlaces tipo HATEOAS manuales (next, previous, first, last)
        Map<String, String> links = new LinkedHashMap<>();
        links.put("firstPage", baseUrl + "?page=0&size=" + size + "&texto=" + texto);
        links.put("lastPage", baseUrl + "?page=" + (pageResult.getTotalPages() - 1) + "&size=" + size + "&texto=" + texto );
        links.put("nextPage", pageResult.hasNext() ? baseUrl + "?page=" + (page + 1) + "&size=" + size + "&texto=" + texto : null);
        links.put("previousPage", pageResult.hasPrevious() ? baseUrl + "?page=" + (page - 1) + "&size=" + size + "&texto=" + texto  : null);

        // Creamos la respuesta final con todos los datos
        PagedResponse<TaskDTO> response = new PagedResponse<>(
                200,                // Código HTTP
                "Listado de Tareas Encontradas.",           // Mensaje
                pageResult.getContent(),       // Con .getContent() obtenés solo la lista de elementos sin los metadatos que trae Page.
                pagination,                    // Info de paginación
                links                          // Enlaces de navegación
        );

        return ResponseEntity.ok(response);
    }
}