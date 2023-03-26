package com.ian.service.impl;

import cn.hutool.db.sql.Condition;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.ian.common.OntologyPropertyDataType;
import com.ian.controller.req.GermplasmReq;
import com.ian.controller.resp.CountVO;
import com.ian.entity.OntologyOid;
import com.ian.entity.OntologyProperty;
import com.ian.entity.Record;
import com.ian.exception.ServiceException;
import com.ian.utils.WorkbookUtils;
import com.migozi.ApplicationException;
import org.apache.commons.collections4.MapUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 平台OID数据存储服务
 *
 * @author ianhau
 * @date 2023-03-21 15:29
 */
@Service
public class OntologyOidStoreService {

    @Resource
    private OntologyOidService ontologyOidService;

    @Resource
    private OntologyPropertyService ontologyPropertyService;

    @Resource
    private WorkbookUtils workbookUtils;

    @Resource
    private MongoTemplate mongoTemplate;
    @Resource
    private AreaService areaService;

    public Record loadRecord(String oid, String id) {
        OntologyOid ontologyOid = ontologyOidService.loadByOid(oid);
        String tableName = ontologyOid.getTableName();
        String idName = "_id";
        return ontologyOidService.loadRecord(tableName, idName, id);
    }

    public void saveRecord(String oid, Map<String, Object> data) {
        data.forEach((key, value) -> {
            OntologyProperty ontologyProperty = ontologyPropertyService.loadByTerm(key);
            if (ObjectUtils.isEmpty(ontologyProperty)) {
                return;
            }
            if (Boolean.TRUE.equals(ontologyProperty.getRequired()) && StringUtils.isBlank(value.toString())) {
                throw new ApplicationException("必填项不能为空");
            }
        });

        ontologyOidService.saveRecord(ontologyOidService.loadByOid(oid), data);
    }

    public void removeRecord(String oid, String id) {
        OntologyOid ontologyOid = ontologyOidService.loadByOid(oid);
        String tableName = ontologyOid.getTableName();
        ontologyOidService.removeRecord(tableName, id);
    }

    public Object search(GermplasmReq input, Pageable pageable) {
        String tableName = input.getTableName();
        Condition condition = input.getCondition();
        Query query = new Query();
        if (condition != null) {
            query.addCriteria(Criteria.where(condition.getField()).is(condition.getValue()));
        }
        long total = mongoTemplate.count(query, tableName);
        query.with(pageable);
        return new PageImpl<>(mongoTemplate.find(query, Record.class, tableName), pageable, total);
    }

    public void importFromExcel(InputStream is, String term) throws IOException {
        String oid = ontologyOidService.loadByTerm(term).getOid();
        List<Map<String, Object>> data = convertSheetToList(is, oid);
        Map<String, OntologyProperty> propertyMap = ontologyPropertyService.loadByOid(oid).stream().collect(Collectors.toMap(OntologyProperty::getName, v -> v));
        OntologyOid ontologyOid = ontologyOidService.loadByOid(oid);
        try {
            data.forEach(it -> {
                propertyMap.forEach((key, value) -> {
                    if (value.getRequired() != null && value.getRequired() && (it.get(key) == null)) {
                        throw new IllegalArgumentException();
                    }
                });
                ontologyOidService.saveRecord(ontologyOid, it);
            });
        } catch (Exception e) {
            throw new ServiceException("500", "必填项不能为空");
        }
    }

    private List<Map<String, Object>> convertSheetToList(InputStream is, String oid) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook(is);
        XSSFSheet sheet = workbook.getSheetAt(0);
        int num = sheet.getLastRowNum();
        List<Map<String, Object>> data = new ArrayList<>(num);
        Map<String/*showName*/, String/*name*/> showNameWithName = ontologyPropertyService.loadByOid(oid)
                .stream()
                .collect(Collectors.toMap(OntologyProperty::getShowName, OntologyProperty::getName));

        Map<String/*showName*/, String/*name*/> showNameWithDataType = ontologyPropertyService.loadByOid(oid)
                .stream()
                .collect(Collectors.toMap(OntologyProperty::getShowName, OntologyProperty::getDataType));

        String tableName = ontologyOidService.loadByOid(oid).getTableName();
        List<Map> exists = mongoTemplate.findAll(Map.class, tableName)
                .stream()
                .peek(r -> r.remove("_id"))
                .collect(Collectors.toList());
        XSSFRow header = sheet.getRow(0);
        int cellNumber = header.getPhysicalNumberOfCells();
        List<String> cellName = new ArrayList<>();
        for (int i = 0; i < cellNumber; i++) {
            cellName.add(workbookUtils.getCellValue(header, i, String.class, ""));
        }
        if (cellName.stream().anyMatch(name -> !showNameWithName.containsKey(name))) {
            throw new ApplicationException("不正确的文件数据!");
        }
        Map<String, Object> map;
        for (int i = 1; i <= num; i++) {
            map = new HashMap<>();
            XSSFRow row = sheet.getRow(i);
            for (int index = 0; index < cellNumber; index++) {
                String dataType = MapUtils.getString(showNameWithDataType, cellName.get(index), OntologyPropertyDataType.String.name());
                if (OntologyPropertyDataType.Date.name().equals(dataType)) {
                    Date value = workbookUtils.getCellValue(row, index, Date.class, null);
                    map.put(showNameWithName.get(cellName.get(index)), value);
                    continue;
                }
                String value = workbookUtils.getCellValue(row, index, String.class, "");
                map.put(showNameWithName.get(cellName.get(index)), value);
            }
            Map<String, Object> finalMap = map;
            if (exists.parallelStream().anyMatch(x -> x.equals(finalMap))) {
                continue;
            }
            data.add(map);
        }
        return data;
    }

    private final LoadingCache<String, CountVO> cache = CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.MINUTES)
            .build(new CacheLoader<String, CountVO>() {
                @Override
                public CountVO load(String term) {
                    return calculateTotal(term);
                }
            });


    public CountVO number(String term) {
        try {
            return cache.get(term);
        } catch (Exception e) {
            throw new ApplicationException("数据获取失败");
        }
    }

    private CountVO calculateTotal(String term) {
        CountVO homePageVO = new CountVO();
        OntologyOid ontologyOid = ontologyOidService.loadByTerm(term);
        //各数据总统计数
        List<CountVO.TypeNumber> typeNumbers = Stream.of(ontologyOid)
                .map(oid -> {
                    long count = mongoTemplate.count(new Query(), oid.getTableName());
                    return new CountVO.TypeNumber(oid.getAlias(), count);
                }).collect(Collectors.toList());

        //物种统计
        Aggregation specisAggr = Aggregation.newAggregation(
                Aggregation.group("species").count().as("varietyNumber"),
                Aggregation.project("varietyNumber").and("species").previousOperation()
        );
        AggregationResults<CountVO.SpeciesList> species = mongoTemplate.aggregate(specisAggr, ontologyOid.getTableName(), CountVO.SpeciesList.class);
        List<CountVO.SpeciesList> speciesLists = species.getMappedResults();

        //种质分类占比
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.group("classification").count().as("categoryNumber"),
                Aggregation.project("categoryNumber").and("classification").previousOperation()
        );
        AggregationResults<CountVO.ClassCount> ganodermalucidum = mongoTemplate.aggregate(aggregation, ontologyOid.getTableName(), CountVO.ClassCount.class);
        List<CountVO.ClassCount> mappedResults = ganodermalucidum.getMappedResults();

        //统计省市信息
        Aggregation distribution = Aggregation.newAggregation(
                Aggregation.group("selectArea").count().as("value"),
                Aggregation.project("value").and("selectArea").previousOperation()

        );
        AggregationResults<CountVO.Distribution> distributions = mongoTemplate.aggregate(distribution, ontologyOid.getTableName(), CountVO.Distribution.class);
        Map<String, Long> provinceStatisticalResult = distributions.getMappedResults()
                .stream().collect(Collectors.toMap(CountVO.Distribution::getSelectArea, CountVO.Distribution::getValue));

        List<CountVO.Distribution> distributionList = areaService.listProvinces().stream()
                .map(it ->
                        new CountVO.Distribution(it.getShortName(), it.getCode(), MapUtils.getLong(provinceStatisticalResult, it.getShortName(), 0L)))
                .collect(Collectors.toList());


        homePageVO.setSpeciesList(speciesLists);
        homePageVO.setCategoryList(mappedResults);
        homePageVO.setDistribution(distributionList);
        homePageVO.setTypeNumbers(typeNumbers);
        return homePageVO;
    }
}
