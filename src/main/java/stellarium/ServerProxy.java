package stellarium;

import cpw.mods.fml.relauncher.Side;
import stellarium.stellars.StellarManager;

public class ServerProxy extends BaseProxy{

	@Override
	public void InitSided(StellarManager m) {
		m.side = Side.CLIENT;
		
	}

}