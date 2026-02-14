package com.quinzex.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BulkCreatePaperDto {
    private List<CreatePaperDto> requests = new ArrayList<>();
}
