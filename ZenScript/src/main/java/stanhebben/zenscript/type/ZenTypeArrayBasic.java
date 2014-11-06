package stanhebben.zenscript.type;

import org.objectweb.asm.Label;
import org.objectweb.asm.Type;
import org.openzen.zencode.symbolic.AccessScope;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.expression.ExpressionArrayGet;
import stanhebben.zenscript.expression.ExpressionArrayLength;
import stanhebben.zenscript.expression.ExpressionArraySet;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import org.openzen.zencode.symbolic.type.casting.CastingRuleArrayArray;
import org.openzen.zencode.symbolic.type.casting.CastingRuleArrayList;
import org.openzen.zencode.symbolic.type.casting.CastingRuleDelegateArray;
import org.openzen.zencode.symbolic.type.casting.ICastingRule;
import org.openzen.zencode.symbolic.type.casting.ICastingRuleDelegate;
import stanhebben.zenscript.util.MethodOutput;
import org.openzen.zencode.util.CodePosition;

public class ZenTypeArrayBasic extends ZenTypeArray {
	private final Type asmType;
	
	public ZenTypeArrayBasic(ZenType base) {
		super(base);
		
		asmType = Type.getType("[" + base.toASMType().getDescriptor());
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof ZenTypeArrayBasic) {
			ZenTypeArrayBasic o = (ZenTypeArrayBasic) other;
			return o.getBaseType().equals(getBaseType());
		}
		
		return false;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 23 * hash + (this.getBaseType() != null ? this.getBaseType().hashCode() : 0);
		return hash;
	}
	
	@Override
	public ICastingRule getCastingRule(AccessScope accessScope, ZenType type) {
		ZenType any = getScope().getTypes().ANY;
		
		ICastingRule base = super.getCastingRule(accessScope, type);
		if (base == null && getBaseType() == any && type instanceof ZenTypeArray) {
			ZenType toBaseType = ((ZenTypeArray)type).getBaseType();
			if (type instanceof ZenTypeArrayBasic) {
				return new CastingRuleArrayArray(any.getCastingRule(accessScope, toBaseType), this, (ZenTypeArrayBasic) type);
			} else if (type instanceof ZenTypeArrayList) {
				return new CastingRuleArrayList(any.getCastingRule(accessScope, toBaseType), this, (ZenTypeArrayList) type);
			} else {
				throw new RuntimeException("Invalid array type: " + type);
			}
		} else {
			return base;
		}
	}
	
	@Override
	public void constructCastingRules(AccessScope accessScope, ICastingRuleDelegate rules, boolean followCasters) {
		ICastingRuleDelegate arrayRules = new CastingRuleDelegateArray(getScope(), rules, this);
		getBaseType().constructCastingRules(accessScope, arrayRules, followCasters);
		
		if (followCasters) {
			constructExpansionCastingRules(accessScope, rules);
		}
	}
	
	@Override
	public IZenIterator makeIterator(int numValues) {
		if (numValues == 1) {
			return new ValueIterator();
		} else if (numValues == 2) {
			return new IndexValueIterator();
		} else {
			return null;
		}
	}
	
	@Override
	public String getAnyClassName() {
		return null;
	}

	@Override
	public Type toASMType() {
		return asmType;
	}

	@Override
	public Class toJavaClass() {
		try {
			return Class.forName("[L" + getBaseType().toJavaClass().getName() + ";");
		} catch (ClassNotFoundException ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public String getSignature() {
		return "[" + getBaseType().getSignature();
	}
	
	@Override
	public IPartialExpression getMemberLength(CodePosition position, IScopeMethod environment, IPartialExpression value) {
		return new ExpressionArrayLength(position, environment, value.eval());
	}

	@Override
	public Expression indexGet(CodePosition position, IScopeMethod environment, Expression array, Expression index) {
		return new ExpressionArrayGet(
				position,
				environment,
				array,
				index.cast(position, getScope().getTypes().INT));
	}

	@Override
	public Expression indexSet(CodePosition position, IScopeMethod environment, Expression array, Expression index, Expression value) {
		return new ExpressionArraySet(
				position,
				environment,
				array,
				index.cast(position, getScope().getTypes().INT),
				value.cast(position, getBaseType()));
	}
	
	private class ValueIterator implements IZenIterator {
		private int index;
		
		public ValueIterator() {
		}

		@Override
		public void compileStart(MethodOutput output, int[] locals) {
			index = output.local(Type.INT_TYPE);
			output.iConst0();
			output.storeInt(index);
		}

		@Override
		public void compilePreIterate(MethodOutput output, int[] locals, Label exit) {
			output.dup();
			output.arrayLength();
			output.loadInt(index);
			output.ifICmpLE(exit);
			
			output.dup();
			output.loadInt(index);
			output.arrayLoad(getBaseType().toASMType());
			output.store(getBaseType().toASMType(), locals[0]);
		}
		
		@Override
		public void compilePostIterate(MethodOutput output, int[] locals, Label exit, Label repeat) {
			output.iinc(index, 1);
			output.goTo(repeat);
		}
		
		@Override
		public void compileEnd(MethodOutput output) {
			// pop the array
			output.pop();
		}

		@Override
		public ZenType getType(int i) {
			return getBaseType();
		}
	}
	
	private class IndexValueIterator implements IZenIterator {
		@Override
		public void compileStart(MethodOutput output, int[] locals) {
			output.iConst0();
			output.storeInt(locals[0]);
		}

		@Override
		public void compilePreIterate(MethodOutput output, int[] locals, Label exit) {
			output.dup();
			output.arrayLength();
			output.loadInt(locals[0]);
			output.ifICmpLE(exit);
			
			output.dup();
			output.loadInt(locals[0]);
			output.arrayLoad(getBaseType().toASMType());
			output.store(getBaseType().toASMType(), locals[1]);
		}
		
		@Override
		public void compilePostIterate(MethodOutput output, int[] locals, Label exit, Label repeat) {
			output.iinc(locals[0]);
			output.goTo(repeat);
		}
		
		@Override
		public void compileEnd(MethodOutput output) {
			// pop the array
			output.pop();
		}

		@Override
		public ZenType getType(int i) {
			return i == 0 ? getScope().getTypes().INT : getBaseType();
		}
	}
}
