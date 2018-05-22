package dk.skat.dmr.b2b.sample;

import org.junit.Test;

/**
 * USKoeretoejRegistreringHentClient Test
 *
 * @author SKAT
 * @since 1.0
 */
public class USKoeretoejRegistreringHentClientTest {

    @Test
    public void invoke() throws Exception {
        String endpointURL = System.getProperty("dk.skat.dmr.b2b.sample.USImportoerService.ENDPOINT");

        String registreringNummerNummer = "AB12345";

        USKoeretoejRegistreringHentClient oioLedsageDocumentClient = new USKoeretoejRegistreringHentClient(endpointURL);
        oioLedsageDocumentClient.invoke(registreringNummerNummer);
    }

}