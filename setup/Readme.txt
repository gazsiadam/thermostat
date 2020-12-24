1. Install wiringpi: 'sudo apt-get install wiringpi'
2. Install Pi4J: 'curl -sSL https://pi4j.com/install | sudo bash'
3. Copy actuator.jar file to /home/pi/smarthome/
4. Copy smarthome_thermostate.service to /lib/systemd/system
5. Run 'sudo systemctl daemon-reload
6. Run 'sudo systemctl enable smarthome_thermostate.service'
7. Run 'sudo systemctl start smarthome_thermostate.service'
8. Follow log: 'sudo journalctl -u smarthome_thermostat.service -f'