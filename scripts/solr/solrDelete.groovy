def code = 'delete-southwireB2CIndex-cronJob'

def cronjob = cronJobService.getCronJob(code)
cronJobService.performCronJob(cronjob, false)

return 'started delete index'
