package edu.gatech.gtri.trustmark.trpt.domain

import org.gtri.fj.data.Option
import org.gtri.fj.function.Effect0
import org.gtri.fj.function.F0

import java.time.LocalDateTime

import static org.gtri.fj.data.List.iterableList
import static org.gtri.fj.data.Option.fromNull

class TrustmarkUri {

    String uri
    String hash
    String xml
    LocalDateTime requestLocalDateTime
    LocalDateTime successLocalDateTime
    LocalDateTime failureLocalDateTime
    LocalDateTime changeLocalDateTime
    String failureMessage

    static constraints = {
        uri nullable: true
        hash nullable: true
        xml nullable: true
        requestLocalDateTime nullable: true
        successLocalDateTime nullable: true
        failureLocalDateTime nullable: true
        changeLocalDateTime nullable: true
        failureMessage nullable: true
    }

    static hasMany = [
            partnerSystemCandidateTrustmarkUriSet: PartnerSystemCandidateTrustmarkUri
    ]

    static mapping = {
        table 'trustmark_uri'
        uri length: 1000
        hash length: 1000
        xml type: 'text'
        failureMessage length: 1000
    }

    long idHelper() { id }

    org.gtri.fj.data.List<PartnerSystemCandidateTrustmarkUri> partnerSystemCandidateTrustmarkUriSetHelper() { fromNull(getPartnerSystemCandidateTrustmarkUriSet()).map({ list -> iterableList(list) }).orSome(org.gtri.fj.data.List.<PartnerSystemCandidateTrustmarkUri> nil()) }

    void partnerSystemCandidateTrustmarkUriSetHelper(final org.gtri.fj.data.List<PartnerSystemCandidateTrustmarkUri> partnerSystemCandidateTrustmarkUriSet) { setPartnerSystemCandidateTrustmarkUriSet(new HashSet<>(partnerSystemCandidateTrustmarkUriSet.toJavaList())) }

    void deleteHelper() { delete(failOnError: true) }

    void deleteAndFlushHelper() { delete(flush: true, failOnError: true) }

    TrustmarkUri saveHelper() { save(failOnError: true) }

    TrustmarkUri saveAndFlushHelper() { save(flush: true, failOnError: true) }

    static final org.gtri.fj.data.List<TrustmarkUri> findAllHelper() {

        fromNull(findAll())
                .map({ list -> iterableList(list) })
                .orSome(org.gtri.fj.data.List.<TrustmarkUri> nil());
    }

    static final Option<TrustmarkUri> findByIdHelper(long id) { fromNull(findById(id)) }

    static final Option<TrustmarkUri> findByUriHelper(final String uri) { fromNull(findByUri(uri)) }

    static final <T> T withTransactionHelper(final F0<T> f0) {
        return withTransaction({ return f0.f() })
    }

    static final void withTransactionHelper(final Effect0 effect0) {
        withTransaction({ return effect0.f() })
    }
}
