import automation.HacGroovyTask

task directImport(description: 'Direct Import', group: 'update', type: HacGroovyTask) {
	def file = "$platform_home/../custom/southwire/southwireinitialdata/resources/southwireinitialdata/import/direct/import.groovy"
	
	doFirst {
		script "$file"
		settings hac_settings
	}
}
