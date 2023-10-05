package stats;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.internal.utils.FileUtil;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public class Parser {
	
	public static final String projectPath = "/Users/lololemono/Documents/MasterGL_2/Evolution-logiciel/TP1/org.anonbnr.design_patterns-main";
	public static final String projectSourcePath = projectPath + "/src";
	public static final String jrePath = "/System/Library/Frameworks/JavaVM.framework/";

	public static void main(String[] args) throws IOException {

		// read java files
		final File folder = new File(projectSourcePath);
		ArrayList<File> javaFiles = listJavaFilesForFolder(folder);
		
		Scanner scanner = new Scanner(System.in);  // Create a Scanner object
		System.out.print("Traitement en cours ...");

		// VARIABLES
	    int nbClasses = 0;
	    int nbLignes = 0;
	    int nbMethods = 0;
	    int nbPackages = 0;
	    
	    String className;
	    String methodName;
	    
	    HashMap<String, Integer> nbMethodsPerClassName = new HashMap<String, Integer>();
	    HashMap<String, Integer> nbAttributsPerClassName = new HashMap<String, Integer>();
	    HashMap<String, Integer> nbLinesPerMethodName = new HashMap<String, Integer>();
	    
	    // Pour chaques fichiers, on fais passer les visiteurs qui vont stocker les infos
		for (File fileEntry : javaFiles) {
			String content = FileUtils.readFileToString(fileEntry);
			// System.out.println(content);

			CompilationUnit parse = parse(content.toCharArray());
			
			className = getClasseName(parse).toString();
			nbClasses += getClasses(parse);
			nbLignes += parse.getLineNumber(parse.getLength() - 1);
			nbMethods += getMethods(parse);
			nbPackages += getPackages(parse);
			nbMethodsPerClassName.put(className, getMethods(parse));
			nbLinesPerMethodName = addMethodName(parse, nbLinesPerMethodName);
		}
		
		System.out.println(" FINI");
		System.out.println("RESULTATS");
		System.out.println("Nombres de classes : " + nbClasses);
		System.out.println("Nombres de lignes : " + nbLignes);
		System.out.println("Nombres de méthodes : " + nbMethods);
		System.out.println("Nombres de packages : " + nbPackages);
		
		//Average methods
		int mSomme = 0;
		
        for (Integer nbM : nbMethodsPerClassName.values())
        {
        	mSomme += nbM;
        }
		System.out.println("Moyenne de méthodes par classes : " + (double) mSomme / nbMethodsPerClassName.size());
		
		//Average lines
		mSomme = 0;
		
        for (Integer nbL : nbLinesPerMethodName.values())
        {
        	mSomme += nbL;
        }
		System.out.println("Moyenne de lignes par méthodes : " + mSomme / nbLinesPerMethodName.size());	
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

	// navigate method information
	public static void printMethodInfo(CompilationUnit parse) {
		MethodDeclarationVisitor visitor = new MethodDeclarationVisitor();
		parse.accept(visitor);

		for (MethodDeclaration method : visitor.getMethods()) {
			System.out.println("Method name: " + method.getName()
					+ " Return type: " + method.getReturnType2());
		}

	}

	// navigate variables inside method
	public static void printVariableInfo(CompilationUnit parse) {

		MethodDeclarationVisitor visitor1 = new MethodDeclarationVisitor();
		parse.accept(visitor1);
		for (MethodDeclaration method : visitor1.getMethods()) {

			VariableDeclarationFragmentVisitor visitor2 = new VariableDeclarationFragmentVisitor();
			method.accept(visitor2);

			for (VariableDeclarationFragment variableDeclarationFragment : visitor2
					.getVariables()) {
				System.out.println("variable name: "
						+ variableDeclarationFragment.getName()
						+ " variable Initializer: "
						+ variableDeclarationFragment.getInitializer());
			}

		}
	}
	
	// navigate method invocations inside method
	public static void printMethodInvocationInfo(CompilationUnit parse) {

		MethodDeclarationVisitor visitor1 = new MethodDeclarationVisitor();
		parse.accept(visitor1);
		for (MethodDeclaration method : visitor1.getMethods()) {

			MethodInvocationVisitor visitor2 = new MethodInvocationVisitor();
			method.accept(visitor2);

			for (MethodInvocation methodInvocation : visitor2.getMethods()) {
				System.out.println("method " + method.getName() + " invoc method "
						+ methodInvocation.getName());
			}

		}
	}
	
	public static void printMethodInvocationInfo(CompilationUnit parse, TypeDeclaration c) {

		MethodDeclarationVisitor visitor1 = new MethodDeclarationVisitor();
		parse.accept(visitor1);
		for (MethodDeclaration method : visitor1.getMethods()) {

			MethodInvocationVisitor visitor2 = new MethodInvocationVisitor();
			method.accept(visitor2);

			for (MethodInvocation methodInvocation : visitor2.getMethods()) {
				System.out.println("method " + method.getName() + " invoc method "
						+ methodInvocation.getName());
			}

		}
	}
	
	private static void printClassInfo(CompilationUnit parse, String name)
	{
		TypeDeclarationVisitor visitor = new TypeDeclarationVisitor();
		parse.accept(visitor);

		for (TypeDeclaration c : visitor.getTypes())
		{
			if (c.getName().toString().equals(name) && !c.isInterface()) {
				System.out.println("Class found : " + c.getName());
				
				MethodDeclarationVisitor visitor1 = new MethodDeclarationVisitor();
				parse.accept(visitor1);
				System.out.println("Liste des méthodes de la classe : " + c.getName());
				
				for (MethodDeclaration m : visitor1.getMethods())
				{
					System.out.println("└ " + m.getName());
					MethodInvocationVisitor visitor2 = new MethodInvocationVisitor();
					parse.accept(visitor2);
					
					for (MethodInvocation mInv : visitor2.getMethods())
					{
						System.out.println("  └ " + mInv.getName());
					}
					
				}
			}
		}
	}
	
	private static int getClasses(CompilationUnit parse)
	{
		TypeDeclarationVisitor visitor = new TypeDeclarationVisitor();
		parse.accept(visitor);
		int compt = 0;

		for (TypeDeclaration c : visitor.getTypes())
		{
			if (!c.isInterface()) 
			{
				compt++;
			}
		}
		
		return compt;
	}
	
	private static String getClasseName(CompilationUnit parse)
	{
		TypeDeclarationVisitor visitor = new TypeDeclarationVisitor();
		parse.accept(visitor);
		String name = "";

		for (TypeDeclaration c : visitor.getTypes())
		{
			if (!c.isInterface()) 
			{
				name = c.getName().toString();
			}
		}
		
		return name;
	}
	
	private static int getMethods(CompilationUnit parse)
	{
		MethodDeclarationVisitor visitor = new MethodDeclarationVisitor();
		parse.accept(visitor);
		int compt = 0;

		for (MethodDeclaration m : visitor.getMethods())
		{
			compt++;
		}
		
		return compt;
	}
	
	private static HashMap<String, Integer> addMethodName(CompilationUnit parse, HashMap<String, Integer> nbLinesPerMethodName)
	{
		MethodDeclarationVisitor visitor = new MethodDeclarationVisitor();
		parse.accept(visitor);

		for (MethodDeclaration m : visitor.getMethods())
		{
			nbLinesPerMethodName.put(m.getName().toString(), parse.getLineNumber(parse.getLength() - 1));
		}
		
		return nbLinesPerMethodName;
	}

	private static int getPackages(CompilationUnit parse)
	{
		PackageDeclarationVisitor visitor = new PackageDeclarationVisitor();
		parse.accept(visitor);
		int compt = 0;

		for (PackageDeclaration p : visitor.getPackages())
		{
			compt++;
		}
		
		return compt;
	}

}