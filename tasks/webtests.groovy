// ant unittests -Dtestclasses.packages="com.southwire.*" -Dtestclasses.suppress.junit.tenant=true -Dtestclasses.web=true

task webTests(group: 'test', type: Exec, description: 'web tests') {

	def cmd = [ "$platform_home/apache-ant/bin/ant",
				'unittests',
				'-Dtestclasses.packages=com.southwire.*',
				'-Dtestclasses.suppress.junit.tenant=true',
				'-Dtestclasses.web=true' ]

	workingDir platform_home
	environment('JAVA_HOME', org.gradle.internal.jvm.Jvm.current().getJavaHome())
	commandLine cmd

	doLast {
		def path = "file://$platform_home/../../log/junit/index.html"
		def os = org.gradle.internal.os.OperatingSystem.current()

		if (os.isWindows()) {
			exec { commandLine 'cmd', '/c', "start $path" }
		} else if (os.isMacOsX()) {
			exec { commandLine 'open', "$path" }
		}
	}
}
