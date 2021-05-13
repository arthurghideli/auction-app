package com.auction.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

@Service
public class HazelcastFactory {
	private HazelcastInstance hazelcastInstance;

	private static final String DEFAULT_FALSE = "false";
	private final String ipLocalhost;

	public HazelcastFactory(@Value("${hazelcast.ipLocalhost}") String ipLocalhost) {
		this.ipLocalhost = ipLocalhost;
	}

	@PostConstruct
	public void init() {
		getHazelcastInstance();
	}

	public HazelcastInstance getHazelcastInstance() {
		if (hazelcastInstance == null || !hazelcastInstance.getLifecycleService().isRunning()) {
			Config config = config();
			hazelcastInstance = Hazelcast.newHazelcastInstance(config);
		}
		return hazelcastInstance;
	}

	private Config config() {
		Config config = new Config();
		config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
		config.getNetworkConfig().getJoin().getTcpIpConfig().setEnabled(true);
		config.getNetworkConfig().getJoin().getTcpIpConfig().addMember(ipLocalhost);
		return config;
	}
}
