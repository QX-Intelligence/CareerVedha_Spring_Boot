package com.quinzex.service;

import com.quinzex.FeignClient.AcademicsFeignClient;
import com.quinzex.dto.ClassHierarchyDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DjangoCategories implements IDjangoCategories {
private final AcademicsFeignClient academicsFeignClient;
@Override
public List<ClassHierarchyDTO> fetchHierarchy(){
    return academicsFeignClient.getHierarchy();
}

}
