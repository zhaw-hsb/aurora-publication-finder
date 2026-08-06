/*
* This file is part of the Aurora Publication Finder.
*
* (c) ZHAW HSB <apps.hsb@zhaw.ch>
*
* For the full copyright and license information, please view the LICENSE
* file that was distributed with this source code.
*/
package ch.zhaw.hsb.aurora.publicationfinder.Organisation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.databind.JsonNode;

import ch.zhaw.hsb.aurora.publicationfinder.Core.LogCollector.AdminLogCollector;
import ch.zhaw.hsb.aurora.publicationfinder.Core.Mapping.Intern2OrganisationMapping;
import ch.zhaw.hsb.aurora.publicationfinder.Core.Model.InternModel;
import ch.zhaw.hsb.aurora.publicationfinder.Core.Model.PersonModel;
import ch.zhaw.hsb.aurora.publicationfinder.Core.Transformer.BaseOrganisationTransformer;
import ch.zhaw.hsb.aurora.publicationfinder.Organisation.Service.ExclusionService;

/**
 * This class is the extended transformer with organisation specific requirements.
 * 
 * @author Dana Ghousson ZHAW
 * @author Iris Hausmann ZHAW
 */
public class OrganisationFieldsTransformer extends BaseOrganisationTransformer {

    /**
     * Constructor
     * @param map map of the data as intern models
     * @param provider the name of the provider
     * @param mapping the mapping from intern model to organisation specific
     */
    public OrganisationFieldsTransformer(Map<String, InternModel> map, String provider, Intern2OrganisationMapping mapping) {
        super(map, provider, mapping);
    }


    @Override
    public Map<String, Object> getTransformedElement(InternModel internModel) {

        ExclusionService exclusionService = new ExclusionService(); 

        if(exclusionService.publicationStatusExclusionCriteria(providerName, internModel.getPublicationStatus()) |
            exclusionService.updateToExclusionCriteria(internModel.getUpdateTo())){
                
                return null;
        }

        Map<String, Object> organisationMap = new HashMap<>();

        for (Entry<String, String[]> entry : fieldDict.entrySet()) {

            String organisationKey = entry.getKey();
            String[] internalKeys = entry.getValue();

            // always take first get even if there are multiple_fields in config -->
            // Reihenfolge in Config wichtig
            if (internalKeys.length > 0) {
                String methodName = "get" + internalKeys[0].substring(0, 1).toUpperCase()
                        + internalKeys[0].substring(1);
                Method method;
                try {
                    try {
                        method = this.getClass().getDeclaredMethod(methodName, InternModel.class);

                    } catch (NoSuchMethodException ex) {
                        method = this.getClass().getSuperclass().getDeclaredMethod(methodName, InternModel.class);
                    }
                    Object returnedValue = method.invoke(this, internModel);

                    if(internalKeys[0].equals("type") && exclusionService.typeExclusionCriteria((String[]) returnedValue)){
                        return null;
                    }


                    if (!this.multValuesDict.get(organisationKey) && returnedValue instanceof String[]) {
                        if (returnedValue != null) {
                            organisationMap.put(organisationKey, returnedValue);
                            continue;

                        }
                    }
                    organisationMap.put(organisationKey, returnedValue);

                } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
                        | InvocationTargetException e) {
                    AdminLogCollector.logWarning("Could not transform item.", e);
                }

            }

        }

        return organisationMap;

    }



    @Override
    public String[] getAuthors(InternModel internModel) {

        List<PersonModel> authorsList = internModel.getAuthors();
        if(authorsList !=null){
            String[] authors = new String[authorsList.size()];
            int count = 0;
            for (PersonModel person : authorsList) {
                authors[count++] = person.getAuthor();
            }
            return authors;
        }
        
        return null;

    }

    
    @Override
    public String[] getISSN(InternModel internModel) {

        // openalex
        if (internModel.getPublisherType() != null) {
            if (internModel.getPublisherType()[0].equals("book series")) {
                return null;

            } else {
                return internModel.getISSN();

            }
            // crossref
        }
        return internModel.getISSN();

    }

    @Override
    public String[] getPublisher(InternModel internModel) {

        // openalex
        if (internModel.getPublisherType() != null) {

            if (internModel.getPublisherType()[0].equals("journal")
                    || internModel.getPublisherType()[0].equals("ebook platform")) {
                return internModel.getPublisher();
            } else {
                return null;
            }
            // crossref
        }
        return internModel.getPublisher();

    }

    @Override
    public String[] getRelationIspartofseries(InternModel internModel) {

        if (internModel.getPublisherType() != null) {

            if (internModel.getPublisherType()[0].equals("book series")) {
                return internModel.getRelationIspartof();
            } else {
                return null;
            }
        }
        return null;

    }

    @Override
    public String[] getRelationIspartof(InternModel internModel) {

        if (internModel.getPublisherType() != null) {

            if (internModel.getPublisherType()[0].equals("journal")) {
                return internModel.getRelationIspartof();
            } else {
                return null;
            }
        }
        return internModel.getRelationIspartof();
    }

    @Override
    public String[] getLicenses(InternModel internModel) {

        if (internModel.getLicenses() != null) {

            if(internModel.getLicenses().length>0){
                for (String license : internModel.getLicenses()) {
                    JsonNode licenseNode = this.licenses.get(license);
                    if(licenseNode != null) {
                        return new String[]{licenseNode.asText()};
                        
                    } 
                }

            }
            

        }
        return new String[]{this.licenses.get("Default").asText()};
    }

    @Override
    public String[] getType(InternModel internModel) {

        if (internModel.getType() != null) {

            String[] newTypes = new String[internModel.getType().length];
            int count = 0;

            for (String type : internModel.getType()) {
                JsonNode typeNode = this.types.get(type);

                if (typeNode != null) {
                    newTypes[count] = typeNode.asText();
                } else {

                    newTypes[count] = this.types.get("Default").asText();

                }
                count++;
            }

            return newTypes;
        }

        return new String[]{this.types.get("Default").asText()};

    }

    @Override
    public String[] getLanguage(InternModel internModel) {

        if (internModel.getLanguage() != null) {

            String[] newLanguages = new String[internModel.getLanguage().length];
            int count = 0;

            for (String language : internModel.getLanguage()) {
                JsonNode languageNode = this.language.get(language);
                if(languageNode != null) {
                    newLanguages[count] = languageNode.asText();
                } else {

                    newLanguages[count] = this.language.get("Default").asText();

                }
                count++;
            }

            return newLanguages;
        }
        return new String[]{this.language.get("Default").asText()};

    }

    @Override
    public String[] getPublicationStatus(InternModel internModel) {

        if (internModel.getPublicationStatus() != null) {

            String[] newPublicationStatuses = new String[internModel.getPublicationStatus().length];
            int count = 0;

            for (String publicationStatus : internModel.getPublicationStatus()) {
                JsonNode publicationStatusNode = this.publicationStatuses.get(publicationStatus);
                if(publicationStatusNode != null) {
                    newPublicationStatuses[count] = publicationStatusNode.asText();
                } else {

                    JsonNode defaultPS = this.publicationStatuses.get("Default");

                    if(defaultPS != null){
                        newPublicationStatuses[count] = defaultPS.asText();
                    }else{
                        newPublicationStatuses[count] = null;

                    }

                }
                count++;
            }

            return newPublicationStatuses;

        }
        return new String[]{this.publicationStatuses.get("Default").asText()};

    }


}
