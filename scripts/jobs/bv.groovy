def code = 'bvProductFeedB2C'

def cronjob = cronJobService.getCronJob(code)
cronJobService.performCronJob(cronjob, false)

return 'started bv cronjob'
