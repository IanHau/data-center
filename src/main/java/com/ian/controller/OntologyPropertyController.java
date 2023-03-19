package com.ian.controller;

import com.ian.common.Result;
import com.ian.controller.req.PropertyReq;
import com.ian.entity.OntologyProperty;
import com.ian.service.impl.OntologyPropertyService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

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

    @PostMapping("/loadByOid")
    public Result create(@RequestBody OntologyProperty input) {
        return Result.success(ontologyPropertyService.loadByOid(input.getOntologyOid()));
    }

    @PostMapping("/delete")
    public Result delete(@RequestBody OntologyProperty input) {
        ontologyPropertyService.delete(input.getId());
        return Result.success();
    }

    @PostMapping("/create")
    public Result save(@RequestBody OntologyProperty input) {
        ontologyPropertyService.createProperty(input);
        return Result.success();
    }

    @PostMapping("/update")
    public Result update(@RequestBody OntologyProperty input) {
        ontologyPropertyService.update(input);
        return Result.success();
    }

    @PostMapping("/allProperties")
    public Result allProperties(@RequestBody PropertyReq input) {
        List<String> ontologyOids = input.getOntologyOids();
        return Result.success(ontologyPropertyService.allProperties(ontologyOids));
    }
}

