package de.fhwedel.pimpl;

import java.util.Properties;

import de.fhwedel.pimpl.model.Room;
import de.fhwedel.pimpl.model.RoomCategory;
import de.fhwedel.pimpl.repos.RoomCategoryRepo;
import de.fhwedel.pimpl.repos.RoomRepo;
import jakarta.persistence.EntityManagerFactory;
import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import de.fhwedel.pimpl.model.Customer;
import de.fhwedel.pimpl.repos.CustomerRepo;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories
public class PIMPLPersistence {

	@Bean
	public CommandLineRunner exampleData(PIMPLConfig config, CustomerRepo cs, RoomCategoryRepo rc, RoomRepo roomRepo) {
		return (args) -> {
			if (config.isRegenerate()) {
				for (int i = 0; i < 25; i++) {
					Customer c = Customer.createRandomCustomer();
					cs.save(c);
				}
				// Room Categories
				RoomCategory small = RoomCategory.createExampleRoomCategory(RoomCategory.RoomTypes.Small, 1, 20, 10);
				RoomCategory big = RoomCategory.createExampleRoomCategory(RoomCategory.RoomTypes.Big, 5, 100, 50);
				rc.save(small);
				rc.save(big);
				// Rooms
				for(int i = 1; i < 20; i++) {
					Room room = new Room(i, i > 10 ? big : small);
					roomRepo.save(room);
				}
			}
		};
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(PIMPLConfig config, DataSource dataSource) {
		Properties props = new Properties();

		if (config.isRegenerate()) {
			props.put(PersistenceUnitProperties.DDL_GENERATION, PersistenceUnitProperties.DROP_AND_CREATE);
			props.put(PersistenceUnitProperties.DDL_GENERATION_MODE, PersistenceUnitProperties.DDL_DATABASE_GENERATION);
		}
		props.put(PersistenceUnitProperties.WEAVING, "" + config.getEclipselink().isWeaving());
		props.put(PersistenceUnitProperties.BATCH_WRITING, config.getEclipselink().getJdbcBatchWriting());
		props.put(PersistenceUnitProperties.TARGET_DATABASE, config.getEclipselink().getTargetDatabase());
		props.put(PersistenceUnitProperties.LOGGING_LEVEL, config.getEclipselink().getLoggingLevel());

		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		EclipseLinkJpaVendorAdapter vendorAdapter = new EclipseLinkJpaVendorAdapter();
		factory.setDataSource(dataSource);
		factory.setJpaVendorAdapter(vendorAdapter);
		factory.setPersistenceUnitName(config.getPersistenceunit());
		factory.setJpaProperties(props);
		factory.setPackagesToScan(config.getModelPackage());

		return factory;
	}

	@Bean
	@ConfigurationProperties(prefix = "de.fhwedel.pimpl.datasource")
	public DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean
	public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory);
		return transactionManager;
	}

}
