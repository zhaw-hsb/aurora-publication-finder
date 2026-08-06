/*
* This file is part of the Aurora Publication Finder.
*
* (c) ZHAW HSB <apps.hsb@zhaw.ch>
*
* For the full copyright and license information, please view the LICENSE
* file that was distributed with this source code.
*/
package ch.zhaw.hsb.aurora.publicationfinder.Core.Service;

import ch.zhaw.hsb.aurora.publicationfinder.Core.Configuration.PropertyProviderConfiguration;
import ch.zhaw.hsb.aurora.publicationfinder.Core.LogCollector.AdminLogCollector;
import ch.zhaw.hsb.aurora.publicationfinder.Core.LogCollector.HelpdeskLogCollector;

/**
 * This class sends the reports to the admin and helpdesk with an email
 * 
 * @author Dana Ghousson ZHAW
 * @author Iris Hausmann ZHAW
 */
public class EmailReportService {

    private EmailService emailService;

    public EmailReportService(EmailService emailService) {

        this.emailService = emailService;

    }

    /**
     * Method to send reports of the collected logs to the admin and helpdesk email
     */
    public void sendReports(boolean errorOccured) {
        try {

            // helpdesk email
            String helpdeskContent = "";
            if (!HelpdeskLogCollector.getMessages().isEmpty()) {
                helpdeskContent = String.join("\n---\n", HelpdeskLogCollector.getMessages());
            }

            if(errorOccured){
                
                // Admin email only if error occured
                if (!AdminLogCollector.getErrors().isEmpty()) {
                    String adminContent = String.join("\n---\n", AdminLogCollector.getErrors());
                    emailService.sendEmail(
                        PropertyProviderConfiguration.getMailAdmin(),
                        "[AURORA publicationfinder: SYSTEM ERROR REPORT]",
                        adminContent
                    );
                    AdminLogCollector.clear();
                }

                helpdeskContent = helpdeskContent.concat("\n---\nAn error occured. The administrator has been notified.");

                    
            }

            emailService.sendEmail(PropertyProviderConfiguration.getMailHelpdesk(),"AURORA publicationfinder: System Summary", helpdeskContent);
            HelpdeskLogCollector.clear();
           

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
