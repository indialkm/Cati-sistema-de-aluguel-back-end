package com.cati.tcc.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cati.tcc.dto.request.ChecklistRequest;
import com.cati.tcc.dto.response.ChecklistResponse;
import com.cati.tcc.mapper.ChecklistMapper;

import com.cati.tcc.service.ChecklistService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;


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
