def toggle(logClass) {
	def facade = new de.hybris.platform.hac.facade.HacLog4JFacade()

	def existing = facade.loggers.find { it.name == logClass }

	def level = existing ? existing.effectiveLevel.name : 'OFF'
	def flipped = level == "OFF" ? "DEBUG" : "OFF"

	facade.changeLogLevel(logClass, flipped)

	println "$logClass -> $flipped"
}

toggle('de.hybris.platform.solrfacetsearch.search.impl.LegacyFacetSearchStrategy')
toggle('de.hybris.platform.solrfacetsearch.search.impl.DefaultFacetSearchStrategy')
