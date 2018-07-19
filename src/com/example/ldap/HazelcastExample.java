package com.example.ldap;

import java.io.IOException;


import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.client.config.SocketOptions;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ICacheManager;
import com.hazelcast.core.IMap;
import com.opencsv.CSVParser;

public class HazelcastExample {

	private static HazelcastInstance hazelcastInstance;
	static CSVParser csvParser = new CSVParser(',');

	public static void main(String[] args) throws IOException, InterruptedException {

		HazelcastExample example=new HazelcastExample();
		
		ClientConfig config = new ClientConfig();

		String groupName = "dev";
		config.getGroupConfig().setName(groupName);

		String groupPassword = "dev-pass";
		config.getGroupConfig().setPassword(groupPassword);

		String host = "localhost";

		String retval[] = csvParser.parseLine(host);
		ClientNetworkConfig networkConfig = new ClientNetworkConfig();
		networkConfig.addAddress(retval);

		SocketOptions options = new SocketOptions();
		options.setKeepAlive(true);

		networkConfig.setSocketOptions(options);

		config.setNetworkConfig(networkConfig);

		hazelcastInstance = HazelcastClient.newHazelcastClient(config);

		
		example.setData(hazelcastInstance);
		
		example.getData(hazelcastInstance);
		
		example.manageCache(hazelcastInstance);
		
		
		
		Thread.sleep(2000);
		hazelcastInstance.shutdown();

	}

	private void manageCache(HazelcastInstance hazelcastInstance2) {
		
		
		
	}

	private void setData(HazelcastInstance hazelcastInstance2) {
		IMap<Object, Object> iMap = hazelcastInstance2.getMap("deault");
		Object object = iMap.get("hello");
		System.out.println(object.toString());
	
		
	}

	private void getData(HazelcastInstance hazelcastInstance) {
		
		//IMap<Object, Object> map = hazelcastInstance.getMap("deault");
		//System.out.println("map is : "+map);
		//map.put("hello", "dude");
		
		
	}

}
