package com.ian.controller;

import com.ian.service.impl.OntologyPropertyService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author ianhau
 * @since 2023-02-19
 */
@RestController
@RequestMapping("/oidProperty")
public class OntologyPropertyController {

    @Resource
    OntologyPropertyService ontologyPropertyService;

}

