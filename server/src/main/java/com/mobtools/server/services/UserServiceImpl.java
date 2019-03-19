package com.mobtools.server.services;

import com.mobtools.server.domains.User;
import com.mobtools.server.repositories.UserRepository;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.stereotype.Service;
import reactor.core.scheduler.Scheduler;

@Service
public class UserServiceImpl extends BaseService<UserRepository, User, String> implements UserService {

    public UserServiceImpl(Scheduler scheduler,
                           MongoConverter mongoConverter,
                           ReactiveMongoTemplate reactiveMongoTemplate,
                           UserRepository repository) {
        super(scheduler, mongoConverter, reactiveMongoTemplate, repository);
    }
}
