def code = 'full-southwireDirectIndex-cronJob'

def cronjob = cronJobService.getCronJob(code)
cronJobService.performCronJob(cronjob, false)

return 'started full index'
