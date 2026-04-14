package com.cati.tcc.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cati.tcc.mapper.ItemCarrinhoMapper;
import com.cati.tcc.service.ItemCarrinhoService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/itens-carrinho")
@Tag(name = "Item Carrinho")
public class ItemCarrinhoController {
	
	 	private final ItemCarrinhoService service;
	    private final ItemCarrinhoMapper mapper;

	    public ItemCarrinhoController(ItemCarrinhoService service, ItemCarrinhoMapper mapper) {
	        this.service = service;
	        this.mapper = mapper;
	    }

}
