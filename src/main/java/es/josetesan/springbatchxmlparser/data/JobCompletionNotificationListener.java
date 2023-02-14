package es.josetesan.springbatchxmlparser.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class JobCompletionNotificationListener implements JobExecutionListener {

  private static final Logger log =
      LoggerFactory.getLogger(JobCompletionNotificationListener.class);

  @Override
  public void beforeJob(JobExecution jobExecution) {
    log.info("Empiezo el trabajo");
  }

  @Override
  public void afterJob(JobExecution jobExecution) {
    if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
      log.info("!!! JOB FINISHED! Time to verify the results");
    } else {
      log.info("El estado es este : {}", jobExecution.getStatus());
    }
  }
}
