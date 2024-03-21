package com.hrs.booking.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.hrs.booking.service.BaseService.getCurrentUser;

//This BaseEntity class extends the LazyAuditable class and adds additional fields and auditing behavior
@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity extends LazyAuditable<HRSUser, Long> {

    //    Represents a unique identifier for the entity
    private String uuid;

    //    executed before the entity is persisted to the database
    @PrePersist
    private void preCreate() {
        this.setUuid(UUID.randomUUID().toString());
        this.setCreatedDate(LocalDateTime.now());

        HRSUser current = getCurrentUser();
        if (current != null) {
            this.setCreatedBy(current);
        }
    }

    public String getUuid() {
        return uuid.toString();
    }

    //    executed before the entity is updated in the database
    @PreUpdate
    private void preUpdate() {
        this.setLastModifiedDate(LocalDateTime.now());
        HRSUser current = getCurrentUser();
        if (current != null) {
            this.setLastModifiedBy(current);
        }

    }

    public void setId(Long Id) {
        super.setId(Id);
    }
}

