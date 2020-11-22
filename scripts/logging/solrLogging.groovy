import org.apache.logging.log4j.*
import org.apache.logging.log4j.core.config.*
import de.hybris.platform.util.logging.log4j2.HybrisLoggerContext

toggle('de.hybris.platform.solrfacetsearch.search.impl.LegacyFacetSearchStrategy')
toggle('de.hybris.platform.solrfacetsearch.search.impl.DefaultFacetSearchStrategy')

def toggle(logClass) {
	final HybrisLoggerContext loggerCtx = (HybrisLoggerContext) LogManager.getContext(false)
	final Configuration loggerCfg = loggerCtx.getConfiguration()
	LoggerConfig loggerConfig = loggerCfg.getLoggers().get(logClass)

	def OFF = Level.getLevel('OFF')
	def DEBUG = Level.getLevel('DEBUG')

	if (loggerConfig == null) {

		LoggerConfig createdLoggerConfig = LoggerConfig.createLogger(
			'true', OFF, logClass, 'true', null, null, loggerCfg, null)

		loggerCfg.addLogger(logClass, createdLoggerConfig)
	}

	def logger = loggerCfg.loggers.get(logClass);
	logger.level = logger.level == OFF ? DEBUG : OFF

	println 'level = ' + logger.level
	loggerCtx.updateLoggers()
}
