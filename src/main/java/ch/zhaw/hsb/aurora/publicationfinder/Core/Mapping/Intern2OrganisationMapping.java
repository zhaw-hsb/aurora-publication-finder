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
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import ch.zhaw.hsb.aurora.publicationfinder.Main;
import ch.zhaw.hsb.aurora.publicationfinder.Core.Util.ConverterUtil;
import ch.zhaw.hsb.aurora.publicationfinder.Core.Util.JSONUtil;

/**
 * This class maps the data from an intern to an organisation format.
 * 
 * @author Dana Ghousson ZHAW
 * @author Iris Hausmann ZHAW
 */
public class Intern2OrganisationMapping implements MappingInterface {

    String provider;
    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Constructor
     */
    public Intern2OrganisationMapping() {
        this.provider = "organisation";
    }

    @Override
    public Object getByFieldName(String fieldName) {
        try {

            InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("assets/config/" + this.provider + ".json");

            if (inputStream == null) {
                throw new FileNotFoundException("Config file not found: " + this.provider + ".json");
            }
            
            try (InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
                
                JsonNode sourceJSON = objectMapper.readTree(reader);

                switch (fieldName) {
                    case "map:record:fields":
                        Map<String, String[]> fieldDict = new Hashtable<String, String[]>();
    
                        ArrayNode records = (ArrayNode) JSONUtil.getField(sourceJSON, "map:record");
    
                        for (JsonNode record : records) {
    
                            if (JSONUtil.getField(record, "fields") != null) {
                                fieldDict.put((String)JSONUtil.getField(record, "field_name"),
                                        ConverterUtil.JSONArraytoStringArray((ArrayNode) JSONUtil.getField(record, "fields")));
                            }
    
                        }
                        return fieldDict;
                    case "map:record:fields:inverted":
                        Map<String, String> fieldDict7 = new Hashtable<String, String>();
    
                        ArrayNode records7 = (ArrayNode) JSONUtil.getField(sourceJSON, "map:record");
    
                        for (JsonNode record : records7) {
    
        
                            ArrayNode fields = (ArrayNode) JSONUtil.getField(record, "fields");
                            if(fields != null){
                                for (int j = 0; j < fields.size(); j++) {
    
                                    fieldDict7.put(fields.get(j).asText(),
                                            (String) JSONUtil.getField(record, "field_name"));
        
                                }
    
                            }
                            
    
                        }
                        return fieldDict7;
    
                    case "map:record:multVals":
                        Map<String, Boolean> multValsDict = new Hashtable<String, Boolean>();
    
                        ArrayNode records2 = (ArrayNode) JSONUtil.getField(sourceJSON, "map:record");
    
                        for (JsonNode record : records2) {
    
                            if (JSONUtil.getField(record, "multiple_values") != null) {
                                Boolean value = (Boolean) JSONUtil.getField(record, "multiple_values");
                                multValsDict.put((String) JSONUtil.getField(record, "field_name"), value);
                            }
    
                        }
                        return multValsDict;
                    case "map:record:section":
                        Map<String, String> sectionDict = new Hashtable<String, String>();
    
                        ArrayNode records3 = (ArrayNode) JSONUtil.getField(sourceJSON, "map:record");
    
                        for (JsonNode record : records3) {
    
                            Object value = JSONUtil.getField(record, "section");
                            if (value != null) {
                                sectionDict.put((String) JSONUtil.getField(record, "field_name"), value.toString());
                            }
    
                        }
                        return sectionDict;
                    case "map:record:default":
                        Map<String, String> defaultDict = new Hashtable<String, String>();
    
                        ArrayNode records4 = (ArrayNode) JSONUtil.getField(sourceJSON, "map:record");
    
                        for (JsonNode record : records4) {
    
                            Object value = JSONUtil.getField(record, "default");
                            if (value != null) {
                                defaultDict.put((String) JSONUtil.getField(record, "field_name"), value.toString());
                            }
    
                        }
                        return defaultDict;
                    case "map:record:language_code":
                        Map<String, String> languageDict = new Hashtable<String, String>();
    
                        ArrayNode records5 = (ArrayNode) JSONUtil.getField(sourceJSON, "map:record");
    
                        for (JsonNode record : records5) {
    
                            Object value = JSONUtil.getField(record, "language_code");
                            if (value != null) {
                                languageDict.put((String) JSONUtil.getField(record, "field_name"), value.toString());
                            }
    
                        }
                        return languageDict;
                    case "map:record:place":
                        Map<String, String> placeDict = new Hashtable<String, String>();
    
                        ArrayNode records6 = (ArrayNode) JSONUtil.getField(sourceJSON, "map:record");
    
                        for (JsonNode record : records6) {
    
                            Object value = JSONUtil.getField(record, "place");
                            if (value != null) {
                                placeDict.put((String) JSONUtil.getField(record, "field_name"), value.toString());
                            }
    
                        }
                        return placeDict;
                    case "map:record:visibleOnlyInWorkflow":
    
                        List<String> workflowFieldNames = new ArrayList<>();
    
    
                        ArrayNode records8 = (ArrayNode) JSONUtil.getField(sourceJSON, "map:record");
                        for (JsonNode record : records8) {
                            Object value = JSONUtil.getField(record, "visibleOnlyInWorkflow");
                            if (value != null) {
                                if((boolean) value){
                                    workflowFieldNames.add((String) JSONUtil.getField(record, "field_name"));
                                }
                            }
    
                        }
    
    
                        return workflowFieldNames;
                    case "types":
                        return (JsonNode) JSONUtil.getField(sourceJSON, fieldName);
                    case "licenses":
                        return (JsonNode) JSONUtil.getField(sourceJSON, fieldName);
                    case "publication_statuses":
                        return (JsonNode) JSONUtil.getField(sourceJSON, fieldName);
                    case "language":
                        return (JsonNode) JSONUtil.getField(sourceJSON, fieldName);
                    case "oa_categories":
                        return (JsonNode) JSONUtil.getField(sourceJSON, fieldName);
                    default:
                        break;
            }


            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;

    }

    @Override
    public Map<String, String[]> getMapRecord() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMapRecord'");
    }

}
