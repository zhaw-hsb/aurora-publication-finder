/*
* This file is part of the Aurora Publication Finder.
*
* (c) ZHAW HSB <apps.hsb@zhaw.ch>
*
* For the full copyright and license information, please view the LICENSE
* file that was distributed with this source code.
*/
package ch.zhaw.hsb.aurora.publicationfinder;

import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ch.zhaw.hsb.aurora.publicationfinder.Core.Configuration.PropertyProviderConfiguration;
import ch.zhaw.hsb.aurora.publicationfinder.Core.Configuration.UserInputConfiguration;
import ch.zhaw.hsb.aurora.publicationfinder.Core.DataSource.DataSourceContainerAbstract;
import ch.zhaw.hsb.aurora.publicationfinder.Core.DataSource.DataSourceProviderInterface;
import ch.zhaw.hsb.aurora.publicationfinder.Core.Fusion.Fusion;
import ch.zhaw.hsb.aurora.publicationfinder.Core.LogCollector.AdminLogCollector;
import ch.zhaw.hsb.aurora.publicationfinder.Core.LogCollector.HelpdeskLogCollector;
import ch.zhaw.hsb.aurora.publicationfinder.Core.Matcher.SingleFieldMatcher;
import ch.zhaw.hsb.aurora.publicationfinder.Core.Merger.FieldMerger;
import ch.zhaw.hsb.aurora.publicationfinder.Core.Service.EmailReportService;
import ch.zhaw.hsb.aurora.publicationfinder.Core.Service.EmailService;
import ch.zhaw.hsb.aurora.publicationfinder.Core.Util.FileUtil;
import ch.zhaw.hsb.aurora.publicationfinder.Organisation.OrganisationDuplicateCheck;
import ch.zhaw.hsb.aurora.publicationfinder.Organisation.OrganisationImporter;
import ch.zhaw.hsb.aurora.publicationfinder.Organisation.Providers.CrossRef.CrossRefDataSourceProvider;
import ch.zhaw.hsb.aurora.publicationfinder.Organisation.Providers.OpenAlex.OpenAlexDataSourceProvider;

/**
 * This class runs the publication finder processes
 * 
 * @author Dana Ghousson ZHAW
 * @author Iris Hausmann ZHAW
 */
public class Application extends DataSourceContainerAbstract implements Runnable {

    private final EmailReportService emailReportService;
    protected String date;


    private Application(){

        this.emailReportService = new EmailReportService(new EmailService());

        //on errors send email and exit program
        AdminLogCollector.setOnErrorHandler(errors -> {
            emailReportService.sendReports(true);
            System.exit(1);
        });

    }

    /**
     * Method to create the application
     * @return Application
     */
    public static Application createApplication() {

        Application application = new Application();
        application.registerDataSources();

        
        return application;
    }

    
    protected void registerDataSources() {

        this.registerDataSource(new OpenAlexDataSourceProvider());
        // TODO: to test matching and merging three sources:
        // this.registerDataSource(new OpenAlexDataSourceProvider());
        this.registerDataSource(new CrossRefDataSourceProvider());

    }

    protected void setDate(String date){
        this.date = date;

    }


    @Override
    public void run() {

        UserInputConfiguration.setDate(this.date);

        
        List<Map<String, Map<String, Object>>> listOfMaps = new ArrayList<Map<String, Map<String, Object>>>();

        // Transform (WP3)
        for (DataSourceProviderInterface dataSourceProvider : this.dataSourceProviders) {

            dataSourceProvider = dataSourceProvider.setup().retrieve().build().transform().transformForOrganisation();
            listOfMaps.add(dataSourceProvider.getOrganisationData());

        }

        // Match (WP4)
        SingleFieldMatcher matcher = new SingleFieldMatcher(listOfMaps, this.dataSourceProviders);
        List<Map<String, Map<String, Object>>> listOfMapsMatched = matcher.getAllMatchedData();

        // Merge (WP4)
        FieldMerger merger = new FieldMerger(listOfMapsMatched, this.dataSourceProviders);
        Map<String, Map<String, Object>> mergedData = merger.getAllMergedData();

        // Fuse merged and non-matched back together
        Map<String, Map<String, Object>> allData = Fusion.fuseMergedAndNonMatched(mergedData, listOfMaps);
        HelpdeskLogCollector.logInfo("All found and merged items: "+ allData.size());

        // Duplicate Check (WP4)
        OrganisationDuplicateCheck duplicateCheck = new OrganisationDuplicateCheck(allData);
        Map<String, Map<String, Object>> allDeduplicatedData = duplicateCheck.getDeduplicatedData();

        // Import (WP4/WP7)
        OrganisationImporter importer = new OrganisationImporter(allDeduplicatedData);
        importer.importData();

        // Send email reports
        emailReportService.sendReports(false);

        // Timestamp to limit data
        FileUtil.writeToFile(Paths.get(PropertyProviderConfiguration.getExternalFilePath()+"/timestamp.txt"),
        LocalDate.now().toString());

    }



}
