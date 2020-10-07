package com.cricket.wsipl2020.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
@Getter @Setter
public class Allocations {

    @EmbeddedId
    AllocationsPK allocationsPK;
    Float points;

}
