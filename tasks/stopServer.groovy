task stopServer(group: 'server', type: Exec, description: 'Stop server') {
    workingDir platform_home
    commandLine './hybrisserver.sh', 'stop'
}
