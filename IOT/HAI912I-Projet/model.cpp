// model.cpp
#include "model.h"

//Model model;

void Model::initialize() {
  // Initialisation du modèle
  ledState = false;
}

void Model::setLedState(bool state) {
  // Mettre à jour l'état de la LED dans le modèle
  ledState = state;
}

bool Model::getLedState() {
  // Obtenir l'état actuel de la LED
  return ledState;
}