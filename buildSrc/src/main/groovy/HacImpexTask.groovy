package automation

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.Input
import com.sap.hybris.hac.Configuration
import com.sap.hybris.hac.HybrisAdministrationConsole
import com.sap.hybris.hac.scripting.Script
import com.sap.hybris.hac.scripting.ScriptType

// alternative way to load **LOCAL** impex files
// useful for large files that cannot be loaded via hac

// See: https://github.com/klaushauschild1984/jhac

class HacImpexTask extends DefaultTask {

	@Input
	String impex = '<none>'

	@Input
	String settings = 'http://localhost:9001/hac;admin;nimda'

	@TaskAction
	def postScript() {

		def template = '''
			import de.hybris.platform.servicelayer.impex.ImportConfig
			import de.hybris.platform.servicelayer.impex.ImportConfig.ValidationMode
			import de.hybris.platform.servicelayer.impex.impl.StreamBasedImpExResource

			def bytes = new File("%s").bytes

			def config = new ImportConfig().with {
				maxThreads = 1
				synchronous = true
				legacyMode = false
				enableCodeExecution = false
				distributedImpexEnabled = false
				validationMode = ValidationMode.STRICT
				script = new StreamBasedImpExResource(new ByteArrayInputStream(bytes), "UTF-8")
				it
			}

			def importResult = importService.importData(config)

			if (importResult.hasUnresolvedLines()) {
				println new String(mediaService.getDataFromMedia(importResult.unresolvedLines))
			}

			if (importResult.cronJob && importResult.cronJob.logText) {
				println new String(importResult.getCronJob().logText)
			}

			if (importResult.successful) {
				println "Import successful"
			} else {
				println "Import finished with errors"
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

		def result = hac.scripting().execute(script)

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
