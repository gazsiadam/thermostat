const PROXY_CONFIG = {
  "/thermostat-api/*": {
    "target": "http://localhost:8082",
    "secure": false,
    "logLevel": "debug",
    "pathRewrite": {
      "^/thermostat-api": ""
    }
  }
};

module.exports = PROXY_CONFIG;
