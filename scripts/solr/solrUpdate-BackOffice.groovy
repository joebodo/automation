def code = 'update-backofficeIndex-CronJob'

def cronjob = cronJobService.getCronJob(code)
cronJobService.performCronJob(cronjob, false)

return 'started index update'
