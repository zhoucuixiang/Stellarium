package stellarium.world;

import sciapi.api.value.IValRef;
import sciapi.api.value.euclidian.EVector;
import sciapi.api.value.euclidian.EVectorSet;
import sciapi.api.value.util.COp;
import stellarium.settings.StellarSettings;
import stellarium.stellars.ExtinctionRefraction;
import stellarium.util.math.Spmath;
import stellarium.util.math.VecMath;
import net.minecraft.world.WorldProviderSurface;

public class StellarWorldProvider extends WorldProviderSurface {
		
    public float calculateCelestialAngle(long par1, float par3)
    {
    	if(StellarSettings.Earth.EcRPos == null)
    		StellarSettings.Update(par1+par3, isSurfaceWorld());
    	
    	IValRef<EVector> sun = EVectorSet.ins(3).getNew();
    	
    	sun.set(StellarSettings.Sun.GetPosition());
    	sun.set(ExtinctionRefraction.Refraction(sun, true));
    	sun.set(VecMath.normalize(sun));
    	
    	double h=Math.asin(VecMath.getZ(sun));
    	
    	if(VecMath.getCoord(sun, 0).asDouble()<0) h=Math.PI-h;
    	if(VecMath.getCoord(sun, 0).asDouble()>0 && h<0) h=h+2*Math.PI;
    	    	
    	return (float)(Spmath.fmod((h/2/Math.PI)+0.75,2));
    }

    public int getMoonPhase(long par1)
    {
    	if(StellarSettings.Earth.EcRPos==null)
    		StellarSettings.Update(par1, isSurfaceWorld());
    	return (int)(StellarSettings.Moon.Phase_Time()*8);
    }

}
