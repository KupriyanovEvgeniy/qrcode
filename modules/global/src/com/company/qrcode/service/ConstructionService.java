package com.company.qrcode.service;

import com.company.qrcode.entity.TextTemplate;
import java.util.List;

public interface ConstructionService {
    String NAME = "qrcode_ConstructionService";
    List<TextTemplate> getActiveConstructions();
}