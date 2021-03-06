package stellarium.catalog;

import stellarium.StellarManager;
import stellarium.config.IConfigFormatter;
import stellarium.config.IStellarConfig;

public interface IStellarCatalogProvider<T extends IStellarCatalogData> {
	
	/**Gives the name of this catalog, also used as ID for this catalog*/
	public String getCatalogName();
	
	/**
	 * Provides new catalog data.
	 * @param isPhysical flag for physical(saving/packet) check
	 * */
	public T provideCatalogData(boolean isPhysical);
	
	/**
	 * Provides physical copy of logical catalog data.
	 * @param logical logical catalog data
	 * */
	public T providePhysicalData(T logical);
	
	/**
	 * Provides new catalog.
	 * @param manager the StellarManager instance
	 * @param data physical catalog data
	 * */
	public IStellarCatalog provideCatalog(StellarManager manager, T data);
	
	/**
	 * @return true iff. this catalog contains variables, etc.
	 * */
	public boolean isVariable();
	
	/**
	 * @return true iff. this catalog contains only pointy objects.
	 * */
	public boolean isPointy();
	
	/**
	 * gives the (Average) Magnitude of this catalog.
	 * this will be used to determine render or not.
	 * */
	public double getMag();
	
	/**
	 * Priority value for Search.
	 * The bigger value, the prior it be.
	 * */
	public double prioritySearch();
	
	/**
	 * Priority value for Render.
	 * The bigger value, the prior it be.
	 * (NOTE: prior object will be rendered first.)
	 * */
	public double priorityRender();
	
	/**
	 * called while initialization to load this catalog.
	 * */
	public void load();

	
	/**
	 * gives the type of object this catalog contains.
	 * */
	public EnumCatalogType getType();
}
