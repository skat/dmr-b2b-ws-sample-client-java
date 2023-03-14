#!/bin/sh

CLIENT_TRUSTSTORE=src/main/resources/keystore/client-truststore.jks

echo "Before import ..."
keytool -list -keystore $CLIENT_TRUSTSTORE -storepass storepassword

echo "Importing ..."

# Trust certificate used by server to sign response.
keytool -noprompt -import -alias skatserver -file pem/dmr-test-system.pem -keystore $CLIENT_TRUSTSTORE -storepass storepassword

# Import SSL cert for secure transport (https://..)
# The full cert. chain is viewable with openssl command: openssl s_client -connect host:port -showcerts
#
# Level 1 is sufficient
keytool -noprompt -import -alias ssl_chain_1 -file pem/ssl-chain-1.pem -keystore $CLIENT_TUSTSTORE -storepass storepassword
#
# ... but for completeness we could import the full chain (level 1,2,3..)
# keytool -noprompt -import -alias ssl_chain_0 -file pem/ssl-chain-0.pem -keystore $CLIENT_TRUSTSTORE -storepass storepassword
# keytool -noprompt -import -alias ssl_chain_1 -file pem/ssl-chain-1.pem -keystore $CLIENT_TRUSTSTORE -storepass storepassword
# keytool -noprompt -import -alias ssl_chain_2 -file pem/ssl-chain-2.pem -keystore $CLIENT_TRUSTSTORE -storepass storepassword


echo "After import ..."
keytool -list -keystore $CLIENT_TRUSTSTORE -storepass storepassword

### Reverse imports
### NOT ACTIVE
# keytool -delete -alias skatserver -keystore src/main/resources/keystore/client-truststore.jks -storepass storepassword
# keytool -delete -alias ssl_chain_0 -keystore src/main/resources/keystore/client-truststore.jks -storepass storepassword
# keytool -delete -alias ssl_chain_1 -keystore src/main/resources/keystore/client-truststore.jks -storepass storepassword
# keytool -delete -alias ssl_chain_2 -keystore src/main/resources/keystore/client-truststore.jks -storepass storepassword
