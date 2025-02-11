package com.timni.springbootwithauth.controllers.base;

import com.timni.springbootwithauth.entities.base.BaseDocument;
import com.timni.springbootwithauth.mappers.base.BaseMapper;
import com.timni.springbootwithauth.responses.base.ApiListPaginationSuccess;
import com.timni.springbootwithauth.responses.base.ApiResponse;
import com.timni.springbootwithauth.services.base.BaseService;
import com.timni.springbootwithauth.utils.ResponseBuilder;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

@Slf4j
public abstract class BaseController<E extends BaseDocument<I>, I extends Serializable, C, U, P, R> {

    public abstract BaseMapper<E, C, U, P, R> getMapper();

    public abstract BaseService<E, I> getService();

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<R>> findById(@PathVariable("id") final I id) {
        log.debug("[request] retrieve {} with id {}", this.getName(), id);
        final E entity = this.getService().findById(id);
        final R response = this.getMapper().toResponse(entity);
        ApiResponse<R> apiResponse = new ApiResponse<>("Success", HttpStatus.OK.value(), response);
        return ResponseEntity.ok(apiResponse);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public ResponseEntity<ApiResponse<ApiListPaginationSuccess<R>>> findAll(final Pageable pageable) {
        log.debug("[request] retrieve all {}", this.getName());

        final Page<E> entities = this.getService().findAll(pageable);
        final Page<R> response = entities.map(this.getMapper()::toResponse);
        ApiListPaginationSuccess<R> paginationSuccess = ApiListPaginationSuccess.of(response);
        ApiResponse<ApiListPaginationSuccess<R>> apiResponse =
                new ApiResponse<>("Success", HttpStatus.OK.value(), paginationSuccess);
        return ResponseEntity.ok(apiResponse);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<R>> create(@Valid @RequestBody final C request) {
        log.info("[request] create {}", request);
        final E entity = this.getService().create(this.getMapper().toEntity(request));
        final R response = this.getMapper().toResponse(entity);
        ApiResponse<R> apiResponse = new ApiResponse<>("Created", HttpStatus.CREATED.value(), response);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<R>> update(@PathVariable("id") final I id, @Valid @RequestBody final U request) {
        log.info("[request] update {} {}", id, request);
        final E original = this.getService().findById(id);
        final E merged = this.getMapper().updateWithRequest(request, original);
        final E entity = this.getService().update(merged);
        final R response = this.getMapper().toResponse(entity);
        ApiResponse<R> apiResponse = new ApiResponse<>("Updated", HttpStatus.OK.value(), response);
        return ResponseEntity.ok(apiResponse);
    }

    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<R>> patch(@PathVariable("id") final I id, @Valid @RequestBody final P request) {
        log.info("[request] patch {} {}", id, request);
        final E original = this.getService().findById(id);
        final E patched = this.getMapper().patchWithRequest(request, original);
        final E entity = this.getService().update(patched);
        final R response = this.getMapper().toResponse(entity);
        ApiResponse<R> apiResponse = new ApiResponse<>("Patched", HttpStatus.OK.value(), response);
        return ResponseEntity.ok(apiResponse);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable("id") final I id) {
        log.info("[request] delete {} {}", this.getName(), id);
        this.getService().delete(id);
        return ResponseBuilder.noContent("deleted.successfully");
    }

    protected String getName() {
        final Class<E> entityModelClass =
                (Class<E>)
                        ((ParameterizedType) this.getClass().getGenericSuperclass())
                                .getActualTypeArguments()[0];
        return entityModelClass.getSimpleName();
    }
}
