/*
* This file is part of the Aurora Publication Finder.
*
* (c) ZHAW HSB <apps.hsb@zhaw.ch>
*
* For the full copyright and license information, please view the LICENSE
* file that was distributed with this source code.
*/
package ch.zhaw.hsb.aurora.publicationfinder.Core.Mapping;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.JsonNode;

import ch.zhaw.hsb.aurora.publicationfinder.Main;
import ch.zhaw.hsb.aurora.publicationfinder.Core.Util.JSONUtil;

/**
 * This class maps the data from a provider to an intern format.
 * 
 * @author Dana Ghousson ZHAW
 * @author Iris Hausmann ZHAW
 */
public class Provider2InternMapping implements MappingInterface {

    String provider;
    JsonNode sourceJSON;
    

    /**
     * Constructor
     * @param provider name of the provider
     */
    public Provider2InternMapping(String provider) {

        this.provider = provider;
        
        try {
            System.out.println("assets/config/" + this.provider + ".json");
            InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("assets/config/" + this.provider + ".json");

            if (inputStream == null) {
                throw new FileNotFoundException("Config file not found: " + this.provider + ".json");
            }
            ObjectMapper objectMapper = new ObjectMapper();

            this.sourceJSON = objectMapper.readTree(new String(inputStream.readAllBytes()));

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    
    @Override
    public Map<String, String[]> getMapRecord() {
        
        Map<String, String[]> fieldDict = new Hashtable<String, String[]>();

        ArrayNode records = (ArrayNode) JSONUtil.getField(this.sourceJSON, "map:record");

        for (int i = 0; i < records.size(); i++) {

            JsonNode record = records.get(i);

            String field_name = (String) JSONUtil.getField(record, "field_name");

            ArrayNode fieldsJArray = (ArrayNode) JSONUtil.getField(record, "fields");
            String[] fields = new String[fieldsJArray.size()];

            for (int j = 0; j < fieldsJArray.size(); j++) {
                fields[j] = fieldsJArray.get(j).asText();

            }

            fieldDict.put(field_name, fields);
        }
        return fieldDict;
    }
    
    @Override
    public JsonNode getByFieldName(String fieldName) {
            
        return (JsonNode) JSONUtil.getField(this.sourceJSON, fieldName);   

    }

}
