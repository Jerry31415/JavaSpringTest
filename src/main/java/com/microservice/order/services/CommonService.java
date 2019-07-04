package com.microservice.order.services;

import com.microservice.order.models.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public class CommonService<ModelType extends Model, RepoType extends CrudRepository<ModelType, Long>> {
    @Autowired
    protected RepoType repo;

    public List<ModelType> getAll() {
        List<ModelType> objects = null;
        if(repo.count()>0) {
            objects = (List<ModelType>) repo.findAll();
        }
        return objects;
    }

    public Long count() {
        return repo.count();
    }

    public synchronized ModelType create(ModelType object){
        return repo.save(object);
    }

    public synchronized boolean remove(Long id){
        if(repo.existsById(id)){
            repo.deleteById(id);
            return true;
        }
        return false;
    }

    public ModelType get(Long id){
        return (repo.existsById(id))?repo.findById(id).get():null;
    }

    public synchronized ModelType update(ModelType object){
        if(repo.existsById(object.getId())){
            return repo.save(object);
        }
        return null;
    }
}

