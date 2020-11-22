package automation

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.Input

// following is a cleaner approach
// See: https://github.com/klaushauschild1984/jhac/blob/master/jhac-core/src/main/java/com/sap/hybris/hac/Base.java

class HacGroovyTask extends DefaultTask {

	@Input
	String script = '<none>'
	@Input
	String settings = 'http://localhost:9001/hac;admin;nimda'

	@TaskAction
	def postScript() {

		def (url, username, password) = settings.tokenize( ';' )

		def settings = new Project().with {
			hacLogin = username
			hacPassword = password
			hostHacUrl = url
			it // Note the explicit mention of it as the return value
		}

		def result = new HybrisHacHttpClient().executeGroovyScript(settings, new File(script).text, true)

		if (result.outputText) {
			println "\nOutput--------------\n"
			println result.outputText
		}
		if (result.stacktraceText) {
			println "\nStacktrace----------\n"
			println result.stacktraceText
		}
		if (result.executionResult) {
			println "\nResult--------------\n"
			println result.executionResult
		}
	}
}
