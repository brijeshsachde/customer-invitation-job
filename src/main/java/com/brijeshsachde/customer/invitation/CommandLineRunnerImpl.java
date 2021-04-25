package com.brijeshsachde.customer.invitation;

import com.brijeshsachde.customer.invitation.service.InvitationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private InvitationService invitationService;

    @Override
    public void run(String... args) {
        LOG.info("EXECUTING customer invitation job.");
        invitationService.inviteCustomers();
    }

}