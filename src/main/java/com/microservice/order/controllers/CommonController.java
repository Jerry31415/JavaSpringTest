package com.microservice.order.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.order.models.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import com.microservice.order.services.CommonService;

import java.util.List;

public class CommonController<ModelType extends Model,
                              RepoType extends CrudRepository<ModelType, Long>,
                              ServiceType extends CommonService<ModelType, RepoType>>{

    @Autowired
    protected ServiceType service;

    public String getAllObjects(){
        List<ModelType> obj_list = service.getAll();
        if(obj_list!=null){
            try {
                StringBuilder builder = new StringBuilder();
                for (ModelType obj : obj_list){
                    builder.append(new ObjectMapper().writeValueAsString(obj) + "\n");
                }
                return builder.toString();
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public String count(){
        return service.count().toString();
    }

    public String getObjectJSON(Long id){
        ModelType obj = service.get(id);
        if(obj==null){
            return "Error: element with id=" + id + " is not exist";
        }
        try {
            StringBuilder builder = new StringBuilder();
            builder.append(new ObjectMapper().writeValueAsString(obj));
            return builder.toString();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return e.toString();
        }
    }
}