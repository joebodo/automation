task initJunit(group: 'test', description: 'init Junit') {
	doFirst {
		ant.properties['tenant'] = 'junit'
		ant.ant antfile: 'build.xml', target: 'initialize', dir: platform_home
	}
}
