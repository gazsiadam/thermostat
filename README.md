On/Off thermostat for combi boilers using Java, Spring Boot, Angular running on Raspberry Pi 4.
The code is written to use a DHT11 temperature sensor wired to GPIO_8 pin 
The relay should be wired to GPIO_29 pin (I used a SRD-05VDC-SL-C running on 3V - on 5V it was remaining on the on position)

It has a working API implementation for OpenWeatherMap. It needs an API key in the appliaction.yml file to make it work.
Optinally, you can also log in dynamoDB from AWS which can also be run locally.


Minimal instructions for getting it running on Raspberry Pi 4
1. Install Java 11
2. Install Apache2
    - sudo apt get install apache2
    - sudo chmod 777 /var/www/html/
    - add reverse proxy for /thermostat-api
        - sudo a2enmod proxy
        - sudo a2enmod proxy_http
        - sudo a2enmod proxy_wstunnel
        - sudo nano /etc/apache2/sites-available/000-default.conf - Add these lines to the VirtualHost *:80 tag
            ProxyPreserveHost On
            ProxyPass /thermostat-api http://127.0.0.1:8082/
            ProxyPassReverse /thermostat-api http://127.0.0.1:8082
            ProxyPreserveHost On
            ProxyPass /data/websocket ws://127.0.0.1:8082/data/websocket
            ProxyPassReverse /data/websocket ws://127.0.0.1:8082/data/websocket
        - sudo systemctl restart apache2
    - copy frontend to /var/www/html
    
   
For Local development 
	- DynamoDB: java "-Djava.library.path=/DynamoDBLocal_lib" -jar DynamoDBLocal.jar -sharedDb