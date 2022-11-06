package io.easyware.shared.log;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LogMessageRepository implements PanacheRepository<LogMessage> {
}
