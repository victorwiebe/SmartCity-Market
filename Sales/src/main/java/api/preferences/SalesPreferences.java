package api.preferences;

import java.io.Serializable;

/**
 * This class represents sale preferences used by the Sales modules 
 * @author noam
 *
 */
public class SalesPreferences implements Serializable {

	/**
	 * Serial number for Serialization
	 */
	private static final long serialVersionUID = 0x52BCBD32DDF4D197L;

	double maxDiscount = 0.5;

	public double getMaxDiscount() {
		return maxDiscount;
	}

	public SalesPreferences(double maxDiscount) {
		this.maxDiscount = maxDiscount;
	}
	
	
}
