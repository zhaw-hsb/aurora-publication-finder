/*
* This file is part of the Aurora Publication Finder.
*
* (c) ZHAW HSB <apps.hsb@zhaw.ch>
*
* For the full copyright and license information, please view the LICENSE
* file that was distributed with this source code.
*/
package ch.zhaw.hsb.aurora.publicationfinder.Core.DataSource;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.zhaw.hsb.aurora.publicationfinder.Main;
import ch.zhaw.hsb.aurora.publicationfinder.Core.Configuration.PropertyProviderConfiguration;
import ch.zhaw.hsb.aurora.publicationfinder.Core.Service.HttpService;
import ch.zhaw.hsb.aurora.publicationfinder.Core.Util.JSONUtil;
import ch.zhaw.hsb.aurora.publicationfinder.Core.Util.URLUtil;

/**
 * This class retrieves the data from the data sources.
 * 
 * @author Dana Ghousson ZHAW
 * @author Iris Hausmann ZHAW
 */
public class DataSourceRetriever {

    DataSourceProviderInterface dataSourceProvider;
    String date;

    /**
     * Constructor
     * 
     * @param dataSourceProvider
     */
    public DataSourceRetriever(DataSourceProviderInterface dataSourceProvider) {

        try {

            this.dataSourceProvider = dataSourceProvider;
            InputStream inputStream;

            if (Files.exists(Paths.get(PropertyProviderConfiguration.getExternalFilePath() + "/timestamp.txt"))) {
                inputStream = new FileInputStream(PropertyProviderConfiguration.getExternalFilePath() + "/timestamp.txt");

            } else {

                // Read from JAR if external file does not exist
                inputStream = Main.class.getClassLoader().getResourceAsStream("assets/config/timestamp.txt");
                if (inputStream == null) {
                    throw new FileNotFoundException("Timestamp file not found inside JAR.");
                }

            }

            this.date = new String(inputStream.readAllBytes());

            inputStream.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * Method to get the retrieved data from all types (affiliation and ror)
     * 
     * @return ArrayNode
     */
    public ArrayNode getDataFromAllTypes() {
        ArrayNode all = new ArrayNode(JsonNodeFactory.instance);

        if (this.dataSourceProvider.getUrlAffiliations() != null) {
            all = JSONUtil.concatArray(all, this.getDataFromType("affiliation"));
        }
        if (this.dataSourceProvider.getUrlRor() != null) {
            all = JSONUtil.concatArray(all, this.getDataFromType("ror"));
        }

        return all;

    }

    /**
     * Method to get the retrieved data with a type
     * 
     * @param type either affiliation or ror
     * @return
     */
    private ArrayNode getDataFromType(String type) {

        System.out.println("Type: " + type);

        URL url;
        ArrayNode arrayNodeAll = new ArrayNode(JsonNodeFactory.instance);
        String[] searchList = null;
        String nextCursor = dataSourceProvider.getStartCursor();

        switch (type) {
            case "ror":
                searchList = dataSourceProvider.getRors();
                break;
            case "affiliation":
                searchList = dataSourceProvider.getAffiliations();
                break;
            default:
                System.out.println("Unknown type:  " + type);
        }

        for (int index = 0; index < searchList.length; index++) {
            nextCursor = dataSourceProvider.getStartCursor();

            while (nextCursor != "null") {

                try {
                    url = this.getURL(type, nextCursor, index);
                    System.out.println("URL: " + url);
                } catch (Exception e) {

                    return null;
                }

                StringBuilder sb = HttpService.getStringBuilder(url);
                if (sb == null) {
                    break;
                }

                // JSON Object with all data
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jObject;
                try {

                    jObject = objectMapper.readTree(sb.toString());

                    nextCursor = URLUtil.getNextCursor(jObject, dataSourceProvider.getMetadata(),
                    dataSourceProvider.getNextCursorField());

                    // add results data to JSON Array with all results
                    ArrayNode results = (ArrayNode) JSONUtil.getField(jObject, dataSourceProvider.getItemsSection());

            
                    if (results.size() == 0) {
                        break;
                    }
                    arrayNodeAll = JSONUtil.concatArray(arrayNodeAll, results);

                } catch (JsonProcessingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
  



            }
        }

        System.out.println("All elements with type " + type + ": " + arrayNodeAll.size());
        return arrayNodeAll;
    }

    /**
     * Method to create and get the built url
     * 
     * @param type       affiliation or ror
     * @param nextCursor element of the next cursor
     * @param index      index of the affiliation / ror element
     * @return URL
     * @throws Exception
     */
    private URL getURL(String type, String nextCursor, Integer index) throws Exception {

        try {
            System.out.println("Type bei getUrl:  " + type + " (" + index.toString() + ")");
            URL url = null;
            switch (type) {
                case "ror":
                    url = new URL(dataSourceProvider.getUrlRor()
                            + URLEncoder.encode(dataSourceProvider.getRor(index), "UTF-8")
                            + dataSourceProvider.getConnectionElement() + dataSourceProvider.getFromDateField() + ":"
                            + this.date
                            + "&" + dataSourceProvider.getMaxItemField() + "="
                            + dataSourceProvider.getMaxItem().toString()
                            + "&cursor" + "=" + nextCursor);
                    break;
                case "affiliation":
                    url = new URL(dataSourceProvider.getUrlAffiliations()
                            + "\"" + URLEncoder.encode(dataSourceProvider.getAffiliation(index), "UTF-8") + "\""
                            + dataSourceProvider.getConnectionElement() + dataSourceProvider.getFromDateField() + ":"
                            + this.date
                            + "&" + dataSourceProvider.getMaxItemField() + "="
                            + dataSourceProvider.getMaxItem().toString()
                            + "&cursor" + "=" + nextCursor);
                    break;
                default:
                    url = null;
                    System.out.println("Unknown type:  " + type);
            }

            return url;

        } catch (Exception e) {
            // TODO Auto-generated catch block

            throw new Exception(e);

        }
    };

}
