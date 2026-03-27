package com.pms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProjectResponse {

    private Long id;
    private String name;
    private String description;
    private String status;
    private String priority;
    private String ownerName;
    private int taskCount;
    private int completedTaskCount;
}