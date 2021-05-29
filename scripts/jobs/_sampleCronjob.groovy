def code = sampleCronjobName'

def cronjob = cronJobService.getCronJob(code)
cronJobService.performCronJob(cronjob, false)

return 'started sample cronjob'
