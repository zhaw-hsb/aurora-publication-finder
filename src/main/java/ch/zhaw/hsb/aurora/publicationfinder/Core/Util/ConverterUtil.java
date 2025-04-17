/*
* This file is part of the Aurora Publication Finder.
*
* (c) ZHAW HSB <apps.hsb@zhaw.ch>
*
* For the full copyright and license information, please view the LICENSE
* file that was distributed with this source code.
*/
package ch.zhaw.hsb.aurora.publicationfinder.Core.Util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import ch.zhaw.hsb.aurora.publicationfinder.Core.Configuration.PropertyConfiguration;
import ch.zhaw.hsb.aurora.publicationfinder.Core.Configuration.PropertyProviderConfiguration;
import ch.zhaw.hsb.aurora.publicationfinder.Core.Model.InternModel;
import ch.zhaw.hsb.aurora.publicationfinder.Core.Model.PersonModel;

/**
 * This class offers utilities for converting data into various formats.
 * 
 * @author Dana Ghousson ZHAW
 * @author Iris Hausmann ZHAW
 */
public class ConverterUtil {
    
    
    /**
     * method to convert an intern model to a CSV
     * @param modelMap a publication as intern model
     * @param path path where to save the CSV
     */
    @SuppressWarnings("unchecked")
    public static void internModelToCSV(Map<String, InternModel> modelMap, String path) {

        File file = new File(PropertyProviderConfiguration.getExternalFilePath()+path);
        file.getParentFile().mkdirs();
        

        try (Writer writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            Method[] methods = InternModel.class.getMethods();
            Boolean newLine = true;
            // CSV header line
            for (Method method : methods) {
                
                if (method.getName().startsWith("get") && !method.getName().equals("getClass")) {
                    if (!newLine) {
                        writer.append(PropertyConfiguration.getCsvFieldSeparator());
                    }
                    newLine = false;
                    if (method.getName().equals("getAuthors")) {
                        writer.append("authors").append(PropertyConfiguration.getCsvFieldSeparator()).append("affiliations");
                    } else {
                        writer.append(method.getName().substring(3));
                    }
                }
            }
            writer.append(PropertyConfiguration.getCsvEol());
            
            // CSV add values for each line
            for (Entry<String, InternModel> entry : modelMap.entrySet()) {
                newLine = true;
                InternModel internModel = entry.getValue();
                for (Method method : methods) {

                    if (method.getName().startsWith("get") && !method.getName().equals("getClass")) { 
                        try {
                            Object returnValue = method.invoke(internModel);
                            
                            if (!newLine) {
                                writer.append(PropertyConfiguration.getCsvFieldSeparator());
                            }
                            newLine = false;
                            // cannot check type inside list
                            if (returnValue instanceof List) {
                                String authors = "";
                                String affiliations = "";
                                // do loop over persons names + affiliations
                                for (PersonModel person : (List<PersonModel>) returnValue) {
                                    if (authors != "") {
                                        authors = authors.concat(PropertyConfiguration.getCsvValueSeparator());
                                    }
                                    authors = authors.concat(person.getAuthor());
                                    try {
                                        if (affiliations != "" && (person.getAffiliations().length > 0)) {
                                            affiliations = affiliations.concat(PropertyConfiguration.getCsvValueSeparator());
                                        }
                                        affiliations = affiliations.concat(String.join(PropertyConfiguration.getCsvValueSeparator(), person.getAffiliations()));
                                    } catch (Exception e) {

                                    }
                                }
                                if (!authors.isEmpty()) {
                                    writer.append(normalizeCSVFieldValue(authors)).append(PropertyConfiguration.getCsvFieldSeparator());
                                }
                                if (!affiliations.isEmpty()) {
                                    writer.append(normalizeCSVFieldValue(affiliations));
                                }

                            } else if (returnValue instanceof String[]) {
                                String returnValueString = String.join(PropertyConfiguration.getCsvValueSeparator(), (String[]) returnValue);
                                if (!returnValueString.isEmpty()) {
                                    // Value
                                    writer.append(normalizeCSVFieldValue(returnValueString));
                                }

                            } else if (returnValue != null && !returnValue.toString().isEmpty()) {
                                writer.append(normalizeCSVFieldValue(returnValue.toString()));

                            }
                            
                        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

                }

                writer.append(PropertyConfiguration.getCsvEol());
                writer.flush();

            }

        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }

    }

    /**
     * Method to convert an organisation model to CSV
     * @param modelMap publication as organisation model
     * @param path path where to save the CSV
     */
    public static void organisationModelToCSV(Map<String, Map<String, Object>> modelMap, String path) {
        

        try (Writer writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(PropertyProviderConfiguration.getExternalFilePath()+path), StandardCharsets.UTF_8))) {

            Boolean newLine = true;
            Boolean firstLine = true;
            for (Entry<String, Map<String, Object>> entry : modelMap.entrySet()) {

                Map<String, Object> map = entry.getValue();
                // CSV header line
                if (firstLine) {
                    for (String metadataField : map.keySet()) {
                        if (!newLine) {
                            writer.append(PropertyConfiguration.getCsvFieldSeparator());
                        }
                        newLine = false;
                        writer.append(metadataField);
                    }
                    firstLine = false;;
                    writer.append(PropertyConfiguration.getCsvEol());
                }
                
                // CSV add values for each line
                newLine = true;
                for (Object metadataValue : map.values()) {
                    
                    if (!newLine) {
                        writer.append(PropertyConfiguration.getCsvFieldSeparator());
                    }
                    newLine = false;
                    if (metadataValue instanceof String[]) {
                        String returnMetadataValueString = String.join(PropertyConfiguration.getCsvValueSeparator(), (String[]) metadataValue);
                        if (!returnMetadataValueString.isEmpty()) {
                            writer.append(normalizeCSVFieldValue(returnMetadataValueString));
                        }     

                    } else if (metadataValue instanceof String[][]) {
                        String returnMetadataValueString = "";
                        for (String[] metadata : (String[][]) metadataValue) {
                            if (!returnMetadataValueString.isEmpty()) {
                                returnMetadataValueString = returnMetadataValueString.concat(PropertyConfiguration.getCsvValueSeparator());
                            }
                            if (metadata != null) {
                                returnMetadataValueString = returnMetadataValueString.concat(String.join(PropertyConfiguration.getCsvValueSeparator(), (String[]) metadata));
                            }
                        }
                        if (!returnMetadataValueString.isEmpty()) {
                            writer.append(normalizeCSVFieldValue(returnMetadataValueString));
                        }

                    } else if (metadataValue != null && !metadataValue.toString().isEmpty()) {
                        writer.append(normalizeCSVFieldValue(metadataValue.toString()));
                    }
                }
                writer.append(PropertyConfiguration.getCsvEol());
            }
            
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }

    }

    /**
     * Method to convert a json array to a hash map
     * @param jsonArray the jsonArray to be converted
     * @param identifier the identifier field of a json object
     * @return Map<String, JSONObject>
     */
    public static Map<String, JsonNode> JSONArrayToHashMap(ArrayNode jsonArray, String identifier) {

        Map<String, JsonNode> hashMap = new HashMap<>();
        int count = 0;
        for (int i = 0; i < jsonArray.size(); i++) {
           
            String id = jsonArray.get(i).get(identifier).asText();

            JsonNode object = jsonArray.get(i);
            if (hashMap.containsKey(id)) {

                count++;

            }
            hashMap.put(id, object);

           
        }

        System.out.println("Number of duplicates: " + count);
        System.out.println("Size of HashMap: " + hashMap.size());

        return hashMap;

    }

    /**
     * Method to convert a json array to a string array
     * @param array json array to be converted
     * @return String[]
     */
    public static String[] JSONArraytoStringArray(ArrayNode array) {
        if (array == null)
            return new String[0];
        String[] arr = new String[array.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] =  array.path(i).asText("");
        }
        return arr;
    }
    
    private static String normalizeCSVFieldValue(String value) {
        value= "\"" + value.replace("\"", "\"\"") + "\"";
        return value.trim();
    }

}
