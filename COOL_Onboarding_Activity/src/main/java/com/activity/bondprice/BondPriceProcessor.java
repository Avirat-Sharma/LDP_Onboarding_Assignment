package com.activity.bondprice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class BondPriceProcessor {
    private static final Logger logger = LoggerFactory.getLogger(BondPriceProcessor.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String enhanceBondPrice(String jsonPrice) {
        try {
            Map<String, Object> bondData = objectMapper.readValue(jsonPrice, Map.class);
            double price = (double) bondData.get("price");
            bondData.put("enhancedPrice", price * 1.02); // Example enhancement
            return objectMapper.writeValueAsString(bondData);
        } catch (Exception e) {
            logger.error("Error processing bond price", e);
            return jsonPrice;
        }
    }
}
