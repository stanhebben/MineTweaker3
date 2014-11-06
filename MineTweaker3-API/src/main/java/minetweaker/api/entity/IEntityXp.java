package minetweaker.api.entity;

import org.openzen.zencode.annotations.ZenClass;
import org.openzen.zencode.annotations.ZenGetter;

/**
 * Represents an xp orb.
 * 
 * @author Stan Hebben
 */
@ZenClass("minetweaker.entity.IEntityXp")
public interface IEntityXp extends IEntity {
	/**
	 * Gets the amount of xp in this orb.
	 * 
	 * @return xp amount
	 */
	@ZenGetter("xp")
	public float getXp();
}
