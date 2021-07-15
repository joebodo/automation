package automation

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.Input
import com.sap.hybris.hac.Configuration
import com.sap.hybris.hac.HybrisAdministrationConsole
import com.sap.hybris.hac.scripting.Script
import com.sap.hybris.hac.scripting.ScriptType
import com.automation.ScriptRunner

// alternative way to load **LOCAL** impex files
// useful for large files that cannot be loaded via hac

// See: https://github.com/klaushauschild1984/jhac

class HacImpexTask extends DefaultTask {

	@Input
	String impex

	@Input
	String settings

	@TaskAction
	def postScript() {

		def template = '''
			import de.hybris.platform.servicelayer.impex.ImportConfig
			import de.hybris.platform.servicelayer.impex.ImportConfig.ValidationMode
			import de.hybris.platform.servicelayer.impex.impl.StreamBasedImpExResource
			import org.apache.log4j.Logger

			def logger = Logger.getLogger('automation')

			def bytes = new File("%s").bytes

			def config = new ImportConfig().with {
				maxThreads = 8
				synchronous = true
				legacyMode = false
				enableCodeExecution = false
				distributedImpexEnabled = false
				validationMode = ValidationMode.STRICT
				script = new StreamBasedImpExResource(new ByteArrayInputStream(bytes), "UTF-8")
				it
			}

			// bust out of transaction to use maxThreads
			def tx = de.hybris.platform.tx.Transaction.current();
			if (tx.isRunning()) {
				tx.commit()
			}

			def importResult = importService.importData(config)

			if (importResult.hasUnresolvedLines()) {
				def lines = new String(mediaService.getDataFromMedia(importResult.unresolvedLines))
				logger.warn "Unresolved lines: $lines"
			}

			if (importResult.cronJob && importResult.cronJob.logText) {
				logger.warn new String(importResult.getCronJob().logText)
			}

			if (!importResult.successful) {
				logger.error "Import finished with errors"
			}
		'''

		def (url, username, password) = settings.tokenize(';')

		def configuration = new Configuration(url, username, password, null)
		def hac = HybrisAdministrationConsole.hac(configuration)

		def impexScript = String.format(template, impex)

		def script = Script.builder()
				.scriptType(ScriptType.groovy)
				.script(impexScript)
				.build()

		script.commit = true

		def result = ScriptRunner.execute(hac, script)

		if (result.outputText) {
			println '\nOutput--------------'
			println result.outputText
		}
		if (result.stacktraceText) {
			println '\nStacktrace----------'
			println result.stacktraceText
		}
		if (result.executionResult) {
			println '\nResult--------------'
			println result.executionResult
		}
	}
}
