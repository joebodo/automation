def lines = 300
def dir = System.getProperty("HYBRIS_LOG_DIR")

def p = ['/bin/bash', '-c', "ls -t ${dir}/tomcat/cons*.log | head -n 1"].execute()
p.waitFor()

def cmd = "tail -n ${lines} ${p.text}"
println cmd.execute().text
