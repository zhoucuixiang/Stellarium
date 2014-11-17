package stellarium.config;

import java.util.List;

public interface IStellarConfig {
	
	/**Sets category type of this configuration*/
	public void setCategoryType(EnumCategoryType t);
	
	/**
	 * Sets whether category is modifiable or not
	 * @param modif modifiability flag
	 * @param warn warning flag. when it is enabled, any modification will be warned.
	 * */
	public boolean setModifiable(boolean modif, boolean warn);
	
	/**Adds Configuration Arrangement Modification Listener.*/
	public void addAMListener(ICfgArrMListener list);
	
	/**Marks a Category as Immutable.*/
	public void markImmutable(IConfigCategory cat);
	
	
	/**Adds category for this configuration.*/
	public IConfigCategory addCategory(String cid);
	
	/**Removes category for this configuration.*/
	public void removeCategory(String cid);
	
	/**Gets category for this configuration.*/
	public IConfigCategory getCategory(String cid);
	
	/**Gets all categories for this configuration.*/
	public List<IConfigCategory> getAllCategories();
	
	
	/**
	 * Sets category as sub-configuration.
	 * Only valid for <code>{@link EnumCategoryType#ConfigList}</code> 
	 * */
	public IStellarConfig setSubConfig(IConfigCategory cat);
	
	/**
	 * Gets sub-configuration from category.
	 * Only valid for <code>{@link EnumCategoryType#ConfigList}</code> 
	 * */
	public IStellarConfig getSubConfig(IConfigCategory cat);
	
	
	/**Adds sub-category for this configuration.*/
	public IConfigCategory addSubCategory(IConfigCategory parent, String subid);

	/**Removes sub-category for this configuration.*/
	public void removeSubCategory(IConfigCategory parent, String subid);
	
	/**Gets sub-category for this configuration.*/
	public IConfigCategory getSubCategory(IConfigCategory parent, String subid);

	/**Gets all sub-categories with certain parent for this configuration.*/
	public List<IConfigCategory> getAllSubCategories(IConfigCategory parent);
}