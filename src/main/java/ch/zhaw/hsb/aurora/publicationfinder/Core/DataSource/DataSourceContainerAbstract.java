/*
* This file is part of the Aurora Publication Finder.
*
* (c) ZHAW HSB <apps.hsb@zhaw.ch>
*
* For the full copyright and license information, please view the LICENSE
* file that was distributed with this source code.
*/
package ch.zhaw.hsb.aurora.publicationfinder.Core.DataSource;

import java.util.ArrayList;

/**
 * This abstract class defines the characteristics of a container with registered data sources and provides defaults.
 * 
 * @author Dana Ghousson ZHAW
 * @author Iris Hausmann ZHAW
 */
public abstract class DataSourceContainerAbstract {
    protected ArrayList<DataSourceProviderInterface> dataSourceProviders;

    /**
     * Constructor
     */
    public DataSourceContainerAbstract() {
        this.dataSourceProviders = new ArrayList<DataSourceProviderInterface>(); 
    }


    /**
     * Method to register multiple data sources to the container
     */
    protected abstract void registerDataSources();


    /**
     * Method to register a single data source to the container
     * @param dataSourceProvider a DataSourceProvider object 
     */
    protected void registerDataSource(DataSourceProviderInterface dataSourceProvider){

        this.dataSourceProviders.add(dataSourceProvider);
    }

    /**
     * Method to get all data source providers which are registered
     * @return ArrayList<DataSourceProviderInterface>
     */
    public ArrayList<DataSourceProviderInterface> getDataSourceProviders(){
        return this.dataSourceProviders;
    }
    
}
