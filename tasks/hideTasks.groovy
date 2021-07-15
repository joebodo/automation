// hide some of the standard tasks

def hidden = [ 'help', 'Build Setup', 'IDE' ]

project.tasks.each {
	if (it.group in hidden) {

		configure(it) {
			group = null
		}
	}
}
