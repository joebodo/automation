task customizeHac(group: 'setup', description: 'customize Hac') {
	doFirst {
		copy {
			from "${project.rootDir}/customize"
			into "$platform_home"
		}
	}
}
