package com.pccube.CRUDTest.entities;

import java.util.List;


import org.springframework.data.repository.CrudRepository;


public interface TaskRepository extends CrudRepository<Task, Long> {

}
