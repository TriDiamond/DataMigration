package com.intelizign.datamigration.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class TopicConfiguration {

	@Bean
	public NewTopic DataMigrationTopic1() {
		return TopicBuilder.name("Migrate-WorkItemObject-To-Polarion").build();
      
	}
	
	@Bean
    public NewTopic DataMigrationTopic2() {
        return TopicBuilder.name("Migrate-DocumentObject-To-Polarion")
            .build();
    }

}
