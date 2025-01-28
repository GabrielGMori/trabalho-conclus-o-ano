package com.tca.model;

import java.time.LocalDateTime;

public class CheckIn {
    private Integer id;
    private LocalDateTime data;
    
    public CheckIn(LocalDateTime data) {
        this.data = data;
    }

    public CheckIn(Integer id, LocalDateTime data) {
        this(data);
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }  

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }
}
