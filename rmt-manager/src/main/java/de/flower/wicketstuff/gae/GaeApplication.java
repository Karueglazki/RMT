package de.flower.wicketstuff.gae;

import org.apache.wicket.pageStore.memory.DataStoreEvictionStrategy;

public interface GaeApplication
{

	DataStoreEvictionStrategy getEvictionStrategy();
}
