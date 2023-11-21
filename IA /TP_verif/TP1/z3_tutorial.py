from __future__ import annotations

import z3

def example_sat():
    solver = z3.Solver()

    # cette fonction génère une variable SMT, de type int
    x = z3.Int("x")

    # l'API de Z3 utilise la surcharge des opérateurs arithmétiques
    # habituels de Python pour déclarer de manière succinte des
    # formules SMT
    constraint = (x / 5 == 2)

    # on ajoute cette formule comme un contrainte dans le problème SMT
    solver.add(constraint)

    result = solver.check()
    if result == z3.unsat:
        print("aucune affectation ne satisfait la contrainte")
    elif result == z3.sat:
        print("le problème a une solution")
        model = solver.model()
        vx = model[x]
        print("la valeur de 'x' dans cette solution est " + str(vx))
        # on peut aussi évaluer directement des expressions complexes en prenant les valeurs de la solution 
        vexp = model.evaluate(x - 8)
        print("la valeur de 'x - 8' dans cette solution est " + str(vexp))
    else:
        print("le solveur n'a pas pu déterminer si le problème était satisfiable ou non")      

        
def example_unsat():
    # on peut aussi utiliser un solveur SMT pour prouver des théorèmes
    # par réfutation. On souhaite ici prouver le célébre
    # syllogisme "tous les humains sont mortels, et Socrate est
    # humain, donc Socrate est mortel"
    
    solver = z3.Solver()

    # on déclare un type non-interprété
    t = z3.DeclareSort("t")

    # et une constante de ce type
    socrate = z3.Const('Socrate', t)

    # ainsi que les prédicats nécessaires (dans le standard SMT, prédicat = fonction booléenne)
    humain = z3.Function('humain', t, z3.BoolSort())
    mortel = z3.Function('mortel', t, z3.BoolSort())
    
    # hypothèses
    solver.add(humain(socrate))
    x = z3.Const('x', t)
    solver.add(z3.ForAll([x], z3.Implies(humain(x), mortel(x))))

    # négation du but à prouver (notez que contrairement aux
    # opérateurs arithmétiques, il n'y pas de surcharge des opérateurs
    # booléens, il faut utiliser z3.Not, z3.Or, z3.And...)
    solver.add(z3.Not(mortel(socrate)))

    result = solver.check()
    if result == z3.unsat:
        print("la formule est un théorème")
    elif result == z3.sat:
        print("la formule a un contre-modèle")
    else:
        print("le solveur n'a pas pu déterminer si la formule était satisfiable ou non")      

        
def example_array():
    solver = z3.Solver()

    # a1 et a2 sont deux talbeaux de booléens indexés par des entiers
    a1 = z3.Array("a1", z3.IntSort(), z3.BoolSort())
    a2 = z3.Array("a2", z3.IntSort(), z3.BoolSort())
    
    p = z3.Int("p")
    b = z3.Bool("b")

    # a1[25] = False et a2[58] False
    solver.add(z3.Select(a1, 25) == False)
    solver.add(z3.Select(a1, 58) == False)

    # a2 est obtenu en mettant à jour une seule position p de a1
    solver.add(a2 == z3.Store(a1, p, b))
    # a2[1] = True
    solver.add(z3.Select(a2, 58) == True)

    # quelle est la valeur de p?
    result = solver.check()
    if result == z3.sat:
        model = solver.model()
        print("la valeur de p est " + str(model[p]))
    else:
        assert False

        
def example_function_synthesis():
    solver = z3.Solver()

    n = 2
    
    # vecteurs de n bits
    x = z3.BitVec("x", n)
    y = z3.BitVec("y", n)

    # spécification de la fonction f qui retourne le maximum de deux vecteurs de bits
    # attention l'opérateur >= qui est utilisé ici est la version signée (la version non-signée est z3.UGE)
    f = z3.Function("f", z3.BitVecSort(n), z3.BitVecSort(n), z3.BitVecSort(n))
    solver.add(z3.ForAll([x, y], z3.And(f(x, y) >= x,
                                        f(x, y) >= y,
                                        z3.Or(f(x, y) == x, f(x, y) == y))))

    # le solveur doit synthétiser une fonction f qui satisfait la contrainte
    result = solver.check()
    if result == z3.sat:
        model = solver.model()
        print("fonction synthétisée : " + str(model[f]))
    else:
        print("échec de la synthèse")

        
def example_bitvector_synthesis():
    solver = z3.Solver()
    
    # vecteur de 32 bits
    x = z3.BitVec("x", 32)

    # l'expression qui nous sert de spécification (déclarée avec la surcharge d'opérateur)
    reference_expression = x * 8

    # on souhaite synthétiser une expression équivalente, de la forme x << c
    # on utilise le solveur pour trouver la valeur de c 
    c = z3.BitVec("c", 32)
    synthesized_expression = x << c

    solver.add(z3.ForAll([x], synthesized_expression == reference_expression))    

    result = solver.check()
    if result == z3.sat:
        model = solver.model()
        print("expression synthétisée : x << " + str(model[c]))
    else:
        print("échec de la synthèse")

        
if __name__ == "__main__":
    example_sat()
    example_unsat()
    example_array()
    example_function_synthesis()
    example_bitvector_synthesis()
