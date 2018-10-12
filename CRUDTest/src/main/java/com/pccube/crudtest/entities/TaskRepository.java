package com.pccube.crudtest.entities;


import org.springframework.data.repository.CrudRepository;


public interface TaskRepository extends CrudRepository<Task, Long> {


	
}
