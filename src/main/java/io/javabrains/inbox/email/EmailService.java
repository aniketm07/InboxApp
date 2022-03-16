package io.javabrains.inbox.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class EmailService {
    @Autowired
    private EmailRepository emailRepository;

    public Email findById(UUID id){
        Optional<Email> optionalEmail = emailRepository.findById(id);
        return optionalEmail.orElse(null);
    }
}
