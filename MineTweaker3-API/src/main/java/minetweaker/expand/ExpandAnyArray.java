package minetweaker.expand;

import java.util.Arrays;
import minetweaker.api.data.DataList;
import minetweaker.api.data.IData;
import org.openzen.zencode.annotations.ZenCaster;
import org.openzen.zencode.annotations.ZenExpansion;

/**
 * Makes arrays convertable to IData.
 * 
 * @author Stan Hebben
 */
@ZenExpansion("any[]")
public class ExpandAnyArray {
	@ZenCaster
	public static IData asData(IData[] values) {
		return new DataList(Arrays.asList(values), true);
	}
}
