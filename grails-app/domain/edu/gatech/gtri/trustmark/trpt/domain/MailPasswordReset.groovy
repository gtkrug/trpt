package edu.gatech.gtri.trustmark.trpt.domain

import org.gtri.fj.data.Option

import java.time.LocalDateTime

import static org.gtri.fj.data.List.iterableList
import static org.gtri.fj.data.Option.fromNull

class MailPasswordReset {

    String external
    LocalDateTime requestDateTime
    LocalDateTime mailDateTime
    LocalDateTime resetDateTime
    LocalDateTime expireDateTime

    static belongsTo = [
            user: User
    ]

    static constraints = {
        external nullable: true, length: 36
        requestDateTime nullable: true
        mailDateTime nullable: true
        resetDateTime nullable: true
        expireDateTime nullable: true
    }

    long idHelper() { getId() }

    User userHelper() { getUser() }

    void userHelper(final User user) { setUser(user) }

    void deleteHelper() { delete(failOnError: true) }

    void deleteAndFlushHelper() { delete(flush: true, failOnError: true) }

    MailPasswordReset saveHelper() { save(failOnError: true) }

    MailPasswordReset saveAndFlushHelper() { save(flush: true, failOnError: true) }

    static final org.gtri.fj.data.List<MailPasswordReset> findAllByUserHelper(
            final User user) {

        fromNull(findAllByUser(user))
                .map({ list -> iterableList(list) })
                .orSome(org.gtri.fj.data.List.<MailPasswordReset> nil())
    }

    static final Option<MailPasswordReset> findByExternalHelper(
            final String external) {

        fromNull(findByExternal(external))
    }
}
