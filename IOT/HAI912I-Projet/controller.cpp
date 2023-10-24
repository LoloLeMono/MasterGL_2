// controller.cpp
#include "controller.h"
#include "model.h"
#include "view.h"

void Controller::handleUserInput() {
  // Gérer les actions de l'utilisateur (par exemple, basculer l'état de la LED)
  bool newState = !this->model.getLedState();
  this->model.setLedState(newState);
  this->view.displayLedState(newState);
}