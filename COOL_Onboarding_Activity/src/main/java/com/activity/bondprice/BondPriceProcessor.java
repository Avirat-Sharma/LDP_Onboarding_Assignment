package com.activity.bondprice;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class BondPriceProcessor {
    private static final Logger logger = LoggerFactory.getLogger(BondPriceProcessor.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String enhanceBondPrice(String bondId, String priceStr) {
        try {
            //  Parse JSON input
            JsonNode jsonNode = objectMapper.readTree(priceStr);
            double price = jsonNode.get("price").asDouble();  // Extract price from JSON

            //  Enhance price
            double enhancedPrice = price * 1.02;

            // Create JSON output
            Map<String, Object> bondData = new HashMap<>();
            bondData.put("bond", bondId);
            bondData.put("originalPrice", price);
            bondData.put("enhancedPrice", enhancedPrice);

            String jsonOutput = objectMapper.writeValueAsString(bondData);
            logger.info("Enhanced bond price: {}", jsonOutput);
            return jsonOutput;

        } catch (Exception e) {
            logger.error("Error processing bond price for key: {}. Error: {}", bondId, e.getMessage());
            return "{}";  // Return empty JSON in case of error
        }
    }
}
