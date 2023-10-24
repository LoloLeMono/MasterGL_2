// model.h
#pragma once

struct Model {
  bool ledState;
  // Déclarations de fonctions liées au modèle
  void initialize();
  void setLedState(bool state);
  bool getLedState();
};