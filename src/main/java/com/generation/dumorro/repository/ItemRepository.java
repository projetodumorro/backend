package com.generation.dumorro.repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.generation.dumorro.model.*;


public interface ItemRepository extends JpaRepository<Item, Long> {
	
	 public List<Item> findAllByNomeContainingIgnoreCase(@Param("Nome") String Nome);
	 

}
