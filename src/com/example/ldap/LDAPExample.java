package com.example.ldap;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.directory.api.ldap.codec.standalone.StandaloneLdapApiService;
import org.apache.directory.api.ldap.model.cursor.EntryCursor;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.message.SearchScope;
import org.apache.directory.ldap.client.api.LdapConnection;
import org.apache.directory.ldap.client.api.LdapConnectionConfig;
import org.apache.directory.ldap.client.api.LdapConnectionPool;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

public class LDAPExample  {

	private static Entry entry;
	
	/*
	 * Inner class for Zookeper event watcher
	 * Watcher is used to get notification when 1st time read or any changes made to a znode
	 */
	class Mywatcher implements Watcher{

		@Override
		public void process(WatchedEvent arg0) {
			System.out.println("Event class triggered");
			System.out.println(arg0.getState().toString());
			
			
		}
		
		
	}
	
	private Map<String,String> Zoo() throws IOException, KeeperException, InterruptedException {
		
		
		Map<String,String> map=new HashMap<String,String>();
		
		
		// Zookeeper data
		String zHost="52.208.113.111";
		String zPort="2181";
		String zAuthScheme="digest";
		String zUser="user";
		String zPassword="password";
		
		System.out.println("creating watcher object .......");
		Mywatcher watcher=new Mywatcher();
		System.out.println("creating Zclient with watcher......");
		Thread.sleep(500);
		
		//zclient gets data
		ZooKeeper zclient=new ZooKeeper(zHost+":"+zPort, 60*1000, watcher);
		zclient.addAuthInfo(zAuthScheme, (zUser+":"+zPassword).getBytes());
		
		//stat is maintained for each znode and contains basic info like version,timestamp and datalength
		Stat stat = zclient.exists("/vigil_platform/core/config/LDAP", false);
		
		System.out.println("version from stat " +stat.getVersion());
		System.out.println("data length of stat is " +stat.getDataLength());
		
		//get the data from particular node
		
		
		byte[] data = zclient.getData("/vigil_platform/core/config/LDAP/user", false, stat);
		map.put("user", new String(data));
		data = zclient.getData("/vigil_platform/core/config/LDAP/host", false, stat);
		map.put("host", new String(data));
		data = zclient.getData("/vigil_platform/core/config/LDAP/password", false, stat);
		map.put("password", new String(data));
		data = zclient.getData("/vigil_platform/core/config/LDAP/port", false, stat);
		map.put("port", new String(data));
		System.out.println(new String(data));
		
		return map;
		
	}
	

	public static void main(String[] args) throws Exception {
		
		LDAPExample example=new LDAPExample();
		Map<String,String> ldapInfo=new HashMap<String,String>();
				
		ldapInfo=example.Zoo();
		
		LdapConnectionConfig config = new LdapConnectionConfig();
		config.setLdapHost("localhost");
		config.setLdapPort(Integer.parseInt(ldapInfo.get("port")));
		config.setName(ldapInfo.get("user"));
		config.setCredentials(ldapInfo.get("password"));

		

		LdapConnectionPool connectionPool = new LdapConnectionPool(config, new StandaloneLdapApiService(), 60 * 1000);
		connectionPool.setMaxActive(10);// unlimited connections can be active
		connectionPool.setWhenExhaustedAction(LdapConnectionPool.WHEN_EXHAUSTED_GROW);
		connectionPool.setMaxIdle(20);
		connectionPool.setTestOnBorrow(true);
		connectionPool.setTestWhileIdle(true);
		
		LdapConnection connection = connectionPool.getConnection();
		System.out.println(connection.connect()+" "+connection.toString());
		EntryCursor cursor = connection.search("o=IMSS,dc=example,dc=com", "(objectclass=*)", SearchScope.SUBTREE, "*");
	
		while (cursor.next()) {

				entry = cursor.get();
				System.out.println(entry);
		}
		cursor.close();
		/*
		 * LdapConnectionPool class has releaseConnection() which has returnObject() inside releaseConnection()
		 * returnObject() is a method of GenericObjectPool<LdapConnection> class which is super 
		 				class of LdapConnectionPool
		 */
		connectionPool.releaseConnection(connection);
		
		
		
		connectionPool.close();
		
		

	}


	

}
