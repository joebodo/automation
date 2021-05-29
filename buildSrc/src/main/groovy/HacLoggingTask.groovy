package automation

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.Input
import com.sap.hybris.hac.Configuration
import com.sap.hybris.hac.HybrisAdministrationConsole
import com.sap.hybris.hac.scripting.Script
import com.sap.hybris.hac.scripting.ScriptType

class HacLoggingTask extends DefaultTask {

	@Input
	String className

	@Input
	String settings

	@TaskAction
	def postScript() {

		def template = '''
			import de.hybris.platform.hac.facade.HacLog4JFacade
			import org.apache.logging.log4j.*

			new HacLog4JFacade().changeLogLevel("%s", "DEBUG")
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
