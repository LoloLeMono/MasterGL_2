// controller.h
#pragma once
#include "model.h"
#include "view.h"

class Controller {
public:
  Model model;
  View view;
  // Déclarations de fonctions liées au contrôleur
  void handleUserInput();
};
