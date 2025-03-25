/*
* This file is part of the Aurora Publication Finder.
*
* (c) ZHAW HSB <apps.hsb@zhaw.ch>
*
* For the full copyright and license information, please view the LICENSE
* file that was distributed with this source code.
*/
package ch.zhaw.hsb.aurora.publicationfinder.Organisation.Providers.CrossRef;

import java.util.Map;
import java.util.stream.Collectors;

import ch.zhaw.hsb.aurora.publicationfinder.Core.DataSource.BaseDataSourceProviderAbstract;
import ch.zhaw.hsb.aurora.publicationfinder.Core.DataSource.DataSourceProviderInterface;
import ch.zhaw.hsb.aurora.publicationfinder.Core.Model.InternModel;
import ch.zhaw.hsb.aurora.publicationfinder.Organisation.Providers.CrossRef.Transformer.CrossRefDataSourceTransformer;

/**
 * This class provides all actions needed for the crossref data source.
 * 
 * @author Dana Ghousson ZHAW
 * @author Iris Hausmann ZHAW
 */
public class CrossRefDataSourceProvider extends BaseDataSourceProviderAbstract {
    

    /**
     * Constructor
     */
    public CrossRefDataSourceProvider() {
        super("crossref");
    }

    @Override
    public DataSourceProviderInterface transform() {

        System.out.println("CrossRef transform: ");

        CrossRefDataSourceTransformer transformer = new CrossRefDataSourceTransformer(this.getBuiltData(),
                this.getMapping(), this.getIdName(), this.getName());


        // damit Transformer generisch bleibt auch für OrganisationTransformer, welche
        // dann wahrscheinlich ZHAWModel und FHNWModel haben werden (?)
        Map<String, InternModel> newMap = transformer.getTransformedData().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> (InternModel) e.getValue()));
        this.setTransformedData(newMap);


        return this;
    }

   
}
