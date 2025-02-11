package com.timni.springbootwithauth.services.base;

import com.timni.springbootwithauth.entities.base.BaseDocument;
import com.timni.springbootwithauth.exceptions.types.ResourceNotFoundException;
import com.timni.springbootwithauth.listeners.EntityTransactionLogListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
public abstract class BaseService<E extends BaseDocument<ID>, ID extends Serializable> {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public abstract MongoRepository<E, ID> getRepository();

    @Transactional(readOnly = true)
    public E findById(final ID id) {
        log.debug("[retrieving] {} {}", this.getEntityName(), id);
        return this.getRepository().findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public Optional<E> findByIdOptional(final ID id) {
        log.debug("[retrieving] {} {}", this.getEntityName(), id);
        return this.getRepository().findById(id);
    }

    @Transactional(readOnly = true)
    public Page<E> findAll(final Pageable pageable) {
        log.debug("[retrieving] all {}", this.getEntityName());
        return this.getRepository().findAll(pageable);
    }

    @Transactional
    public E create(final E entity) {
        log.info("[creating] {} {}", this.getEntityName(), entity);
        if (entity.getId() != null) {
            throw new IllegalStateException("You are trying to create an entity that already exist.");
        }
        entity.setCreatedBy("system");
        entity.setCreatedAt(LocalDateTime.now());
        this.getRepository().save(entity);
        this.publishTxCreateLog(entity.getId());
        return entity;
    }

    @Transactional
    public E update(final E entity) {
        log.info("[updating] {} {}", this.getEntityName(), entity);
        if (entity.getId() == null) {
            throw new IllegalStateException("You are trying to update an entity that does not exist.");
        }
        entity.setUpdatedBy("system");
        entity.setUpdatedAt(LocalDateTime.now());
        this.getRepository().save(entity);
        this.publishTxUpdateLog(entity.getId());
        return entity;
    }

    @Transactional
    public void delete(final ID id) {
        log.info("[deleting] {} {}", this.getEntityName(), id);
        E entity =
                this.getRepository().findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
        this.getRepository().delete(entity);
        this.publishTxDeleteLog(id);
    }

    protected void publishTxLogEvent(EntityTransactionLogListener.TransactionEvent.TransactionType type, ID entityId) {
        this.applicationEventPublisher.publishEvent(
                new EntityTransactionLogListener.TransactionEvent(type, this.getEntityName(), entityId.toString()));
    }

    protected void publishTxCreateLog(ID entityId) {
        this.publishTxLogEvent(EntityTransactionLogListener.TransactionEvent.TransactionType.CREATE, entityId);
    }

    protected void publishTxUpdateLog(ID entityId) {
        this.publishTxLogEvent(EntityTransactionLogListener.TransactionEvent.TransactionType.UPDATE, entityId);
    }

    protected void publishTxDeleteLog(ID entityId) {
        this.publishTxLogEvent(EntityTransactionLogListener.TransactionEvent.TransactionType.DELETE, entityId);
    }

    protected String getEntityName() {
        final Class<E> entityModelClass =
                (Class<E>)
                        ((ParameterizedType) this.getClass().getGenericSuperclass())
                                .getActualTypeArguments()[0];
        return entityModelClass.getSimpleName();
    }
}
