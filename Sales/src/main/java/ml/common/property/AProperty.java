package ml.common.property;

import ml.deducer.deductionrules.ADeductionRule;
import ml.deducer.deductionrules.AnyDeductionRule;

/**
 * This class is abstract for all the properties 
 *  
 * @author noam
 *
 */
public abstract class AProperty {
	
	ADeductionRule deducer;

	public AProperty(ADeductionRule deducer) {
		this.deducer = deducer;
	}
	
	public AProperty() {
		this.deducer = new AnyDeductionRule();
	}

	@Override
	public int hashCode() {

		return 0;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		AProperty other = (AProperty) o;
		if (deducer == null) {
			if (other.deducer != null)
				return false;
		} else if (!deducer.equals(other.deducer))
			return false;
		return true;
	}

	public ADeductionRule getDeductionRule() {
		return deducer;
	}
	
	public abstract String getDescription();
	
	
}
