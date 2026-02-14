package com.quinzex.service;

import com.quinzex.dto.ClassHierarchyDTO;

import java.util.List;

public interface IDjangoCategories {
    public List<ClassHierarchyDTO> fetchHierarchy();
}
