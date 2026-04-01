package com.company.qrcode.service;
import com.company.qrcode.entity.TextTemplate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.security.app.UserSettingService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service(ConstructionService.NAME)
public class ConstructionServiceBean implements ConstructionService {
    @Inject
    private UserSettingService userSettingService;
    @Inject
    private DataManager dataManager;
    @Override
    @Cacheable(value = "constructions-cache")
    public List<TextTemplate> getActiveConstructions(){
        ObjectMapper mapper = new ObjectMapper();
        List<UUID> ids;
        try{
            String first = userSettingService.loadSetting("selectedTemplates");
            if(first!=null){
                ids = mapper.readValue(first, new TypeReference<List<UUID>>(){});
            }
            else{
                ids = null;
            }
        }
        catch (JsonProcessingException e){
            throw new RuntimeException("Ошибка чтения JSON с текстовыми шаблонами", e);
        }
        return dataManager
                .load(TextTemplate.class)
                .query("SELECT e FROM qrcode$TextTemplate e WHERE e.category.active=true AND e.id IN :selectedIds")
                .parameter("selectedIds",ids)
                .view("textTemplate-view").list();
    }
}