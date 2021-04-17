// ant unittests -Dtestclasses.packages="com.southwire.*" -Dtestclasses.suppress.junit.tenant=true -Dtestclasses.web=true

task webTests(group: 'test', description: 'web tests') {
	doFirst {
		ant.properties['testclasses.packages'] = 'com.southwire.test.*'
		ant.properties['testclasses.suppress.junit.tenant'] = 'true'
		ant.properties['testclasses.web'] = 'true'
		ant.ant antfile: 'build.xml', target: 'unittests', dir: platform_home
	}
}
