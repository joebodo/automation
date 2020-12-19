def code = 'update-southwireIndex-cronJob'

def cronjob = cronJobService.getCronJob(code)
cronJobService.performCronJob(cronjob, false)

return 'started index update'
