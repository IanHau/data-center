package com.ian.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ian.common.Result;
import com.ian.entity.OntologyOid;
import com.ian.exception.ServiceException;
import com.ian.service.impl.OntologyOidService;
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
@RequestMapping("/oid")
public class OntologyOidController {
    @Resource
    OntologyOidService ontologyOidService;

    @PostMapping("/create")
    public Result save(@RequestBody OntologyOid input) {
        ontologyOidService.create(input);
        return Result.success();
    }

    @PostMapping("/delete")
    public Result delete(@RequestBody OntologyOid input) {
        ontologyOidService.delete(input.getId());
        return Result.success();
    }

    @PostMapping("/update")
    public Result update(@RequestBody OntologyOid input) {
        ontologyOidService.update(input);
        return Result.success();
    }

    @PostMapping("/load")
    public Result load(@RequestBody OntologyOid input) {
        return Result.success(ontologyOidService.load(input.getId()));
    }

    @GetMapping("/list")
    public Result list() {
        return Result.success(ontologyOidService.list());
    }

    @RequestMapping("/leaves")
    @ResponseBody
    public Result loadLeaves(@RequestBody OntologyOid input) {
        if (StringUtils.isBlank(input.getTerm()) && StringUtils.isBlank(input.getOid())) {
            throw new ServiceException("400", "oid and term both empty.");
        }
        return Result.success(ontologyOidService.loadLeaves(input.getOid(), input.getTerm()));
    }

    @GetMapping("/tree")
    @ResponseBody
    public Result tree() {
        return Result.success(ontologyOidService.tree());
    }
}

