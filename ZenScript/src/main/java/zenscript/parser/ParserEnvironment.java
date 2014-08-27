/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zenscript.parser;

import java.util.ArrayList;
import java.util.List;
import stanhebben.zenscript.IZenCompileEnvironment;

/**
 *
 * @author Stan
 */
public class ParserEnvironment {
	private final IZenCompileEnvironment compileEnvironment;
	private final List<ParsedModule> modules;
	
	public ParserEnvironment(IZenCompileEnvironment compileEnvironment) {
		this.compileEnvironment = compileEnvironment;
		modules = new ArrayList<ParsedModule>();
	}
	
	public ParsedModule makeModule(String name, IFileLoader fileLoader) {
		ParsedModule result = new ParsedModule(this, fileLoader, name);
		modules.add(result);
		return result;
	}
	
	public IZenCompileEnvironment getCompileEnvironment() {
		return compileEnvironment;
	}
}
