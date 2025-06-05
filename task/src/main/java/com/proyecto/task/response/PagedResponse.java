package com.proyecto.task.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagedResponse<T> {

    private int statusCode;
    private String message;
    private List<T> data;
    private Pagination pagination;
    private Map<String, String> links;

//    public PagedResponse() {
//    }
//
//    public PagedResponse(int statusCode, String message, List<T> data, Pagination pagination, Map<String, String> links) {
//        this.statusCode = statusCode;
//        this.message = message;
//        this.data = data;
//        this.pagination = pagination;
//        this.links = links;
//    }
//
//    public int getStatusCode() {
//        return statusCode;
//    }
//
//    public void setStatusCode(int statusCode) {
//        this.statusCode = statusCode;
//    }
//
//    public Pagination getPagination() {
//        return pagination;
//    }
//
//    public void setPagination(Pagination pagination) {
//        this.pagination = pagination;
//    }
//
//    public List<T> getData() {
//        return data;
//    }
//
//    public void setData(List<T> data) {
//        this.data = data;
//    }
//
//    public String getMessage() {
//        return message;
//    }
//
//    public void setMessage(String message) {
//        this.message = message;
//    }
//
//    public Map<String, String> getLinks() {
//        return links;
//    }
//
//    public void setLinks(Map<String, String> links) {
//        this.links = links;
//    }
}
