task customizeHac(group: 'misc', description: 'customize Hac') {
	doFirst {
		copy {
			from "${project.rootDir}/customize"
			into "$platform_home"
		}
	}
}
