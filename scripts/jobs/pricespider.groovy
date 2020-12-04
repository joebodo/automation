def code = 'priceSpiderFeed'

def cronjob = cronJobService.getCronJob(code)
cronJobService.performCronJob(cronjob, false)

return 'started priceSpiderFeed cronjob'
