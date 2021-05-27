package automation

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.Input
import com.sap.hybris.hac.Configuration
import com.sap.hybris.hac.HybrisAdministrationConsole
import com.sap.hybris.hac.scripting.Script
import com.sap.hybris.hac.scripting.ScriptType

class HacTestingTask extends DefaultTask {

	@Input
	String className = '<none>'

	@Input
	String settings = 'http://localhost:9001/hac;admin;nimda'

	@TaskAction
	def postScript() {

		def template = '''
			Class testClass = Class.forName("%s")

			def runner = new org.junit.runner.JUnitCore();
			def results = runner.run(testClass)

			results.failures.each { f ->
				println "desc: $f.description"
				println "trac: $f.trace"
			}

			println "Failures: $results.failureCount"
			println "Count: $results.runCount"
			println "Successful: " + results.wasSuccessful()
		'''

		def (url, username, password) = settings.tokenize(';')

		if (url.endsWith('/')) {
			url = url.substring(0, url.length() - 1)
		}

		def configuration = new Configuration(url + "_junit", username, password, null)
		def hac = HybrisAdministrationConsole.hac(configuration)

		def testingScript = String.format(template, className)

		def script = Script.builder()
				.scriptType(ScriptType.groovy)
				.script(testingScript)
				.build()

		//script.commit = true

		println "Executing test for $className"

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
