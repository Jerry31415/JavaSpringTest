package com.microservice.shop.services;

import com.microservice.shop.models.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public class CommonService<ModelType extends Model, RepoType extends CrudRepository<ModelType, Long>> {
    @Autowired
    protected RepoType repo;

    /**
     * @return Возвращает список всех объектов типа ModelType из репозитория RepoType
     */
    public List<ModelType> getAll() {
        List<ModelType> objects = null;
        if(repo.count()>0) {
            objects = (List<ModelType>) repo.findAll();
        }
        return objects;
    }

    /**
     * @return Возвращает число строк таблицы
     */
    public Long count() {
        return repo.count();
    }

    /**
     * <p>Создает новую запись в таблице</p>
     * @param object - объект записываемый в таблицу
     * @return Возвращает сохраненный объект
     */
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

    /**
     * @param id - идентификатор объекта в таблице
     * @return Возвращет объект с идентификатором id, если он существует в таблице, иначе null
     */
    public ModelType get(Long id){
        return (repo.existsById(id))?repo.findById(id).get():null;
    }

    /**
     *  <p>Обновляет объект в таблице</p>
     * @param object - обновляемый объект
     * @return Возвращет объект с обновленными полями, если он существует в таблице, иначе null
     */
    public synchronized ModelType update(ModelType object){
        if(repo.existsById(object.getId())){
            return repo.save(object);
        }
        return null;
    }
}

