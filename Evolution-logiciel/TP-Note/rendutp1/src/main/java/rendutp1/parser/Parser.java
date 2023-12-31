package rendutp1.parser;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.DefaultAttribute;
import org.jgrapht.nio.dot.DOTExporter;

import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.MutableGraph;

import rendutp1.cli.UserInterface;
import rendutp1.utils.Edge;
import rendutp1.utils.Node;
import rendutp1.visitor.MethodDeclarationVisitor;
import rendutp1.visitor.MethodInvocationVisitor;
import rendutp1.visitor.PackageDeclarationVisitor;
import rendutp1.visitor.TypeDeclarationVisitor;

public class Parser {
	
	
	public static final String projectPath = "/Users/lololemono/Documents/MasterGL_2/Evolution-logiciel/TP1/org.anonbnr.design_patterns-main";
	public static final String projectSourcePath = projectPath + "/src";
	public static final String jrePath = "/System/Library/Frameworks/JavaVM.framework/";
	
	public static UserInterface userCLI = new UserInterface();
	
	public static CompilationUnit parse;

	public static void main(String[] args) throws IOException, InterruptedException {
		
		Scanner scan = new Scanner(System.in);
		// read java files
		final File folder = new File(projectSourcePath);
		ArrayList<File> javaFiles = listJavaFilesForFolder(folder);
		
		int indice = 0;
		while(userCLI.getTypeVisitor() != 14) {
			userCLI.printCLI(indice, scan);
			indice++;
			System.out.println("\n");
			System.out.println("======================================================");
			userChoice(userCLI.getTypeVisitor(), javaFiles);
			System.out.println("======================================================");
			System.out.println("\n");
			System.out.println("Voulez vous continuer ? Y or N");
			String charReaded = scan.next();
			if (charReaded.equals("N")) {
				System.exit(1);
			}
		}
	}

	// read all java files from specific folder
	public static ArrayList<File> listJavaFilesForFolder(final File folder) {
		ArrayList<File> javaFiles = new ArrayList<File>();
		for (File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				javaFiles.addAll(listJavaFilesForFolder(fileEntry));
			} else if (fileEntry.getName().contains(".java")) {
				// System.out.println(fileEntry.getName());
				javaFiles.add(fileEntry);
			}
		}

		return javaFiles;
	}

	// create AST
	private static CompilationUnit parse(char[] classSource) {
		ASTParser parser = ASTParser.newParser(AST.JLS4); // java +1.6
		parser.setResolveBindings(true);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
 
		parser.setBindingsRecovery(true);
 
		Map options = JavaCore.getOptions();
		parser.setCompilerOptions(options);
 
		parser.setUnitName("");
 
		String[] sources = { projectSourcePath }; 
		String[] classpath = {jrePath};
 
		parser.setEnvironment(classpath, sources, new String[] { "UTF-8"}, true);
		parser.setSource(classSource);
		
		return (CompilationUnit) parser.createAST(null); // create and parse
	}
	
	//choice made by the user
	public static void userChoice(int choice, ArrayList<File> javaFiles) throws IOException {
		
		TypeDeclarationVisitor visitorClass = new TypeDeclarationVisitor();
		MethodDeclarationVisitor visitorMethod = new MethodDeclarationVisitor();
		MethodInvocationVisitor visitorMethodInv = new MethodInvocationVisitor();
		PackageDeclarationVisitor visitorPackage = new PackageDeclarationVisitor();
		
		switch(choice) {
		
		case 0:
			parseFilesClass(visitorClass, javaFiles);
			parseFilesMethod(visitorMethod, javaFiles);
			printCallGraph(visitorClass);
			break;
		
		case 1:
			parseFilesClass(visitorClass, javaFiles);
			System.out.println("Nombre de classe(s) : " + visitorClass.sizeList());
			visitorClass.printTypeDeclaration();
			break;
			
		case 2:
			int nbLines = 0;
			for (File fileEntry : javaFiles) {
				String content = FileUtils.readFileToString(fileEntry);
				parse = parse(content.toCharArray());
				nbLines += countLineNumber();
			}
			System.out.println("Nombre de ligne(s) : " + nbLines);
			break;
			
		case 3:
			parseFilesMethod(visitorMethod, javaFiles);
			System.out.println("Nombre de methode(s) : " + visitorMethod.sizeList());
			visitorMethod.printMethodDeclaration();
			break;
			
		case 4: 
			parseFilesPackage(visitorPackage, javaFiles);
			System.out.println("Nombre de package(s) : " + visitorPackage.sizeList());
			visitorPackage.printPackageDeclaration();
			break;
			
		case 5: 
			parseFilesClass(visitorClass, javaFiles);
			System.out.println("Nombre moyen de methode(s) par classe : " + visitorClass.averageNumberOfMethods());
			break;
		
		case 6:
			averageNumberOfLinesPerMethods(visitorMethod, javaFiles);
			System.out.println("Nombre moyen de ligne(s) par methode : " + (float)(visitorMethod.getNumberOfLines()/(float)visitorMethod.getMethods().size()));
			break;
			
		case 7:
			parseFilesClass(visitorClass, javaFiles);
			System.out.println("Nombre moyen de variable(s) par classe : " + visitorClass.averageNumberOfVariables());
			break;
			
		case 8:
			parseFilesClass(visitorClass, javaFiles);
			List<TypeDeclaration> topClassByNumberOfMethods = visitorClass.printTypeDeclaration10PourcentMethod();
			System.out.println("Remarque : Si vous n'avez pas suffisamment de classe dans votre projet pour faire 10%");
			System.out.println("L'application vous affichera seulement la classe qui contient le plus de methodes \n");
			System.out.println("Voici les 10% des classes qui possèdent le plus de méthodes : \n");
			
			for(TypeDeclaration type : topClassByNumberOfMethods) {
				System.out.println("Class : " + type.getName());
				System.out.println("Nombre de methode(s) de celle-ci : " + type.getMethods().length + "\n");
			}
			break;
			
		case 9:
			parseFilesClass(visitorClass, javaFiles);
			List<TypeDeclaration> topClassByNumberOfVariables = visitorClass.printTypeDeclaration10PourcentVariable();
			System.out.println("Remarque : Si vous n'avez pas suffisamment de classe dans votre projet pour faire 10%");
			System.out.println("L'application vous affichera seulement la classe qui contient le plus de variables \n");
			System.out.println("Voici les 10% des classes qui possèdent le plus de variables : \n");
			
			for(TypeDeclaration type : topClassByNumberOfVariables) {
				System.out.println("Class : " + type.getName());
				System.out.println("Nombre de variable(s) de celle-ci : " + type.getFields().length + "\n");
			}
			break;
			
		case 10:
			parseFilesClass(visitorClass, javaFiles);
			List<TypeDeclaration> topClassByNumberMethods = visitorClass.printTypeDeclaration10PourcentMethod();
			List<TypeDeclaration> topClassByNumberVariables = visitorClass.printTypeDeclaration10PourcentVariable();
			System.out.println("Voici les classes qui font parties des 10% possédant le plus de variables et de methodes : \n");
			
			for(TypeDeclaration typeMethods : topClassByNumberMethods) {
				for(TypeDeclaration typeVariables : topClassByNumberVariables) {
					if (typeMethods.getName().toString().equals(typeVariables.getName().toString())) {
						System.out.println("Class : " + typeMethods.getName());
						System.out.println("Nombre de variable(s) de celle-ci : " + typeMethods.getFields().length);
						System.out.println("Nombre de methode(s) de celle-ci : " + typeMethods.getMethods().length + "\n");
					}
				}
			}
			break;
			
		case 11:
			parseFilesClass(visitorClass, javaFiles);
			Scanner scanInt = new Scanner(System.in);
			System.out.println("Veuillez donner un nombre minimal de methodes par classe : ");
			visitorClass.printTypeDeclarationWithXMethods(scanInt.nextInt());
			break;
			
		case 12:
			BestMethodPerClass(visitorClass, javaFiles);
			break;
			
		case 13:
			parseFilesMethod(visitorMethod, javaFiles);
			visitorMethod.printMethodWithMaxParam();
			break;
			
		case 14:
			System.exit(1);
		}
	}
	
	public static void parseFilesClass(TypeDeclarationVisitor visitorClass, ArrayList<File> javaFiles) throws IOException {
		for (File fileEntry : javaFiles) {
			String content = FileUtils.readFileToString(fileEntry);
			parse = parse(content.toCharArray());
			classInfo(visitorClass);
		}
	}
		
	public static void parseFilesMethod(MethodDeclarationVisitor visitorMethod, ArrayList<File> javaFiles) throws IOException {
		for (File fileEntry : javaFiles) {
			String content = FileUtils.readFileToString(fileEntry);
			parse = parse(content.toCharArray());
			methodInfo(visitorMethod);
		}
	}
		
	public static void parseFilesPackage(PackageDeclarationVisitor visitorPackage, ArrayList<File> javaFiles) throws IOException {
		for (File fileEntry : javaFiles) {
			String content = FileUtils.readFileToString(fileEntry);
			parse = parse(content.toCharArray());
			packageInfo(visitorPackage);
		}
	}
		
	// methods to accept visitor
	public static void classInfo(TypeDeclarationVisitor visitorClass) {
		parse.accept(visitorClass);
	}
		
	public static void methodInfo(MethodDeclarationVisitor visitorMethod) {
		parse.accept(visitorMethod);
	}
		
	public static void packageInfo(PackageDeclarationVisitor visitorPackage) {
		parse.accept(visitorPackage);
	}
		
	// get number of lines in the application
	public static int countLineNumber() {
		return parse.getLineNumber(parse.getLength() - 1);
	}
	
	public static void averageNumberOfLinesPerMethods(MethodDeclarationVisitor visitorMethod, ArrayList<File> javaFiles) throws IOException {
		for (File fileEntry : javaFiles) {
			String content = FileUtils.readFileToString(fileEntry);
			parse = parse(content.toCharArray());
			methodInfo(visitorMethod);
			visitorMethod.numberOfLinesOfMethodsPerFiles(parse);
		}
	}
	
	public static void BestMethodPerClass(TypeDeclarationVisitor visitorClass, ArrayList<File> javaFiles) throws IOException {
		System.out.println("Les 10% des méthodes qui ont le plus grand nombre de lignes, par classe");
		for (File fileEntry : javaFiles) {
			String content = FileUtils.readFileToString(fileEntry);
			parse = parse(content.toCharArray());
			classInfo(visitorClass);
			visitorClass.print10PourcentMethodPerClass(parse);
		}
	}
	
	public static void printCallGraph(TypeDeclarationVisitor visitorClass) throws IOException {
		List<Node> listNode = new ArrayList<Node>();
		List<Edge> listEdge = new ArrayList<Edge>();
		
		for(TypeDeclaration type : visitorClass.getTypes()) {
			
			for(MethodDeclaration method : type.getMethods()) {
				
				MethodInvocationVisitor visitor2 = new MethodInvocationVisitor();
				method.accept(visitor2);
				
				Node nodeMethodDecla = new Node(type.getName().toString() + "." + method.getName().toString());
				listNode.add(nodeMethodDecla);
				
				
				if (visitor2.getMethods().size() != 0) {
					//System.out.println("\nMethod : " + nodeMethodDecla.getNode());

					for (MethodInvocation methodInvocation : visitor2.getMethods()) {
						
						if (methodInvocation.getExpression() != null) {
							
							if (methodInvocation.getExpression().resolveTypeBinding() != null) {
								
								Node nodeMethodInv = new Node(methodInvocation.getExpression().resolveTypeBinding().getName() + "." + methodInvocation.getName().toString());
								listNode.add(nodeMethodInv);
								listEdge.add(new Edge(nodeMethodDecla.getNode(), nodeMethodInv.getNode()));
							}
						}
						else {
							
							Node nodeMethodInv = new Node(methodInvocation.getName().toString());
							listNode.add(nodeMethodInv);
							listEdge.add(new Edge(nodeMethodDecla.getNode(), nodeMethodInv.getNode()));
						}
						
//						System.out.println(" --- calls : " + nodeMethodInv.getNode());
					}
				}
			}
		}
		
		// Add vertex and edges to the graph
		Graph<String, DefaultEdge> graph = new DefaultDirectedGraph<String, DefaultEdge>(DefaultEdge.class);
		for (Node node : listNode) {
			graph.addVertex(node.getNode());
		}
		for (Edge edge : listEdge) {
			graph.addEdge(edge.getNodeSource(), edge.getNodeTarget());
		}
		
//		System.out.println(graph.toString());
		
		// Export the graph in a png file
		DOTExporter<String, DefaultEdge> exporter = new DOTExporter<String, DefaultEdge>();
	        exporter.setVertexAttributeProvider((v) -> {
	            Map<String, Attribute> map = new LinkedHashMap<String, Attribute>();
	            map.put("label", DefaultAttribute.createAttribute(v.toString()));
	            return map;
	        });
	        Writer writer = new StringWriter();
	        exporter.exportGraph(graph, writer);
	        exporter.exportGraph(graph, new File("example/callGraph.dot")); // To DOT File
	        MutableGraph g = new guru.nidi.graphviz.parse.Parser().read(writer.toString());
	        //Graphviz.fromGraph(g).height(1000).render(Format.PNG).toFile(new File("example/callGraph.png"));
	        System.out.println(writer.toString());
	}	
}
