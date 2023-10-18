const fs = require('fs');
const esprima = require('esprima');
const walk = require( 'esprima-walk' );
const { type } = require('os');

function parseJavaScript(code) {
    try {
        const ast = esprima.parseScript(code);
        return ast;
    } catch (error) {
        console.error("Erreur lors de l'analyse:", error);
        return null;
    }
}

// VÃ©rifiez si un argument de fichier est fourni
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

    const parsedAST = parseJavaScript(data);
    console.log(JSON.stringify(parsedAST, null, 2));

    let inter = [];

    walk(parsedAST,
        function handleNode(node) {
            switch (node.type) {
                case "FunctionDeclaration":
                    let params = [];
                    if(node.params){
                        for(const param of node.params){
                            params.push(param.name);
                        }
                    }
                    let body = handleInstruction(node.body) || [];
                    inter.push(new FunctionDeclaration(node.id.name, params, body));
                    break;
                case "CallExpression":
                    inter.push(new Call(node.callee.name));
                    break;
                default:
                    return;
            }
        });
    console.log(JSON.stringify(inter, null, 2)); 
});


function handleInstruction(node) {
    if (!node) return null;

    switch (node.type) {
        case 'BlockStatement':
            return node.body.map(handleInstruction).filter(item => item !== null);
        case 'ReturnStatement':
            return { type: 'Return', value: handleInstruction(node.argument) };
        case 'BinaryExpression':
            return {
                type: 'BinaryExpression',
                operator: node.operator,
                left: handleInstruction(node.left),
                right: handleInstruction(node.right)
            };
        case 'Identifier':
            return node.name;
        default:
            return { type: node.type };
    }
}

class FunctionDeclaration{
    constructor(functionName, params = [], body = []) {
        this.functionName = functionName;
        this.params = params;
        this.body = body;
    }
}

class Call {
    constructor(functionName) {
        this.functionName = functionName;
    }
}