package com.savdev.demo.wf.cron;

import com.savdev.demo.wf.AsyncWrapper;
import com.savdev.demo.wf.Executor;
import com.savdev.demo.wf.service.LongRunningService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.ScheduleExpression;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.TimerConfig;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.HashMap;
import java.util.Map;

@Singleton
@Startup
public class ApplicationAsyncTimer {

  private static final Log logger = LogFactory.getLog(ApplicationAsyncTimer.class);

  private static final String TIMER = "JEE Async Timeout check";

  @Resource
  javax.ejb.TimerService timerService;

  Map<String, Executor> cronJobs = new HashMap<>();

  @Inject
  LongRunningService longRunningService;

  @Inject
  AsyncWrapper asyncWrapper;

  @PostConstruct
  public void init() {
    TimerConfig timerConfig = new TimerConfig();
    timerConfig.setInfo(TIMER);
    timerConfig.setPersistent(false);

    LocalDateTime in20Second = LocalDateTime.now().plusSeconds(20);
    ScheduleExpression scheduleExpression = new ScheduleExpression()
      .second(in20Second.getSecond())
      .minute(in20Second.getMinute())
      .hour(in20Second.getHour())
      .dayOfMonth(in20Second.getDayOfMonth())
      .month(in20Second.getMonth().getValue())
      .dayOfWeek(in20Second.get(ChronoField.DAY_OF_WEEK));

    timerService.createCalendarTimer(
      scheduleExpression, timerConfig);

    cronJobs.put(TIMER, () -> asyncWrapper.runAsynchronously(
      longRunningService::longRunning));

    logger.info("Timer '" + TIMER + "' has been initialed with: "
      + scheduleExpression.toString());
  }

  @Timeout
  public void trigger(javax.ejb.Timer timer) {
    try {
      String timerName = (String) timer.getInfo();
      if (cronJobs.containsKey(timerName)) {
        logger.info("Timer '" + timer.getInfo() + "' has been triggered. ");
        cronJobs.get(timerName)
          .execute();
        logger.info("Timer '" + timer.getInfo() + "' has successfully completed. ");
      }
    } catch (Exception e) {
      logger.error("Timer " + timer.getInfo() + " failed. ", e);
    }
  }
}
