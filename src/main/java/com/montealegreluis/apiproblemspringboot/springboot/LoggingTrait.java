package com.montealegreluis.apiproblemspringboot.springboot;

import com.montealegreluis.activityfeed.Activity;
import com.montealegreluis.activityfeed.ActivityFeed;
import org.slf4j.LoggerFactory;

public interface LoggingTrait {
  ActivityFeed feed = new ActivityFeed(LoggerFactory.getLogger(ApiProblemHandler.class));

  default void log(final Activity activity) {
    feed().record(activity);
  }

  default ActivityFeed feed() {
    return feed;
  }
}
