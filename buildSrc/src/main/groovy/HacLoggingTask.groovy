package automation

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.Input
import com.sap.hybris.hac.Configuration
import com.sap.hybris.hac.HybrisAdministrationConsole
import com.sap.hybris.hac.scripting.Script
import com.sap.hybris.hac.scripting.ScriptType
import com.automation.ScriptRunner

class HacLoggingTask extends DefaultTask {

	@Input
	String className

	@Input
	String settings

	@TaskAction
	def postScript() {

		def template = '''
			def facade = new de.hybris.platform.hac.facade.HacLog4JFacade()

			def logClass = '%s'
			def loggers = facade.getLoggers()
			def existing = loggers.find { it.name == logClass }

			def level = existing ? existing.effectiveLevel.name : 'OFF'
			def flipped = level == "OFF" ? "DEBUG" : "OFF"

			facade.changeLogLevel(logClass, flipped)

			println "$logClass -> $flipped"
		'''

		def (url, username, password) = settings.tokenize(';')

		def configuration = new Configuration(url, username, password, null)
		def hac = HybrisAdministrationConsole.hac(configuration)

		def loggingScript = String.format(template, className)

		def script = Script.builder()
				.scriptType(ScriptType.groovy)
				.script(loggingScript)
				.build()

		script.commit = true

		println "Enabling logging for $className"

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
