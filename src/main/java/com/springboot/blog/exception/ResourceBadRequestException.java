package com.springboot.blog.exception;

public class ResourceBadRequestException extends RuntimeException{

    private int pageNumber;
    private int pageSize;

    public ResourceBadRequestException(int pageNumber, int pageSize) {
        super(String.format("there is no data in the page %s with %s size",pageNumber,pageSize));
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

}
