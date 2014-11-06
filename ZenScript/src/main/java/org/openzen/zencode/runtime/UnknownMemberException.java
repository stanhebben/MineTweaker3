/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openzen.zencode.runtime;

/**
 *
 * @author Stan
 */
public class UnknownMemberException extends RuntimeException {
	private final String member;
	
	public UnknownMemberException(String member, String type) {
		super(type + " doesn have a member " + member);
		
		this.member = member;
	}
	
	public String getMember() {
		return member;
	}
}
