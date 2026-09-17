package com.kristina.fintech;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.core.read.ListAppender;
import com.kristina.fintech.tokenization.TokenizationService;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import static org.junit.jupiter.api.Assertions.*;

class TokenVaultLogRegressionTest {
  @Test void rawPanNeverAppearsInApplicationLogs(){
    Logger logger=(Logger) LoggerFactory.getLogger(TokenizationService.class); ListAppender<ch.qos.logback.classic.spi.ILoggingEvent> appender=new ListAppender<>(); appender.start(); logger.addAppender(appender);
    String pan="4111111111111111"; new TokenizationService("test-secret").tokenize(pan);
    assertTrue(appender.list.stream().noneMatch(e->e.getFormattedMessage().contains(pan)));
    logger.detachAppender(appender);
  }
}
