package com.proyecto.task.repository;

import com.proyecto.task.entities.Task;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Esta clase es la implementación de la interfaz personalizada TaskRepositoryCustom
@Repository // Marcamos esta clase como componente de repositorio para que Spring la detecte
public class TaskRepositoryImpl implements TaskRepositoryCustom {

    @PersistenceContext // Inyectamos el EntityManager para trabajar con la Criteria API
    private EntityManager entityManager;

    // Implementación del método para buscar tareas con filtros dinámicos
    @Override
    public Page<Task> buscarTareasConFiltros(
            String estado,
            String palabraClave,
            LocalDate fechaInicio,
            LocalDate fechaFin,
            boolean vencidas,
            boolean vencenHoy,
            Pageable pageable) {

        // ============================
        // CONSULTA PRINCIPAL (traer datos)
        // ============================

        CriteriaBuilder cb = entityManager.getCriteriaBuilder(); // Obtenemos el constructor de criterios
        CriteriaQuery<Task> query = cb.createQuery(Task.class); // Creamos una consulta que devuelve objetos Task
        Root<Task> task = query.from(Task.class); // Definimos la entidad base de la consulta (FROM Task)
        List<Predicate> predicates = new ArrayList<>(); // Lista que acumulará los filtros dinámicos (WHERE)

        // ============================
        // APLICAMOS FILTROS DINÁMICOS (solo si fueron enviados)
        // ============================

        if (estado != null && !estado.isBlank()) {
            // Convertimos el String recibido a Enum y comparamos con el atributo status
            predicates.add(cb.equal(task.get("status"), Task.Status.valueOf(estado)));
        }

        if (palabraClave != null && !palabraClave.isBlank()) {
            // Convertimos a minúsculas para hacer búsqueda insensible a mayúsculas
            Predicate tituloLike = cb.like(cb.lower(task.get("title")), "%" + palabraClave.toLowerCase() + "%");
            Predicate descripcionLike = cb.like(cb.lower(task.get("description")), "%" + palabraClave.toLowerCase() + "%");

            // Buscamos que se cumpla uno u otro (título o descripción)
            predicates.add(cb.or(tituloLike, descripcionLike));
        }

        // Rango de fechas: si se manda fechaInicio y fechaFin, usamos BETWEEN
        if (fechaInicio != null && fechaFin != null) {
            predicates.add(cb.between(task.get("expirationDate"), fechaInicio, fechaFin));
        } else if (fechaInicio != null) {
            predicates.add(cb.greaterThanOrEqualTo(task.get("expirationDate"), fechaInicio));
        } else if (fechaFin != null) {
            predicates.add(cb.lessThanOrEqualTo(task.get("expirationDate"), fechaFin));
        }

        // Válida si se envían ambos flags como true: es un conflicto
        if (vencidas && vencenHoy) {
            throw new IllegalArgumentException("No puede filtrar tareas vencidas y que vencen hoy al mismo tiempo.");
        } else if (vencidas) {
            // Vencidas: fecha de vencimiento anterior a hoy
            predicates.add(cb.lessThan(task.get("expirationDate"), LocalDate.now()));
        } else if (vencenHoy) {
            // Vencen hoy: fecha de vencimiento igual a hoy
            predicates.add(cb.equal(task.get("expirationDate"), LocalDate.now()));
        }

        // Aplicamos todos los filtros acumulados con AND
        query.where(cb.and(predicates.toArray(new Predicate[0])));

        // Ordenamos los resultados por fecha de vencimiento descendente
        query.orderBy(cb.desc(task.get("expirationDate")));

        // ============================
        // EJECUTAMOS LA CONSULTA PRINCIPAL CON PAGINACIÓN
        // ============================

        TypedQuery<Task> typedQuery = entityManager.createQuery(query); // Creamos la consulta final

        // Configuramos el offset (desde qué fila iniciar) y el máximo de resultados
        typedQuery.setFirstResult((int) pageable.getOffset()); // Ej: página 2 con size 10 → offset = 20
        typedQuery.setMaxResults(pageable.getPageSize()); // Cantidad de resultados por página

        // Ejecutamos la consulta: obtenemos la lista de tareas ya paginada
        List<Task> resultList = typedQuery.getResultList();

        // ============================
        // CONSULTA DE CONTEO (para saber cuántos registros en total hay con los filtros aplicados)
        // ============================

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class); // Creamos consulta de tipo Long (conteo)
        Root<Task> countRoot = countQuery.from(Task.class); // Nueva raíz, no se puede usar la anterior
        List<Predicate> countPredicates = new ArrayList<>(); // Lista de filtros (de nuevo, pero para count)

        // Volvemos a aplicar los mismos filtros que usamos antes, pero ahora sobre countRoot
        if (estado != null && !estado.isBlank()) {
            countPredicates.add(cb.equal(countRoot.get("status"), Task.Status.valueOf(estado)));
        }

        if (palabraClave != null && !palabraClave.isBlank()) {
            Predicate tituloLike = cb.like(cb.lower(countRoot.get("title")), "%" + palabraClave.toLowerCase() + "%");
            Predicate descripcionLike = cb.like(cb.lower(countRoot.get("description")), "%" + palabraClave.toLowerCase() + "%");
            countPredicates.add(cb.or(tituloLike, descripcionLike));
        }

        if (fechaInicio != null && fechaFin != null) {
            countPredicates.add(cb.between(countRoot.get("expirationDate"), fechaInicio, fechaFin));
        } else if (fechaInicio != null) {
            countPredicates.add(cb.greaterThanOrEqualTo(countRoot.get("expirationDate"), fechaInicio));
        } else if (fechaFin != null) {
            countPredicates.add(cb.lessThanOrEqualTo(countRoot.get("expirationDate"), fechaFin));
        }

        if (vencidas && vencenHoy) {
            throw new IllegalArgumentException("No puede filtrar tareas vencidas y que vencen hoy al mismo tiempo.");
        } else if (vencidas) {
            countPredicates.add(cb.lessThan(countRoot.get("expirationDate"), LocalDate.now()));
        } else if (vencenHoy) {
            countPredicates.add(cb.equal(countRoot.get("expirationDate"), LocalDate.now()));
        }

        // Definimos la operación de conteo con todos los filtros aplicados
        countQuery.select(cb.count(countRoot)).where(cb.and(countPredicates.toArray(new Predicate[0])));

        // Ejecutamos la consulta y obtenemos el total de resultados
        Long total = entityManager.createQuery(countQuery).getSingleResult();

        // ============================
        // RETORNAMOS LA PÁGINA RESULTANTE
        // ============================

        // Devolvemos un PageImpl con la lista de tareas, la paginación original, y el total
        return new PageImpl<>(resultList, pageable, total);
    }
}
