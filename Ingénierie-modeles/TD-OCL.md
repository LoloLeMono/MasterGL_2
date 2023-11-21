# Tortue

## Question 1

### 1.1 La taille d'une tortue est comprise entre 0 et la taille maximale admise pour son espece

```
context Tortue inv:
    self.taille > 0 and self.taille <= self.espece.tailleMaxAdulte
```

### 1.2 la température de jour est supérieure à la température de nuit

```
context ModeElevage inv:
    self.tempJour > self.tempNuit
```

### 1.3 On ne peut changer la taille d’une tortue que pour l’augmenter ; Précisez la pré-condition et post-condition de l’opération ;

```java
context Tortue::changerTaille(nvlleTaille: entier)
    pre: nvlleTaille > 0 and nvlleTaille > taille and nvlleTaille <= espece.tailleMaxAdulte
    post: nvlleTaille = self.taille
```

## Question 2 - Collection

### 2.1.1 Une tortue mâle ne peut avoir de dates de ponte 

```
context Tortue inv:
   self.sexe = #M implies self.datesPonte->isEmpty()
```

### 2.1.1 Une tortue habite l’un des lieux de la répartition géographique de son espéce

```
context Tortue inv:
    self.espece.repartitionGeographique.lieu->includes(self.lieu)
```

### 2.1.3 Tout aliment utilisable pour l’élevage en captivité fait partie du régime alimentaire général 

```
context EspeceTortue inv:
    biologie
    .typeAliment
    .aliment
        ->flatten()
        ->asSet()
        ->includesAll(modeElevage.regimeCaptivité.aliment->flatten()->asSet)

```

### 2.1.4 écrire la post-condition de l’opération nourriturePossible(t:TypeAliment):boolean de la classe espèceTortue ; t est un un type d’aliment possible s’il fait partie du régime alimentaire général de l’espèce

```
context EspeceTortue::nourriturePossible(t: TypeAliment): boolean
    post: result = self.biologie.typeAliment->includes(t)
```

### 2.1.5 écrire la pré-condition de l’opération mange(a:Aliment) de la classe Tortue ; une tortue ne peut manger que des aliments prévus par un des régimes alimentaires de son espèce (régime général ou de captivité), il faut donc tester si la tortue est captive ou non pour connaître le régime alimentaire et savoir si a est admissible.

```
context Tortue::mange(a: Aliment)
    pre: if self.captive then
            espece
                .modeElevage
                .regimeCaptivite
                .typeAliment
                .aliment
                    ->flatten()
                    ->asSet()
                    ->includes(a)
         else
            biologie
                .typeAliment
                .aliment
                    ->flatten()
                    ->asSet()
                    ->includes(a)
         endif;
```

# Meta Model

## Question 1.1

> La borne inférieure doit être positive ou nulle

_Solution 1_

```
context MultiplicityElement inv:
    lowerBound() >= 0
```
_Solution 2_

```
context MultiplicityElement inv:
    not(lowerValue = null or lowerValue.integerValue = null)
        implies lowerValue.integerValue() >= 0
```


> La borne supérieure doit être supérieur à la borne inférieure.

```
context MultiplicityElement inv:
    upperBound() > lowerBound()
```

> La valeur dérivée de /lower doit être égale à la borne inférieure donnée par la requête ci-dessus.

```
context MultiplicityElement::lower derive: 
    lowerBound()
```

> La valeur dérivée de /upper doit être égale à la borne supérieure par
la requête ci-dessus.

```
context MultiplicityElement::upper derive: 
    upperBound()
```

> La requête isMultivalued() retourne vrai si la propriété peut prendre
plus d’une valeur ; elle ne s’applique que lorsqu’une borne supérieure
a été spécifiée.

On suppose que isMultivalued() est défini dans la classe MultiplicityElement.

```
context MultiplicityElement::isMultivalued()
    pre: upperValue <> null | upperValue.notEmpty()
    body: upper > 1;    
```

> La requête includesMultiplicity(M: MultiplicityElement) retourne
vrai si la multiplicité de l’élément inclut M. Vous devez déterminer
les conditions d’application.

```
context MultiplicityElement::includesMultiplicty(M: MultiplicityElement)
    body: self.lower <= M.lower and self.upper >= M.upper
```

## Question 2.2

> 1. Requête retournant les features static.

```
context Classifier
    def: staticFeatures(): Set(Feature)
        self.feature->select(f: Featurex | f.isStatic)
```

> 2. Requête retournant les BehavioralFeatures abstraites.

```
context Classifier
    def: abstractBehavioralFeatures(): Set(BeavioralFeature)
        self.feature
            ->select(f | f.oclIsTypeOf(BeavioralFeature) and f.oclAsType(BeavioralFeaturex).isAbstract)
```

> 3. Contrainte indiquant que les BehavioralFeatures abstraites n’ont pas
de méthode associée.

```
context Classifier inv:
    abstractBehavioralFeatures().forAll(f: BeavioralFeature | f.method.isEmpty())
```

Si on etait dans le context de BehavioralFeature directement

```
context BehavioralFeature inv:
    isAbstract implies method.isEmpty()
```


> 4. Requête retournant les StructuralFeatures dont la collection des va-
leurs est un bag

```
context Classifier
    def: structuralFeatures(): Bag(StructuralFeature)
        self.feature
            ->select(f | f.oclIsTypeOf(StructuralFeature) and not(f.oclAsType(StructuralFeature).isUnique) and not f.oclAsType(StructuralFeature).isOrdered)
```

> 5. Requête retournant les BehavioralFeatures ordonnées par leur nombre
de paramètres. 

```
context Classifier
    def: behavioralFeatures(): Set(BehavioralFeature)
        self.feature
            ->select(f | f.oclIsTypeOf(BeavioralFeature))
            ->collect(f | f.oclAsTypeOf(BehavioralFeature))
            ->sortedBy(f | f.ownedParameter.size())
       
```

> 6. Requête retournant les noms des BehavioralFeatures dont tous les pa-
ramètres sont passés avec la direction in ou return.

```
context Classifier
    def: f(): Set(String)
        self.feature
            ->select(f | f.oclIsTypeOf(BeavioralFeature))
            ->collect(f | f.oclAsTypeOf(BehavioralFeature))
            ->forAll(direction = #in or direction = #return)
            ->collect(name)
            ->asSet()
    
```

## Question 3


> 3.2.1 Requête parents():Classifier [0..*] retournant tous les successeurs immédiats (généralisations) d’un classifier 

```
def parents(): Set(Classifier)
    generalization.general.asSet()
```