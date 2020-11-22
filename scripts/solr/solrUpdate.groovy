def code = 'update-southwireB2CIndex-cronJob'

def cronjob = cronJobService.getCronJob(code);
cronJobService.performCronJob(cronjob, false);

return 'started index update'