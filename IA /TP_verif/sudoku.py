from __future__ import annotations

import z3

if __name__ == "__main__":

    # les valeurs de la grille de départ, 0 si la case est vide
    grid = [[0, 0, 0, 0, 0, 0, 0, 0, 6],
            [0, 0, 0, 8, 6, 0, 2, 0, 0],
            [0, 0, 3, 9, 0, 0, 0, 0, 8],
            [0, 2, 0, 0, 0, 0, 0, 7, 0],
            [6, 8, 9, 0, 0, 0, 0, 0, 0],
            [0, 3, 0, 0, 0, 0, 0, 2, 5],
            [0, 0, 0, 0, 0, 5, 1, 4, 0],
            [9, 0, 0, 0, 0, 3, 0, 0, 0],
            [0, 0, 1, 0, 2, 0, 0, 0, 0]]

    # les variables entières du problème SMT, arrangées dans un tableau à deux dimensions
    v = [[z3.Int('v' + str(x) + str(y)) for y in range(9)] for x in range(9)]
    
    solver = z3.Solver()

    # contrainte: la valeur des cases non-vides est fixées à la valeur indiquée dans 'grid'
    for i in range(len(grid)):
        for j in range(len(grid[i])):
            if grid[i][j] != 0:
                solver.add(v[i][j] == grid[i][j])

    # TODO: ajouter les contraintes manquantes ici
        
    result = solver.check()

    if result == z3.unsat:
        print("This grid does not have a solution")
    elif result == z3.sat:
        model = solver.model()
        # affichage de la solution
        print("-" * 31)
        for i in range(3):
            for j in range(3):
                print("|", end='')
                for k in range(3):
                    for l in range(3):
                        print(" " + str(model[v[3 * i + j][3 * k + l]]) + " ", end='')
                    print('|', end='')
                print('')                
            print("-" * 31)
    else:
        # dans certain cas, z3 peut répondre 'unknown', mais pas pour ce problème
        assert False
