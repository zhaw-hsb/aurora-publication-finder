/*
* This file is part of the Aurora Publication Finder.
*
* (c) ZHAW HSB <apps.hsb@zhaw.ch>
*
* For the full copyright and license information, please view the LICENSE
* file that was distributed with this source code.
*/
package ch.zhaw.hsb.aurora.publicationfinder.Organisation;

import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;


import ch.zhaw.hsb.aurora.publicationfinder.Modules.DSpace.Importer.BaseImporter;

/**
 * This class is the extended importer with organisation specific requirements.
 * 
 * @author Dana Ghousson ZHAW
 * @author Iris Hausmann ZHAW
 */
public class OrganisationImporter extends BaseImporter {

    JsonNodeFactory factory = JsonNodeFactory.instance;
    ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Constructor
     * @param data organisation specific data
     */
    public OrganisationImporter(Map<String, Map<String, Object>> data) {

        super(data);

    }

    @Override
    protected String formValues(Map<String, Object> valueDict) {

        ArrayNode arrayNode = new ArrayNode(factory);

        for (Entry<String, Object> itemValue : valueDict.entrySet()) {

            String metadataField = itemValue.getKey();


            // id soll nicht importiert werden
            if (metadataField.equals("id")) {
                continue;
            }

            String[] metadataValue = (String[]) itemValue.getValue();

            if (this.sectionDict.get(metadataField) != null) {

                ArrayNode bodyValues = new ArrayNode(factory);

                if (metadataValue != null) {
                    int countEntry = 0;
                    for (String value : metadataValue) {
                        ObjectNode jObject = factory.objectNode();
                        jObject.put("value", value);
                        if (this.placeDict.containsKey(metadataField)) {
                            jObject.put("place", countEntry++);
                        }
                        bodyValues.add(jObject);

                    }

                    ObjectNode jsonNode = factory.objectNode();
                    jsonNode.put("op", "add")
                            .put("path", "/sections/" + this.sectionDict.get(metadataField) + "/" + metadataField)
                            .put("value", bodyValues);

                    arrayNode.add(jsonNode);

                }

            }

        }

        // needs to be added here because it will not show in the missing paths
        // required ZHAW fields

        String defaultValue = this.defaultDict.get("granted");
        String section = this.sectionDict.get("granted");

        arrayNode.add(factory.objectNode().put("op", "add")
                .put("path", "/sections/" + section + "/granted")
                .put("value", defaultValue));

        return arrayNode.toString();

    }

    @Override
    protected boolean enableFieldsForSpecificType(Map<String, Object> valueDict, int itemId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'enableFieldsForSpecificType'");
    }

}
