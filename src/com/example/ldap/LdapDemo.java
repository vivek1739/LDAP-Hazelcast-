package com.example.ldap;

import java.io.IOException;

import org.apache.directory.api.ldap.model.cursor.EntryCursor;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.api.ldap.model.message.SearchScope;
import org.apache.directory.ldap.client.api.LdapConnection;
import org.apache.directory.ldap.client.api.LdapNetworkConnection;

class LdapDemo {
	
	public static void main(String[] args) throws LdapException, IOException {
		
		
		
		LdapConnection connection=new LdapNetworkConnection("localhost", 10389);
		connection.bind("uid=admin,ou=system","secret");
		
		SearchScope scope=SearchScope.SUBTREE;
		EntryCursor cursor=connection.se
		
		connection.close();
		
		
		
	}

}
