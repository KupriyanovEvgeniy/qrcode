/*
 * Copyright (c) 2026 LTD Haulmont Samara. All Rights Reserved.
 * Haulmont Samara proprietary and confidential.
 * Use is subject to license terms.
 */

package com.company.qrcode.web.ui;

import com.company.qrcode.entity.TextTemplate;
import com.vaadin.annotations.JavaScript;
import com.vaadin.annotations.StyleSheet;
import com.vaadin.server.AbstractJavaScriptExtension;
import com.vaadin.ui.AbstractComponent;

import java.util.List;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.Map;

//@JavaScript({"vaadin://tribute/tribute.js", "vaadin://tribute/tribute-connector.js"})
//@StyleSheet({"vaadin://tribute/tribute.css"})
public class AutocompleteExtension extends AbstractJavaScriptExtension {
    public AutocompleteExtension(AbstractComponent target, List<TextTemplate> templates){
        super(target);
        setTemplates(templates);
    }
    public void setTemplates(List<TextTemplate> templates){
        getState().templates = templates.stream().map(t->{
            Map<String, String> map = new HashMap<>();
            map.put("code", t.getCode());
            map.put("content", t.getContent());
            return map;
        }).collect(Collectors.toList());
    }
    @Override
    protected AutocompleteState getState(){
        return (AutocompleteState) super.getState();
    }
}
