package com.hrs.booking.entity;

import org.springframework.data.domain.Auditable;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

//This code defines a LazyAuditable abstract class annotated with @MappedSuperclass,
// This class serves as a base class for entities that require auditing functionality
//AbstractPersistable is a Spring Data JPA class that provides default implementation for entity identifiers
//Auditable is a custom interface providing auditing functionality.
@MappedSuperclass
public abstract class LazyAuditable<U, PK extends Serializable> extends AbstractPersistable<PK> implements
        Auditable<U, PK, LocalDateTime> {

    //    value of the Authenticated User who calls API to create the entity.
    @ManyToOne(fetch = FetchType.LAZY)
    private @Nullable
    U createdBy;

    //    value of the Date at the entity was created.
    @Temporal(TemporalType.TIMESTAMP)
    private @Nullable
    Date createdDate;

    //    value of the Authenticated User who calls API to update the entity.
    @ManyToOne(fetch = FetchType.LAZY)
    private @Nullable
    U lastModifiedBy;

    //    value of the Date at the entity was updated.
    @Temporal(TemporalType.TIMESTAMP)
    private @Nullable
    Date lastModifiedDate;

    @Override
    public Optional<U> getCreatedBy() {
        return Optional.ofNullable(createdBy);
    }

    @Override
    public void setCreatedBy(U createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public Optional<LocalDateTime> getCreatedDate() {
        return null == createdDate ? Optional.empty()
                : Optional.of(LocalDateTime.ofInstant(createdDate.toInstant(), ZoneId.systemDefault()));
    }

    @Override
    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = Date.from(createdDate.atZone(ZoneId.systemDefault()).toInstant());
    }

    @Override
    public Optional<U> getLastModifiedBy() {
        return Optional.ofNullable(lastModifiedBy);
    }

    @Override
    public void setLastModifiedBy(U lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    @Override
    public Optional<LocalDateTime> getLastModifiedDate() {
        return null == lastModifiedDate ? Optional.empty()
                : Optional.of(LocalDateTime.ofInstant(lastModifiedDate.toInstant(), ZoneId.systemDefault()));
    }

    @Override
    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = Date.from(lastModifiedDate.atZone(ZoneId.systemDefault()).toInstant());
    }
}