/*
* This file is part of the Aurora Publication Finder.
*
* (c) ZHAW HSB <apps.hsb@zhaw.ch>
*
* For the full copyright and license information, please view the LICENSE
* file that was distributed with this source code.
*/
package ch.zhaw.hsb.aurora.publicationfinder.Core.Util;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * This class offers utilities for JSON.
 * 
 * @author Dana Ghousson ZHAW
 * @author Iris Hausmann ZHAW
 */
public class JSONUtil {

    /**
     * Method to concatenate two json arrays
     * 
     * @param arr1 first json array
     * @param arr2 second json array
     * @return ArrayNode
     */
    public static ArrayNode concatArray(ArrayNode arr1, ArrayNode arr2) {

        ArrayNode result = new ArrayNode(JsonNodeFactory.instance);

        // Add arr1 and arr2 if they are not null
        if (arr1 != null) result.addAll(arr1);
        if (arr2 != null) result.addAll(arr2);
    
        return result;
    }

    /**
     * Method to get a field value of a json object
     * 
     * @param JSONobj the json object
     * @param path    path to the field value fe. firstField:secondField:chosenField
     * @return Object
     */
    public static Object getField(JsonNode JSONobj, String path) {

        Boolean arrayFlag = false;


        Object obj = JSONobj;

        String[] subfields = path.split(":");

        for (String subfield : subfields) {

            if (obj instanceof ArrayNode) {
                arrayFlag = true;

                obj = getSubfieldObject((ArrayNode) obj, subfield);

            } else if (obj instanceof JsonNode) {
                obj = getSubfieldObject((JsonNode) obj, subfield, arrayFlag);

            }

            if (obj == null) {
                return null;
            }

        }

        return obj;

    }

    /**
     * Method to get the json object of a subfield (for json arrays)
     * 
     * @param ArrayNode the json array
     * @param subfield  the subfield in question f.e. firstField
     * @return Object
     */
    public static ArrayNode getSubfieldObject(ArrayNode jsonArray, String subfield) {
        ArrayNode objectsFromSubfield = JsonNodeFactory.instance.arrayNode();

        for (JsonNode element : jsonArray) {
            if (element.has(subfield)) {
                objectsFromSubfield.add(element.get(subfield));
            } else if (element.isArray()) {
                JsonNode returnedValue = getSubfieldObject((ArrayNode) element, subfield);
                if(returnedValue != null){
                    objectsFromSubfield.add(returnedValue);
                }else{
                    objectsFromSubfield.add("null");
                }
            }
        }
        return objectsFromSubfield.size() > 0 ? objectsFromSubfield : null;
    }

    /**
     * Method to get the JsonNode of a subfield (for json objects)
     * 
     * @param jsonObject the json object
     * @param subfield the subfield in question f.e. secondField
     * @return Object
     */
    public static Object getSubfieldObject(JsonNode jsonObject, String subfield, boolean arrayFlag) {
        Boolean hasSubfield = jsonObject.has(subfield);

        if (hasSubfield) {
            JsonNode jsonNode = jsonObject.get(subfield);
            if (jsonNode.isTextual()) {
                return jsonNode.asText();
            } else if (jsonNode.isBoolean()) {
                return jsonNode.asBoolean();

            }

            if(jsonNode != null && ! (jsonNode instanceof NullNode)){
                return jsonNode ;
            }
            

        }

        if(arrayFlag){
            return "null";
        }

        return null;

    }

}
