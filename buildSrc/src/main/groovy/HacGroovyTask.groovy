package automation

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.Input
import com.sap.hybris.hac.Configuration
import com.sap.hybris.hac.HybrisAdministrationConsole
import com.sap.hybris.hac.scripting.Script
import com.sap.hybris.hac.scripting.ScriptType
import com.automation.ScriptRunner

// See: https://github.com/klaushauschild1984/jhac

class HacGroovyTask extends DefaultTask {

	@Input
	String script = '<none>'

	@Input
	String settings = '<none>'

	@TaskAction
	def postScript() {

		def (url, username, password) = settings.tokenize(';')

		def configuration = new Configuration(url, username, password, null)
		def hac = HybrisAdministrationConsole.hac(configuration)

		final InputStream inputStream = new BufferedInputStream(new FileInputStream(script))

		def script = Script.builder()
				.scriptType(ScriptType.groovy)
				.script(inputStream)
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
