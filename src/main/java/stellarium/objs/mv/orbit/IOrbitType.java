package stellarium.objs.mv.orbit;

import stellarium.config.IConfigCategory;
import stellarium.config.IStellarConfig;
import stellarium.objs.mv.CMvEntry;
import stellarium.objs.mv.StellarMvLogical;

public interface IOrbitType {
	
	/**gives name of this type*/
	public String getTypeName();
	
	/**initialization for this type*/
	public void init();
	
	
	/**forms configuration for this type
	 * @param mv */
	public void formatConfig(IConfigCategory cat, StellarMvLogical mv);
	
	/**remove properties from this type*/
	public void removeConfig(IConfigCategory cat);
	
	/**provides Orbit from the entry*/
	public Orbit provideOrbit(CMvEntry e);
	
	/**applies the configuration to the orbit*/
	public void apply(Orbit orbit, IConfigCategory cat);
	
	/**saves the orbit as configuration*/
	public void save(Orbit orbit, IConfigCategory cat);
	
	/**checks the current settings and forms the orbit*/
	public void formOrbit(Orbit orb, IStellarConfig subConfig);
	
	/**sets the target orbit to be scaled orbit of reference orbit*/
	public void setScaled(Orbit ref, Orbit target, double scale);
	
	
	/**@return true iff. the orbit with this type has parent orbit.*/
	public boolean hasParent();
	
	/**do tasks needed for remove*/
	public void onRemove(Orbit orbit);

}
