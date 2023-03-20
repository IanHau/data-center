package com.ian.controller;

import com.ian.common.Result;
import com.ian.controller.req.GermplasmReq;
import com.ian.service.impl.OntologyOidService;
import com.ian.service.impl.OntologyOidStoreService;
import com.ian.service.impl.OntologyPropertyService;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/template/{term}")
    public void exportTemplate(HttpServletResponse response, @PathVariable String term) {
        ontologyPropertyService.template(term,response);
    }

    @PostMapping("/import/{term}")
    @ResponseBody
    public Result importFromExcel(@RequestParam("file") MultipartFile file, @PathVariable String term) throws IOException {
        ontologyOidStoreService.importFromExcel(file.getInputStream(), term);
        return new Result();
    }
}
