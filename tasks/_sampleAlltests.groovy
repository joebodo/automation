// ant alltests -Dtestclasses.packages="com.southwire.*"

task allTests(group: 'test', type: Exec, description: 'all tests') {

	def cmd = [ "$platform_home/apache-ant/bin/ant",
				'alltests',
				'-Dtestclasses.packages=com.southwire.sap.orderfulfillment.*,com.southwire.upsintegration.*' ]

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
