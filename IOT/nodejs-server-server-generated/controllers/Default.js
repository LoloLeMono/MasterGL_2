'use strict';

var utils = require('../utils/writer.js');
var DefaultService = require('../service/DefaultService');

module.exports.ledPOST = function ledPOST (req, res, next, body) {
  DefaultService.ledPOST(body)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.sensorsGET = function sensorsGET (req, res, next) {
  DefaultService.sensorsGET()
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.sensorsSensor_idGET = function sensorsSensor_idGET (req, res, next, sensor_id) {
  DefaultService.sensorsSensor_idGET(sensor_id)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.stop_infosPOST = function stop_infosPOST (req, res, next, body) {
  DefaultService.stop_infosPOST(body)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.thresholdPOST = function thresholdPOST (req, res, next, body) {
  DefaultService.thresholdPOST(body)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};
