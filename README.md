# Danish Register of Motor Vehicles - API/B2B Integration - Java Sample

Sample client for the Register of Motor Vehicles **B2B Web Service Gateway** developed in Java and 
using open source libraries.

> **IMPORTANT NOTICE**: SKAT does not provide any kind of support for the code in this repository.
> This Java-client is just one example of how a B2B web service can be accessed. The client must not be 
> perceived as a piece of production code but more as an example one can take inspiration from and can use
> to quickly get started to test whether your company can implement a successful call to one of the B2B web 
> service using the company's digital signature. SKAT can not be held responsible if a company uses this client
> or parts of it in their own systems. 

> **VIGTIG MEDDELELSE**: SKAT yder ikke support på kildekoden i nærværende kodebibliotek.
> Denne Java-klient er kun et eksempel på hvordan B2B webservicene kan tilgås. Klienten skal således ikke 
> opfattes som et stykke produktionskode men mere som en eksempel man kan lade sig inspirere af og kan bruge 
> til hurtigt at komme i gang og få afprøvet om ens virksomhed kan gennemføre et succesfuldt kald til en af 
> B2B webservicene ved at bruge virksomhedens digitale signatur. SKAT kan ikke stå til ansvar hvis en virksomhed
> anvender klienten eller dele af denne i deres egne systemer. 

## About the client

The sample client implementation is based on the [Apache CXF](http://cxf.apache.org/) framework,
the Spring Framework, and Java 8. See `pom.xml` file in this repository for details regarding 
the current versions of the mentioned frameworks in use.
 
The sample client currently invokes the service:
 
* **USImportoerService** : A service provided for legal entities importing vehicles to Denmark. The sample code 
in this repository invokes a call to the service operation **USKoeretoejRegistreringHent** that provides vehicle details
when provided a valid registration number (i.e. the *Number Plate*).

The starting point of the source code of the implementation is this class:

* [USKoeretoejRegistreringHentClient.java](src/main/java/dk/skat/dmr/b2b/sample/USKoeretoejRegistreringHentClient.java)

This class constructs the request, invokes a Apache CXF generated client, and parses the response
by printing out relevant values to the log.

## Fulfillment of WS Policy of B2B Web Services

The fulfillment of policies required to invoke the Web Service is configured in the file:

[dmr-b2b-policy.xml](src/main/resources/dmr-b2b-policy.xml)

Fulfillment of WS Policy requirements is achieved using CXF's in and out interceptor framework and 
the `dmr-policy.xml` file details which parts are to be signed and how to present 
certificate for authentication on the server side. This configuration file also demonstrates how
secure transport (https) is enabled client side.

In addition to configuration in `dmr-b2b-policy.xml`, the *Canonicalization Method* required (the request) 
and produced (the response) by the Web Service is `http://www.w3.org/2001/10/xml-exc-c14n#`, e.g. as
would be rendered in the request:

```xml
<ds:CanonicalizationMethod xmlns:ds="http://www.w3.org/2000/09/xmldsig#" Algorithm="http://www.w3.org/2001/10/xml-exc-c14n#"/>
```

## Run clients

> **IMPORTANT** The sample clients must be configured with JVM parameters that are necessary for the client to run and
> invoke a call to the test environment of B2B Web Service Gateway. The two parameters may be obtained by contacting 
> SKAT Help Desk.

The full list of parameters for running a test against the **USKoeretoejRegistreringHent** operation:

* **dk.skat.dmr.b2b.sample.P12_PASSPHRASE** (REQUIRED): Passphrase to the certificate used for authentication and signing.
* **dk.skat.dmr.b2b.sample.USImportoerService.ENDPOINT** (REQUIRED): The endpoint of the **USImportoerService** service being invoked.
* **dk.skat.dmr.b2b.sample.TXID_PREFIX** (OPTIONAL): This parameter sets a custom prefix to the generated transaction id and is very useful when asking SKAT Help Desk to trace a particular request.

The client is then invoked as part of the **test phase** of the Maven build process using the following
command:

```sh
$ mvn clean install \
  -Ddk.skat.dmr.b2b.sample.P12_PASSPHRASE=<CHANGE_THIS> \
  -Ddk.skat.dmr.b2b.sample.USImportoerService.ENDPOINT=<CHANGE_THIS>
  -Ddk.skat.dmr.b2b.sample.TXID_PREFIX=ACME_01_
```

The following is partial output of build and exhibits the request and response written
to the log:

**Request**:
```
May 22, 2018 7:18:49 AM dk.skat.dmr.b2b.sample.USKoeretoejRegistreringHentClient invoke
INFO: 
*******************************************************************
** Endpoint: https://xxxxxxxxxxxxxxxx/xxx/xxx/xxx
*******************************************************************
** HovedOplysninger
**** Transaction Time: 2018-05-22T07:18:47.427+02:00
** VirksomhedIdentifikationStruktur
** VirksomhedIdentifikationStruktur
**** AfgiftOperatoerPunktAfgiftIdentifikator: AB12345
*******************************************************************

May 22, 2018 7:18:50 AM org.apache.cxf.services.USImportoerService.Port.USImportoerServiceType
INFO: Outbound Message
---------------------------
ID: 1
Address: https://xxxxxxxxxxxxxxxx/xxx/xxx/xxx
Encoding: UTF-8
Http-Method: POST
Content-Type: text/xml
Headers: {Accept=[*/*], Connection=[Keep-Alive], SOAPAction=["getUSKoeretoejRegistreringHent"]}
Payload:
<soap:Envelope>
        <wsse:Security xmlns:wsse="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd"
                       xmlns:wsu="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd"
                       soap:mustUnderstand="1">
        ...
        ...
        </wsse:Security>
    <soap:Body xmlns:wsu="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd"
               wsu:Id="id-6edc2aa2-e491-4603-a1b7-e7fd0a042f88">
        <ns2:USKoeretoejRegistreringHent_I xmlns="http://rep.oio.dk/skat.dk/basis/kontekst/xml/schemas/2006/09/01/"
                                           xmlns:ns2="http://skat.dk/dmr/2007/05/31/">
            <HovedOplysninger>
                <TransaktionIdentifikator>ACME_01_90bde8af-83d4-4a70-9792-a3a2cda970f4</TransaktionIdentifikator>
                <TransaktionTid>2018-05-22T07:18:47.427+02:00</TransaktionTid>
            </HovedOplysninger>
            <ns2:KoeretoejGenerelIdentifikatorStruktur>
                <ns2:KoeretoejGenerelIdentifikatorValg>
                    <ns2:RegistreringNummerNummer>AB12345</ns2:RegistreringNummerNummer>
                </ns2:KoeretoejGenerelIdentifikatorValg>
            </ns2:KoeretoejGenerelIdentifikatorStruktur>
        </ns2:USKoeretoejRegistreringHent_I>
    </soap:Body>
</soap:Envelope>
---------------------------
```

**Response**:

```
May 22, 2018 7:18:53 AM org.apache.cxf.services.USImportoerService.Port.USImportoerServiceType
INFO: Inbound Message
----------------------------
ID: 1
Response-Code: 200
Encoding: UTF-8
Content-Type: text/xml; charset=utf-8
Headers: {connection=[keep-alive], content-type=[text/xml; charset=utf-8], Date=[Tue, 22 May 2018 05:18:53 GMT], Server=[nginx], SOAPAction=["getUSKoeretoejRegistreringHent"], transfer-encoding=[chunked]}
Payload: 
<env:Envelope xmlns:env="http://schemas.xmlsoap.org/soap/envelope/">
    <env:Header>
        <wsse:Security xmlns:wsse="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd"
                       env:mustUnderstand="1">

        ...
        ...

        </wsse:Security>
    </env:Header>
    <env:Body wsu:Id="Body_TT2Rle67uKnirYp3"
              xmlns:wsu="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd">
        <ns:USKoeretoejRegistreringHent_O xmlns:ns="http://skat.dk/dmr/2007/05/31/">
            <ns1:HovedOplysningerSvar xmlns:ns1="http://rep.oio.dk/skat.dk/basis/kontekst/xml/schemas/2006/09/01/">
                <ns1:TransaktionIdentifikator>ACME_01_90bde8af-83d4-4a70-9792-a3a2cda970f4
                </ns1:TransaktionIdentifikator>
                <ns1:ServiceIdentifikator>dk.skat.dmr.service.RegistreringServiceType</ns1:ServiceIdentifikator>
                <ns1:TransaktionTid>2018-05-22T07:18:52.348+02:00</ns1:TransaktionTid>
                <ns1:SvarStruktur/>
            </ns1:HovedOplysningerSvar>
            <ns:Indhold>
                <ns:RegistreringNummerStruktur>
                    <ns:RegistreringNummerType>Normal</ns:RegistreringNummerType>
                    <ns:RegistreringNummerStatus>Afmeldt</ns:RegistreringNummerStatus>
                    <ns:RegistreringNummerStatusDato>2016-11-11T10:11:39.000+01:00</ns:RegistreringNummerStatusDato>
                    <ns:RegistreringNummerKvadratiskIndhold1>AB12345</ns:RegistreringNummerKvadratiskIndhold1>
                    <ns:RegistreringNummerAflangIndhold>AB12345</ns:RegistreringNummerAflangIndhold>
                    <ns:RegistreringNummerUdloebDato>2016-11-11+01:00</ns:RegistreringNummerUdloebDato>
                    <ns:RegistreringNummerNummer>AB12345</ns:RegistreringNummerNummer>
                </ns:RegistreringNummerStruktur>
            </ns:Indhold>
        </ns:USKoeretoejRegistreringHent_O>
    </env:Body>
</env:Envelope>
--------------------------------------
May 22, 2018 7:18:53 AM dk.skat.dmr.b2b.sample.USKoeretoejRegistreringHentClient invoke
INFO: 
*******************************************************************
** HovedOplysningerSvar
**** Transaction Id: ACME_01_90bde8af-83d4-4a70-9792-a3a2cda970f4
**** Transaction Time: 2018-05-22T07:18:52.348+02:00
**** Service Identification: dk.skat.dmr.service.RegistreringServiceType
** RegistreringNummerStatus: AFMELDT
*******************************************************************
```

## Add another service

Complete these step when adding another service to the sample client project:

**Step 1.** Add the WSDL and Schemas (XSD) to `src/main/resources/wsdls`, e.g. `src/main/resources/wsdls/ANOTHER-SERVICE.wsdl`

**Step 2.** Add the new WSDL to the `pom.xml` file:

```xml
<plugin>
    <groupId>org.apache.cxf</groupId>
    <artifactId>cxf-codegen-plugin</artifactId>
    <version>3.1.9</version>
    <executions>
        <execution>
            <id>generate-sources</id>
            <phase>generate-sources</phase>
            <configuration>
                <sourceRoot>${project.build.directory}/generated/cxf</sourceRoot>
                <wsdlOptions>
                    <wsdlOption>
                        <wsdl>${basedir}/src/main/resources/wsdls/USImportoerService.wsdl</wsdl>
                    </wsdlOption>
                    <!-- Add WSDL reference here:-->
                    <wsdlOption>
                        <wsdl>${basedir}/src/main/resources/wsdls/ANOTHER-SERVICE.wsdl</wsdl>
                    </wsdlOption>
                </wsdlOptions>
            </configuration>
            <goals>
                <goal>wsdl2java</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```
**Step 3.** Build the service stub and objects:

```sh
$ mvn clean install -Dmaven.test.skip=true
```

The `-Dmaven.test.skip=true` part is simply to avoid invoking calls of while adding the new WSDL.

**Step 4.** Implement code to run the generated client:

Clone [USKoeretoejRegistreringHentClient.java](src/main/java/dk/skat/dmr/b2b/sample/USKoeretoejRegistreringHentClient.java) and
simply change to the generated Web Service Client Code (from the WSDL) and Java Objects (from the XSD).

Retain this section of code that instruments the CXF Framework to add the required WS Security header:

```java
Bus bus = new SpringBusFactory().createBus("dmr-b2b-policy.xml", false);
BusFactory.setDefaultBus(bus);
```

Finally, clone [USKoeretoejRegistreringHentClientTest.java](src/test/java/dk/skat/dmr/b2b/sample/USKoeretoejRegistreringHentClientTest.java)
and modify the class to invoke the newly implemented code.

## Advanced Configuration

### Testing Expired and Revoked Certificates

The client keystore includes three certificates:

1. VOCES_gyldig.p12 with alias = `valid`.
2. VOCES_spaerret.p12 with alias = `revoked`.
3. VOCES_udloebet.p12 with alias = `expired`.

By default the client is configured to run with certificate with alias `valid`.

In order to complete a test with any of the other certificates the following files must be
changed:

* File: **src/main/resources/etc/Client_Sign.properties**

Change as described in the file itself:

```
# SKAT: Options =
# - valid (default)
# - revoked
# - expired
org.apache.ws.security.crypto.merlin.keystore.alias=revoked
```

File: **src/main/resources/dmr-b2b-policy.xml**

Change as described in the file itself:

```
<!-- SKAT: Options =
     - valid
     - expired
     - revoked
-->
<entry key="signatureUser" value="valid"/>
```

### Installing other OCESII Certificates in the client keystore

The keystore `src/main/resources/keystore/client-keystore.jks` is already prepared with the
necessary test certificate that is authorized to access the test environment. However, in the
event that another test certificate has been issued this may be installed as follows:

```
$ export P12_PASSPHRASE=CHANGE_ME
$ export P12_CURRENT_ALIAS=CHANGE_ME
$ keytool -delete -alias valid -keystore src/main/resources/keystore/client-keystore.jks -storepass storepassword
$ keytool -changealias -keystore target/VOCES_yours.p12 -storepass $P12_PASSPHRASE -alias $P12_CURRENT_ALIAS -destalias "valid"
$ keytool -v -importkeystore -srckeystore target/VOCES_yours.p12 -srcstoretype PKCS12 -destkeystore src/main/resources/keystore/client-keystore.jks -deststoretype JKS -deststorepass storepassword -srcstorepass $P12_PASSPHRASE
```

Where `P12_PASSPHRASE` and `P12_CURRENT_ALIAS` are passphrase and alias of the OCESII certificate,
respectively. The three keytool command removes the pre configured certificate, changes the the alias
of the new certificate, and finally imports it into the keystore.

## References

* [Apache CXF](http://cxf.apache.org/)
* [Apache CXF Samples](https://github.com/apache/cxf/tree/master/distribution/src/main/release/samples)
