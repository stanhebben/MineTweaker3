/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openzen.zencode.parser;

/**
 *
 * @author Stan
 */
public class ParsedPackage {
	private final String name;
	
	public ParsedPackage(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
