// Définir une fonction pour changer la couleur d'arrière-plan
function changeBackgroundColor()
{
    const colors = ["red", "blue", "green", "yellow"];
    const randomColor = colors[Math.floor(Math.random() * colors.length)];
    document.body.style.backgroundColor = randomColor;
}
  
// Sélectionner un bouton HTML par son ID
const button = document.getElementById("colorButton");

// Ajouter un gestionnaire d'événement au bouton
button.addEventListener("click", changeBackgroundColor);

// Afficher un message dans la console lorsque la page se charge
window.onload = function()
{
  console.log("La page est chargée.");
}