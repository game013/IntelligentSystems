/**
 * COPYRIGHT (C) 2015. All Rights Reserved.
 */
package com.game013.codeanalyzer.repository;

import org.springframework.data.repository.CrudRepository;

import com.game013.codeanalyzer.domain.Coder;

/**
 * @author Oscar Garavito
 *
 */
public interface CoderRepository extends CrudRepository<Coder, String> {

}
