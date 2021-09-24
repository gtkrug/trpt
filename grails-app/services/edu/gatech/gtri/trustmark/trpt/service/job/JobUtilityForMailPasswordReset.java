package edu.gatech.gtri.trustmark.trpt.service.job;

import edu.gatech.gtri.trustmark.trpt.domain.MailPasswordReset;
import edu.gatech.gtri.trustmark.trpt.domain.Server;
import edu.gatech.gtri.trustmark.trpt.service.mail.MailUtility;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static java.lang.String.format;

public class JobUtilityForMailPasswordReset {

    private JobUtilityForMailPasswordReset() {
    }

    private static final Log log = LogFactory.getLog(JobUtilityForMailPasswordReset.class);

    public static void synchronizeMailPasswordReset() {

        final LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);

        MailPasswordReset.withTransactionHelper(() -> MailPasswordReset.findAllByMailDateTimeIsNullHelper()
                .forEach(mailPasswordReset -> {
                    MailUtility.send(
                            mailPasswordReset.userHelper().getUsername(),
                            "The system has reset your password.",
                            format(
                                    "The system has reset your password.%n%n" +
                                            "You may change your password here:%n%n" +
                                            "%spassword/changeWithoutAuthentication?external=%s", Server.findAllHelper().head().getUrl(), mailPasswordReset.getExternal()));

                    mailPasswordReset.setMailDateTime(now);
                    mailPasswordReset.saveHelper();
                }));
    }
}