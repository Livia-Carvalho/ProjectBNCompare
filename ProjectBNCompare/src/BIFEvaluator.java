import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.BayesNet;
import weka.classifiers.bayes.net.BIFReader;
import weka.core.Instances;

public class BIFEvaluator extends BIFReader
{

	public BIFEvaluator()
	{
		
	}
	
	public void buildClassifier(Instances instances) throws Exception 
	{
	}
	
	static public void main(String args[])
	{
		new BIFEvaluator(args);				
	}

	public BIFEvaluator(String args[])
	{
		if(args.length != 2)
		{
			System.out.println("Erro de sintaxe!");
			System.out.println("Sintaxe correta: Evaluator.jar " +/*
					 		   "'nome do arquivo da base de testes'.arff " +*/
					           "'nome do arquivo da rede induzida'.xml"+
					           "'nome do arquivo da rede original'.xml");			
		}
		else
		{
			
			try 
			{				
				System.out.println();
				System.out.println("-- Avaliando a classificacao e distancia da rede --");
				//BIFEvaluator netFile = new BIFEvaluator(); 
				//String inducedNetName = args[0];
				//netFile.processFile(inducedNetName);			
				// o método getInstances lê o arquivo e pede para o Weka formar o  
				// Instances contendo todas as linhas (instancias) do arquivo ARFF
				//-> ver a implementaçao do método getInstances mais abaixo
				//Instances instances = getInstances(args[0]);
				//passamos o indice do atributo considerado como CLASSE
				//instances.setClassIndex(0);
				//netFile.m_Instances = instances;				
				// Evaluation é uma classe do Weka que entre um monte de 
				// coisas consegue fazer validação cruzada. O objeto aqui 
				// eh usado para avaliar o modelo.
				// Realizando a classificacao a partir do modelo.
							
				
				//Evaluation nbEv = new Evaluation(instances);
				//nbEv.evaluateModel(netFile, instances);
				//double classification = nbEv.pctCorrect();
				//saveClassification(classification);
				//savePercents(instances, netFile);
				
				// comparacao entre a rede induzida e a original.		
				Comparison(args[0], args[1]);
				
			}
			catch (Throwable t) 
			{
				t.printStackTrace();
			}
		}
	}
	/**
	 * Responsavel pela comparacao entre a rede induzida e a rede original.				 
	 * @param inducedNetName
	 * @param originalNetName
	 */
	public void Comparison(String inducedNetName, String originalNetName)
	{
		try
		{			
			System.out.println("Comparando a rede " + inducedNetName + 
	        " e a original");
			BIFEvaluator inducedNet = new BIFEvaluator();
			inducedNet.processFile(inducedNetName);
			BIFEvaluator originalNet = new BIFEvaluator();
			originalNet.processFile(originalNetName);
			System.out.println("N. arcos extras:" + originalNet.extraArcs(inducedNet));
			System.out.println("N. arcos ausentes:" + originalNet.missingArcs(inducedNet));
			System.out.println("N. arcos reversos:" + originalNet.reversedArcs(inducedNet));
		}
		catch (Throwable t) 
		{
			t.printStackTrace();
		}
	}
	
	/**
	 * @param arffName - O caminho completo do arquivo Arff
	 * @return - Um objeto Instances (do Weka) que é uma coleção de vários objetos 
	 *           Instance (cada Instance representa uma linha do arquivo ARFF)
	 * @throws Exception - se algo der errado, lança uma Exceçao genérica
	 */
	private static Instances getInstances(String arffName) throws Exception
	{
		// FileReader é uma classe do Java para leitura de arquivos, no construtor 
		// dela passamos o caminho do arquivo que queremos ler
		FileReader arffReader = new FileReader(arffName);
		
		//Instances é uma classe do Weka, e no construtor dela podemos passar um 
		//objeto FileReader (que construímos na linha 
		//acima). Daí o Weka recupera os dados do arquivo pelo FileReader e monta 
		//uma coleção de Instance num objeto Instances
		Instances instances = new Instances(arffReader);
		arffReader.close(); //fechamos o leitor de arquivo
			
		return instances; //retorna o objeto de Instances
	}
	
	/**
	 * A Funcao saveResults salva os resultados obtidos pelo modelo em arquivo e os
	 * mostra na tela.
	 * @param instances - as amostras da base de dados
	 * @param net - o modelo criado
	 */
	private void savePercents(Instances instances, BayesNet net)
	{
		try
		{
			FileWriter writer = new FileWriter(new File("porcentagens.xls"));
			PrintWriter percents = new PrintWriter(writer,true);
			double probabilities[];
			
			//System.out.println("Probabilidades \t Acertou (1) ou Errou (0)");
			percents.println("Probabilidades \t Acertou (1) ou Errou (0)");
			for(int i=0; i<instances.numInstances(); i++)
			{				
				probabilities = net.distributionForInstance(instances.instance(i));
				Double prob_classe_zero = probabilities[0];				
				Double prob_classe_um = probabilities[1];
				double probability = net.classifyInstance(instances.instance(i));
				if(prob_classe_zero>prob_classe_um)
				{
					//System.out.print(prob_classe_zero);
					percents.print(prob_classe_zero);
				}
				else
				{
					//System.out.print(prob_classe_um);
					percents.print(prob_classe_um);
				}
				//System.out.print("\t" + probability + "\n");	
				percents.print("\t" + probability + "\n");
			}
			percents.close();
		}
		catch(Exception ex)
		{
			//mostra o erro e a pilha de execuçao até onde o erro ocorreu
			ex.printStackTrace(); 
		}
	}
	
	private void saveClassification(double classification)
	{
		try
		{
			FileWriter writer = new FileWriter(new File("classificacao.xls"), true);
			PrintWriter writerClassification = new PrintWriter(writer,true);
			
			writerClassification.println(classification);
			writerClassification.close();
			System.out.println(classification);
		}
		catch(Exception ex)
		{
			//mostra o erro e a pilha de execuçao até onde o erro ocorreu
			ex.printStackTrace(); 
		}
	}
}
