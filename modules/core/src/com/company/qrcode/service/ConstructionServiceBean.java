package com.company.qrcode.service;
import com.company.qrcode.entity.TextTemplate;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.UserSessionSource;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service(ConstructionService.NAME)
public class ConstructionServiceBean implements ConstructionService {
    @Inject
    private UserSessionSource userSessionSource;
    @Inject
    private DataManager dataManager;

    @Override
    @Cacheable(value = "constructions-cache")
    public List<TextTemplate> getActiveConstructions(){
        List<UUID> ids = userSessionSource.getUserSession().getAttribute("selectedTemplates");
        return dataManager
                .load(TextTemplate.class)
                .query("SELECT e FROM qrcode$TextTemplate e WHERE e.active=true AND e.id IN :selectedIds")
                .parameter("selectedIds",ids)
                .view("textTemplate-view").list();
    }

    @Override
    @CacheEvict(value = "constructions-cache", allEntries = true)
    public void updateCache(){
    }

    @Override
    public List<TextTemplate> filterTemplates(String userInput){
        if(userInput==null || userInput.isEmpty()){
            Collections.emptyList();
        }
        String lowerQuery = userInput.toLowerCase();

        return getActiveConstructions().stream()
                .filter(t->(t.getCode()!=null&&t.getCode().toLowerCase().contains(lowerQuery))||
                        (t.getContent()!=null&&t.getContent().toLowerCase().contains(lowerQuery)))
                .limit(20)
                .collect(Collectors.toList());
    }
}