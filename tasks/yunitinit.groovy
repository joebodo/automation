task yunitinit(group: 'test', description: 'yunitinit') {
	doFirst {
		ant.ant antfile: 'build.xml', target: 'yunitinit', dir: platform_home
	}
}
