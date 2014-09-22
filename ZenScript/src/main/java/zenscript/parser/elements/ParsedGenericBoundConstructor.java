/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zenscript.parser.elements;

/**
 *
 * @author Stan
 */
public class ParsedGenericBoundConstructor implements IParsedGenericBound {
	private final ParsedFunctionSignature signature;
	
	public ParsedGenericBoundConstructor(ParsedFunctionSignature signature) {
		this.signature = signature;
	}
}
