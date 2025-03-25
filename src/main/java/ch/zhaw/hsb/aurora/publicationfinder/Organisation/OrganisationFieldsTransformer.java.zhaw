/*
* This file is part of the Aurora Publication Finder.
*
* (c) ZHAW HSB <apps.hsb@zhaw.ch>
*
* For the full copyright and license information, please view the LICENSE
* file that was distributed with this source code.
*/
package ch.zhaw.hsb.aurora.publicationfinder.Organisation;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;

import ch.zhaw.hsb.aurora.publicationfinder.Core.Mapping.Intern2OrganisationMapping;
import ch.zhaw.hsb.aurora.publicationfinder.Core.Model.InternModel;
import ch.zhaw.hsb.aurora.publicationfinder.Core.Model.PersonModel;
import ch.zhaw.hsb.aurora.publicationfinder.Core.Transformer.BaseOrganisationTransformer;

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
    public String[] getAuthors(InternModel internModel) {

        List<PersonModel> authorsList = internModel.getAuthors();
        String[] authors = new String[authorsList.size()];
        int count = 0;
        for (PersonModel person : authorsList) {
            authors[count++] = person.getAuthor();

        }
        return authors;

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

            String[] newLicenses = new String[internModel.getLicenses().length];
            int count = 0;

            if(internModel.getLicenses().length>0){
                for (String license : internModel.getLicenses()) {
                    JsonNode licenseNode = this.licenses.get(license);
                    if(licenseNode != null) {
                        newLicenses[count] = licenseNode.asText();
                    } else {
    
                        newLicenses[count] = this.licenses.get("Default").asText();
    
                    }
                    count++;
                }
                return newLicenses;

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
