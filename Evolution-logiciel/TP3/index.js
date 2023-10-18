const fs = require('fs');
const esprima = require('esprima');
var walk = require('esprima-walk')

// Vérifiez si un argument de fichier est fourni
if (process.argv.length < 3) {
    console.error('Veuillez fournir un chemin de fichier en tant qu\'argument.');
    process.exit(1);
}

const filePath = process.argv[2];

fs.readFile(filePath, 'utf8', (err, data) => {
    if (err) {
        console.error('Erreur lors de la lecture du fichier:', err);
        return;
    }
    parseScript(data)
        .then(parser => {
            console.log('Fin de l\'analyse');
            createIR(parser);
        })
        .catch(error => {
            console.error("Erreur analyse", error);
        });
});

function parseScript(code) {
    return new Promise((resolve, reject) => {
        try {
            const parser = esprima.parseScript(code);
            console.log(parser.body);
            resolve(parser);
        } catch (error) {
            reject(error);
        }
    });
}

function createIR(parser)
{
    var types = []
    walk (parser,
        function (node)
        {
            switch (node.type)
            {
                case ("NewExpression"):
                case ("FunctionDeclaration"):
                case ("ClassDeclaration"):
                    types.push(new ClassDeclaration);
                case ("AssignementExpression"):
                case ("CallExpression"):
            }
            types.push(node.type);
        }
    );
    console.log(types);
}