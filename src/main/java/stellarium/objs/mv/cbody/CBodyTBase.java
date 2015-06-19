package stellarium.objs.mv.cbody;

import sciapi.api.value.euclidian.CrossUtil;
import sciapi.api.value.euclidian.ECoord;
import sciapi.api.value.euclidian.EVector;
import stellarium.config.IConfigCategory;
import stellarium.config.IConfigProperty;
import stellarium.config.IMConfigProperty;
import stellarium.config.IPropertyRelation;
import stellarium.lang.CPropLangStrs;
import stellarium.lang.CPropLangStrsCBody;
import stellarium.objs.mv.CMvEntry;
import stellarium.util.math.VecMath;
import stellarium.world.CWorldProvider;

public abstract class CBodyTBase implements ICBodyType {

	@Override
	public void formatConfig(IConfigCategory cat, boolean isMain) {
		// TODO Auto-generated method stub
		
		IConfigProperty tidalLock = null;
		
		if(!isMain)
		{
			 tidalLock = CPropLangStrs.addProperty(cat, "toggleYesNo", CPropLangStrsCBody.tidalLocked, false);
		}
		
		IConfigProperty pole = CPropLangStrs.addProperty(cat, "vector3", CPropLangStrsCBody.pole, new EVector(0.0, 0.0, 1.0));
		IConfigProperty tickViewVernal = CPropLangStrs.addProperty(cat, "udouble", CPropLangStrsCBody.dayViewVernal, 0.25);
		
		IConfigProperty periodRot = CPropLangStrs.addProperty(cat, "udouble", CPropLangStrsCBody.periodRotation, 1.0);
		
		IConfigProperty hasPrec = CPropLangStrs.addProperty(cat, "toggleYesNo", CPropLangStrsCBody.hasPrecession, false);
		IConfigProperty periodPrec = CPropLangStrs.addProperty(cat, "udouble", CPropLangStrsCBody.periodPrecession, 26000.0);
		
		cat.addPropertyRelation(new IPropertyRelation() {
			IMConfigProperty<Boolean> hasPrec;
			IMConfigProperty periodPrec;
			
			@Override
			public void setProps(IMConfigProperty... props) {
				this.hasPrec = props[0];
				this.periodPrec = props[1];
			}

			@Override
			public void onEnable(int i) {
				if(i == 1)
					periodPrec.setEnabled(hasPrec.getVal());
			}

			@Override
			public void onDisable(int i) { }

			@Override
			public void onValueChange(int i) {
				periodPrec.setEnabled(hasPrec.getVal());
			}

			@Override
			public String getRelationToolTip() {
				return "";
			}
			
		}, hasPrec, periodPrec);
		
		if(!isMain) {
			cat.addPropertyRelation(new IPropertyRelation() {
				
				IMConfigProperty<Boolean> check;
				IMConfigProperty[] props;
				
				@Override
				public void setProps(IMConfigProperty... props) {
					this.check = props[0];
					this.props = new IMConfigProperty[props.length - 1];
					System.arraycopy(props, 1, this.props, 0, props.length - 1);
				}

				@Override
				public void onEnable(int i) {
					if(check.getVal())
						for(IMConfigProperty prop : props)
							prop.setEnabled(false);
				}

				@Override
				public void onDisable(int i) { }

				@Override
				public void onValueChange(int i) {
					if(i == 0 && check.getVal())
						for(IMConfigProperty prop : props)
							prop.setEnabled(false);
				}

				@Override
				public String getRelationToolTip() {
					return "";
				}
				
			}, tidalLock, pole, periodRot, hasPrec, periodPrec);
		}
	}

	@Override
	public void removeConfig(IConfigCategory cat) {
		// TODO Auto-generated method stub
		cat.removeProperty(CPropLangStrsCBody.tidalLocked);
		cat.removeProperty(CPropLangStrsCBody.pole);
		cat.removeProperty(CPropLangStrsCBody.dayViewVernal);
		cat.removeProperty(CPropLangStrsCBody.periodRotation);
		cat.removeProperty(CPropLangStrsCBody.hasPrecession);
		cat.removeProperty(CPropLangStrsCBody.periodPrecession);
	}

	@Override
	public void apply(CBody body, IConfigCategory cat) {
		// TODO Auto-generated method stub
		body.isTidalLocked = (boolean) cat.getProperty(CPropLangStrsCBody.tidalLocked).getVal();
		boolean hasPrec = (boolean) cat.getProperty(CPropLangStrsCBody.hasPrecession).getVal();
		ECoord vernalCoord;
		
		if(!body.isTidalLocked) {
			IConfigProperty<EVector> propPole = cat.getProperty(CPropLangStrsCBody.pole);
			EVector pole = propPole.getVal();
			EVector vernal = new EVector(1.0, 0.0, 0.0), third = new EVector(3);
			
			third.set(CrossUtil.cross(pole, vernal));
			vernal.set(CrossUtil.cross(third, pole));
			vernalCoord = new ECoord(pole, vernal, third);
			
			body.w_rot = 2 * Math.PI / (double) cat.getProperty(CPropLangStrsCBody.periodRotation).getVal();
			
			if(hasPrec)
				body.w_prec = 2 * Math.PI / (double) cat.getProperty(CPropLangStrsCBody.periodPrecession).getVal();
			else body.w_prec = 0.0;
		} else {
			body.w_rot = 2 * Math.PI / body.getEntry().orbit().getAvgPeriod();
			vernalCoord = VecMath.copyCoord(body.getEntry().orbit().getOrbCoord(0.0));
		}
		
		double dayViewVernal = (double) cat.getProperty(CPropLangStrsCBody.dayViewVernal).getVal();
		body.initialCoord = VecMath.rotateCoordZ(vernalCoord, -body.w_rot * dayViewVernal);
	}

	@Override
	public void save(CBody body, IConfigCategory cfg) {
		// TODO Auto-generated method stub
		cfg.getProperty(CPropLangStrsCBody.tidalLocked).simSetVal(body.isTidalLocked);
		
		if(!body.isTidalLocked) {
			cfg.getProperty(CPropLangStrsCBody.periodRotation).simSetEnabled(true);
			cfg.getProperty(CPropLangStrsCBody.periodRotation).simSetVal(2 * Math.PI / body.w_rot);
			
			cfg.getProperty(CPropLangStrsCBody.hasPrecession).simSetEnabled(true);
			
			if(body.w_prec != 0.0) {
				cfg.getProperty(CPropLangStrsCBody.hasPrecession).simSetVal(true);
				
				cfg.getProperty(CPropLangStrsCBody.periodPrecession).simSetEnabled(true);
				cfg.getProperty(CPropLangStrsCBody.periodPrecession).simSetVal(2 * Math.PI / body.w_prec);
			} else {
				cfg.getProperty(CPropLangStrsCBody.hasPrecession).simSetVal(false);
			}
		}
	}

	@Override
	public void formCBody(CBody body) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCopy(CBody ref, CBody target) {
		// TODO Auto-generated method stub
		target.initialCoord = VecMath.copyCoord(ref.initialCoord);
		target.isTidalLocked = ref.isTidalLocked;
		target.w_prec = ref.w_prec;
		target.w_rot = ref.w_rot;
	}

	@Override
	public void onRemove(CBody body) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ICBodyRenderer getCBodyRenderer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CWorldProvider getCWorldProvider() {
		// TODO Auto-generated method stub
		return null;
	}

}
