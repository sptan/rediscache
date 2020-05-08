package com.sptan.exec.rediscache.base;

import java.util.List;

/**
 * The interface Base mapper.
 *
 * @param <D> the type parameter
 * @param <E> the type parameter
 * @author lp
 * @date 2018 -11-23
 */
public interface BaseMapper<D, E> {

    /**
     * DTO转Entity
     *
     * @param dto the dto
     * @return the e
     */
    E toEntity(D dto);

    /**
     * Entity转DTO
     *
     * @param entity the entity
     * @return the d
     */
    D toDto(E entity);

    /**
     * DTO集合转Entity集合
     *
     * @param dtoList the dto list
     * @return the list
     */
    List<E> toEntity(List<D> dtoList);

    /**
     * Entity集合转DTO集合
     *
     * @param entityList the entity list
     * @return the list
     */
    List<D> toDto(List<E> entityList);
}
