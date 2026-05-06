/*
* This file is part of the Aurora Publication Finder.
*
* (c) ZHAW HSB <apps.hsb@zhaw.ch>
*
* For the full copyright and license information, please view the LICENSE
* file that was distributed with this source code.
*/
package ch.zhaw.hsb.aurora.publicationfinder.Modules.DSpace.Importer;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import ch.zhaw.hsb.aurora.publicationfinder.Core.Configuration.PropertyProviderConfiguration;
import ch.zhaw.hsb.aurora.publicationfinder.Core.LogCollector.AdminLogCollector;
import ch.zhaw.hsb.aurora.publicationfinder.Core.LogCollector.HelpdeskLogCollector;
import ch.zhaw.hsb.aurora.publicationfinder.Core.Mapping.Intern2OrganisationMapping;
import ch.zhaw.hsb.aurora.publicationfinder.Modules.DSpace.Service.HTTPService;
import ch.zhaw.hsb.aurora.publicationfinder.Organisation.Item.WorkflowItem;

/**
 * This abstract class defines characteristics of an importer and provides defaults. It imports the items into the repository.
 * 
 * @author Dana Ghousson ZHAW
 * @author Iris Hausmann ZHAW
 */
public abstract class BaseImporter {

    protected String repositoryAPIUrl;
    String collectionId;

    protected Map<String, String> sectionDict;
    protected Map<String, String> defaultDict;
    protected Map<String, String> placeDict;
    protected ArrayList<String> workflowFieldNames;
    
    JsonNodeFactory factory = JsonNodeFactory.instance;
    ObjectMapper objectMapper = new ObjectMapper();


    protected Map<String, Map<String, Object>> data;


    /*
         * Fields are organisation specific
         * data =
         * {
         * "ID1": {"field1": "value1", "field2": "value2", etc.},
         * "ID2": {"field1": "value1", "field2": "value2", etc.},
         * etc.
         * 
         * }
         * 
         */
    /**
    /**
     * Constructor
     * @param data data to be imported as a map of maps
     */
    public BaseImporter(Map<String, Map<String, Object>> data) {

        this.data = data;

        this.repositoryAPIUrl = PropertyProviderConfiguration.getRepositoryAPIUrl();
        this.collectionId = PropertyProviderConfiguration.getCollectionId();

        Intern2OrganisationMapping mapping = new Intern2OrganisationMapping();
        this.sectionDict = (Map<String, String>) mapping.getByFieldName("map:record:section");
        this.defaultDict = (Map<String, String>) mapping.getByFieldName("map:record:default");
        this.placeDict = (Map<String, String>) mapping.getByFieldName("map:record:place");
        this.workflowFieldNames = (ArrayList<String>) mapping.getByFieldName("map:record:visibleOnlyInWorkflow");

    }

    /**
     * Method to import all the data to the repository
     */
    public void importData() {

        if (this.data.size() < 1) {                
            HelpdeskLogCollector.logInfo("There is no data to import.");
            return;
        }


        int countSucces = 0;
        int countFailure = 0;
        for (Entry<String, Map<String, Object>> item : this.data.entrySet()) {

            String doi = item.getKey();
            Map<String, Object> valueDict = item.getValue();

            // create workspaceitem
            int itemId = this.createWorkspaceItem();
            boolean hasFailed = true;
            if (itemId != -1) {

                String formedValues = this.formValues(valueDict);

                // add values
                if (this.addValuesToWorkspaceItem(itemId, formedValues)) {
                    if (this.addWorkspaceItemToWorkflow(itemId) != null) {
                        countSucces++;
                        hasFailed = false;
                    }

                }
            }

            if(hasFailed){
                countFailure++;
                HelpdeskLogCollector.logInfo("Import failed for item with DOI "+doi+".");
            }

        }

        HelpdeskLogCollector.logInfo("Successfully imported "+countSucces+" items.");
        HelpdeskLogCollector.logInfo("Failed to import "+countFailure+" items.");


    }

    /**
     * Method to create a new workspace item in the repository and get the id of it
     * @return int
     */
    protected int createWorkspaceItem() {

        HttpResponse<String> response = HTTPService.sendRequestWithAuthentication(
                "",
                "application/json",
                this.repositoryAPIUrl + "/submission/workspaceitems?owningCollection=" +
                        this.collectionId,
                "POST");

        if (response != null) {

            String jsonData = response.body();
            JsonNode jobject;
            try {
                jobject = objectMapper.readTree(jsonData);
                int id = jobject.get("id").asInt();

                return id;
            } catch (JsonProcessingException e) {
                AdminLogCollector.logWarning("Failed to get id of workspaceitem.", e);
            }
           

        }

        return -1;

    }

    
    /**
     * Method to add the missing fields and values to the body of a request
     * @param missingPaths the paths of the missing fields
     * @return String
     */
    protected String addMissingValues(ArrayNode missingPaths) {


        ArrayNode arrayNode = new ArrayNode(factory);

        for (JsonNode missingPath : missingPaths) {

            String path = missingPath.asText();
            String[] splitPath = path.split("/");
            String bodyValue = "undefined";

            if (this.defaultDict.containsKey(splitPath[3])) {
                bodyValue = this.defaultDict.get(splitPath[3]);
            }

            ObjectNode node1 = factory.objectNode().put("op", "add").put("path", "/sections/" + splitPath[2] + "/" + splitPath[3]);
            ArrayNode array1 = new ArrayNode(factory).add(factory.objectNode().put("value", bodyValue));

            arrayNode.add(node1.set("value", array1 ));

        }

        return arrayNode.toString();
    }

    /**
     * Method to add the values to the fields of a workspace item (submission)
     * @param itemId the id of the workspace item
     * @param formedValues the values formed to be ready as body in the request
     * @return boolean
     */
    protected boolean addValuesToWorkspaceItem(int itemId, String formedValues) {

        HttpResponse<String> response = HTTPService.sendRequestWithAuthentication(
                formedValues,
                "application/json",
                this.repositoryAPIUrl + "/submission/workspaceitems/" + itemId,
                "PATCH");

        if (response != null) {

            String jsonData = response.body();
            JsonNode jobject;
            try {
                jobject = objectMapper.readTree(jsonData);
                ArrayNode missingPaths = new ArrayNode(factory);
                if(jobject.has("errors")){
                    missingPaths = (ArrayNode) jobject.get("errors").get(0).get("paths");
                }
    
                 
                if (missingPaths.size() > 0) {
                    addValuesToWorkspaceItem(itemId, this.addMissingValues(missingPaths));
                }
    
                return true;
            } catch (JsonProcessingException e) {
                AdminLogCollector.logWarning("Failed to get missing paths and add values.", e);
            }
         
        }

        return false;

    }

    /**
     * Method to add the workspace item to the workflow
     * @param itemId the id of the item
     * @return WorkflowItem
     */
    protected WorkflowItem addWorkspaceItemToWorkflow(int itemId) {

        HttpResponse<String> response = HTTPService.sendRequestWithAuthentication(
                "/api/submission/workspaceitems/" + itemId,
                "text/uri-list",
                this.repositoryAPIUrl + "/workflow/workflowitems" + (PropertyProviderConfiguration.getDSpaceVersionShort() == 7?"":"?embed=item,sections,collection"
                ),
                "POST");

        if (response != null) {

            String jsonData = response.body();
            JsonNode jobject;
            try {
                jobject = objectMapper.readTree(jsonData);
                int id = jobject.get("id").asInt();
                String uuid = jobject.get("_embedded")
                        .get("item")
                        .get("uuid").asText();
    
                return new WorkflowItem(id, uuid);
            } catch (JsonProcessingException e) {
                AdminLogCollector.logWarning("Failed to get id and uuid of workflowitem.", e);

            }

        }

        return null;
    }

    /**
     * Method to add values to the fields of the workflow item
     * @param workflowItem the workflow item
     * @param valueDict the values of the fields as a dictionary
     * @param workflowFieldPathsWithValue  the paths and values of a workflow field
     * @return boolean
     */
    protected boolean addValuesToWorkflowItem(WorkflowItem workflowItem, Map<String, Object> valueDict,
            Map<String, String[]> workflowFieldPathsWithValue) {

        Integer claimedID = -1;

        HttpResponse<String> response = HTTPService
                .sendRequestWithAuthentication(null, null,
                        this.repositoryAPIUrl
                                + "/workflow/pooltasks/search/findByItem?uuid="
                                + workflowItem.getUuid(),
                        "GET");

        if (response != null) {

            String responseBodyPoolTask = response.body();
            Integer poolTaskID;
            try {
                poolTaskID = objectMapper.readTree(responseBodyPoolTask).get("id").asInt();
                HttpResponse<String> response2 = HTTPService.sendRequestWithAuthentication(
                    this.repositoryAPIUrl
                            + "/workflow/pooltasks/"
                            + poolTaskID,
                    "text/uri-list",
                    this.repositoryAPIUrl
                            + "/workflow/claimedtasks",
                    "POST");

            if (response2 != null) {
                String responseBodyClaimedTask = response2.body();
                try {
                    claimedID = objectMapper.readTree(responseBodyClaimedTask).get("id").asInt();
                } catch (JsonProcessingException e) {
                    AdminLogCollector.logWarning("Failed to get id of claimed item.", e);

                }
            }
            } catch (JsonProcessingException e) {
                AdminLogCollector.logWarning("Failed to get id of pooltask item.", e);

            }


        }

        ArrayNode arrayNode = new ArrayNode(factory);
        for (Entry<String, String[]> workflowFieldPathWithValue : workflowFieldPathsWithValue.entrySet()) {
           

            JsonNode objectNode = factory.objectNode().put("op", "add")
                    .put("path", workflowFieldPathWithValue.getKey())
                    .set("value", new ArrayNode(factory)
                            .add(factory.objectNode().put("value", ((String[]) workflowFieldPathWithValue.getValue())[0])));

            arrayNode.add(objectNode);
        }

        HttpResponse<String> response2 = HTTPService.sendRequestWithAuthentication(
                arrayNode.toString(),
                "application/json",
                this.repositoryAPIUrl + "/workflow/workflowitems/" + workflowItem.getId(),
                "PATCH");

        if (response2 != null) {
            String responseBody = response.body();

            // return item back to pool only if we added it to claimedtasks before
            if (claimedID != -1) {

                HttpResponse<String> response3 = HTTPService.sendRequestWithAuthentication(
                        null, null,
                        this.repositoryAPIUrl
                                + "/workflow/claimedtasks/"
                                + claimedID,
                        "DELETE");
                String responseBodyDeleteFromClaimedTask = response3.body();

                if (responseBodyDeleteFromClaimedTask == null) {
                    AdminLogCollector.logWarning("Failed to move claimed item " + workflowItem.getUuid() + "back to pool", null);
                    return false;

                }
            }

            if (responseBody != null) {

                return true;

            }
        }

        return false;

    }

    /**
     * Method to form the values of fields as required for the body in the request
     * @param valueDict a dictionary of the fields and their values
     * @return String
     */
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
                        ObjectNode jObject = factory.objectNode().put("value", value);
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

        String defaultValue = this.defaultDict.get("granted");
        String section = this.sectionDict.get("granted");

        arrayNode.add(factory.objectNode().put("op", "add")
                .put("path", "/sections/" + section + "/granted")
                .put("value", defaultValue));

        return arrayNode.toString();

    };

    /**
     * Method to enable other fields depending on a specific type of the item
     * @param valueDict the dictionary of fields and its values
     * @param itemId the id of the item
     * @return boolean
     */
    protected abstract boolean enableFieldsForSpecificType(Map<String, Object> valueDict, int itemId);

    protected Map<String, String[]> formWorkflowValues(Map<String, Object> valueDict) {

        Map<String, String[]> pathsWithValues = new HashMap<String, String[]>();

        for (String workflowFieldName : this.workflowFieldNames) {
            String path = "/sections/" + this.sectionDict.get(workflowFieldName) + "/" + workflowFieldName;
            if (valueDict.get(workflowFieldName) != null) {
                pathsWithValues.put(path, (String[]) valueDict.get(workflowFieldName));

            } else if (this.defaultDict.get(workflowFieldName) != null) {
                pathsWithValues.put(path, new String[] { this.defaultDict.get(workflowFieldName) });

            } else {
                // do not add to map
            }

        }

        return pathsWithValues;

    }

}
