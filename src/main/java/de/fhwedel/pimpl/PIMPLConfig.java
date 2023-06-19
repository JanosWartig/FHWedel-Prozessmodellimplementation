package de.fhwedel.pimpl;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "de.fhwedel.pimpl")
class PIMPLConfig {

	public PIMPLConfig() {
	}

	private String persistenceunit;
	private boolean regenerate;
	private String modelPackage;
	private final EclipseLink eclipselink = new EclipseLink();

	public String getPersistenceunit() {
		return persistenceunit;
	}

	public void setPersistenceunit(String persistenceunit) {
		this.persistenceunit = persistenceunit;
	}

	public boolean isRegenerate() {
		return regenerate;
	}

	public void setRegenerate(boolean regenerate) {
		this.regenerate = regenerate;
	}

	public String getModelPackage() {
		return modelPackage;
	}

	public void setModelPackage(String modelPackage) {
		this.modelPackage = modelPackage;
	}

	public EclipseLink getEclipselink() {
		return eclipselink;
	}

	public static class EclipseLink {
		private String targetDatabase;
		private String jdbcBatchWriting;
		private String loggingLevel;
		private boolean weaving;

		public String getTargetDatabase() {
			return targetDatabase;
		}

		public void setTargetDatabase(String targetDatabase) {
			this.targetDatabase = targetDatabase;
		}

		public String getJdbcBatchWriting() {
			return jdbcBatchWriting;
		}

		public void setJdbcBatchWriting(String jdbcBatchWriting) {
			this.jdbcBatchWriting = jdbcBatchWriting;
		}

		public String getLoggingLevel() {
			return loggingLevel;
		}

		public void setLoggingLevel(String loggingLevel) {
			this.loggingLevel = loggingLevel;
		}

		public boolean isWeaving() {
			return weaving;
		}

		public void setWeaving(boolean weaving) {
			this.weaving = weaving;
		}

	}

}