package com.ian.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.migozi.conversion.Conversion;
import com.migozi.conversion.ConversionConfig;
import com.migozi.web.GsonEntityExclusion;
import com.migozi.web.GsonMessageConverter;
import org.bson.types.ObjectId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;
import java.util.UUID;

/**
 * @author ianhau
 */
@Configuration
public class WebConfig {
    /**
     * Bean: 类型转换配置
     */
    @Bean
    public ConversionConfig conversionConfig() {
        return new ConversionConfig().addDefaultConverters();
    }
    /**
     * conversion
     */
    @Bean
    public Conversion conversion() {
        return new Conversion();
    }
    /**
     * Gson
     */
    @Bean
    public Gson gson(Conversion conversion) {
        Gson gson = new GsonBuilder()
                .addSerializationExclusionStrategy(new GsonEntityExclusion.SerializeStrategy())
                .addDeserializationExclusionStrategy(new GsonEntityExclusion.DeserializeStrategy())
                .registerTypeAdapter(Date.class, new GsonMessageConverter.DateDeserializeAdapter(conversion))
                .registerTypeAdapter(Integer.class, new GsonMessageConverter.IntegerDeserializeAdapter())
                .registerTypeAdapter(Double.class, new GsonMessageConverter.DoubleDeserializeAdapter())
                .registerTypeAdapter(Float.class, new GsonMessageConverter.FloatDeserializeAdapter())
                .registerTypeAdapter(Boolean.class, new GsonMessageConverter.BooleanDeserializeAdapter(conversion))
                .registerTypeAdapter(UUID.class, new GsonMessageConverter.UUIDDeserializeAdapter())
                .registerTypeAdapter(ObjectId.class, new GsonMessageConverter.ObjectIdDeserializeAdapter())
                .registerTypeAdapter(ObjectId.class, new GsonMessageConverter.ObjectIdSerializeAdapter())
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
                .serializeSpecialFloatingPointValues()
                .create();
        conversion.setGson(gson);
        return gson;
    }
}
