package com.cati.tcc.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cati.tcc.mapper.ChecklistMapper;

import com.cati.tcc.service.ChecklistService;

import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/checklist")
@Tag(name = "Checklist")
public class ChecklistController {
	
	private final ChecklistService checkService;
	private final ChecklistMapper checkMapper;
	
	public ChecklistController(ChecklistService checkService, ChecklistMapper checkMapper) {
		this.checkService = checkService;
		this.checkMapper = checkMapper;
	}

	

}
