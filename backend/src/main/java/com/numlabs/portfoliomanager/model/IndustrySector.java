package com.numlabs.portfoliomanager.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity(name = "industry_sector")
public class IndustrySector {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private Long id;

    @Column
    private String name;

    @Column
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", referencedColumnName = "ID")
    private IndustrySector parent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public IndustrySector getParent() {
        return parent;
    }

    public void setParent(IndustrySector parent) {
        this.parent = parent;
    }
}
