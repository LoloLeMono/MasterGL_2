; déclaration d'un type arbitraire pour les éléments du domaine de discours
(declare-sort T 0)

; déclaration des constantes
(declare-const agatha T)
(declare-const butler T)
(declare-const charles T)

; déclaration des prédicats (= fonctions booléennes)
(declare-fun dreadbury (T) Bool)
(declare-fun hates (T T) Bool)
(declare-fun killed (T T) Bool)

; Prédicat binaire richer pour la richesse
(declare-fun richer (T T) Bool)

; Déclaration d'une constante pour le meurtrier
(declare-const murderer T)



; un habitant du manoir Dreadbury a tué Agatha
(assert (exists ((x T)) (and (killed x agatha) (dreadbury x))))

; Agatha, le majordome et Charles sont les trois seuls habitants du manoir Dreadbury, et sont trois personnes distinctes
(assert (dreadbury agatha))
(assert (dreadbury butler))
(assert (dreadbury charles))
(assert (forall ((x T)) (implies (dreadbury x) (or (= x agatha) (= x butler) (= x charles)))))
(assert (distinct agatha butler))
(assert (distinct agatha charles))
(assert (distinct butler charles))

; un tueur déteste nécessairement sa victime et n’est jamais plus riche qu’elle.
(assert (forall ((x T) (y T)) (implies (killed x y) (and (hates x y) (not (richer y x))))))

; Charles ne déteste aucune des personnes qu’Agatha détestait
(assert (forall ((x T)) (implies (hates agatha x) (not (hates charles x)))))

; Agatha détestait tout le monde à l’exception du majordome
(assert (forall ((x T)) (implies (distinct x butler) (hates agatha x))))

; Le majordome déteste toutes les personnes qui ne sont pas plus riches qu’Agatha ainsi que toutes les personnes qu’Agatha détestait
(assert (forall ((x T)) (implies (or (hates agatha x) (richer agatha x)) (hates butler x))))

; personne ne déteste tout le monde
(assert (forall ((x T)) (exists ((y T)) (not (hates x y)))))

; Le tueur à tué Agatha et est un habitant du manoir Dreadbury 
(assert (and (killed murderer agatha) (dreadbury murderer)))


; QUESTION 2

; Agatha se détestait elle-même, et le majordome la détestait aussi.
(assert (hates agatha agatha))
(assert (hates butler agatha))

; Charles ne la détestait pas.
(assert (not (hates charles agatha)))

; Le majordome est plus riche qu’Agatha.
(assert (richer butler agatha))

; Agatha s’est tuée elle-même.
(assert (killed agatha agatha))


; cette commande demande au solveur de vérifier la satisfiabilité
(check-sat)
(get-model)