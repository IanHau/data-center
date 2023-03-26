package com.ian.service.impl;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.ian.entity.AreaEntry;
import com.ian.exception.ServiceException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AreaService {
    private static List<AreaEntry> areaEntries;
    private static List<AreaEntry> cities;
    private static List<AreaEntry> provinces;
    private static Map<String, AreaEntry> provincesMap;
    private static Map<String, AreaEntry> citiesMap;

    /**
     * 获取地区列表
     */
    public List<AreaEntry> listAreas() {
        ensureMetaLoaded();
        return areaEntries;
    }

    /**
     * 获取所有城市列表
     */
    public List<AreaEntry> listCities() {
        ensureMetaLoaded();
        return cities;
    }

    /**
     * 获取所有省份
     */
    public List<AreaEntry> listProvinces() {
        ensureMetaLoaded();
        return provinces;
    }

    /**
     * 根据省份代码获取城市
     */
    public List<AreaEntry> listCitiesByProvince(String provinceCode) {
        String prefix = StringUtils.substring(provinceCode, 0, 2);
        if(StringUtils.isEmpty(prefix) || prefix.length() < 2) {
            return new ArrayList<>();
        }
        ensureMetaLoaded();
        return cities.stream().filter(a-> StringUtils.startsWith(a.getCode(), prefix)).collect(Collectors.toList());
    }

    /**
     * 获取城市的省份编码
     */
    public String getProvinceCode(String cityCode) {
        if(StringUtils.isEmpty(cityCode) || cityCode.length() < 4) {
            throw new ServiceException("500", "加载地理信息失败");
        }
        String prefix = cityCode.substring(0, 2);
        return String.format("%s00", prefix);
    }

    /**
     * 根据城市获取省份
     */
    public AreaEntry getProvince(String areaCode) {
        if(isProvinceCode(areaCode)) {
            return getProvinceEntry(areaCode);
        }
        return getProvinceEntry(getProvinceCode(areaCode));
    }

    /**
     * 是否省份代码
     */
    public boolean isProvinceCode(String code) {
        if(StringUtils.isEmpty(code)) {
            return false;
        }
        return code.length() == 2 || (code.length() == 4 && StringUtils.endsWith(code, "00"));
    }

    /**
     * 获取省份信息
     */
    private AreaEntry getProvinceEntry(String provinceCode) {
        ensureMetaLoaded();
        return provincesMap.get(provinceCode);
    }

    /**
     * 获取城市信息
     */
    public AreaEntry getCity(String cityCode) {
        ensureMetaLoaded();
        return citiesMap.get(StringUtils.substring(cityCode, 0, 4));
    }

    /**
     * 读取meta
     */
    private synchronized void ensureMetaLoaded() {
        if(areaEntries != null) {
            return;
        }
        Gson gson = new Gson();
        InputStream is = AreaService.class.getResourceAsStream("/meta/area.json");
        if (is == null) {
            throw new ServiceException("500", "地理信息加载失败");
        }

        // 原始数据结构
        areaEntries = gson.fromJson(new InputStreamReader(is), new TypeToken<List<AreaEntry>>() {}.getType());

        // 省份map
        provincesMap = new LinkedHashMap<>();
        for(AreaEntry province : areaEntries) {
            String provinceCode = getProvinceCode(province.getCode());
            AreaEntry entry = AreaEntry.cloneWithoutChildren(province);

            // 如果是直辖市，修正代码
            if(entry.isMunicipality()) {
                entry.setCode(provinceCode);
            }

            provincesMap.put(provinceCode, entry);
        }

        provinces = provincesMap.values()
                .stream()
                .sorted((p1, p2)-> StringUtils.compare(p1.getCode(), p2.getCode()))
                .collect(Collectors.toList());

        // 城市列表
        cities = new ArrayList<>();
        // 添加直辖市
        cities.addAll(areaEntries.stream().filter(AreaEntry::isMunicipality).collect(Collectors.toList()));
        // 添加下级城市
        cities.addAll(
                areaEntries.stream()
                        .map(AreaEntry::getChildren)
                        .filter(Objects::nonNull)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList())
        );

        // 城市map
        citiesMap = new LinkedHashMap<>();
        for(AreaEntry city : cities) {
            citiesMap.put(StringUtils.substring(city.getCode(), 0, 4), city);
        }
    }
}
