'use strict';


/**
 * Control LED
 * Turn the LED on or off.
 *
 * body Led_body  (optional)
 * no response value expected for this operation
 **/
exports.ledPOST = function(body) {
  return new Promise(function(resolve, reject) {
    resolve();
  });
}


/**
 * List Sensors
 * Retrieve a list of connected sensors.
 *
 * no response value expected for this operation
 **/
exports.sensorsGET = function() {
  return new Promise(function(resolve, reject) {
    resolve();
  });
}


/**
 * Get Sensor Info
 * Retrieve information for a specific sensor.
 *
 * sensor_id String ID of the sensor
 * no response value expected for this operation
 **/
exports.sensorsSensor_idGET = function(sensor_id) {
  return new Promise(function(resolve, reject) {
    resolve();
  });
}


/**
 * Set stop infos
 * Set the names of the stop and the terminus of the line and the line number.
 *
 * body Stop_infos_body  (optional)
 * no response value expected for this operation
 **/
exports.stop_infosPOST = function(body) {
  return new Promise(function(resolve, reject) {
    // Appel à l'API Tam pour afficher les données sur le TTGO
    resolve();
  });
}


/**
 * Set Threshold
 * Set the threshold for the LED based on sensor values.
 *
 * body Threshold_body  (optional)
 * no response value expected for this operation
 **/
exports.thresholdPOST = function(body) {
  return new Promise(function(resolve, reject) {
    resolve();
  });
}

