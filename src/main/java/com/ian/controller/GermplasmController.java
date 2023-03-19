package com.ian.controller;

import com.ian.common.Result;
import com.ian.controller.req.GermplasmReq;
import com.ian.service.impl.OntologyOidService;
import com.ian.service.impl.OntologyOidStoreService;
import com.ian.service.impl.OntologyPropertyService;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ianhau
 */
@RestController
@RequestMapping("/germplasm")
public class GermplasmController {
    @Resource
    OntologyOidStoreService ontologyOidStoreService;
    @Resource
    OntologyOidService ontologyOidService;
    @Resource
    OntologyPropertyService ontologyPropertyService;

    @PostMapping("/list")
    public Result list() {
        return Result.success();
    }

    @GetMapping("/allTables")
    public Result allTables() {
        return Result.success(ontologyOidService.allTable());
    }

    @PostMapping("/search")
    public Result search(@RequestBody GermplasmReq input, Pageable pageable) {
        return Result.success(ontologyOidStoreService.search(input, pageable));
    }

    @PostMapping("/tableHeaders")
    public Result tableHeaders(@RequestBody GermplasmReq input) {
        return Result.success(ontologyPropertyService.tableHeaders(input.getOntologyOid()));
    }

    @PostMapping("/template")
    public void exportTemplate(@RequestBody GermplasmReq input, HttpServletResponse response) {
//        ontologyPropertyService.template(input,response);
    }
}
