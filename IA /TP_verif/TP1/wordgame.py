from z3 import *

WORD_LENGTH = 4

def array_to_string(model, array):
    # TODO
    return ""

if __name__ == "__main__":

    # initialiser dictionary en lisant le fichier 'words.txt'
    with open('words.txt') as f:
        dictionary = [line.rstrip() for line in f]
        for w in dictionary:
            assert len(w) == WORD_LENGTH

    starting_word = "hope"
    ending_word = "lips"

    starting_array = Array("start", IntSort(), IntSort())
    intermediate_array = Array("intermediate", IntSort(), IntSort())
    ending_array = Array("end", IntSort(), IntSort())

    # TODO ajouter les contraintes ici

    solver = Solver()

    result = solver.check()

    if result == unsat:
        print("Ending word cannot be reached in 1 move")
    elif result == sat:
        model = solver.model()
        print(array_to_string(model, starting_array))
        print(array_to_string(model, intermediate_array))
        print(array_to_string(model, ending_array))
