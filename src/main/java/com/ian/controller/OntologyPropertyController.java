package com.ian.controller;

import com.ian.common.Result;
import com.ian.entity.OntologyProperty;
import com.ian.service.impl.OntologyPropertyService;
import org.springframework.web.bind.annotation.*;

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

//    @RequestMapping("/create")
//    @ResponseBody
//    public Object create(@RequestBody OntologyProperty input) {
//        ontologyPropertyService.createProperty(input);
//        return Result.success();
//    }

    @PostMapping("/loadByOid")
    @ResponseBody
    public Result create(@RequestBody OntologyProperty input) {
        return Result.success(ontologyPropertyService.loadByOid(input.getOntologyOid()));
    }
}

