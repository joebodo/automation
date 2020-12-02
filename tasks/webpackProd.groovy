task webpackProd(group: 'ui', type: Exec, description: 'Webpack production') {
	workingDir storefront_home
	commandLine '/usr/local/bin/npx', 'webpack', '--env.production'
}
