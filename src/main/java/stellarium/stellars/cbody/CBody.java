package stellarium.stellars.cbody;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import stellarium.initials.CCertificateHelper;
import stellarium.lighting.*;
import stellarium.stellars.*;
import stellarium.stellars.local.*;
import stellarium.stellars.orbit.*;
import stellarium.util.math.*;
import stellarium.viewrender.render.RBase;
import stellarium.viewrender.render.RHost;
import stellarium.viewrender.render.StellarRenders;
import stellarium.viewrender.viewpoint.ViewPoint;

public abstract class CBody {
	

	//Orbit of the Celestial Body
	public Orbit theOrbit;
	
	//Name of the Celestial Body
	public String Name;
	
	//Presence of World
	public boolean HasWorld;
	
	//Mass (M_sun), Radius (AU)
	public double Mass, Radius;
	
	//Resonance with its parent body
	public boolean IsResonant=false;
	
	//Initial Pole, Prime Meridian, East (Unit Vector)
	public Vec Pol0;
	
	//On Equatorial Plane, Initial Angle from projection of x-axis to Prime Meridian (Degree)
	double PMAngle;
	
	//Angular velocity of Precession and Rotation (Degree/yr)
	public double Prec, Rot;
		
	public double Temp;
		
	//Luminosity of the Celestial Body (L_sun)
	public double Lum;

	//Pole, Prime Meridian, East-from Prime Meridian (Unit Vector)
	public Vec Pol, PrMer, East;

		
	//Texture Locations
	@SideOnly(Side.CLIENT)
	public String PointyTexLoc;
	
	@SideOnly(Side.CLIENT)
	public String ImgTexLoc;
		
	//StellarManager
	public StellarManager manager;
	
	
	
	public static final String Resonance = "R";
	public static final String PPole = "Po";
	public static final String PPMAngle = "PA";
	public static final String PRot="Ro";
	public static final String PPrec="Pr";
	
	public static final String PRadius="Ra";
	
	protected int j;
	
	protected double Lo, La;
	

	public abstract void RegisterCBody();

	
	public void Construct(String[] ConBody){
		String[] ConCB = null;
		int num=ConBody.length;
		j=0;
		while(j<num){
			ConstructLoop(ConBody);
			j++;
		}
		
		Pol=Transforms.GetVec(new SpCoord(Lo, La));
	}
	
	public void ConstructLoop(String[] ConBody) {
		if(!IsResonant && ConBody[j].equals(PPole)){
			j++;
			Lo=Spmath.StrtoD(ConBody[j]);
			j++;
			La=Spmath.StrtoD(ConBody[j]);
		}
		if(ConBody[j].equals(PPMAngle)){
			j++;
			PMAngle=Spmath.StrtoD(ConBody[j]);
		}
		if(!IsResonant && ConBody[j].equals(PRot)){
			j++;
			Rot=Spmath.StrtoD(ConBody[j]);
		}
		if(theOrbit instanceof OrbitMv && !IsResonant && ConBody[j].equals(PPrec)){
			j++;
			Prec=Spmath.StrtoD(ConBody[j]);
		}
		if(ConBody[j].equals(Resonance)){
			this.IsResonant=true;
		}
		if(IsResonant){
			Rot=((OrbitMv)theOrbit).GetAvgRot();
		}
		
		if(ConBody[j].equals(PRadius)){
			j++;
			Radius=Spmath.StrtoD(ConBody[j]);
		}
	}
	
	public void PreCertificate() {
		
	}
	
	public void Certificate(){
		CCertificateHelper cch = null;
		if(IsResonant && theOrbit instanceof OrbitSt)
			cch.IllegalConfig(this.Name+"can't be Resonant for It is on Stationary Orbit.");
		if((theOrbit instanceof OrbitMv) && ((OrbitMv)theOrbit).Hill_Radius < this.Radius)
			cch.Unstable("The Celestial Body "+this.Name+" Is Going to be Shattered!");

	}
	


	
	@SideOnly(Side.SERVER)
	public void Update(double yr){
		UpdateCoord(yr);
		UpdateLuminosity();
	}
	
	@SideOnly(Side.SERVER)
	protected void UpdateCoord(double yr){
		if(theOrbit instanceof OrbitSt)
			Pol=Pol0;
		else{
			OrbitMv orbit=(OrbitMv)theOrbit;
			if(IsResonant)
				Pol=orbit.Pol;
			else Pol=Spmath.AxisRotate(orbit.Pol, Prec*yr, Pol0);
		}
		PrMer=Spmath.Projection(Pol, new Vec(1.0, 0.0, 0.0));
		PrMer=Spmath.AxisRotate(Pol, PMAngle+Rot*yr, PrMer);
		East=Vec.Cross(Pol, PrMer);
	}
	
	@SideOnly(Side.SERVER)
	protected abstract void UpdateLuminosity();

	
	public Vec GetZen(double lat, double lon){
		Vec Pl=Vec.Add(Vec.Add(Vec.Mul(Pol, Spmath.sind(lat)), Vec.Mul(PrMer, Spmath.cosd(lat)*Spmath.cosd(lon))), Vec.Mul(East,Spmath.cosd(lat)*Spmath.sind(lon)));
		return Vec.Mul(Pl, Radius);
	}
	
	public Vec GetZenDir(double lat, double lon){
		return Vec.Add(Vec.Add(Vec.Mul(Pol, Spmath.sind(lat)), Vec.Mul(PrMer, Spmath.cosd(lat)*Spmath.cosd(lon))), Vec.Mul(East,Spmath.cosd(lat)*Spmath.sind(lon)));
	}

	/*
	 * Local for Viewpoint
	 * Start
	 * */

	@SideOnly(Side.SERVER)
	public void GetLocalized(ViewPoint vp, LocalCValue lcv){
		lcv.Size = Spmath.Degrees(2*Radius/lcv.Dist);
		lcv.Pol.Set(Pol);
		lcv.PrMer.Set(PrMer);
		lcv.East.Set(East);
	}
	
	@SideOnly(Side.SERVER)
	public boolean IsHosting(double heightau){
		if(heightau<=this.Radius*0.01)
			return true;
		return false;
	}
	
	/*
	 * For Client
	 * Start
	 * */
	
	@SideOnly(Side.CLIENT)
	abstract public RBase RenderBody(LocalCValue lcv, double res);
	
	@SideOnly(Side.CLIENT)
	public abstract void DrawImg();


	@SideOnly(Side.CLIENT)
	public RHost RenderHost(LocalCValue lcv) {
		return null;		
	}
	
	@SideOnly(Side.CLIENT)
	abstract public double GetMag();









	
}