package com.proyecto.task.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pagination {

    private int currentPage;
    private int pageSize;
    private int totalPages;
    private long totalElements;
    private boolean hasNext;
    private boolean hasPrevious;

//    public Pagination() {
//    }
//
//    public Pagination(boolean hasPrevious, boolean hasNext, long totalElements, int totalPages, int pageSize, int currentPage) {
//        this.hasPrevious = hasPrevious;
//        this.hasNext = hasNext;
//        this.totalElements = totalElements;
//        this.totalPages = totalPages;
//        this.pageSize = pageSize;
//        this.currentPage = currentPage;
//    }
//
//    public int getTotalPages() {
//        return totalPages;
//    }
//
//    public void setTotalPages(int totalPages) {
//        this.totalPages = totalPages;
//    }
//
//    public long getTotalElements() {
//        return totalElements;
//    }
//
//    public void setTotalElements(long totalElements) {
//        this.totalElements = totalElements;
//    }
//
//    public int getPageSize() {
//        return pageSize;
//    }
//
//    public void setPageSize(int pageSize) {
//        this.pageSize = pageSize;
//    }
//
//    public int getCurrentPage() {
//        return currentPage;
//    }
//
//    public void setCurrentPage(int currentPage) {
//        this.currentPage = currentPage;
//    }
//
//    public boolean isHasPrevious() {
//        return hasPrevious;
//    }
//
//    public void setHasPrevious(boolean hasPrevious) {
//        this.hasPrevious = hasPrevious;
//    }
//
//    public boolean isHasNext() {
//        return hasNext;
//    }
//
//    public void setHasNext(boolean hasNext) {
//        this.hasNext = hasNext;
//    }
}
