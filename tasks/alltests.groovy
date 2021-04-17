// ant alltests -Dtestclasses.packages="com.southwire.*" -Dtestclasses.suppress.junit.tenant=true -Dtestclasses.web=true

task allTests(group: 'test', description: 'all tests') {
	doFirst {
		ant.properties['testclasses.packages'] = 'com.southwire.test.*'
		ant.properties['testclasses.suppress.junit.tenant'] = 'true'
		ant.properties['testclasses.web'] = 'true'
		ant.ant antfile: 'build.xml', target: 'alltests', dir: platform_home
	}
}
