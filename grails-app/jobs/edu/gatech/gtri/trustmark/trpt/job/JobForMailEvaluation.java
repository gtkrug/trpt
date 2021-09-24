package edu.gatech.gtri.trustmark.trpt.job;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import static edu.gatech.gtri.trustmark.trpt.service.job.JobUtilityForMailEvaluation.synchronizeMailEvaluation;
import static edu.gatech.gtri.trustmark.trpt.service.job.JobUtilityForMailPasswordReset.synchronizeMailPasswordReset;
import static java.lang.String.format;

@DisallowConcurrentExecution
public class JobForMailEvaluation implements Job {

    private static final Log log = LogFactory.getLog(JobForMailEvaluation.class);

    @Override
    public void execute(final JobExecutionContext context) throws JobExecutionException {

        log.info(format("%s: previous at %s; next at %s.", getClass().getSimpleName(), context.getPreviousFireTime(), context.getNextFireTime()));

        synchronizeMailEvaluation();
    }
}
