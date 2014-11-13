/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stanhebben.zenscript.expression;

import java.lang.reflect.Method;
import java.util.List;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.openzen.zencode.symbolic.scope.ScopeClass;
import org.openzen.zencode.symbolic.scope.IScopeMethod;
import org.openzen.zencode.symbolic.scope.ScopeMethod;
import org.openzen.zencode.symbolic.scope.IScopeClass;
import org.openzen.zencode.symbolic.method.MethodParameter;
import org.openzen.zencode.symbolic.method.MethodHeader;
import stanhebben.zenscript.statements.Statement;
import stanhebben.zenscript.type.ZenType;
import org.openzen.zencode.util.MethodOutput;
import org.openzen.zencode.util.CodePosition;
import stanhebben.zenscript.symbols.SymbolLocal;
import static org.openzen.zencode.util.ZenTypeUtil.descriptor;
import static org.openzen.zencode.util.ZenTypeUtil.internal;

/**
 *
 * @author Stanneke
 */
public class ExpressionJavaLambda extends Expression {
	private final Class interfaceClass;
	private final MethodHeader methodHeader;
	private final List<Statement> statements;
	
	private final ZenType type;
	
	public ExpressionJavaLambda(
			CodePosition position,
			IScopeMethod scope,
			Class interfaceClass,
			MethodHeader methodHeader,
			List<Statement> statements,
			ZenType type) {
		super(position, scope);
		
		this.interfaceClass = interfaceClass;
		this.methodHeader = methodHeader;
		this.statements = statements;
		
		this.type = type;
	}

	@Override
	public ZenType getType() {
		return type;
	}

	@Override
	public void compile(boolean result, MethodOutput output) {
		if (!result) return;
		
		Method method = interfaceClass.getMethods()[0];
		
		// generate class
		String clsName = getScope().makeClassName();
		
		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		cw.visit(Opcodes.V1_6, Opcodes.ACC_PUBLIC, clsName, null, "java/lang/Object", new String[] { internal(interfaceClass) });
		
		MethodOutput constructor = new MethodOutput(cw, Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
		constructor.start();
		constructor.loadObject(0);
		constructor.invokeSpecial("java/lang/Object", "<init>", "()V");
		constructor.ret();
		constructor.end();
		
		MethodOutput outputMethod = new MethodOutput(cw, Opcodes.ACC_PUBLIC, method.getName(), descriptor(method), null, null);
		
		IScopeClass environmentClass = new ScopeClass(getScope());
		IScopeMethod environmentMethod = new ScopeMethod(environmentClass, methodHeader.getReturnType());
		
		for (int i = 0; i < methodHeader.getArguments().size(); i++) {
			MethodParameter argument = methodHeader.getArguments().get(i);
			environmentMethod.putValue(
					argument.getName(),
					argument.getLocal(),
					getPosition());
		}
		
		outputMethod.start();
		for (Statement statement : statements) {
			statement.compile(outputMethod);
		}
		outputMethod.end();
		
		getScope().putClass(clsName, cw.toByteArray());
		
		// make class instance
		output.newObject(clsName);
		output.dup();
		output.construct(clsName);
	}
}
