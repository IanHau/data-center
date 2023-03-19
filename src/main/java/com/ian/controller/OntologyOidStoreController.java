package com.ian.controller;

import com.ian.common.Result;
import com.ian.controller.req.RecordLoadReq;
import com.ian.controller.req.RecordSaveReq;
import com.ian.exception.ServiceException;
import com.ian.service.impl.OntologyOidStoreService;
import com.ian.service.impl.OntologyPropertyService;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @author ianhau
 */
@Controller
@RequestMapping("/ontology/record")
public class OntologyOidStoreController {

    @Resource
    private OntologyPropertyService ontologyPropertyService;

    @Resource
    private OntologyOidStoreService storeService;

    @RequestMapping("/save")
    @ResponseBody
    public Object create(@RequestBody RecordSaveReq input) {
        if (MapUtils.isEmpty(input.getData())) {
            throw new ServiceException("400", "invalid data");
        }
        storeService.saveRecord(input.getOntologyOid(), input.getData());
        return Result.success();
    }
//
//    @RequestMapping("/batchSave")
//    @ResponseBody
//    public Object batchSave(@RequestParam("file") MultipartFile file, @RequestParam("oid") String ontologyOid) throws IOException {
//        return ajaxSuccess(storeService.batchSave(file.getInputStream(), ontologyOid));
//    }

    @RequestMapping("/remove")
    @ResponseBody
    public Result remove(@RequestBody RecordLoadReq input) {
        storeService.removeRecord(input.getOntologyOid(), input.getId());
        return Result.success();
    }
//
//    @RequestMapping("/update")
//    @ResponseBody
//    public Object update(@RequestBody RecordSaveReq input) {
//        storeService.updateData(input.getOntologyOid(), input.getData().get("_id").toString(), Record.fromMap(input.getData()));
//        return ajaxSuccess();
//    }

    @RequestMapping("/load")
    @ResponseBody
    public Result load(@RequestBody RecordLoadReq input) {
        return Result.success(storeService.loadRecord(input.getOntologyOid(), input.getId()));
    }
//
//    @RequestMapping("/universalSearch")
//    @ResponseBody
//    public PageResult<Record> universalSearch(@RequestBody OntologyOidUniversalSearchReq input, Pageable page) {
//        return ajaxSuccess(storeService.lookupData(input.getCondition(), input.getOid(), input.getLogic(), page));
//    }
//
//    @RequestMapping("/list")
//    @ResponseBody
//    public Object list(@RequestBody RecordQueryReq input) {
//        return ajaxSuccess(storeService.listRecord(input.getOntologyOid(), input.getCondition(), input.getOrderInfo()));
//    }
//
//    @RequestMapping("/pagination")
//    @ResponseBody
//    public Object pagination(@RequestBody RecordPageReq input, Pageable pageable) {
//        input.getOrderInfo().setColumn("collectDate");
//        input.getOrderInfo().setDirection("desc");
//        PagedData<Record> data = storeService.pagingRecord(input.getOntologyOid(), input.getCondition(), input.getOrderInfo(),
//                pageable.getPageNumber(),
//                pageable.getPageSize());
//        List<Record> records = data.getData();
//        return data;
//    }
//    @RequestMapping("/template")
//    public void exportTemplate(@RequestBody RecordLoadReq input, HttpServletResponse response) {
//        try (XSSFWorkbook workbook = ontologyPropertyService.createTemplate(input.getOntologyOid(), "模板")) {
//            WorkbookUtils.writeToResponse(response, workbook);
//        } catch (IOException e) {
//            throw new AppException("导出模板异常");
//        }
//    }
}
