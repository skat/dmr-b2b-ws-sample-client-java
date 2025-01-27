# Using SoapUI

The services may also be explored using SoapUI. Here is how based using **SoapUI 5.4.0** with
a configuration that demonstrates adding a timestamp and signing the request.

**Step 1:**  Import the project:

```
USImportoer-soapui-project.xml
```

**Step 2:** Change endpoint provided to you:

![ChangeEndpoint](/assets/SoapUI-ChangeEndpoint.png)

**Step 2:** Add the file `p12/B2B_testklient_SIT.p12` as keystore here using the passphrase provided to you: TODO!!!

![Keystore](/assets/WSSecurityConfig-Keystore.png)

**Step 3:** Select keystore and alias:

![Signature](/assets/WSSecurityConfig-Signature.png)

**Step 4:** Expand `getUSKoeretoejRegistreringHent`, select `Request`, and run:

![Run](/assets/SoapUI-Run.png)
