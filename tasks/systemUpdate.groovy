// ant updatesystem -Dtenant=<my tenant> -DconfigFile=path/to/my/config.json

task systemUpdate(group: 'update', description: 'system update') {
	doFirst {
		def configFile = project.rootDir.toString() + '/system-update.json'

		assert file(configFile).exists(), 'produce a system update file from hac'

		ant.lifecycleLogLevel = "INFO"
		ant.properties['tenant'] = 'master'
		ant.properties['configFile'] = configFile
		ant.ant antfile: 'build.xml', target: 'updatesystem', dir: platform_home
	}
}
