package com.tca.model;

import java.time.LocalDate;

public class CheckIn {
    private Integer id;
    private LocalDate data;
    
    public CheckIn(LocalDate data) {
        this.data = data;
    }

    public CheckIn(Integer id, LocalDate data) {
        this(data);
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }  

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }
}
