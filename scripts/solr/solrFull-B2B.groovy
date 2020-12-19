def code = 'full-southwireIndex-cronJob'

def cronjob = cronJobService.getCronJob(code)
cronJobService.performCronJob(cronjob, false)

return 'started full index'
