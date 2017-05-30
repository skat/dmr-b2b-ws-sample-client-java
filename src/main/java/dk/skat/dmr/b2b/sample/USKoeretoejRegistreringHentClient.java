package dk.skat.dmr.b2b.sample;

import dk.oio.rep.skat_dk.basis.kontekst.xml.schemas._2006._09._01.AdvisStrukturType;
import dk.oio.rep.skat_dk.basis.kontekst.xml.schemas._2006._09._01.FejlStrukturType;
import dk.oio.rep.skat_dk.basis.kontekst.xml.schemas._2006._09._01.HovedOplysningerType;
import dk.skat.dmr._2007._05._31.*;
import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.bus.spring.SpringBusFactory;
import org.xml.sax.SAXException;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.ws.BindingProvider;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

/**
 * USKoeretoejRegistreringHentClient
 *
 * @author SKAT
 * @since 1.0
 */
@SuppressWarnings("ALL")
public class USKoeretoejRegistreringHentClient {

    private static final Logger LOGGER = Logger.getLogger(USKoeretoejRegistreringHentClient.class.getName());

    private String endpointURL;

    /**
     * Private constructor
     */
    private USKoeretoejRegistreringHentClient() {
    }

    /**
     * Constructor
     *
     * @param endpointURL Endpoint of USKoeretoejRegistreringHent service
     */
    public USKoeretoejRegistreringHentClient(String endpointURL) {
        this.endpointURL = endpointURL;
    }

    /**
     * Call USKoeretoejTekniskDataHent service
     *
     * <ns2:USKoeretoejTekniskDataHent_I xmlns:ns2=\x22http://skat.dk/dmr/2007/05/31/\x22 xmlns=\x22http://rep.oio.dk/skat.dk/basis/kontekst/xml/schemas/2006/09/01/\x22>
     * <HovedOplysninger>
     *     <TransaktionIdentifikator>d137f01f-1f33-4b03-8ffa-08bf17f80660.1</TransaktionIdentifikator>
     *     <TransaktionTid>2017-05-26T12:56:51.686+02:00</TransaktionTid>
     * </HovedOplysninger>
     * <ns2:KoeretoejGenerelIdentifikatorStruktur>
     *     <ns2:KoeretoejGenerelIdentifikatorValg>
     *         <ns2:RegistreringNummerNummer>HC11554</ns2:RegistreringNummerNummer>
     *     </ns2:KoeretoejGenerelIdentifikatorValg>
     * </ns2:KoeretoejGenerelIdentifikatorStruktur>
     * </ns2:USKoeretoejTekniskDataHent_I>
     *
     * @param registreringNummerNummer Number Plate
     * @throws DatatypeConfigurationException N/A
     * @throws ParserConfigurationException N/A
     * @throws IOException N/A
     * @throws SAXException N/A
     */
    public void invoke(String registreringNummerNummer) throws DatatypeConfigurationException, ParserConfigurationException, IOException, SAXException {

        final String newLine = System.getProperty("line.separator");

        // Generate Transaction Id
        final String transactionID = TransactionIdGenerator.getTransactionId();

        // Generate Transaction Time
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(new Date());
        XMLGregorianCalendar transactionTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);

        // Build HovedOplysninger Object and set transaction id and time
        HovedOplysningerType hovedOplysningerType = new HovedOplysningerType();
        hovedOplysningerType.setTransaktionIdentifikator(transactionID);
        hovedOplysningerType.setTransaktionTid(transactionTime);

        // Build VirksomhedIdentifikationStruktur
        USKoeretoejRegistreringHentI usKoeretoejTekniskDataHentIType = new USKoeretoejRegistreringHentI();

        usKoeretoejTekniskDataHentIType.setHovedOplysninger(hovedOplysningerType);
        KoeretoejGenerelIdentifikatorStrukturType koeretoejGenerelIdentifikatorStrukturType = new KoeretoejGenerelIdentifikatorStrukturType();
        KoeretoejGenerelIdentifikatorStrukturType.KoeretoejGenerelIdentifikatorValg valg = new KoeretoejGenerelIdentifikatorStrukturType.KoeretoejGenerelIdentifikatorValg();
        valg.setRegistreringNummerNummer(registreringNummerNummer);
        koeretoejGenerelIdentifikatorStrukturType.setKoeretoejGenerelIdentifikatorValg(valg);
        usKoeretoejTekniskDataHentIType.setKoeretoejGenerelIdentifikatorStruktur(koeretoejGenerelIdentifikatorStrukturType);

        Bus bus = new SpringBusFactory().createBus("dmr-b2b-policy.xml", false);
        BusFactory.setDefaultBus(bus);


        USImportoerService service = new USImportoerService();
        USImportoerServiceType port = service.getPort();

        // Set endpoint of service.
        BindingProvider bp = (BindingProvider)port;
        bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, this.endpointURL);

        StringBuilder sbRequest = new StringBuilder();
        sbRequest.append("*******************************************************************").append(newLine);
        sbRequest.append("** Endpoint: ").append(endpointURL).append(newLine);
        sbRequest.append("*******************************************************************").append(newLine);
        sbRequest.append("** HovedOplysninger").append(newLine);
        sbRequest.append("**** Transaction Id: ").append(usKoeretoejTekniskDataHentIType.getHovedOplysninger().getTransaktionIdentifikator()).append(newLine);
        sbRequest.append("**** Transaction Time: ").append(usKoeretoejTekniskDataHentIType.getHovedOplysninger().getTransaktionTid()).append(newLine);
        sbRequest.append("** VirksomhedIdentifikationStruktur").append(newLine);
        sbRequest.append("**** AfgiftOperatoerPunktAfgiftIdentifikator: ").append(usKoeretoejTekniskDataHentIType.getKoeretoejGenerelIdentifikatorStruktur().getKoeretoejGenerelIdentifikatorValg().getRegistreringNummerNummer()).append(newLine);
        sbRequest.append("*******************************************************************").append(newLine);
        LOGGER.info(newLine + sbRequest.toString());


        USKoeretoejRegistreringHentO out = port.getUSKoeretoejRegistreringHent(usKoeretoejTekniskDataHentIType);
        StringBuilder sb = new StringBuilder();
        sb.append("*******************************************************************").append(newLine);
        sb.append("** HovedOplysningerSvar").append(newLine);
        sb.append("**** Transaction Id: ").append(out.getHovedOplysningerSvar().getTransaktionIdentifikator()).append(newLine);
        sb.append("**** Transaction Time: ").append(out.getHovedOplysningerSvar().getTransaktionTid()).append(newLine);
        sb.append("**** Service Identification: ").append(out.getHovedOplysningerSvar().getServiceIdentifikator()).append(newLine);
        if (out.getHovedOplysningerSvar().getSvarStruktur().getAdvisStrukturOrFejlStruktur().size() > 0) {
            for (Object errorOrAdvis : out.getHovedOplysningerSvar().getSvarStruktur().getAdvisStrukturOrFejlStruktur()) {
                if (errorOrAdvis instanceof FejlStrukturType) {
                    FejlStrukturType fejlStrukturType = (FejlStrukturType) errorOrAdvis;
                    sb.append("**** Error").append(newLine);
                    sb.append("****** Error Code: ").append(fejlStrukturType.getFejlIdentifikator()).append(newLine);
                    sb.append("****** Error Text: ").append(fejlStrukturType.getFejlTekst()).append(newLine);
                }
                if (errorOrAdvis instanceof AdvisStrukturType) {
                    AdvisStrukturType advisStrukturType = (AdvisStrukturType) errorOrAdvis;
                    sb.append("**** Advis").append(newLine);
                    sb.append("****** Advis Code: ").append(advisStrukturType.getAdvisIdentifikator()).append(newLine);
                    sb.append("****** Advis Text: ").append(advisStrukturType.getAdvisTekst()).append(newLine);
                }
            }
        } else {
            sb.append("KoeretoejOplysningStelNummer: ").append(out.getIndhold().getKoeretoejOplysningStelNummer()).append(newLine);
        }
        sb.append("*******************************************************************").append(newLine);

        LOGGER.info(newLine + sb.toString());
    }

}
